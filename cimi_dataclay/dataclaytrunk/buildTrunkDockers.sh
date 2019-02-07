#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

# Defaults version of java and python are the ones defined in docker-build 
# Build and start dataClay
pushd ../..
printMsg "Building dataClay using full start"
bash dataClayFullStart.sh --nostart
popd 