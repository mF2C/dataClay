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
LEADER_IP2=`docker inspect -f '{{.NetworkSettings.Networks.dockers_default.IPAddress}}' dockers_logicmodule6_1`:7034

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
if [ -f $JSONDIR/AgentLeader2.json.orig ]; then
	mv $JSONDIR/AgentLeader2.json.orig $JSONDIR/AgentLeader2.json
fi
if [ -f $JSONDIR/DeviceLeader.json.orig ]; then
	mv $JSONDIR/DeviceLeader.json.orig $JSONDIR/DeviceLeader.json
fi
if [ -f $JSONDIR/DeviceChild1.json.orig ]; then
	mv $JSONDIR/DeviceChild1.json.orig $JSONDIR/DeviceChild1.json
fi
if [ -f $JSONDIR/DeviceChild2.json.orig ]; then
	mv $JSONDIR/DeviceChild2.json.orig $JSONDIR/DeviceChild2.json
fi
if [ -f $JSONDIR/DeviceGrandLeader.json.orig ]; then
	mv $JSONDIR/DeviceGrandLeader.json.orig $JSONDIR/DeviceGrandLeader.json
fi
if [ -f $JSONDIR/DeviceLeader2.json.orig ]; then
	mv $JSONDIR/DeviceLeader2.json.orig $JSONDIR/DeviceLeader2.json
fi
cp $JSONDIR/AgentLeader.json $JSONDIR/AgentLeader.json.orig
cp $JSONDIR/AgentChild1.json $JSONDIR/AgentChild1.json.orig
cp $JSONDIR/AgentChild2.json $JSONDIR/AgentChild2.json.orig
cp $JSONDIR/AgentBackup.json $JSONDIR/AgentBackup.json.orig
cp $JSONDIR/AgentGrandLeader.json $JSONDIR/AgentGrandLeader.json.orig
cp $JSONDIR/AgentLeader2.json $JSONDIR/AgentLeader2.json.orig

cp $JSONDIR/DeviceLeader.json $JSONDIR/DeviceLeader.json.orig
cp $JSONDIR/DeviceChild1.json $JSONDIR/DeviceChild1.json.orig
cp $JSONDIR/DeviceChild2.json $JSONDIR/DeviceChild2.json.orig
cp $JSONDIR/DeviceGrandLeader.json $JSONDIR/DeviceGrandLeader.json.orig
cp $JSONDIR/DeviceLeader2.json $JSONDIR/DeviceLeader2.json.orig
##############################################

# Agent initialization of IPs

# backups
sed -i -e "s/backup_ip_here/${BACKUP_IP}/g" $JSONDIR/AgentLeader2.json

# leaders
sed -i -e "s/leader_ip_here/${LEADER_IP}/g" $JSONDIR/AgentChild1.json
sed -i -e "s/leader_ip_here/${LEADER_IP}/g" $JSONDIR/AgentChild2.json
sed -i -e "s/leader_ip_here/${GRANDLEADER_IP}/g" $JSONDIR/AgentLeader.json
sed -i -e "s/leader_ip_here/${GRANDLEADER_IP}/g" $JSONDIR/AgentBackup.json
sed -i -e "s/leader_ip_here/${GRANDLEADER_IP}/g" $JSONDIR/AgentLeader2.json

# ips
sed -i -e "s/my_ip_here/${LEADER_IP}/g" $JSONDIR/AgentLeader.json
sed -i -e "s/my_ip_here/${CHILD1_IP}/g" $JSONDIR/AgentChild1.json
sed -i -e "s/my_ip_here/${CHILD2_IP}/g" $JSONDIR/AgentChild2.json
sed -i -e "s/my_ip_here/${GRANDLEADER_IP}/g" $JSONDIR/AgentGrandLeader.json
sed -i -e "s/my_ip_here/${BACKUP_IP}/g" $JSONDIR/AgentBackup.json
sed -i -e "s/my_ip_here/${LEADER_IP2}/g" $JSONDIR/AgentLeader2.json

# children

# leader has two children: child1, child2
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\"${CHILD1_IP}\", \"${CHILD2_IP}\"\]/g" $JSONDIR/AgentLeader.json
# grand leader has to children: leader, leader2
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\"${LEADER_IP}\", \"${LEADER_IP2}\", \"${BACKUP_IP}\"\]/g" $JSONDIR/AgentGrandLeader.json

