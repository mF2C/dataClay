#!/bin/bash
usage() {
cat << EOF


 Usage: $TOOLNAME <operation> <argument1> <argument2> ...


 -------------------------------------------------------------------------------------------
| Basic          | Arguments                                                                |
 -------------------------------------------------------------------------------------------
 NewAccount        <new_user_name>  <new_user_pass>

 NewModel          <user_name>  <user_pass>  <namespace_name> <class_path> <$SUPPORTEDLANGS>
 GetStubs          <user_name>  <user_pass>  <namespace_name> <stubs_path>

 NewDataContract   <user_name>  <user_pass>  <dataset_name>   <beneficiary_user_name>


 ------------------------------------------------------------------------------------------
| Misc           | Arguments                                                                |
 ------------------------------------------------------------------------------------------
 GetBackends       <user_name>  <user_pass>  <$SUPPORTEDLANGS>

 NewNamespace      <user_name>  <user_pass>  <namespace_name> <$SUPPORTEDLANGS>
 GetNamespaces     <user_name>  <user_pass>

 NewDataset        <user_name>  <user_pass>  <dataset_name>   <$SUPPORTEDDSETS>
 GetDatasets       <user_name>  <user_pass>

EOF
exit 0
}

shopt -s nocasematch # ignore case in case or if clauses

TOOLNAME=$0
SUPPORTEDLANGS="python | java"
SUPPORTEDDSETS="public | private"

errorMsg() {
	echo "[ERROR] $1"
	exit -1
}

if [ "x$DATACLAY_JAR" == "x" ]; then
  # No environment, we are not in Mare so assume relative placement of jar
  SCRIPT=$(readlink -f "$0")
  SCRIPTPATH=`dirname "$SCRIPT"` #script path
  mkdir -p $SCRIPTPATH/lib
  CLIENTJAR=$SCRIPTPATH/lib/dataclayclient.jar
else
  # Environment set means we are in Mare or in some already-prepared computing Environment
  # Assume everything is ok.
  CLIENTJAR=$DATACLAY_JAR
fi

if [ ! -f $CLIENTJAR ]; then
	docker cp orchestration_logicmodule_1:/usr/src/app/dataclay.jar $CLIENTJAR
	if [ ! -f $CLIENTJAR ]; then
		errorMsg "ERROR: dataClay containers not running or cannot copy lib from /usr/src/app/dataclay.jar"
	fi
fi

# Base ops commands
JAVA_OPSBASE="java -cp $CLIENTJAR"
PY_OPSBASE="python -m dataclay.tool"

# Basic operations
NEW_ACCOUNT="$JAVA_OPSBASE dataclay.tool.NewAccount"
GET_BACKENDS="$JAVA_OPSBASE dataclay.tool.GetBackends"
ACCESS_NS_MODEL="$JAVA_OPSBASE dataclay.tool.AccessNamespace"
GET_NAMESPACE_LANG="$JAVA_OPSBASE dataclay.tool.GetNamespaceLang"
GET_NAMESPACES="$JAVA_OPSBASE dataclay.tool.GetNamespaces"
NEW_DATACONTRACT="$JAVA_OPSBASE dataclay.tool.NewDataContract"
GET_DATASETS="$JAVA_OPSBASE dataclay.tool.GetDatasets"
NEW_DATASET="$JAVA_OPSBASE dataclay.tool.NewDataset"

# NewModel operations
NEW_NAMESPACE="$JAVA_OPSBASE dataclay.tool.NewNamespace"
JAVA_NEW_MODEL="$JAVA_OPSBASE dataclay.tool.NewModel"
PY_NEW_MODEL="$PY_OPSBASE register_model"

# Get stubs operations
JAVA_GETSTUBS="$JAVA_OPSBASE dataclay.tool.GetStubs"
PY_GETSTUBS="$PY_OPSBASE get_stubs"

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
#		proposal for defaults:
#		$NEW_NAMESPACE ${@:2} $2_java java
#		$NEW_NAMESPACE ${@:2} $2_py python
#		$NEW_DATACONTRACT ${@:2} $2_ds $2
		;;
	'GetBackends')
		$GET_BACKENDS ${@:2}
		;;
	'GetDatasets')
		$GET_DATASETS ${@:2}
		;;
	'NewDataset')
		$NEW_DATASET ${@:2}
		;;
	'NewDataContract')
		$NEW_DATACONTRACT ${@:2}
		;;
	'NewNamespace')
		$NEW_NAMESPACE ${@:2}
		;;
	'GetNamespaces')
		$GET_NAMESPACES ${@:2}
		;;
	'NewModel')
		FOLDER=$5
		if [ $# -ne 6 ]; then
			errorMsg "Missing arguments. Usage: NewModel <user_name> <user_pass> <namespace_name> <class_path> <$SUPPORTEDLANGS>"
		fi
		if [ ! -d $FOLDER ]; then
			errorMsg "Model path $FOLDER is not a valid directory."
		fi
		case $6 in
			'java')
				$NEW_NAMESPACE $2 $3 $4 java
				if [ $? -ge 0 ]; then
					$JAVA_NEW_MODEL $2 $3 $4 $5
				fi
				;;
			'python')
				$NEW_NAMESPACE $2 $3 $4 python
				if [ $? -ge 0 ]; then
					$PY_NEW_MODEL $2 $3 $4 $5
				fi
				;;
			*)
				errorMsg "Missing or unsupported language: '$6'. Must be one of the supported languages: $SUPPORTEDLANGS."
				;;
		esac
		;;
	'GetStubs')
		FOLDER=$5
		if [ $# -ne 5 ]; then
			errorMsg "Missing arguments. Usage: GetStubs <user_name> <user_pass> <namespace_name> <stubs_path>"
		fi
		if [ ! -d $FOLDER ]; then
			errorMsg "Stubs path $FOLDER is not a valid directory."
		fi
		LANG=`$GET_NAMESPACE_LANG $2 $3 $4 | tail -1`
		case $LANG in
			'LANG_JAVA')
				$ACCESS_NS_MODEL $2 $3 $4
				if [ $? -ge 0 ]; then
					$JAVA_GETSTUBS $2 $3 $4 $5
				fi
				;;
			'LANG_PYTHON')
				CONTRACTID=`$ACCESS_NS_MODEL $2 $3 $4 | tail -1`
				if [ $? -ge 0 ] && [ ! -z $CONTRACTID ]; then
					$PY_GETSTUBS $2 $3 $CONTRACTID $5
				fi
				;;
		esac
		;;
	*)
		echo "[ERROR]: Operation $1 is not supported."
		usage
		exit -1
		;;
esac
