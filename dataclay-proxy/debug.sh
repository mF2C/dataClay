#!/bin/bash -xe

lein do clean, test, uberjar
cp ./target/proxy-trunk-standalone.jar proxy.jar
docker build --tag mf2c/dataclay-proxy:test . 
