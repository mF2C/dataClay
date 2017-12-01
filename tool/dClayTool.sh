#!/bin/bash
SCRIPT=$(readlink -f "$0")
SCRIPTPATH=`dirname "$SCRIPT"` #script path
TOOLNAME=$0
SUPPORTEDLANGS="python | java"

errorMsg() {
	echo "[ERROR] $1"
	exit -1
}
mkdir -p $SCRIPTPATH/lib
CLIENTJAR=$SCRIPTPATH/lib/dataclayclient.jar
if [ ! -f $CLIENTJAR ]; then
	docker cp orchestration_logicmodule_1:/usr/src/app/dataclay.jar $CLIENTJAR
	if [ ! -f $CLIENTJAR ]; then
		errorMsg "ERROR: dataClay containers not running or cannot copy lib from /usr/src/app/dataclay.jar"
	fi
fi

# Base ops commands
JAVA_OPSBASE="java -cp $SCRIPTPATH/lib/dataClayTools.jar:$SCRIPTPATH/lib/dataclayclient.jar"
PY_OPSBASE="python -m dataclay.tool"

# Basic operations
GET_NAMESPACE_LANG="$JAVA_OPSBASE dcops.GetNamespaceLang"
ACCESS_NS_MODEL="$JAVA_OPSBASE dcops.AccessNamespace"
NEW_ACCOUNT="$JAVA_OPSBASE dcops.NewAccount"
NEW_DATACONTRACT="$JAVA_OPSBASE dcops.NewDataContract"
GET_BACKENDS="$JAVA_OPSBASE dcops.GetBackends"

# NewModel operations
NEW_NAMESPACE="$JAVA_OPSBASE dcops.NewNamespace"
JAVA_NEW_MODEL="$JAVA_OPSBASE dcops.NewModel"
PY_NEW_MODEL="$PY_OPSBASE register_model"

# Get stubs operations
JAVA_GETSTUBS="$JAVA_OPSBASE dcops.GetStubs"
PY_GETSTUBS="$PY_OPSBASE get_stubs"


usage() {
	echo ""
	echo " Usage: $TOOLNAME <operation> <argument1> <argument2> ..."
	echo ""
	echo " Operation             Arguments"
	echo " ---------             ---------"
	echo " NewAccount            <new_user_name>  <new_user_pass>"
	echo " NewModel              <user_name>  <user_pass>  <namespace_name> <class_path> <$SUPPORTEDLANGS>"
	echo " GetStubs              <user_name>  <user_pass>  <namespace_name> <stubs_path>"
	echo " NewDataContract       <user_name>  <user_pass>  <dataset_name>   <beneficiary_user_name>"
	echo " GetBackends           <user_name>  <user_pass>"
	echo ""
	exit 0
}

unsupportedLang() {
	errorMsg "Missing or unsupported language: '$1'. Must be one of the supported languages: $SUPPORTEDLANGS."
}


if [ -z $1 ]; then
	usage
	exit 0
fi
OPERATION=$1

case $OPERATION in
	'-h' | '--help' | '?' | 'help')
		usage
		;;
	'NewAccount')
		$NEW_ACCOUNT ${@:2}
		;;
	'GetBackends')
		$GET_BACKENDS ${@:2}
		;;
	'NewDataContract')
		$NEW_DATACONTRACT ${@:2}
		;;
	'NewModel')
		FOLDER=$5
		if [ -z $FOLDER ]; then
			errorMsg "Missing model path."
		fi
		if [ ! -d $FOLDER ]; then
			errorMsg "Model path $FOLDER is not a valid directory."
		fi
		case $6 in
			'java')
				$NEW_NAMESPACE $2 $3 $4 java
				$JAVA_NEW_MODEL $2 $3 $4 $5
				;;
			'python')
				$NEW_NAMESPACE $2 $3 $4 python
				$PY_NEW_MODEL $2 $3 $4 $5
				;;
			*)
				unsupportedLang $6
				;;
		esac
		;;
	'GetStubs')
		LANG=`$GET_NAMESPACE_LANG $2 $3 $4 | tail -1`
		case $LANG in
			'LANG_JAVA')
				$ACCESS_NS_MODEL $2 $3 $4
				$JAVA_GETSTUBS $2 $3 $4 $5
				;;
			'LANG_PYTHON')
				CONTRACTID=`$ACCESS_NS_MODEL $2 $3 $4 | tail -1`
				$PY_GETSTUBS $2 $3 $CONTRACTID $5
				;;
			*)
				unsupportedLang $LANG 
				;;
		esac
		;;
	*)
		echo "[ERROR]: Operation $1 is not supported."
		usage
		exit -1
		;;
esac
