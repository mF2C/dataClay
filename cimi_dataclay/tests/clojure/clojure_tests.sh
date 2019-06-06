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

for filename in $JSONDIR/*.json; do
	output=$(bash $COMMONDIR/runApp.sh "AGENT" "create" $filename 2> /dev/null)
	echo "$output"
	id=$(echo $output | sed -n -e 's/^.*CREATED://p')
	bash $COMMONDIR/runApp.sh "AGENT" "check-equals" $id $filename 
	
	# change the name 
	tmpfilename=$(basename $filename)
	cp $filename /tmp/$tmpfilename
	sed -i "s/resourcename/$tmpfilename/g" /tmp/$tmpfilename
	cat /tmp/$tmpfilename
	bash $COMMONDIR/runApp.sh "AGENT" "update" $id /tmp/$tmpfilename
	bash $COMMONDIR/runApp.sh "AGENT" "check-equals" $id /tmp/$tmpfilename
	
done

#TODO: Complex update and delete tests

# Queries
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "User1" "USER" "1"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "User1" "ADMIN" "1"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "User2" "USER" "0"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "null" "1" # default?
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "ANON" "1" # error!

bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "ADMIN" "1"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "null" "USER" "0"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "User1" "null" "1"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" "null" "User2" "null" "0"

EXPRESSION="[:Filter [:AndExpr [:Comp [:Attribute \"os\"] [:EqOp \"=\"] [:SingleQuoteString \"'Windows'\"]]]]"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" $EXPRESSION "null" "ADMIN" "1"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" $EXPRESSION "null" "USER" "0"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" $EXPRESSION "User1" "null" "1"
bash $COMMONDIR/runApp.sh "AGENT" "query" "device" $EXPRESSION "User2" "null" "0"

# Query after delete
#bash $COMMONDIR/runApp.sh "AGENT" "query" "device" $EXPRESSION "User1" "USER" "0"


popd
