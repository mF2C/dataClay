#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
DATACLAY_LM_ORIGIN_DOCKER_NAME=$1 # ex: dockers_lmpostgres1_1
TMPDUMP=$2
LOGICMODULE_DB_HANDLER=$3
if [ -z $LOGICMODULE_DB_HANDLER ]; then
 	LOGICMODULE_DB_HANDLER="POSTGRES"
fi

TABLES="account credential contract interface ifaceincontract opimplementations datacontract dataset accessedimpl accessedprop type java_type python_type memoryfeature cpufeature langfeature archfeature prefetchinginfo implementation python_implementation java_implementation annotation property java_property python_property operation java_operation python_operation metaclass java_metaclass python_metaclass namespace"
# Dump LM database 
if [ $LOGICMODULE_DB_HANDLER == "POSTGRES" ]; then
	docker exec -t $DATACLAY_LM_ORIGIN_DOCKER_NAME pg_dump dataclay $(for table in $TABLES; do echo -n "--table $table "; done) -c -C -U postgres > $TMPDUMP
elif [ $LOGICMODULE_DB_HANDLER == "SQLITE" ]; then
	# The following names need to be changed in case we use different logicmodule's names
	SRC_LOGICMODULE_NAME="LM"
	DST_LOGICMODULE_NAME="LM"
	for table in $TABLES;
	do
		docker exec -t $DATACLAY_LM_ORIGIN_DOCKER_NAME sqlite3 "//tmp/dataclay/$SRC_LOGICMODULE_NAME" ".dump $table" >> $TMPDUMP
	done
fi
