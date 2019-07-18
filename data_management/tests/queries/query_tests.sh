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
JSONDIR=$SCRIPTDIR/../jsons
COMMONDIR=$SCRIPTDIR/../common
DOCKERDIR=$SCRIPTDIR/../dockers
export APP="demo.AgentTest"

CFGFILE=$PROJECT_PATH/cfgfiles/client.properties
SESSIONFILE=$PROJECT_PATH/cfgfiles/session.properties
pushd $PROJECT_PATH
###################### TESTS ############################## 

# Initialize
export DATACLAYCLIENTCONFIG=$CFGFILE
export DATACLAYSESSIONCONFIG=$SESSIONFILE
	
bash $COMMONDIR/runApp.sh "AGENT" "create" $JSONDIR/Device.json
EXPRESSION='[:Filter [:AndExpr [:Comp [:Attribute \"os\"] [:EqOp \"=\"] [:SingleQuoteString \"'Linux'\"]]]]'
echo "${EXPRESSION}"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "'${EXPRESSION}'" "null" "'#{ANON ADMIN}'" "1" #allowed role admin
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "'${EXPRESSION}'" "null" "#{ADMIN}" "1" #allowed role admin
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "'${EXPRESSION}'" "null" "#{OWNERROLE}" "1" #allowed role admin
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "'${EXPRESSION}'" "mf2c" "null" "1" #allowed user mf2c
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "'${EXPRESSION}'" "ANON" "null" "0" #not allowed user mf2c
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "'${EXPRESSION}'" "null" "null" "0" #not allowed 

# USERS
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "null" "0" # no user or role
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "mf2c" "null" "1" #allowed user by rule
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "ADMIN" "null" "0" #admin is not an allowed user but role
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "ANON" "null" "0" #not allowed user

# ROLES
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "#{OWNERROLE}" "1" #allowed role OWNER role
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "#{ADMIN}" "1" #allowed role admin
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "'#{OWNERROLE ADMIN}'" "1" #allowed role OWNER role
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "'[ADMIN ANON]'" "1" #allowed role admin
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "#{mf2c}" "0" #not allowed role (is user type)

# Query after delete
#bash $COMMONDIR/runApp.sh "AGENT" "query" "device" $EXPRESSION "User1" "USER" "0"


popd
