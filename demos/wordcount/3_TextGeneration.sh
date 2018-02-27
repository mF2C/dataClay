#!/bin/bash

DCLIB="../../tool/lib/dataclayclient.jar"

if [ ! -f $DCLIB ]; then
	echo "[ERROR] dataClay client lib (or link) not found at $DCLIB."
	exit -1
fi

TOEXEC="java -cp stubs:bin:$DCLIB producer.TextCollectionGen $@"
echo ""
echo "Executing:"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
