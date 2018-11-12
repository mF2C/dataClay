#!/bin/bash

bash waitForBackends.sh
bash createAccountAndGetStubs.sh
bash buildApp.sh

java -cp $(pwd)/stubs:$(pwd)/proxy.jar com.sixsq.dataclay.proxy
