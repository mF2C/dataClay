#!/bin/bash
TOOLSDIR=../../tool
TOOLSPATH=$TOOLSDIR/dClayTool.sh
LIBDIR=$TOOLSDIR/lib

# Minimum set of required variables
APP=Wordcount
SRCPATH="src/model"
STUBSPATH="stubs"
CLASSPATH=$LIBDIR/dataclayclient.jar:$LIBDIR/dependencies/*:$CLASSPATH

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
	printf "\n******\n***** $1 \n******\n"
}


##### dClayTools-based script

printMsg "Register account"
$TOOLSPATH NewAccount $USER $PASS

printMsg "Create dataset and grant access to it"
$TOOLSPATH NewDataContract $USER $PASS $DATASET $USER

printMsg "Compile and register model"
TMPBINPATH=`mktemp -d`
javac -cp $CLASSPATH `find $SRCPATH -type f -name *.java` -d $TMPBINPATH
$TOOLSPATH NewModel $USER $PASS $NAMESPACE $TMPBINPATH java
rm -Rf $TMPBINPATH

printMsg "Get stubs"
mkdir -p $STUBSPATH
rm -Rf $STUBSPATH/*
$TOOLSPATH GetStubs $USER $PASS $NAMESPACE $STUBSPATH
