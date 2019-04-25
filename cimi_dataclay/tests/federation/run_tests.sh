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
DOCKERSDIR=$SCRIPTDIR/../dockers

bash $DOCKERSDIR/stopDataClaysAndClean.sh
bash $DOCKERSDIR/startDataClay.sh $DOCKERSDIR/docker-compose-federation.yml

echo " #################################### " 
echo " RUNNING TESTS "
echo " #################################### " 
echo ""
bash $SCRIPTDIR/federation_tests.sh
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

#bash $DOCKERSDIR/stopDataClaysAndClean.sh



