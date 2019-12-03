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
GLOBALFILE=$PROJECT_PATH/cfgfiles/global.properties
pushd $PROJECT_PATH
###################### TESTS ############################## 

# Initialize
export DATACLAYCLIENTCONFIG=$CFGFILE
export DATACLAYSESSIONCONFIG=$SESSIONFILE
export DATACLAYGLOBALCONFIG=$GLOBALFILE

for filename in $JSONDIR/*.json; do

	echo " ***** PROCESSING $filename ****** "

	output=$(bash $COMMONDIR/runApp.sh "AGENT" "create" $filename)
	echo "$output"
	id=$(echo $output | sed -n -e 's/^.*CREATED://p')
	bash $COMMONDIR/runApp.sh "AGENT" "check-equals" $id $filename 
	
	# change the name and updated field
	tmpfilename=$(basename $filename)
	current_date=$(date)
	cp $filename /tmp/$tmpfilename
	sed -i "s/resourcename/$tmpfilename/g" /tmp/$tmpfilename
	sed -i "s/updatetime/$current_date/g" /tmp/$tmpfilename
	cat /tmp/$tmpfilename
	bash $COMMONDIR/runApp.sh "AGENT" "update" $id /tmp/$tmpfilename
	bash $COMMONDIR/runApp.sh "AGENT" "check-equals" $id /tmp/$tmpfilename
	
	echo " *********** "
	
done

popd
