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
    [com.sixsq.dataclay.wrapper :as wrapper])
  (:import (api.exceptions TypeDoesNotExistException)))


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

  ;; FIXME: Why doesn't this test work???
  ;; testing 'success'
  #_(with-redefs [wrapper/read (fn [type id] {:status 200 :body "{\"status\": 200}"})]
    (let [{:keys [status body] :as response} (t/retrieve "type" "id")
          _ (println "DEBUG" response)
          dataclay-response (edn/read-string body)]
      (is (= 200 status))
      (let [{:keys [status body]} dataclay-response]
        (is (= 200 status))
        (is (= {:id "test/id"} body)))))

  ;; one representative exception to test, others tested in lower-level method
  (with-redefs [wrapper/read (fn [type id] (throw (TypeDoesNotExistException. "type")))]
    (try
      (t/retrieve "type" "id")
      (catch Exception ex
        (let [{:keys [status]} (ex-data ex)]
          (is (= 400 status)))))))


