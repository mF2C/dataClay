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

(ns com.sixsq.dataclay.utils.scrud-test
  (:require
    [clojure.test :refer :all]
    [com.sixsq.dataclay.utils.scrud :as t])
  (:import
    (api.exceptions TypeDoesNotExistException
                    ResourceCollectionDoesNotExistException
                    ObjectAlreadyExistsException
                    ObjectDoesNotExistException
                    DataClayFederationException)))


(deftest check-split-id
  (is (= ["test" "123"] (t/split-id "test/123")))
  (is (= ["cloud-entry-point" "cloud-entry-point"] (t/split-id "cloud-entry-point")))
  (is (nil? (t/split-id nil)))
  (is (nil? (t/split-id 123))))


(defn extract-wrapped-exception
  [ex]
  (try
    (t/rethrow ex "type" "id")
    (catch Exception e
      (if-let [{:keys [status]} (ex-data e)]
        status
        e))))


(deftest check-exception->response

  (are [status ex] (= status (extract-wrapped-exception ex))
                   400 (IllegalArgumentException.)
                   400 (TypeDoesNotExistException. "type")
                   404 (ObjectDoesNotExistException. "type" "id")
                   409 (ObjectAlreadyExistsException. "type" "id")
                   405 (ResourceCollectionDoesNotExistException. "type")
                   500 (DataClayFederationException. "msg"))

  (let [e (Exception.)]
    (is (= e (extract-wrapped-exception e)))))
