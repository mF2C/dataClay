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
BUILDX_BUILDER=$1
TAG=$2
PLATFORMS=$3
PUSH=$4

# =========== Building jar ===========

# update project.clj versioning
if [ -f project.clj.orig ]; then
	mv project.clj.orig project.clj #sanity check
fi
cp project.clj project.clj.orig
sed -i "s/dataclay.mf2c\/wrapper \"trunk\"/dataclay.mf2c\/wrapper \"${TAG}\"/g" project.clj
sed -i "s/def +version+ \"trunk\"/def +version+ \"${TAG}\"/g" project.clj
sed -i "s/defproject com.sixsq.dataclay\/proxy \"trunk\"/defproject com.sixsq.dataclay\/proxy \"${TAG}\"/g" project.clj

eval "lein do clean, test, uberjar"
# copy to proxy.jar so Docker build can find it 
cp target/proxy-${TAG}-standalone.jar proxy.jar

# =========== Building docker ===========
# Use buildx builder
docker buildx use $BUILDX_BUILDER
BUILD_PARAMS="--load ."
if [ "$PUSH" = true ] ; then
	BUILD_PARAMS="--push --platform $PLATFORMS ."
fi

docker buildx build -t mf2c/trunk:dataclay-proxy-${TAG} $BUILD_PARAMS

if [ "$PUSH" = true ] ; then
	# this automatically pushes the image 
	docker buildx imagetools create mf2c/trunk:dataclay-proxy-${TAG} -t mf2c/trunk:dataclay-proxy-latest 
else 
	docker tag mf2c/trunk:dataclay-proxy-${TAG} mf2c/trunk:dataclay-proxy-latest
fi 

# =========== Cleaning ===========
eval "lein do clean"
mv project.clj.orig project.clj
rm proxy.jar

