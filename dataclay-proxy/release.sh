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

(cd ../orchestration/ && docker-compose down && docker-compose up -d)

echo "dataClay is up"

TOOLSPATH=../tool/dClayTool.sh

until [ "`$TOOLSPATH GetDataClayID 2>&1 | grep ERROR`" == "" ];
do
    echo " --- waiting for dataclay"
    sleep 2
done

password=`echo $(uuidgen || cat /dev/urandom) | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1`

./createAccountAndGetStubs.sh
./buildApp.sh

lein do clean, uberjar

mv cfgfiles/client.properties cfgfiles/client.properties.orig
cat >cfgfiles/client.properties <<EOF
HOST=logicmodule1
TCPPORT=1034
EOF

cp -fr ../tool tool
docker build -t mf2c/dataclay-proxy:${1} .
#docker tag mf2c/dataclay-proxy:${1} mf2c/dataclay-proxy:latest

# cleanup
mv cfgfiles/session.properties.orig cfgfiles/session.properties
mv cfgfiles/client.properties.orig cfgfiles/client.properties

while true;
do
    read -p "Do you wish to shutdown DataClay? [y/n]" yn
    case $yn in
        [Yy]* ) 
                (cd ../orchestration/ && docker-compose down -v)
                break
                ;;
        [Nn]* ) break;;
        * ) echo "Please answer yes or no.";;
    esac
done

lein do clean
rm -fr stubs bin tool
