#!/bin/bash -xe

rm -f proxy.jar 
lein do clean, uberjar
cp ./target/proxy-trunk-standalone.jar proxy.jar
docker build --tag mf2c/dataclay-proxy:test . 
