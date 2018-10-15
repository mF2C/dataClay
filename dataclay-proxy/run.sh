#!/bin/bash

bash waitForBackends.sh
bash registerModel_v2.sh
bash buildApp.sh

java -cp $(pwd)/stubs:$(pwd)/proxy.jar com.sixsq.dataclay.proxy
