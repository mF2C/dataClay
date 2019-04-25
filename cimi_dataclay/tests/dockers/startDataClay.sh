#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
COMPOSEFILE=$SCRIPTDIR/docker-compose.yml
PROJECT_PATH="$SCRIPTDIR/.."
COMPOSEFILE=$1

pushd $SCRIPTDIR
docker-compose -f $COMPOSEFILE down
docker-compose -f $COMPOSEFILE up -d

popd
