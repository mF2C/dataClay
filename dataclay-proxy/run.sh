#!/bin/bash

bash waitForBackends.sh

java -Dlog4j.configurationFile=file:cfglog/log4j2.xml -cp $(pwd)/proxy.jar com.sixsq.dataclay.proxy
