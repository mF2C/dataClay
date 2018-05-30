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

(def +version+ "1.10-SNAPSHOT")
(def +slipstream-version+ "3.52-SNAPSHOT")

(defproject com.sixsq.dataclay/proxy "0.1.0-SNAPSHOT"
  :description "dataclay proxy service"
  :url "https://github.com/SixSq/dataClay"

  :license {:name         "Apache 2.0"
            :url          "http://www.apache.org/licenses/LICENSE-2.0.txt"
            :distribution :repo}
  
  :plugins [[lein-parent "0.3.2"]
            [lein-environ "1.1.0"]]
  
  :parent-project {:coords  [sixsq/slipstream-parent "5.3.5"]
                   :inherit [:min-lein-version
                             :managed-dependencies
                             :repositories
                             :deploy-repositories]}
  
  :source-paths ["src"]
  
  :resource-paths ["bin" "stubs" "cfglog"]
  
  :pom-location "target/"
  

  #_:dependencies #_[[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [clj-time "0.14.4"]]

  :dependencies
  [[org.clojure/clojure]
   [http-kit "2.2.0"]
   [clj-time]
   #_[dataclay/cimi-stubs "0.0.1-SNAPSHOT"]
   [dataclay/client "0.0.1"]
   #_[com.sixsq.slipstream/SlipStreamCljResources-jar "3.52-SNAPSHOT"]]

  :main com.sixsq.dataclay.proxy)
