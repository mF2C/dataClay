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
    [clojure.tools.logging :as log]
    [com.sixsq.dataclay.handler :refer [handler]]
    [org.httpkit.server :refer [run-server]])
  (:import
    (dataclay.api DataClay)
    (api DataClayWrapper)))


(defn -main [& args]
  (try
    (log/info "initializing dataclay")
    (DataClay/init)
    (catch Exception e
      (log/error "error initializing dataClay:" (str e))
      (log/error "halting service")
      (System/exit 1)))

  (try
    (log/info "adding service")
    (DataClayWrapper/create "service" "123456" (slurp "service.json"))
    (catch Exception e
      (log/error "error adding service:" (str e))))

  (try
    (log/info "reading service")
    (log/error "RESULT: " (DataClayWrapper/read "service" "123456"))
    (catch Exception e
      (log/error "error reading service:" (str e))))

  (try
    (let [port 6472]
      (run-server handler {:port port})
      (log/info "dataClay proxy successfully started on port" port))
    (catch Exception e
      (log/error "error starting web service:" (str e))
      (System/exit 1))))
