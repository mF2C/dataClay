#!/bin/bash

LMHOST=logicmodule2 #default for mf2c demo
LMPORT=2034 #default for mf2c demo
if [ $# -eq 2 ]; then
	LMHOST=$1
	LMPORT=$2
fi

DCID=`cat ../leader/.dcid`
if [ -z $DCID ]; then
	echo "[ERROR] Leader dataClay ID not found, is it initialized?"
	exit -1
fi

../../../tool/dClayTool.sh RegisterDataClay $DCID $DCID $LMHOST $LMPORT
echo ""
echo "   Discovered external dataClay, with info:"
echo ""
echo "        $DCID:$LMHOST:$LMPORT   <---- pass this in next step"
echo ""
