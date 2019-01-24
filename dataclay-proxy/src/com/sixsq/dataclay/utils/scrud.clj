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

(ns com.sixsq.dataclay.utils.scrud
  (:require
    [clojure.string :as str]
    [com.sixsq.slipstream.util.response :as response]
    [clojure.tools.logging :as log])
  (:import
    (api.exceptions DataClayFederationException
                    ObjectAlreadyExistsException
                    ObjectDoesNotExistException
                    ResourceCollectionDoesNotExistException
                    TypeDoesNotExistException)))


(defn split-id
  [id]
  (when (string? id)
    (let [[type uuid] (str/split id #"/")]
      [type (or uuid type)])))


(defn rethrow
  "Creates appropriate responses for expected exceptions than then rethrows
   the wrapped response. Will simply rethrow unexpected exceptions."
  [ex type id]
  (cond

    (instance? IllegalArgumentException ex)
    (throw (response/ex-bad-request (str ex)))

    (instance? TypeDoesNotExistException ex)
    (throw (response/ex-bad-request (str "type '" type "' does not exist")))

    (instance? ObjectDoesNotExistException ex)
    (throw (response/ex-not-found id))

    (instance? ObjectAlreadyExistsException ex)
    (throw (response/ex-conflict id))

    (instance? ResourceCollectionDoesNotExistException ex)
    (throw (response/ex-bad-request (str "resource collection for type '" type "' does not exist")))

    (instance? DataClayFederationException ex)
    (let [msg (str ex)]
      (log/error msg)
      (throw (response/ex-response msg 500)))

    :else
    (throw ex)))


