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
    [com.sixsq.dataclay.utils.json :as json]
    [ring.util.response :as response]
    [clojure.tools.logging :as log])
  (:import
    (api DataClayWrapper)))


(defn split-id
  [id]
  (let [[type uuid] (str/split id #"/")]
    [type (or uuid type)]))


(defn add
  ([{:keys [id] :as data} options]
   (log/debug ":add function:" data options)
   (let [[type uuid] (split-id id)]
     (DataClayWrapper/create type uuid (json/edn->json data))))
  ([_ {:keys [id] :as data} options]
   (log/debug ":add function:" data options)
   (let [[type uuid] (split-id id)]
     (DataClayWrapper/create type uuid (json/edn->json data)))))


(defn retrieve [id options]
  (log/debug ":retrieve function:" id options)
  (let [[type uuid] (split-id id)]
    (json/json->edn (DataClayWrapper/read type uuid))))


(defn delete [{:keys [id] :as data} options]
  (log/debug ":delete function:" id options)
  (let [[type uuid] (split-id id)]
    (try
      (DataClayWrapper/delete type uuid)
      (-> (response/response (format "resource %s deleted" id)))
      (catch Exception e
        (-> (response/response (format "resource %s NOT deleted" id))
            (response/status 400))))))


(defn edit [{:keys [id] :as data} options]
  (log/debug ":edit function:" data options)
  (let [[type uuid] (split-id id)]
    (try
      (DataClayWrapper/update type uuid data)
      (-> (response/response (format "resource %s updated" id)))
      (catch Exception e
        (-> (response/response (format "resource %s NOT updated" id))
            (response/status 400))))))


(defn query [collection-id {:keys [filter user-name user-roles] :as options}]
  (log/debug ":query function:" collection-id options)
  (let [results (DataClayWrapper/query collection-id filter user-name user-roles)
        json-results (map json/json->edn results)
        n (count json-results)
        result {:count                            n
                (keyword (str collection-id "s")) json-results}]
    (-> (response/response (prn-str result))
        (response/header "Content-Type" "application/edn"))))
