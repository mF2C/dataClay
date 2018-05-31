(ns com.sixsq.dataclay.utils.json
  "Utilities for handling JSON data."
  (:require
    [clojure.data.json :as json]))


(defn str->json [s]
  (json/read-str s :key-fn keyword))


(defn edn->json [json]
  (json/write-str json))


(defn json->edn [s]
  (cond
    (nil? s) {}
    (instance? Exception s) s
    :else (str->json s)))
