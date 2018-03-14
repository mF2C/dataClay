#!/bin/bash

INPUTDIR=./files
REMOTEPATH=/tmp

DSs=`docker inspect --format="{{.Id}}" \`docker ps | grep ds.java | cut -d" " -f1\``
for i in $DSs; do
	docker cp $INPUTDIR $i:$REMOTEPATH
done
