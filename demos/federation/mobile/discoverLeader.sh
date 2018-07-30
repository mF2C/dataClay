#!/bin/bash

LMHOST=logicmodule2 #default for mf2c demo
LMPORT=2034 #default for mf2c demo
if [ $# -eq 2 ]; then
	LMHOST=$1
	LMPORT=$2
fi

MSG=`../../../tool/dClayTool.sh RegisterDataClay $LMHOST $LMPORT | grep -i found`
DCID=`echo $MSG | cut -d":" -f2 | cut -d" " -f2`
printf "\n\n Info for next step (external dataClay HOST:PORT): $LMHOST:$LMPORT\n\n"

