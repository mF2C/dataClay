#!/bin/sh

sh registerModel_v2.sh

sh buildApp.sh

java -cp $(pwd)/stubs:$(pwd)/proxy.jar com.sixsq.dataclay.proxy