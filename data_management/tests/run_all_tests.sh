#!/bin/bash
pushd clojure
bash run_tests.sh
if [ $? -ne 0 ]; then exit -1; fi
popd 

pushd queries
bash run_tests.sh
if [ $? -ne 0 ]; then exit -1; fi
popd 

pushd security
bash run_tests.sh
if [ $? -ne 0 ]; then exit -1; fi
popd

pushd federation
bash run_tests.sh
if [ $? -ne 0 ]; then exit -1; fi
popd