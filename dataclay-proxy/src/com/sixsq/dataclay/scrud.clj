(ns com.sixsq.dataclay.scrud
  (:require
    [clojure.string :as str]
    [com.sixsq.dataclay.utils.json :as json]
    [ring.util.response :as response])
  (:import
    (api DataClayWrapper)))


(defn add
  ([{:keys [id] :as data} options]
   (let [[type uuid] (str/split id #"/")]
     (DataClayWrapper/create type uuid (json/edn->json data))))
  ([_ {:keys [id] :as data} options]
   (let [[type uuid] (str/split id #"/")]
     (DataClayWrapper/create type uuid (json/edn->json data)))))


(defn retrieve [id options]
  (let [[type uuid] (str/split id #"/")]
    (json/json->edn (DataClayWrapper/read type uuid))))


(defn delete [{:keys [id] :as data} options]
  (let [[type uuid] (str/split id #"/")]
    (try
      (DataClayWrapper/delete type uuid)
      (-> (response/response (format "resource %s deleted" id)))
      (catch Exception e
        (-> (response/response (format "resource %s NOT deleted" id))
            (response/status 400))))))


(defn edit [{:keys [id] :as data} options]
  (let [[type uuid] (str/split id #"/")]
    (try
      (DataClayWrapper/update type uuid data)
      (-> (response/response (format "resource %s updated" id)))
      (catch Exception e
        (-> (response/response (format "resource %s NOT updated" id))
            (response/status 400))))))


(defn query [collection-id {:keys [filter user-name user-roles] :as options}]
  (let [results (DataClayWrapper/query collection-id filter user-name user-roles)
        json-results (map json/json->edn results)
        n (count json-results)
        result {:count                            n
                (keyword (str collection-id "s")) json-results}]
    (-> (response/response (prn-str result))
        (response/header "Content-Type" "application/edn"))))
