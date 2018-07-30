#!/bin/bash
REMOTEPATH="/tmp/files"

if [ -z $1 ]; then
	echo "[ERROR] Missing parameter. Usage $0 <index_alias>"
	exit -1
fi

LIBDIR=../../tool/lib
CLASSPATH=stubs:bin:$LIBDIR/dataclayclient.jar:$LIBDIR/dependencies/*

TOEXEC="java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $CLASSPATH producer.TextCollectionGen $1 $REMOTEPATH ${@:2}"
echo ""
echo "Executing:"
echo ""
echo $TOEXEC
echo ""

$TOEXEC
