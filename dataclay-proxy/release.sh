#!/bin/bash -e
#
#  Copyright (c) 2018, SixSq Sarl
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#  implied.  See the License for the specific language governing
#  permissions and limitations under the License.
#
if [[ $# -eq 0 ]] ; then
    echo 'Please pass the Docker image tag version as an argument'
    exit 0
fi
# update project.clj versioning
if [ -f project.clj.orig ]; then
	mv project.clj.orig project.clj #sanity check
fi
cp project.clj project.clj.orig
sed -i "s/dataclay.mf2c\/wrapper \"trunk\"/dataclay.mf2c\/wrapper \"${1}\"/g" project.clj
sed -i "s/def +version+ \"trunk\"/def +version+ \"${1}\"/g" project.clj
sed -i "s/defproject com.sixsq.dataclay\/proxy \"trunk\"/defproject com.sixsq.dataclay\/proxy \"${1}\"/g" project.clj

lein do clean, test, uberjar
# copy to proxy.jar so Docker build can find it 
cp target/proxy-${1}-standalone.jar proxy.jar

docker build -t mf2c/dataclay-proxy:${1} .
docker tag mf2c/dataclay-proxy:${1} mf2c/dataclay-proxy:latest
docker tag mf2c/dataclay-proxy:${1} mf2c/dataclay-proxy:trunk
lein do clean

# Cleaning
mv project.clj.orig project.clj
rm proxy.jar

