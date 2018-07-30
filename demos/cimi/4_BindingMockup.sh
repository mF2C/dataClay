#!/bin/bash
LIBDIR=../../tool/lib
CLASSPATH=stubs:bin:$LIBDIR/dataclayclient.jar:$LIBDIR/dependencies/*:$CLASSPATH

#export DATACLAYSESSIONFILE="$PWD/config.properties"
TOEXEC="java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $CLASSPATH app.DataClayBindingMockup $@"
echo ""
echo "Executing"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
