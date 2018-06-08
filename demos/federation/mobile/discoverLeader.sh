#!/bin/bash

LMHOST=logicmodule2 #default for mf2c demo
LMPORT=2034 #default for mf2c demo
if [ $# -eq 2 ]; then
	LMHOST=$1
	LMPORT=$2
fi

../../../tool/dClayTool.sh RegisterDataClay $LMHOST $LMPORT
