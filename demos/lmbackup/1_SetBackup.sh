#!/bin/bash
TOOLPATH=../../tool/dClayTool.sh
MAINLM=`docker inspect -f '{{.NetworkSettings.Networks.orchestration_default.IPAddress}}' orchestration_logicmodule1_1`:1034
OTHERLM=`docker inspect -f '{{.NetworkSettings.Networks.orchestration_default.IPAddress}}' orchestration_logicmodule_bk_1`:3034
echo
echo "***"
echo "*** Setting backup for LM $MAINLM to $OTHERLM"
echo "***"
$TOOLPATH SetMetadataBackup $MAINLM $OTHERLM
