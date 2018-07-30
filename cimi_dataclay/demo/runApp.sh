#!/bin/bash

DCLIB="../tool/lib/dataclayclient.jar"
DCDEPS="../tool/lib/dependencies"
APP="demo.ClojureMockup"
BASECP="stubs:bin"
echo ""
echo "Executing: "
echo ""
echo "   java -cp $BASECP:$DCLIB:$DCDEPS/* $APP $@"
echo ""
java -Dlog4j.configurationFile=file:cfglog/log4j2.xml -cp $BASECP:$DCLIB:$DCDEPS/* $APP $@
