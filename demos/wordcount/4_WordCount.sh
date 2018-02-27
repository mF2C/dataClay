#!/bin/bash

DCLIB="../../tool/lib/dataclayclient.jar"

if [ ! -f $DCLIB ]; then
	echo "[ERROR] dataClay client lib (or link) not found at $DCLIB."
	exit -1
fi
if [ ! -f $COMPSSLIB ]; then
	echo "[ERROR] COMPSs lib (or link) not found at $COMPSSLIB."
	exit -1
fi

TOEXEC="java -cp stubs:bin:$DCLIB:$COMPSSLIB consumer.Wordcount $@"

echo ""
echo "Executing:"
echo ""
echo $TOEXEC
echo ""

$TOEXEC

