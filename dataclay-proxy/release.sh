#!/bin/bash

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

TOOLSPATH=../tool/dClayTool.sh

lein do clean, test, uberjar

mv cfgfiles/client.properties cfgfiles/client.properties.orig
cat >cfgfiles/client.properties <<EOF
HOST=logicmodule1
TCPPORT=1034
EOF

cp -fr ../tool tool
docker build -t mf2c/dataclay-proxy:${1} .
docker tag mf2c/dataclay-proxy:${1} mf2c/dataclay-proxy:latest
docker tag mf2c/dataclay-proxy:${1} mf2c/dataclay-proxy:trunk

# cleanup
mv cfgfiles/client.properties.orig cfgfiles/client.properties

lein do clean
ls -l *
rm -fr tool
