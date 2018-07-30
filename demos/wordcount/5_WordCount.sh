#!/bin/bash

if [ -z $1 ]; then
	echo "[ERROR] Missing parameter. Usage $0 <index_alias>"
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

LIBDIR=../../tool/lib
CLASSPATH=stubs:bin:$LIBDIR/dataclayclient.jar:$LIBDIR/dependencies/*:$COMPSSLIB

TOEXEC="java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $CLASSPATH consumer.Wordcount $@"
echo ""
echo "Executing:"
echo ""
echo $TOEXEC
echo ""

$TOEXEC

