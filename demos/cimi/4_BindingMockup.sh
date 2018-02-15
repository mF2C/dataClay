#!/bin/bash

#export DATACLAYSESSIONFILE="$PWD/config.properties"
TOEXEC="java -cp stubs:bin:../../tool/lib/dataclayclient.jar app.DataClayBindingMockup $@"
echo ""
echo "Executing"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
