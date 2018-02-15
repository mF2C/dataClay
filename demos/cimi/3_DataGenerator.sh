#!/bin/bash

#export DATACLAYSESSIONFILE="$PWD/config.properties"
TOEXEC="java -cp stubs:bin:lib/dataclayclient.jar app.DataGenerator $@"
echo ""
echo "Executing"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
