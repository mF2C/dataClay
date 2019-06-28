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

(ns com.sixsq.dataclay.wrapper
  "This namespace is a simple facade around the DataClay wrapper written in
   Java. This exists solely to make testing easier."
  (:refer-clojure :exclude [read update])
  (:require
    [clojure.tools.logging :as log]
    [camel-snake-kebab.core :refer [->kebab-case]])
  (:import
    (api DataClayWrapper)))


(defn create
  [type uuid data]
  (log/info "wrapper create arguments:" type uuid data)
  (try
    (let [dc-type (->kebab-case type)
          response (DataClayWrapper/create dc-type uuid data)]
      (log/info "wrapper create response:" dc-type response)
      response)
    (catch Exception ex
      (log/info "wrapper create exception:" type uuid data (str ex))
      (throw ex))))


(defn read
  [type uuid]
  (log/info "wrapper read arguments:" type uuid)
  (try
    (let [dc-type (->kebab-case type)
          response (DataClayWrapper/read dc-type uuid)]
      (log/info "wrapper read response:" dc-type response)
      response)
    (catch Exception ex
      (log/info "wrapper read exception:" (str ex))
      (throw ex))))


(defn delete
  [type uuid]
  (log/info "wrapper delete arguments:" type uuid)
  (try
    (let [dc-type (->kebab-case type)
          response (DataClayWrapper/delete dc-type uuid)]
      (log/info "wrapper delete response:" dc-type response)
      response)
    (catch Exception ex
      (log/info "wrapper delete exception:" (str ex))
      (throw ex))))


(defn update
  [type uuid data]
  (log/info "wrapper update arguments:" type uuid data)
  (try
    (let [dc-type (->kebab-case type)
          response (DataClayWrapper/update dc-type uuid data)]
      (log/info "wrapper update response:" dc-type response)
      response)
    (catch Exception ex
      (log/info "wrapper update exception:" (str ex))
      (throw ex))))


(defn query
  [collection-id filter user-name user-role]
  (log/info "wrapper query arguments:" collection-id (str "'" filter "'") user-name user-role)
  (try
    ;; Setting the user-name and user-role explicitly to nil is a hack to allow
    ;; dataClay to return these resources for all queries. This can be removed when
    ;; the ACL within the DataClayWrapper is corrected.
    (let [user-name (if (.startsWith collection-id "Session") nil user-name)
          user-role (if (.startsWith collection-id "Session") nil user-role)
          dc-collection-id (->kebab-case collection-id)
          _ (log/info "wrapper query modified arguments:" dc-collection-id (str "'" filter "'") user-name user-role)
          response (DataClayWrapper/query dc-collection-id filter user-name user-role)]
      (log/info "wrapper query response:" dc-collection-id response)
      response)
    (catch Exception ex
      (log/info "wrapper query exception:" (str ex))
      (throw ex))))
