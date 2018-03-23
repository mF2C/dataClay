#!/bin/bash

if [ -z $DATACLAY_JAR ]; then
	DATACLAY_JAR="../../../tool/lib/dataclayclient.jar"
fi
if [ ! -f $DATACLAY_JAR ]; then
	echo ""
	echo " Warning. dataClay client lib:'$DATACLAY_JAR' not found. Did you run the register step first? "
    exit -1
fi

TOEXEC="java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp stubs:bin:$DATACLAY_JAR app.AgentMockup $@"
echo ""
echo "Executing"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
