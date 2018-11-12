#!/bin/bash
TOOLSPATH=../tool/dClayTool.sh
DCLIB=../tool/lib/dataclayclient.jar
DCDEPS=../too/lib/dependencies

until $TOOLSPATH GetDataClayID 
do 
    echo " --- waiting for dataclay"
    sleep 2
done

# Minimum set of required variables
STUBSPATH="stubs"

##### Checks and derived variables definition

# Check toolspath
if [ ! -f $TOOLSPATH ]; then
	echo "Bad tools path. Edit TOOLSPATH script variable to change it."
	exit -1
fi
# Local variables
NAMESPACE="CimiNS"
USER=CimiUser
PASS=8b1d63c685d44471bce732c32f4888ed
DATASET=CimiDS
mv cfgfiles/session.properties cfgfiles/session.properties.orig
cat >cfgfiles/session.properties <<EOF
### StorageItf.init variables
Account=$USER
Password=$PASS
StubsClasspath=./stubs
DataSets=$DATASET
DataSetForStore=$DATASET
DataClayClientConfig=./cfgfiles/client.properties
EOF

##### dClayTools-based script
$TOOLSPATH NewAccount $USER $PASS
$TOOLSPATH NewDataContract $USER $PASS $DATASET $USER
mkdir -p $STUBSPATH
$TOOLSPATH GetStubs $USER $PASS $NAMESPACE $STUBSPATH
