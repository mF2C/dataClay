#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
PROJECT_PATH="$SCRIPTDIR/.."
LOG4JCONF=-Dlog4j.configurationFile=file:cfglog/log4j2.xml
pushd $PROJECT_PATH > /dev/null
CMD="mvn clean --quiet compile exec:java -e $LOG4JCONF -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="$APP" \"-Dexec.args=${@}\""
eval $CMD
retVal=$?
popd > /dev/null
exit $retVal 

