#!/bin/bash

#export DATACLAYSESSIONFILE="$PWD/config.properties"
TOEXEC="java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp stubs:bin:../../tool/lib/dataclayclient.jar app.DataClayBindingMockup $@"
echo ""
echo "Executing"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