LEADER_CFGFILE=$PROJECT_PATH/cfgfiles/clientLeader.properties
CHILD1_CFGFILE=$PROJECT_PATH/cfgfiles/clientChild1.properties
CHILD2_CFGFILE=$PROJECT_PATH/cfgfiles/clientChild2.properties
BACKUP_CFGFILE=$PROJECT_PATH/cfgfiles/clientBackup.properties
GRANDLEADER_CFGFILE=$PROJECT_PATH/cfgfiles/clientGrandLeader.properties
LEADER2_CFGFILE=$PROJECT_PATH/cfgfiles/clientLeader2.properties

LEADER_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionLeader.properties
CHILD1_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionChild1.properties
CHILD2_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionChild2.properties
BACKUP_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionBackup.properties
GRANDLEADER_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionGrandLeader.properties
LEADER2_SESSIONFILE=$PROJECT_PATH/cfgfiles/sessionLeader2.properties

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

export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "create" $JSONDIR/AgentLeader2.json 2> /dev/null

#################################################### 

# Grand leader creates device
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "create" $JSONDIR/DeviceGrandLeader.json 

# Leader creates device
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "create" $JSONDIR/DeviceLeader.json 

# Leader 2 creates device
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "create" $JSONDIR/DeviceLeader2.json 

# Child1 creates device
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "create" $JSONDIR/DeviceChild1.json 2> /dev/null
bash $COMMONDIR/runApp.sh "CHILD1" "create" $JSONDIR/DeviceDynamicsChild1.json 2> /dev/null

# Leader checks device of child is present 
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "query" "device" "null" "ADMIN" "#{ADMIN}" "2" #expected 2 devices 
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device-dynamic/child1" $JSONDIR/DeviceDynamicsChild1.json 

# Child2 creates agent and device
export DATACLAYCLIENTCONFIG=$CHILD2_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD2_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD2" "create" $JSONDIR/DeviceChild2.json 2> /dev/null
bash $COMMONDIR/runApp.sh "CHILD2" "create" $JSONDIR/DeviceDynamicsChild2.json 2> /dev/null

# Leader checks device of child is present 
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "query" "device"  "null" "ADMIN" "#{ADMIN}" "3" #expected 3 devices 
bash $COMMONDIR/runApp.sh "LEADER" "query" "device-dynamic"  "null" "ADMIN" "#{ADMIN}" "2" #expected 2 device-dynamics
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device-dynamic/child1" $JSONDIR/DeviceDynamicsChild1.json 
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device-dynamic/child2" $JSONDIR/DeviceDynamicsChild2.json 

# GRAND Leader checks device of child is present 
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device"  "null" "ADMIN" "#{ADMIN}" "5" #expected devices 
bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device-dynamic"  "null" "ADMIN" "#{ADMIN}" "2" #expected 2 device-dynamics
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/grandleader" $JSONDIR/DeviceGrandLeader.json
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device-dynamic/child1" $JSONDIR/DeviceDynamicsChild1.json 
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device-dynamic/child2" $JSONDIR/DeviceDynamicsChild2.json 

# Backup checks have only Leader 2 stuff
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device"  "null" "ADMIN" "#{ADMIN}" "1" #expected 1 device
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "0" #expected 0 device-dynamics
#bash $COMMONDIR/runApp.sh "BACKUP" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json 

##################### UPDATES

sed -i -e "s/.*hwloc.*/\"hwloc\":\"updated\",/g" $JSONDIR/DeviceChild1.json

# Child1 update device 
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "update" "device/child1" $JSONDIR/DeviceChild1.json 2> /dev/null

# Leader checks if can see update
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json

# grand Leader checks if can see update
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json

sed -i -e "s/.*hwloc.*/\"hwloc\":\"updated from leader\",/g" $JSONDIR/DeviceChild1.json

# Leader updates device 
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "update" "device/child1" $JSONDIR/DeviceChild1.json 

# Child checks if can see update
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json

# GRANDLEADER checks if can see update
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json

##################### UPDATE OBJ. IN BACKUP #####################

sed -i -e "s/.*hwloc.*/\"hwloc\":\"updated to backup\",/g" $JSONDIR/DeviceLeader2.json

# Leader updates device
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "update" "device/leader2" $JSONDIR/DeviceLeader2.json 


# GRANDLEADER checks if can see update
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json

##################### DELETE #####################


###################################################
##################### NEW LEADER #####################

# Update children agents to use leader 2
sed -i -e "s/${LEADER_IP}/${LEADER_IP2}/g" $JSONDIR/AgentChild1.json
sed -i -e "s/${LEADER_IP}/${LEADER_IP2}/g" $JSONDIR/AgentChild2.json

