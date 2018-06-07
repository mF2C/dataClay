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


(defn dispatch
  [[fn-name & args :as edn-body]]
  (let [f (case (keyword fn-name)
            :add scrud/add
            :retrieve scrud/retrieve
            :edit scrud/edit
            :delete scrud/delete
            :query scrud/query
            ::invalid-signature)]
    (if-not (= f ::invalid-signature)
      (try
        (log/info "dispatching dataclay function: " fn-name args)
        (apply f args)
        (catch Exception e
          (check-exception e edn-body)))
      (let [msg (str "invalid function name/signature: " fn-name)]
        (log/error msg)
        (throw (ex-info msg (response/response-error msg)))))))


(defn handler [request]
  (if-let [body (request/body-string request)]
    (try
      (-> body parse-edn dispatch)
      (catch Exception e
        (check-exception e body)))
    (let [msg "request received without body"]
      (log/error msg)
      (response/response-error msg))))
