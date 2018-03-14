#!/bin/bash

DCLIB="../../tool/lib/dataclayclient.jar"

if [ ! -f $DCLIB ]; then
	echo ""
	echo " Warning. dataClay client lib:'$DCLIB' not found. Did you run the register step first? "
        exit -1
fi

echo ""
echo -n "Compiling ... "
mkdir -p bin
javac -cp stubs:$DCLIB src/app/*.java -d bin/
echo " done"