# leader lost children: child1, child2
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\\]/g" $JSONDIR/AgentLeader.json
# leader2 has two children: child1, child2
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\"${CHILD1_IP}\", \"${CHILD2_IP}\"\]/g" $JSONDIR/AgentLeader2.json

# Child1 update agent 
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "update" "agent/agent" $JSONDIR/AgentChild1.json
# Child2 update agent 
export DATACLAYCLIENTCONFIG=$CHILD2_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD2_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD2" "update" "agent/agent" $JSONDIR/AgentChild2.json
# Leader update agent 
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "update" "agent/agent" $JSONDIR/AgentLeader.json
# Leader2 update agent 
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "update" "agent/agent" $JSONDIR/AgentLeader2.json

# CHecks new leader has everything except Leader 1 objects
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "query" "device"  "null" "ADMIN" "#{ADMIN}" "3" #expected 3 devices 
bash $COMMONDIR/runApp.sh "LEADER2" "query" "device-dynamic"  "null" "ADMIN" "#{ADMIN}" "2" #expected 2 device-dynamics
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json 
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device-dynamic/child1" $JSONDIR/DeviceDynamicsChild1.json 
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device-dynamic/child2" $JSONDIR/DeviceDynamicsChild2.json 

# CHecks Leader 1 has only its objects
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "query" "device"  "null" "ADMIN" "#{ADMIN}" "1" #expected 1 device
bash $COMMONDIR/runApp.sh "LEADER" "query" "device-dynamic"  "null" "ADMIN" "#{ADMIN}" "0" #expected 0 device-dynamics
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 

###################################################
##################### BACKUP #####################

echo ""
echo " #################################### " 
echo " SHUT DOWN LEADER 2 "
echo " #################################### " 

pushd $DOCKERDIR
docker-compose -f docker-compose-federation.yml stop logicmodule6 ds1java6
popd

echo ""
echo " #################################### " 
echo " NEW LEADER IS BACKUP "
echo " #################################### " 

# Backup checks have the same than elader
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device"  "null" "ADMIN" "#{ADMIN}" "1" #expected only leader 2 device
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device-dynamic"  "null" "ADMIN" "#{ADMIN}" "0"  

# Update children agents to use backup leader
sed -i -e "s/${LEADER_IP2}/${BACKUP_IP}/g" $JSONDIR/AgentChild1.json
sed -i -e "s/${LEADER_IP2}/${BACKUP_IP}/g" $JSONDIR/AgentChild2.json
# Child1 update agent 
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "update" "agent/agent" $JSONDIR/AgentChild1.json 
# Child2 update agent 
export DATACLAYCLIENTCONFIG=$CHILD2_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD2_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD2" "update" "agent/agent" $JSONDIR/AgentChild2.json 


# backup has two children: child1, child2
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\"${CHILD1_IP}\", \"${CHILD2_IP}\"\]/g" $JSONDIR/AgentBackup.json
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "update" "agent/agent" $JSONDIR/AgentBackup.json 

# Backup checks if can see now all children objects
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device" "null" "ADMIN" "#{ADMIN}" "3" #expected 3 devices 
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "2" #expected 2 device-dynamics
bash $COMMONDIR/runApp.sh "BACKUP" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json #recovered from backup
bash $COMMONDIR/runApp.sh "BACKUP" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "BACKUP" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json

###################################################
##################### LOST CHILDREN #####################

#### TESTING STANDALONE LOOSING CHILDREN
# grandleader looses child Leader2
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\"${LEADER_IP}\", \"${BACKUP_IP}\"\]/g" $JSONDIR/AgentGrandLeader.json
bash $COMMONDIR/runApp.sh "GRANDLEADER" "update" "agent/agent" $JSONDIR/AgentGrandLeader.json 
# grand Leader checks 
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device" "null" "ADMIN" "#{ADMIN}" "4" #expected devices 
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "2" #expected 2 device-dynamics
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json #recovered in backup but lost child!
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/grandleader" $JSONDIR/DeviceGrandLeader.json

#bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json

#### TESTING PROPAGETE TO LEADER LOOSING CHILDREN CONNECTION
pushd $DOCKERDIR
docker-compose -f docker-compose-federation.yml stop logicmodule3 ds1java3 # lost children 2
popd

# backup has 1 children: child1
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\"${CHILD1_IP}\"\]/g" $JSONDIR/AgentBackup.json
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "update" "agent/agent" $JSONDIR/AgentBackup.json 

