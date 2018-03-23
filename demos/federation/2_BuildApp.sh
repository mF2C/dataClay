#!/bin/bash

if [ -z $DATACLAY_JAR ]; then
	DATACLAY_JAR="../../../tool/lib/dataclayclient.jar"
fi
if [ ! -f $DATACLAY_JAR ]; then
	echo ""
	echo " Warning. dataClay client lib:'$DATACLAY_JAR' not found. Did you run the register step first? "
    exit -1
fi

mkdir -p bin
TOEXEC="javac -cp stubs:$DATACLAY_JAR ../src/app/*.java -d bin"
echo ""
echo "Executing"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
