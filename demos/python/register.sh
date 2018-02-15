#!/bin/bash
TOOLPATH=../../tool/dClayTool.sh

echo
echo "***"
echo "*** Creating account \`bsc\`"
echo "***"
$TOOLPATH NewAccount bsc password


echo
echo "***"
echo "*** Creating a new DataContract called \`dstest\`"
echo "***"
$TOOLPATH NewDataContract bsc password dstest bsc

echo
echo "***"
echo "*** Registering all the data model_mf2c in ./model_mf2c"
echo "***"
$TOOLPATH NewModel bsc password model_mf2c ./model_mf2c python

echo
echo "***"
echo "*** Getting stubs and putting them in ./demo/stubs"
echo "***"
rm -rf ./demo/stubs
mkdir -p ./demo/stubs
$TOOLPATH GetStubs bsc password model_mf2c ./demo/stubs

echo
echo "***"
echo "*** Storing settings in ./cfgfiles/session.properties"
echo "***"
cat << EOF > ./cfgfiles/session.properties
Account=bsc
Password=password
StubsClasspath=$PWD/demo/stubs
DataSets=dstest
DataSetForStore=dstest
DataClayClientConfig=$PWD/cfgfiles/client.properties
LocalBackend=DS1
EOF