# check backup has only 1 device-dynamic of child 1 + 2 devices
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device" "null" "ADMIN" "#{ADMIN}" "2" #expected 3 devices 
#bash $COMMONDIR/runApp.sh "BACKUP" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "1" #expected 2 device-dynamics
#bash $COMMONDIR/runApp.sh "BACKUP" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json #recovered in backup
#bash $COMMONDIR/runApp.sh "BACKUP" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json

# check grandleader has only 3 devices and 1 device-dynamic
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device" "null" "ADMIN" "#{ADMIN}" "3" #expected devices 
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "1" #expected 2 device-dynamics
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
#bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/grandleader" $JSONDIR/DeviceGrandLeader.json

#### TESTING LOOSING CHILDREN DUE TO MOVE
pushd $DOCKERDIR
docker-compose -f docker-compose-federation.yml up -d logicmodule6 logicmodule3 ds1java6 ds1java3 # leader 2 and children 2 is back
sleep 30 # agent might be not down yet when init is called
popd

export DATACLAYCLIENTCONFIG=$CHILD2_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD2_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD2" "init" 
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "init"

NEW_CHILD2_IP=`docker inspect -f '{{.NetworkSettings.Networks.dockers_default.IPAddress}}' dockers_logicmodule3_1`:4034
NEW_LEADER_IP2=`docker inspect -f '{{.NetworkSettings.Networks.dockers_default.IPAddress}}' dockers_logicmodule6_1`:7034

# update my ips 
sed -i -e "s/${LEADER_IP2}/${NEW_LEADER_IP2}/g" $JSONDIR/AgentLeader2.json 
sed -i -e "s/${CHILD2_IP}/${NEW_CHILD2_IP}/g" $JSONDIR/AgentChild2.json 
LEADER_IP2=$NEW_LEADER_IP2
CHILD2_IP=$NEW_CHILD2_IP
export DATACLAYCLIENTCONFIG=$CHILD2_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD2_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD2" "update" "agent/agent" $JSONDIR/AgentChild2.json 
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "update" "agent/agent" $JSONDIR/AgentLeader2.json 

# backup has no children
# backup detects lost children and grand leader are notified to unfederate all objects with c2 
# c2 is notified to unfederate all objects with grand leader
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\]/g" $JSONDIR/AgentBackup.json
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "update" "agent/agent" $JSONDIR/AgentBackup.json 

# Update children agents to use leader 2
# children detect new leader and migrates objects to new leader which at the same time migrates
# objects to grand leader since L2 has it associated
sed -i -e "s/${BACKUP_IP}/${LEADER_IP2}/g" $JSONDIR/AgentChild1.json # connects to L2
sed -i -e "s/${BACKUP_IP}/${LEADER_IP2}/g" $JSONDIR/AgentChild2.json # connects to L2
export DATACLAYCLIENTCONFIG=$CHILD1_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD1_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD1" "update" "agent/agent" $JSONDIR/AgentChild1.json 
export DATACLAYCLIENTCONFIG=$CHILD2_CFGFILE
export DATACLAYSESSIONCONFIG=$CHILD2_SESSIONFILE
bash $COMMONDIR/runApp.sh "CHILD2" "update" "agent/agent" $JSONDIR/AgentChild2.json 

# new children 
sed -i -e "s/.*childrenIPs.*/\"childrenIPs\":\[\"${CHILD1_IP}\", \"${CHILD2_IP}\"\]/g" $JSONDIR/AgentLeader2.json
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "update" "agent/agent" $JSONDIR/AgentLeader2.json 

# leader2 has two children now and no leader, no backup
# no leader, current children are notified to unfederate all its objects with grand leader
sed -i -e "s/${GRANDLEADER_IP}//g" $JSONDIR/AgentLeader2.json 
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "update" "agent/agent" $JSONDIR/AgentLeader2.json 

# lost backup
sed -i -e "s/${BACKUP_IP}//g" $JSONDIR/AgentLeader2.json 
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "update" "agent/agent" $JSONDIR/AgentLeader2.json 

# checks 
# leader 2 can see child1, child2 and leader 2
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "query" "device" "null" "ADMIN" "#{ADMIN}" "3" #expected devices 
bash $COMMONDIR/runApp.sh "LEADER2" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "2" #expected device-dynamics
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json

# BACKUP cannot see anything
export DATACLAYCLIENTCONFIG=$BACKUP_CFGFILE
export DATACLAYSESSIONCONFIG=$BACKUP_SESSIONFILE
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device" "null" "ADMIN" "#{ADMIN}" "0" #expected devices 
bash $COMMONDIR/runApp.sh "BACKUP" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "0" #expected device-dynamics

