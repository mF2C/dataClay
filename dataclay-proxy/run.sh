#!/bin/bash

bash waitForBackends.sh
#bash createAccountAndGetStubs.sh
#bash buildApp.sh

java -Dlog4j.configurationFile=file:cfglog/log4j2.xml -cp $(pwd)/proxy.jar com.sixsq.dataclay.proxy
