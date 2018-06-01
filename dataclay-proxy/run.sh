#!/bin/sh

sh registerModel_v2.sh

sh buildApp.sh

java -jar proxy.jar