# leader 1 can only see its device
export DATACLAYCLIENTCONFIG=$LEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER" "query" "device" "null" "ADMIN" "#{ADMIN}" "1" #expected devices 
bash $COMMONDIR/runApp.sh "LEADER" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "0" #expected device-dynamics
bash $COMMONDIR/runApp.sh "LEADER" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 

# grandleader can only see leader 1 and its device
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device" "null" "ADMIN" "#{ADMIN}" "2" #expected devices 
bash $COMMONDIR/runApp.sh "GRANDLEADER" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "0" #expected device-dynamics
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "GRANDLEADER" "check-equals" "device/grandleader" $JSONDIR/DeviceGrandLeader.json

##################### LOST/MOVE PARENT #####################

#### TESTING PROPAGETE TO CHILDREN NEW GRAND LEADER (children unfederates with grandleader)
# grand leader has a new leader, all its objects are send to new leader and all its children also
sed -i -e "s/.*leader_ip.*/\"leader_ip\":\"${LEADER_IP2}\",/g" $JSONDIR/AgentGrandLeader.json
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "update" "agent/agent" $JSONDIR/AgentGrandLeader.json 

# check that now L2 can see: devices of child1, child2, grand leader, leader 2 and leader 1 (backup has no device)
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "query" "device" "null" "ADMIN" "#{ADMIN}" "5" #expected devices 
bash $COMMONDIR/runApp.sh "LEADER2" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "2" #expected device-dynamics
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/leader" $JSONDIR/DeviceLeader.json 
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/grandleader" $JSONDIR/DeviceGrandLeader.json
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json

#### TESTING PROPAGETE TO CHILDREN LOOSING PARENT (back to previous)
sed -i -e "s/.*leader_ip.*/\"leader_ip\":\"\",/g" $JSONDIR/AgentGrandLeader.json
export DATACLAYCLIENTCONFIG=$GRANDLEADER_CFGFILE
export DATACLAYSESSIONCONFIG=$GRANDLEADER_SESSIONFILE
bash $COMMONDIR/runApp.sh "GRANDLEADER" "update" "agent/agent" $JSONDIR/AgentGrandLeader.json 


# check leader2 can see only leader2, child1 and child2
export DATACLAYCLIENTCONFIG=$LEADER2_CFGFILE
export DATACLAYSESSIONCONFIG=$LEADER2_SESSIONFILE
bash $COMMONDIR/runApp.sh "LEADER2" "query" "device" "null" "ADMIN" "#{ADMIN}" "3" #expected devices 
bash $COMMONDIR/runApp.sh "LEADER2" "query" "device-dynamic" "null" "ADMIN" "#{ADMIN}" "2" #expected device-dynamics
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/leader2" $JSONDIR/DeviceLeader2.json 
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child1" $JSONDIR/DeviceChild1.json
bash $COMMONDIR/runApp.sh "LEADER2" "check-equals" "device/child2" $JSONDIR/DeviceChild2.json


#### TESTING PROPAGETE TO CHILDREN LOOSING PARENT connection


### LEADER COMES BACK FROM ERROR AND UNFEDERATE ALL OBJECTS WITH CHILDREN AND LEADER (updates of Agent)

## garbage collector enabled? + restart

# multiple leves notification (children and grandleader)

############################# Sanity ##################################
popd
mv $JSONDIR/AgentLeader.json.orig $JSONDIR/AgentLeader.json
mv $JSONDIR/AgentLeader2.json.orig $JSONDIR/AgentLeader2.json
mv $JSONDIR/AgentChild1.json.orig $JSONDIR/AgentChild1.json
mv $JSONDIR/AgentChild2.json.orig $JSONDIR/AgentChild2.json
mv $JSONDIR/AgentBackup.json.orig $JSONDIR/AgentBackup.json
mv $JSONDIR/AgentGrandLeader.json.orig $JSONDIR/AgentGrandLeader.json
mv $JSONDIR/DeviceLeader.json.orig $JSONDIR/DeviceLeader.json
mv $JSONDIR/DeviceLeader2.json.orig $JSONDIR/DeviceLeader2.json
mv $JSONDIR/DeviceChild1.json.orig $JSONDIR/DeviceChild1.json
mv $JSONDIR/DeviceChild2.json.orig $JSONDIR/DeviceChild2.json
mv $JSONDIR/DeviceGrandLeader.json.orig $JSONDIR/DeviceGrandLeader.json