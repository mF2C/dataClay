#!/bin/bash

DCLIB="../../tool/lib/dataclayclient.jar"

if [ ! -f $DCLIB ]; then
	echo "[ERROR] dataClay client lib (or link) not found at $DCLIB."
	exit -1
fi
if [ -z $COMPSSLIB ]; then
	echo "[ERROR] COMPSS lib variable undefined"
	exit -1
fi
if [ ! -f $COMPSSLIB ]; then
	echo "[ERROR] COMPSs lib (or link) not found at $COMPSSLIB."
	exit -1
fi

echo ""
echo -n "Compiling ... "
mkdir -p bin
javac -cp stubs:$DCLIB:$COMPSSLIB src/consumer/*.java -d bin/
javac -cp stubs:$DCLIB:$COMPSSLIB src/producer/*.java -d bin/
echo " done"
