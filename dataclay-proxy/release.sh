#!/bin/bash

if [[ $# -eq 0 ]] ; then
    echo 'Please pass the Docker image tag version as an argument'
    exit 0
fi

(cd ../orchestration/ && docker-compose up -d)

TOOLSPATH=../tool/dClayTool.sh

until $TOOLSPATH GetDataClayID 
do 
    echo " --- waiting for dataclay"
    sleep 2
done

password=`echo $(uuidgen || cat /dev/urandom) | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1`

sed -i '.orig' "s/cHaNgEmE/$password/g" registerModel_v2.sh
./registerModel_v2.sh

./buildApp.sh

lein do clean, uberjar

mv cfgfiles/client.properties cfgfiles/client.properties.orig
cat >cfgfiles/client.properties <<EOF
HOST=logicmodule1
TCPPORT=1034
EOF

cp -fr ../tool tool
docker build -t mf2c/dataclay-proxy:${1} .
docker tag mf2c/dataclay-proxy:${1} mf2c/dataclay-proxy:latest

# cleanup
mv cfgfiles/session.properties.orig cfgfiles/session.properties
mv cfgfiles/client.properties.orig cfgfiles/client.properties
mv registerModel_v2.sh.orig registerModel_v2.sh

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
