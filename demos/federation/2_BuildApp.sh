#!/bin/bash
LIBDIR=../../../tool/lib
CLASSPATH=stubs:$LIBDIR/dataclayclient.jar:$LIBDIR/dependencies/*:$CLASSPATH

mkdir -p bin
TOEXEC="javac -cp $CLASSPATH ../src/app/*.java -d bin"
echo ""
echo "Executing"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
