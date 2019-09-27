#!/bin/bash
SCRIPTDIR="$(cd "$(dirname "$0")" && pwd -P)"
if [[ "$OSTYPE" == "cygwin" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "msys" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "win32" ]]; then
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
fi
echo $SCRIPTDIR
COMMONDIR=$SCRIPTDIR/../common
DOCKERSDIR=$SCRIPTDIR/../dockers
JSONDIR=$SCRIPTDIR/../jsons

bash $DOCKERSDIR/stopDataClaysAndClean.sh
bash $DOCKERSDIR/startDataClay.sh $DOCKERSDIR/docker-compose-ssl.yml

export APP="demo.ClojureMockup"
export DATACLAYCLIENTCONFIG=$PROJECT_PATH/cfgfiles/client.secure.properties

echo " #################################### " 
echo " RUNNING TESTS "
echo " #################################### " 
echo ""
bash $SCRIPTDIR/security_tests.sh
retVal=$?
if [ $retVal -ne 0 ]; then
	echo " +++++++++++++++++++++++++++++++++++ " 
	echo " TESTS FAILED :( "
	echo " +++++++++++++++++++++++++++++++++++ " 
	echo ""
else
	echo " #################################### " 
	echo " TESTS PASSED :)"
	echo " #################################### " 
	echo ""
fi

bash $DOCKERSDIR/stopDataClaysAndClean.sh
exit $retVal
