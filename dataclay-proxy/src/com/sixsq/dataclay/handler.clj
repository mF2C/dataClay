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
    [com.sixsq.dataclay.utils.response :as r]
    [com.sixsq.slipstream.util.response :as response]
    [org.httpkit.server :refer [run-server]]
    [ring.util.request :as request]))


(defn check-exception
  "Checks to see if the exception in 'expected' or not. An expected exception
   is an ExceptionInfo object with the HTTP response available as the
   exception's data. Any other exception is unexpected and will result in a 500
   response being sent."
  [e body]
  (if-let [response (ex-data e)]
    (r/wrapped-response response)
    (let [msg (str "error executing request: " body "\n" (str e))]
      (log/error msg)
      (.printStackTrace e)
      (response/response-error msg))))


(defn parse-edn
  [body]
  (try
    (edn/read-string body)
    (catch Exception e
      (let [msg (str "invalid edn body received: " (str e))]
        (log/error msg)
        (throw (ex-info msg (response/response-error msg)))))))


(defn action-handler
  [f [fn-name & args :as argv]]
  (try
    (log/info "dispatching dataclay function: " fn-name args)
    (apply f args)
    (catch Exception e
      (check-exception e argv))))


(defn invalid-action-handler
  [[fn-name & args :as argv]]
  (let [msg (str "invalid function name/signature: " fn-name)]
    (log/error msg)
    (r/wrapped-response (response/response-error msg))))


(defmulti scrud-action
          "Perform a SCRUD action by dispatching on the first value of the
           argument vector."
          first)


(defmethod scrud-action :default
  [argv]
  (invalid-action-handler argv))


(defmethod scrud-action :add
  [argv]
  (action-handler scrud/add argv))


(defmethod scrud-action :retrieve
  [argv]
  (action-handler scrud/retrieve argv))


(defmethod scrud-action :edit
  [argv]
  (action-handler scrud/edit argv))


(defmethod scrud-action :delete
  [argv]
  (action-handler scrud/delete argv))


(defmethod scrud-action :query
  [argv]
  (action-handler scrud/query argv))


(defn handler [request]
  (if-let [body (request/body-string request)]
    (try
      (-> body parse-edn scrud-action)
      (catch Exception e
        (check-exception e body)))
    (let [msg "request received without body"]
      (log/error msg)
      (response/response-error msg))))
