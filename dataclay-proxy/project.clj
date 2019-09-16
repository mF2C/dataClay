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

(def +version+ "trunk")
(def +slipstream-version+ "3.67")

(defproject com.sixsq.dataclay/proxy "trunk"
  :description "dataclay proxy service"
  :url "https://github.com/SixSq/dataClay"
;  :jvm-opts ["--add-modules" "java.xml.bind"]
  :javac-options ["-target" "11" "-source" "11" "-Xlint:-options" "-g" "--add-modules" "java.xml.bind"]

  :min-lein-version "2.7.1"
  
  :license {:name         "Apache 2.0"
            :url          "http://www.apache.org/licenses/LICENSE-2.0.txt"
            :distribution :repo}

  :plugins [[lein-parent "0.3.5"]
            [lein-environ "1.1.0"]]

 ; :parent-project {:coords  [sixsq/slipstream-parent "5.3.19"]
 ;                  :inherit [:min-lein-version
 ;                            :managed-dependencies
 ;                            :repositories
 ;                            :deploy-repositories]}

  :parent-project {:coords  [sixsq.nuvla/parent "6.2.0"]
                   :inherit [:plugins
                             :min-lein-version
                             :managed-dependencies]}

  :repositories [["dataClay" {:url       "https://raw.github.com/bsc-ssrg/dataclay-maven-mf2c-wrapper/repository"
                              :snapshots true
                              :releases  true
                              :checksum  :fail
                              :update    :daily}]

  		["nuvla-releases" {:url           "https://sixsq-build-artifacts-legacy.s3.amazonaws.com"
                      :snapshots     false
                      :sign-releases false
                      :checksum      :fail
                      :update        :daily}]]

  :source-paths ["src"]

  :resource-paths ["cfglog"]

  :pom-location "target/"

  :dependencies
  [[org.clojure/clojure]
   [org.clojure/tools.logging]
   [org.clojure/data.json]
   [camel-snake-kebab "0.4.0"]
   [ring]
   [http-kit "2.2.0"]
   [dataclay.mf2c/wrapper "trunk"]
   [com.sixsq.slipstream/utils ~+slipstream-version+]
   [javax.xml.bind/jaxb-api "2.4.0-b180830.0359"]]

  :main com.sixsq.dataclay.proxy

  :aot [com.sixsq.dataclay.proxy]
  )
