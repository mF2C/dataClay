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

(ns com.sixsq.dataclay.scrud
  (:require
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [com.sixsq.dataclay.utils.json :as json]
    [com.sixsq.dataclay.utils.response :as r]
    [com.sixsq.slipstream.util.response :as response])
  (:import
    (api DataClayWrapper)
    (dataclay.exceptions.metadataservice ObjectNotRegisteredException)))


(defn split-id
  [id]
  (let [[type uuid] (str/split id #"/")]
    [type (or uuid type)]))


(defn add
  ([{:keys [id] :as data} options]
   (log/info ":add function:" data options)
   (let [[type uuid] (split-id id)]
     (DataClayWrapper/create type uuid (json/edn->json data))
     (r/wrapped-response (response/response-created id))))

  ([_ {:keys [id] :as data} options]
   (log/info ":add function:" data options)
   (let [[type uuid] (split-id id)]
     (DataClayWrapper/create type uuid (json/edn->json data))
     (r/wrapped-response (response/response-created id)))))


(defn retrieve [id options]
  (log/info ":retrieve function:" id options)
  (let [[type uuid] (split-id id)]
    (try
      (let [resource (DataClayWrapper/read type uuid)]
        (r/wrapped-response (json/json->edn resource)))
      (catch ObjectNotRegisteredException e
        (throw (response/ex-not-found id))))))


(defn delete [{:keys [id] :as data} options]
  (log/info ":delete function:" id options)
  (retrieve id options)
  (let [[type uuid] (split-id id)]
    (DataClayWrapper/delete type uuid)
    (r/wrapped-response (response/response-deleted id))))


(defn edit [{:keys [id] :as data} options]
  (log/info ":edit function:" data options)
  (let [[type uuid] (split-id id)]
    (try
      (DataClayWrapper/update type uuid (json/edn->json data))
      (r/wrapped-response (r/wrapped-response data))
      (catch Exception e
        (log/error (str e))
        (.printStackTrace e)
        (r/wrapped-response (response/response-conflict id))))))


(defn query [collection-id {:keys [filter user-name user-roles] :as options}]
  (log/info ":query function:" collection-id user-name user-roles filter)
  (let [results (DataClayWrapper/query collection-id (str filter) user-name (str (first user-roles)))
        hits (map json/json->edn results)
        n (count hits)
        meta {:count n}]
    (r/wrapped-response [meta hits])))
