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

(ns com.sixsq.dataclay.handler
  (:require
    [clojure.edn :as edn]
    [clojure.tools.logging :as log]
    [com.sixsq.dataclay.scrud :as scrud]
    [org.httpkit.server :refer [run-server]]
    [ring.util.request :as request]
    [ring.util.response :as response]))


(defn handler [request]
  (if-let [body (request/body-string request)]
    (try
      (let [[fn-name & args :as edn-body] (edn/read-string body)]
        (log/debug "received valid edn body:" (prn-str edn-body))
        (case (keyword fn-name)
          :add (apply scrud/add args)
          :retrieve (apply scrud/retrieve args)
          :edit (apply scrud/retrieve args)
          :delete (apply scrud/delete args)
          :query (apply scrud/query args)
          (-> (response/response (str "invalid function name: " fn-name))
              (response/status 400))))
      (catch Exception e
        (log/error "invalid edn body received:" body)
        (-> (response/response (str "invalid edn body: " (str e) "\n"))
            (response/status 400))))
    (do
      (log/error "request received without body")
      (-> (response/response "request received without body\n")
          (response/status 400)))))
