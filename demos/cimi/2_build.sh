#!/bin/bash

DCLIB="./lib/dataclayclient.jar"

if [ ! -f $DCLIB ]; then
	echo ""
	echo " Warning. dataClay client lib:'$DCLIB' not found. Trying to fix it ..."
	echo ""

	./dataClayLink.sh

	if [ ! -f $DCLIB ]; then
		echo ""
		echo " Could not fix the problem. Copy (or create a link) $DCLIB"
		echo ""
		exit -1
	fi
	echo "Problem fixed"
fi

echo ""
echo -n "Compiling ... "
javac -cp stubs:$DCLIB src/app/*.java -d bin/
echo " done"
