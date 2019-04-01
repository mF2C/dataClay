#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
PROJECT_PATH="$SCRIPTDIR/../.."

echo " #################################### " 
echo " Stopping and cleaning " 
echo " #################################### "

docker-compose -f $SCRIPTDIR/docker-compose-federation.yml down --remove-orphans
docker-compose -f $SCRIPTDIR/docker-compose.yml down

echo ""
echo ""
echo " Cleaning stubs directories "
rm -Rf $PROJECT_PATH/stubs
rm -Rf $PROJECT_PATH/stubs0
rm -Rf $PROJECT_PATH/stubs1
rm -Rf $PROJECT_PATH/stubs2
echo " Cleaning bin directories "
rm -Rf $PROJECT_PATH/bin
rm -Rf $PROJECT_PATH/bin0
rm -Rf $PROJECT_PATH/bin1
rm -Rf $PROJECT_PATH/bin2
echo " Done! "  

echo ""
echo " #################################### " 
echo " DataClay stopped and cleaned " 
echo " #################################### "
