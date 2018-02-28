#!/bin/bash

REMOTEPATH="/tmp/files"

if [ -z $1 ]; then
	echo "[ERROR] Missing parameter. Usage $0 <index_alias>"
	exit -1
fi

DCLIB="../../tool/lib/dataclayclient.jar"
if [ ! -f $DCLIB ]; then
	echo "[ERROR] dataClay client lib (or link) not found at $DCLIB."
	exit -1
fi

TOEXEC="java -cp stubs:bin:$DCLIB producer.TextCollectionGen $1 $REMOTEPATH ${@:2}"
echo ""
echo "Executing:"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
