#!/bin/bash
set -e #exit when any command fails
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
if [[ "$OSTYPE" == "cygwin" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "msys" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "win32" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
fi
PROJECT_PATH="$SCRIPTDIR/.."
JSONDIR=$SCRIPTDIR/jsons
COMMONDIR=$SCRIPTDIR/../common
DOCKERDIR=$SCRIPTDIR/../dockers
export APP="demo.AgentTest"

LEADER_IP=`docker inspect -f '{{.NetworkSettings.Networks.dockers_default.IPAddress}}' dockers_logicmodule1_1`:1034
BACKUP_IP=`docker inspect -f '{{.NetworkSettings.Networks.dockers_default.IPAddress}}' dockers_logicmodule4_1`:5034
GRANDLEADER_IP=`docker inspect -f '{{.NetworkSettings.Networks.dockers_default.IPAddress}}' dockers_logicmodule5_1`:6034
CHILD1_IP=`docker inspect -f '{{.NetworkSettings.Networks.dockers_default.IPAddress}}' dockers_logicmodule2_1`:2034
CHILD2_IP=`docker inspect -f '{{.NetworkSettings.Networks.dockers_default.IPAddress}}' dockers_logicmodule3_1`:4034

#################### SANITY ##########################
if [ -f $JSONDIR/AgentLeader.json.orig ]; then
	mv $JSONDIR/AgentLeader.json.orig $JSONDIR/AgentLeader.json
fi
if [ -f $JSONDIR/AgentChild1.json.orig ]; then
	mv $JSONDIR/AgentChild1.json.orig $JSONDIR/AgentChild1.json
fi
if [ -f $JSONDIR/AgentChild2.json.orig ]; then
	mv $JSONDIR/AgentChild2.json.orig $JSONDIR/AgentChild2.json
fi
if [ -f $JSONDIR/AgentBackup.json.orig ]; then
	mv $JSONDIR/AgentBackup.json.orig $JSONDIR/AgentBackup.json
fi
if [ -f $JSONDIR/AgentGrandLeader.json.orig ]; then
	mv $JSONDIR/AgentGrandLeader.json.orig $JSONDIR/AgentGrandLeader.json
fi
cp $JSONDIR/AgentLeader.json $JSONDIR/AgentLeader.json.orig
cp $JSONDIR/AgentChild1.json $JSONDIR/AgentChild1.json.orig
cp $JSONDIR/AgentChild2.json $JSONDIR/AgentChild2.json.orig
cp $JSONDIR/AgentBackup.json $JSONDIR/AgentBackup.json.orig
cp $JSONDIR/AgentGrandLeader.json $JSONDIR/AgentGrandLeader.json.orig

##############################################

# Agent initialization of IPs

sed -i -e "s/leader_ip_here/${GRANDLEADER_IP}/g" $JSONDIR/AgentLeader.json
sed -i -e "s/backup_ip_here/${BACKUP_IP}/g" $JSONDIR/AgentLeader.json
sed -i -e "s/leader_ip_here/${LEADER_IP}/g" $JSONDIR/AgentChild1.json
sed -i -e "s/leader_ip_here/${LEADER_IP}/g" $JSONDIR/AgentChild2.json
sed -i -e "s/my_ip_here/${LEADER_IP}/g" $JSONDIR/AgentLeader.json
sed -i -e "s/my_ip_here/${CHILD1_IP}/g" $JSONDIR/AgentChild1.json
sed -i -e "s/my_ip_here/${CHILD2_IP}/g" $JSONDIR/AgentChild2.json
sed -i -e "s/my_ip_here/${GRANDLEADER_IP}/g" $JSONDIR/AgentGrandLeader.json
sed -i -e "s/my_ip_here/${BACKUP_IP}/g" $JSONDIR/AgentBackup.json

LEADER_CFGFILE=$PROJECT_PATH/cfgfiles/clientLeader.properties
CHILD1_CFGFILE=$PROJECT_PATH/cfgfiles/clientChild1.properties
CHILD2_CFGFILE=$PROJECT_PATH/cfgfiles/clientChild2.properties
BACKUP_CFGFILE=$PROJECT_PATH/cfgfiles/clientBackup.properties
GRANDLEADER_CFGFILE=$PROJECT_PATH/cfgfiles/clientGrandLeader.properties

LEADER_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionLeader.properties
CHILD1_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionChild1.properties
CHILD2_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionChild2.properties
BACKUP_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionBackup.properties
GRANDLEADER_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionGrandLeader.properties

pushd $PROJECT_PATH
###################### CREATE AGENTS ############################## 

# Initialize
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "create" $JSONDIR/AgentLeader.json 2> /dev/null

export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "create" $JSONDIR/AgentChild1.json 2> /dev/null

export DATACLAYCLIENTCONFIG=$CHILD2_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD2_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD2" "create" $JSONDIR/AgentChild2.json 2> /dev/null

export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "create" $JSONDIR/AgentBackup.json 2> /dev/null

export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "create" $JSONDIR/AgentGrandLeader.json 2> /dev/null


#################################################### 

# Grand leader creates device
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "create" $JSONDIR/DeviceGrandLeader.json 

# Leader creates device
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "create" $JSONDIR/DeviceLeader.json 

# Child1 creates device
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "create" $JSONDIR/DeviceChild1.json 2> /dev/null
bash $COMMONDIR/runApp.sh "CHILD1" "create" $JSONDIR/DeviceDynamicsChild1.json 2> /dev/null

# Leader checks device of child is present 
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "query" "device" "2" #expected 2 devices 
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/0000" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/1111" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device-dynamic/1111" $JSONDIR/DeviceDynamicsChild1.json 

# Child2 creates agent and device
export DATACLAYCLIENTCONFIG=$CHILD2_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD2_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD2" "create" $JSONDIR/DeviceChild2.json 2> /dev/null
bash $COMMONDIR/runApp.sh "CHILD2" "create" $JSONDIR/DeviceDynamicsChild2.json 2> /dev/null

# Leader checks device of child is present 
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "query" "device" "3" #expected 3 devices 
bash $COMMONDIR/runApp.sh "LEADER" "query" "device-dynamic" "2" #expected 2 device-dynamics
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/0000" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/1111" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/2222" $JSONDIR/DeviceChild2.json
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device-dynamic/1111" $JSONDIR/DeviceDynamicsChild1.json 
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device-dynamic/2222" $JSONDIR/DeviceDynamicsChild2.json 

# GRAND Leader checks device of child is present 
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device" "4" #expected 4 devices 
bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device-dynamic" "2" #expected 2 device-dynamics
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/0000" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/1111" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/2222" $JSONDIR/DeviceChild2.json
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/4444" $JSONDIR/DeviceGrandLeader.json
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device-dynamic/1111" $JSONDIR/DeviceDynamicsChild1.json 
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device-dynamic/2222" $JSONDIR/DeviceDynamicsChild2.json 

# Child1 update device 
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "update" "device/1111" $JSONDIR/UpdatedDeviceForFederation1.json 2> /dev/null

# Leader checks if can see update
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/1111" $JSONDIR/UpdatedDeviceForFederation1.json

# grand Leader checks if can see update
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/1111" $JSONDIR/UpdatedDeviceForFederation1.json

# Leader updates device 
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "update" "device/1111" $JSONDIR/UpdatedDeviceForFederation3.json 

# Child checks if can see update
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "check-equals" "device/1111" $JSONDIR/UpdatedDeviceForFederation3.json

# GRANDLEADER checks if can see update
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/1111" $JSONDIR/UpdatedDeviceForFederation3.json


echo ""
echo " #################################### " 
echo " SHUT DOWN LEADER "
echo " #################################### " 

pushd $DOCKERDIR
docker-compose -f docker-compose-federation.yml stop logicmodule1 ds1java1 
popd

echo ""
echo " #################################### " 
echo " NEW LEADER IS BACKUP "
echo " #################################### " 

# Backup checks have the same than elader
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device" "1" #expected only leader device
bash $COMMONDIR/runApp.sh "BACKUP" "check-equals" "device/0000" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device-dynamic" "0" 

popd 

############################# Sanity ##################################
mv $JSONDIR/AgentLeader.json.orig $JSONDIR/AgentLeader.json
mv $JSONDIR/AgentChild1.json.orig $JSONDIR/AgentChild1.json
mv $JSONDIR/AgentChild2.json.orig $JSONDIR/AgentChild2.json
mv $JSONDIR/AgentBackup.json.orig $JSONDIR/AgentBackup.json
mv $JSONDIR/AgentGrandLeader.json.orig $JSONDIR/AgentGrandLeader.json
