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
bash $DOCKERSDIR/startDataClay.sh $DOCKERSDIR/docker-compose.yml

export APP="demo.ClojureMockup"
export DATACLAYCLIENTCONFIG=$PROJECT_PATH/cfgfiles/client.properties

echo " #################################### " 
echo " RUNNING DEMO "
echo " #################################### " 
echo ""
bash $COMMONDIR/runApp.sh ${JSONDIR}/Services.json ${JSONDIR}/Devices.json ${JSONDIR}/DeviceDynamics.json \
${JSONDIR}/ServiceInstances.json ${JSONDIR}/AgreementTest.json ${JSONDIR}/ServiceTest.json \
${JSONDIR}/AgreementFull.json ${JSONDIR}/CallbackFull.json \
${JSONDIR}/DeviceFull.json ${JSONDIR}/DeviceDynamicFull.json \
${JSONDIR}/EmailFull.json ${JSONDIR}/FogAreaFull.json \
${JSONDIR}/ServiceFull.json \ ${JSONDIR}/ServiceInstanceFull.json \
${JSONDIR}/ServiceOperationReportFull.json ${JSONDIR}/CredentialFull.json ${JSONDIR}/Events.json \
${JSONDIR}/QosModel.json

retVal=$?
if [ $retVal -ne 0 ]; then
	echo " +++++++++++++++++++++++++++++++++++ " 
	echo " DEMO FAILED :( "
	echo " +++++++++++++++++++++++++++++++++++ " 
	echo ""
else
	echo " #################################### " 
	echo " DEMO PASSED :)"
	echo " #################################### " 
	echo ""
fi

bash $DOCKERSDIR/stopDataClaysAndClean.sh
