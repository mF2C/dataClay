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


(defn parse-edn
  [body]
  (try
    (edn/read-string body)
    (catch Exception e
      (let [msg (str "invalid edn body received: " (str e))]
        (log/error msg)
        (throw (ex-info msg (-> (response/response msg)
                                (response/status 400))))))))


(defn dispatch
  [[fn-name & args :as edn-body]]
  (log/debug "dispatching dataclay function: " fn-name args)
  (case (keyword fn-name)
    :add (apply scrud/add args)
    :retrieve (apply scrud/retrieve args)
    :edit (apply scrud/retrieve args)
    :delete (apply scrud/delete args)
    :query (apply scrud/query args)
    (let [msg (str "invalid function name/signature: " fn-name)]
      (log/error msg)
      (throw (ex-info msg (-> (response/response msg)
                              (response/status 400)))))))


(defn handler [request]
  (if-let [body (request/body-string request)]
    (try
      (log/debugf "raw body of request: '%s'" body)
      (let [edn-body (parse-edn body)]
        (log/debug "received valid edn body:" (with-out-str (clojure.pprint/pprint edn-body)))
        (let [response (prn-str (dispatch edn-body))]
          (log/debug "response:" response)
          (response/response response)))
      (catch Exception e
        (or (ex-data e)
            (let [msg (str "error executing request: " body "\n" (str e))]
              (log/error msg)
              (throw (ex-info msg (-> (response/response msg)
                                      (response/status 400))))))))
    (do
      (log/error "request received without body")
      (-> (response/response "request received without body\n")
          (response/status 400)))))
