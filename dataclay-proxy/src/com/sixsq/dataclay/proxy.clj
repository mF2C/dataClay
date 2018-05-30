;;
;; Copyright (c) 2018, SixSq Sarl
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

(ns com.sixsq.dataclay.proxy
  (:require
   [org.httpkit.server :refer [run-server]]
   [clj-time.core :as t])
  (:import
   (dataclay.api DataClay) 
   (api DataClayWrapper)))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str (t/time-now))})

(defn -main [& args]
  (DataClay/init)

  (run-server app {:port 8080})
  (println "Server started on port 8080"))
