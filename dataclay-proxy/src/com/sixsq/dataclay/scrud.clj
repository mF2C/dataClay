;;
;; Copyright (c) 2018-2019, SixSq Sarl
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
    [clojure.tools.logging :as log]
    [com.sixsq.dataclay.utils.json :as json]
    [com.sixsq.dataclay.utils.response :as r]
    [com.sixsq.slipstream.util.response :as response]
    [com.sixsq.dataclay.utils.scrud :as utils]
    [com.sixsq.dataclay.wrapper :as wrapper]))


(defn add
  ([{:keys [id] :as data} options]
   (log/info ":add function:" data options)
   (let [[type uuid] (utils/split-id id)]
     (try

       (->> data
            json/edn->json
            (wrapper/create type uuid))
       (-> id response/response-created r/wrapped-response)

       (catch Exception ex
         (utils/rethrow ex type id)))))

  ([_ data options]
   (add data options)))


(defn retrieve [id options]
  (log/info ":retrieve function:" id options)
  (let [[type uuid] (utils/split-id id)]
    (try

      (-> (wrapper/read type uuid)
          json/json->edn
          r/wrapped-response)

      (catch Exception ex
        (utils/rethrow ex type id)))))


(defn delete [{:keys [id] :as data} options]
  (log/info ":delete function:" id options)
  (let [[type uuid] (utils/split-id id)]
    (try

      (wrapper/delete type uuid)
      (-> id response/response-deleted r/wrapped-response)

      (catch Exception ex
        (utils/rethrow ex type id)))))


(defn edit [{:keys [id] :as data} options]
  (log/info ":edit function:" data options)
  (let [[type uuid] (utils/split-id id)]
    (try

      (->> data
           json/edn->json
           (wrapper/update type uuid)
           r/wrapped-response)

      (catch Exception ex
        (utils/rethrow ex type id)))))


(defn query [collection-id {{:keys [filter user-name user-roles]} :cimi-params}]
  (log/info ":query function:" collection-id user-name user-roles filter)
  (try

    (let [results (wrapper/query collection-id (str filter) user-name (str (first user-roles)))
          hits (map json/json->edn results)
          n (count hits)
          meta {:count n}]
      (r/wrapped-response [meta hits]))

    (catch Exception ex
      (utils/rethrow ex collection-id "undefined"))))
