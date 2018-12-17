#!/bin/bash

DATACLAYLIB="../tool/lib/dataclayclient.jar"
if [ ! -f $DATACLAYLIB ]; then
	echo "[ERROR] dataClay client lib (or link) not found at $DATACLAYLIB."
	exit -1
fi
DATACLAYDEPS="../tool/lib/dependencies"

echo ""
echo -n "Compiling ... "
mkdir -p bin
javac -cp stubs:bin:$DATACLAYLIB:$DATACLAYDEPS/* src/api/*.java src/api/exceptions/*.java -d bin/
echo " done"
