#!/bin/bash
TOOLSPATH=../tool/dClayTool.sh
DCLIB=../tool/lib/dataclayclient.jar

# Minimum set of required variables
APP=CIMIjson
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
PASS=${APP}Pass
DATASET=${APP}DS

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
