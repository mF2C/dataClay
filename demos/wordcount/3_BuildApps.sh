#!/bin/bash
LIBDIR=../../tool/lib
if [ -z $COMPSSLIB ]; then
	echo "[ERROR] COMPSSLIB variable with valid COMPSS path is undefined"
	exit -1
fi
if [ ! -f $COMPSSLIB ]; then
	echo "[ERROR] COMPSs lib (or link) not found at COMPSSLIB=$COMPSSLIB."
	exit -1
fi
CLASSPATH=stubs:$LIBDIR/dataclayclient.jar:$LIBDIR/dependencies/*:$COMPSSLIB

echo ""
echo -n "Compiling ... "
mkdir -p bin
javac -cp $CLASSPATH src/consumer/*.java -d bin/
javac -cp $CLASSPATH src/producer/*.java -d bin/
echo " done"
