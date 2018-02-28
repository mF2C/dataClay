#!/bin/bash

if [ -z $1 ]; then
	echo "[ERROR] Missing parameter. Usage $0 <index_alias>"
	exit -1
fi

DCLIB="../../tool/lib/dataclayclient.jar"
if [ ! -f $DCLIB ]; then
	echo "[ERROR] dataClay client lib (or link) not found at $DCLIB."
	exit -1
fi
if [ -z $COMPSSLIB ]; then
	echo "[ERROR] COMPSSLIB variable is undefined"
	exit -1
fi
if [ ! -f $COMPSSLIB ]; then
	echo "[ERROR] COMPSSLIB (or link) not found at $COMPSSLIB. Check COMPSSLIB variable is well defined."
	exit -1
fi

TOEXEC="java -cp stubs:bin:$DCLIB:$COMPSSLIB consumer.Wordcount $@"

echo ""
echo "Executing:"
echo ""
echo $TOEXEC
echo ""

$TOEXEC

