#!/bin/bash
LIBDIR=../../../tool/lib
CLASSPATH=stubs:bin:$LIBDIR/dataclayclient.jar:$LIBDIR/dependencies/*:$CLASSPATH

TOEXEC="java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $CLASSPATH app.AgentMockup $@"
echo ""
echo "Executing"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
