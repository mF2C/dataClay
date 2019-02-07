#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
function docker-push {
	TAG=$1
	docker push bscdataclay/postgres:${TAG}
	docker push bscdataclay/logicmodule:${TAG}
	docker push bscdataclay/logicmodule:${TAG}-openjdk8
	docker push bscdataclay/dsjava:${TAG}
	docker push bscdataclay/dsjava:${TAG}-openjdk8
	docker push bscdataclay/dspython:${TAG}
	docker push bscdataclay/dspython:${TAG}-py3.6
	docker push bscdataclay/dspython:${TAG}-py2.7
}  

# Push trunk
docker-push trunk