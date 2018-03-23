#!/bin/bash
TOOLBASEDIR='../../../tool'
TOOLSPATH=$TOOLBASEDIR/dClayTool.sh
if [ -z $DATACLAY_JAR ]; then
	DATACLAY_JAR="$TOOLBASEDIR/lib/dataclayclient.jar"
fi

echo " *************** WARNING USING $DATACLAY_JAR ************************** "

##### Minimum set of required variables
APP="CIMIFED_"
SRCPATH="../src/model"
STUBSPATH="stubs"
NAMESPACE="${APP}NS"
if [ ! -z $1 ]; then
	NAMESPACE=$1
fi
USER=${APP}User
PASS=${APP}Pass
DATASET=${APP}DS


##### Checks and derived variables definition
if [ ! -f $TOOLSPATH ]; then
	echo "Bad tools path. Edit TOOLSPATH script variable to change it."
	exit -1
fi

printMsg() {
	printf "\n******\n***** $1 \n******\n"
}


##### dClayTools-based script
printMsg "Register account"
$TOOLSPATH NewAccount $USER $PASS

printMsg "Create dataset and grant access to it"
$TOOLSPATH NewDataContract $USER $PASS $DATASET $USER

printMsg "Compile and register model"
TMPBINPATH=`mktemp -d`
javac -cp $DATACLAY_JAR `find $SRCPATH -type f -name *.java` -d $TMPBINPATH
$TOOLSPATH NewModel $USER $PASS $NAMESPACE $TMPBINPATH java
rm -Rf $TMPBINPATH

printMsg "Get stubs"
mkdir -p $STUBSPATH
rm -Rf $STUBSPATH/*
$TOOLSPATH GetStubs $USER $PASS $NAMESPACE $STUBSPATH
