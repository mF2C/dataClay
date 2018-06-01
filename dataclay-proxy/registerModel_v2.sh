#!/bin/bash
TOOLSPATH=../tool/dClayTool.sh
DCLIB=../tool/lib/dataclayclient.jar

until $TOOLSPATH GetDataClayID 
do 
    echo " --- waiting for dataclay"
    sleep 2
done

# Minimum set of required variables
APP=cimi
STUBSPATH="stubs"
MODELPATH="bin/CIMI"


##### Checks and derived variables definition

# Check toolspath
if [ ! -f $TOOLSPATH ]; then
	echo "Bad tools path. Edit TOOLSPATH script variable to change it."
	exit -1
fi
# Local variables
if [ ! -z $1 ]; then
	NAMESPACE=$1
else
	NAMESPACE="${APP}NS"
fi
USER=${APP}User
PASS=cHaNgEmE
DATASET=${APP}DS

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

printMsg() {
	printf "\n******\n***** $1 \n******\n "
}


##### dClayTools-based script

printMsg "Register account"
$TOOLSPATH NewAccount $USER $PASS

printMsg "Create dataset and grant access to it"
$TOOLSPATH NewDataContract $USER $PASS $DATASET $USER

printMsg "Compiling and registering model"
TMPDIR=`mktemp -d`
javac -cp $DCLIB src/CIMI/*.java -d $TMPDIR
$TOOLSPATH NewModel $USER $PASS $NAMESPACE $TMPDIR java
rm -Rf $TMPDIR

printMsg "Get stubs"
mkdir -p $STUBSPATH
$TOOLSPATH GetStubs $USER $PASS $NAMESPACE $STUBSPATH
