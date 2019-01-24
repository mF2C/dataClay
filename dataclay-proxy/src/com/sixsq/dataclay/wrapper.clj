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
  (:import
    (api DataClayWrapper)))


(defn create
  [type uuid data]
  (DataClayWrapper/create type uuid data))


(defn read
  [type uuid]
  (DataClayWrapper/read type uuid))


(defn delete
  [type uuid]
  (DataClayWrapper/delete type uuid))


(defn update
  [type uuid data]
  (DataClayWrapper/update type uuid data))


(defn query
  [collection-id filter user-name user-role]
  (DataClayWrapper/query collection-id filter user-name user-role))
