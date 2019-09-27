#!/bin/bash
if [ -f "state.txt" ]; then
	if [ "$(cat state.txt)" == "READY" ]; then 
		echo "HEALTHY"
		exit 0
	else
		echo "UNHEALTHY"
		exit -1
	fi
else 
	exit -1
fi