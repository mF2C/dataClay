#!/bin/bash

DCLIB="../tool/lib/dataclayclient.jar"
APP="demo.ClojureMockup"
BASECP="stubs:bin"
echo ""
echo "Executing: "
echo ""
echo "   java -cp $BASECP:$DCLIB $APP $@"
echo ""
java -Dlog4j.configurationFile=file:cfglog/log4j2.xml -cp $BASECP:$DCLIB $APP $@
