#!/bin/bash

INPUTDIR=./files
REMOTEPATH=/tmp

docker cp $INPUTDIR orchestration_ds1java_1:$REMOTEPATH
