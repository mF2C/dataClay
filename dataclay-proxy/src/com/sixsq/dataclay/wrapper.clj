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
    [clojure.tools.logging :as log])
  (:import
    (api DataClayWrapper)))


(defn create
  [type uuid data]
  (try
    (let [response (DataClayWrapper/create type uuid data)]
      (log/info "wrapper create response:" response)
      response)
    (catch Exception ex
      (log/info "wrapper create exception:" (str ex)))))


(defn read
  [type uuid]
  (try
    (let [response (DataClayWrapper/read type uuid)]
      (log/info "wrapper read response:" response)
      response)
    (catch Exception ex
      (log/info "wrapper read exception:" (str ex)))))


(defn delete
  [type uuid]
  (try
    (let [response (DataClayWrapper/delete type uuid)]
      (log/info "wrapper delete response:" response)
      response)
    (catch Exception ex
      (log/info "wrapper delete exception:" (str ex)))))


(defn update
  [type uuid data]
  (try
    (let [response (DataClayWrapper/update type uuid data)]
      (log/info "wrapper update response:" response)
      response)
    (catch Exception ex
      (log/info "wrapper update exception:" (str ex)))))


(defn query
  [collection-id filter user-name user-role]
  (try
    (let [response (DataClayWrapper/query collection-id filter user-name user-role)]
      (log/info "wrapper query response:" response)
      response)
    (catch Exception ex
      (log/info "wrapper query exception:" (str ex)))))
