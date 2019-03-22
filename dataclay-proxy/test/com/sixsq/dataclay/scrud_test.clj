;;
;; Copyright (c) 2019, SixSq Sarl
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;    http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
;; implied.  See the License for the specific language governing
;; permissions and limitations under the License.
;;

(ns com.sixsq.dataclay.scrud-test
  (:require
    [clojure.test :refer :all]
    [clojure.edn :as edn]
    [com.sixsq.dataclay.scrud :as t]
    [com.sixsq.dataclay.wrapper :as wrapper]
    [com.sixsq.dataclay.utils.json :as json])
  (:import
    (api.exceptions TypeDoesNotExistException ObjectDoesNotExistException ResourceCollectionDoesNotExistException)))


(deftest check-add

  ;; testing 'success'
  (with-redefs [wrapper/create (fn [type id data] nil)]
    (let [{:keys [status body]} (t/add {:id "test/id"} nil)
          dataclay-response (edn/read-string body)]
      (is (= 200 status))
      (let [{:keys [status]} dataclay-response]
        (is (= 201 status)))))

  ;; one representative exception to test, others tested in lower-level method
  (with-redefs [wrapper/create (fn [type id data] (throw (IllegalArgumentException.)))]
    (try
      (t/add {:id "test/id"} nil)
      (catch Exception ex
        (let [{:keys [status]} (ex-data ex)]
          (is (= 400 status)))))))


(deftest check-retrieve

  ;; testing 'success'
  (with-redefs [wrapper/read (fn [type id] (json/edn->json {:id (str type "/" id)}))]
    (let [{:keys [status body]} (t/retrieve "resource/123" nil)
          dataclay-response (edn/read-string body)]
      (is (= 200 status))
      (let [{:keys [id]} dataclay-response]
        (is (= "resource/123" id)))))

  ;; one representative exception to test, others tested in lower-level method
  (with-redefs [wrapper/read (fn [type id] (throw (TypeDoesNotExistException. "type")))]
    (try
      (t/retrieve "resource/123" nil)
      (catch Exception ex
        (let [{:keys [status]} (ex-data ex)]
          (is (= 400 status)))))))


(deftest check-delete

  ;; testing 'success'
  (with-redefs [wrapper/delete (fn [type id] nil)]
    (let [{:keys [status body]} (t/delete {:id "resource/123"} nil)
          dataclay-response (edn/read-string body)]
      (is (= 200 status))
      (let [{:keys [status body]} dataclay-response]
        (is (= 200 status))
        (is (= 200 (:status body)))
        (is (= "resource/123" (:resource-id body)))
        (is (= "resource/123 deleted" (:message body))))))

  ;; one representative exception to test, others tested in lower-level method
  (with-redefs [wrapper/delete (fn [type id] (throw (ObjectDoesNotExistException. "type" "id")))]
    (try
      (t/delete {:id "resource/123"} nil)
      (catch Exception ex
        (let [{:keys [status body]} (ex-data ex)]
          (is (= 404 status))
          (is (= 404 (:status body)))
          (is (= "resource/123" (:resource-id body)))
          (is (= "resource/123 not found" (:message body))))))))


(deftest check-edit

  ;; testing 'success'
  (with-redefs [wrapper/update (fn [type uuid data] data)]
    (let [{:keys [status body]} (t/edit {:id "resource/123", :new "item"} nil)
          dataclay-response (edn/read-string body)]
      (is (= 200 status))
      (is (= {:id "resource/123", :new "item"} (json/json->edn dataclay-response)))))

  ;; one representative exception to test, others tested in lower-level method
  (with-redefs [wrapper/update (fn [type uuid data] (throw (ObjectDoesNotExistException. "type" "id")))]
    (try
      (t/edit {:id "resource/123", :new "item"} nil)
      (catch Exception ex
        (let [{:keys [status body]} (ex-data ex)]
          (is (= 404 status))
          (is (= "resource/123 not found" (:message body)))
          (is (= "resource/123" (:resource-id body))))))))


(deftest check-query

  ;; testing 'success'
  (with-redefs [wrapper/query (fn [collection-id filter user-name user-role]
                                (map json/edn->json [{:id "r/1"}
                                                     {:id "r/2"}
                                                     {:id "r/3"}]))]
    (let [{:keys [status body]} (t/query "r" {:filter nil, :user-name "user", :user-roles "role1, role2"})
          dataclay-response (edn/read-string body)]
      (is (= 200 status))
      (let [[meta hits] dataclay-response]
        (is (= 3 (:count meta)))
        (is (= #{"r/1" "r/2" "r/3"} (set (map :id hits)))))))

  ;; one representative exception to test, others tested in lower-level method
  (with-redefs [wrapper/query (fn [collection-id filter user-name user-role]
                                (throw (ResourceCollectionDoesNotExistException. "type")))]
    (try
      (t/query "r" {:filter nil, :user-name "user", :user-roles "role1, role2"})
      (catch Exception ex
        (let [{:keys [status body]} (ex-data ex)]
          (is (= 405 status)))))))
