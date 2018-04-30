#!/bin/bash

# Tool and lib
TOOLSBASE="../../tool"
TOOLSPATH="$TOOLSBASE/dClayTool.sh"
DCLIB="$TOOLSBASE/lib/dataclayclient.jar"

# Minimum set of required variables
APP=LMBackup
STUBSPATH="stubs"
MODELPATH="bin/model"
SRCPATH="src/model"


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

printMsg "Register model"
TMPDIR=`mktemp -d`
javac -cp $DCLIB $SRCPATH/*.java -d $TMPDIR
$TOOLSPATH NewModel $USER $PASS $NAMESPACE $TMPDIR java
rm -Rf $TMPDIR

printMsg "Get stubs"
mkdir -p $STUBSPATH
$TOOLSPATH GetStubs $USER $PASS $NAMESPACE $STUBSPATH
