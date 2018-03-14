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
DCLIB=$(readlink -f "$DCLIB")

if [ -z $COMPSSLIB ]; then
        echo "[ERROR] COMPSSLIB variable is undefined"
        exit -1
fi
if [ ! -f $COMPSSLIB ]; then
        echo "[ERROR] COMPSSLIB (or link) not found at $COMPSSLIB. Check COMPSSLIB variable is well defined."
        exit -1
fi
COMPSSLIB=$(readlink -f "$COMPSSLIB")

BASEDIR=/usr/src/myapp
DK_DCLIB=$BASEDIR/lib/dataclayclient.jar
DK_COMPSSLIB=$BASEDIR/lib/compss-engine.jar
docker run --rm \
        --network 'host' \
        -v "$PWD":$BASEDIR \
        -v "$DCLIB":$DK_DCLIB \
        -v "$COMPSSLIB":$DK_COMPSSLIB \
        -w $BASEDIR openjdk:8 \
        java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp stubs:bin:$DK_DCLIB:$DK_COMPSSLIB consumer.Wordcount $@
