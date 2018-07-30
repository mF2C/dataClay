#!/bin/bash
LIBDIR=../../tool/lib
CLASSPATH=stubs:$LIBDIR/dataclayclient.jar:$LIBDIR/dependencies/*:$CLASSPATH

echo ""
echo -n "Compiling ... "
mkdir -p bin
javac -cp $CLASSPATH src/demo/*.java -d bin/
echo " done"
