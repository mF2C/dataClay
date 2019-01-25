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

(ns com.sixsq.dataclay.utils.json-test
  (:require
    [clojure.test :refer :all]
    [com.sixsq.dataclay.utils.json :as t]))


(deftest check-roundtrip

  (let [initial-value {:id "resource/123", :int 1, :bool true, :float 1.0, :nil nil}
        final-value (-> initial-value
                        t/edn->json
                        t/json->edn)]
    (is (= initial-value final-value))))
