#!/bin/bash

DCID=`cat ../leader/.dcid`
if [ -z $DCID ]; then
	echo "[ERROR] Leader dataClay ID not found, is it initialized?"
	exit -1
fi

../../../tool/dClayTool.sh RegisterDataClay $DCID $DCID logicmodule2 2034
echo ""
echo "   Discovered external dataClay, with info:"
echo ""
echo "        $DCID:logicmodule2:2034   <---- pass this in next step"
echo ""
