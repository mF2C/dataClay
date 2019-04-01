#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
if [[ "$OSTYPE" == "cygwin" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "msys" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "win32" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
fi
echo $SCRIPTDIR
TOOLSBASE=$SCRIPTDIR/../../../../Tools/
DOCKERSDIR=$SCRIPTDIR/../dockers
TOOLSPATH="$TOOLSBASE/dClayTool.sh"
CLASSPATH="$TOOLSBASE/dataclayclient.jar:$TOOLSBASE/lib/*:lib/*"
LOG4JCONF=-Dlog4j.configurationFile=file:cfglog/log4j2.xml
COMMONDIR=$SCRIPTDIR/../common

bash $DOCKERSDIR/stopDataClaysAndClean.sh
bash $DOCKERSDIR/startDataClay.sh $DOCKERSDIR/docker-compose-federation.yml

echo " #################################### " 
echo " RUNNING DEMO "
echo " #################################### " 
echo ""
bash $SCRIPTDIR/runApp.sh
retVal=$?
if [ $retVal -ne 0 ]; then
	echo " +++++++++++++++++++++++++++++++++++ " 
	echo " DEMO FAILED :( "
	echo " +++++++++++++++++++++++++++++++++++ " 
	echo ""
	echo " NOTE: dockers are not stopped to allow debugging "
else
	echo " #################################### " 
	echo " DEMO PASSED :)"
	echo " #################################### " 
	echo ""
	bash $DOCKERSDIR/stopDataClaysAndClean.sh
	
fi



