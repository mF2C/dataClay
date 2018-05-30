#!/bin/bash

DATACLAYLIB="../tool/lib/dataclayclient.jar"
if [ ! -f $DATACLAYLIB ]; then
	echo "[ERROR] dataClay client lib (or link) not found at $DATACLAYLIB."
	exit -1
fi

echo ""
echo -n "Compiling ... "
mkdir -p bin
javac -cp stubs:bin:$DATACLAYLIB src/api/*.java -d bin/
#javac -cp stubs:bin:$DATACLAYLIB src/demo/*.java -d bin/
echo " done"
