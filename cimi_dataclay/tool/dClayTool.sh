#!/bin/bash
usage() {
cat << EOF


 Usage: $TOOLNAME <operation> <argument1> <argument2> ...


 -------------------------------------------------------------------------------------------
| Basic            | Arguments                                                              |
 -------------------------------------------------------------------------------------------
 NewAccount         <new_user_name>  <new_user_pass>

 NewModel           <user_name>  <user_pass>  <namespace_name> <class_path> <$SUPPORTEDLANGS>
 GetStubs           <user_name>  <user_pass>  <namespace_name> <stubs_path>

 NewDataContract    <user_name>  <user_pass>  <dataset_name>   <beneficiary_user_name>


 ------------------------------------------------------------------------------------------
| Misc             | Arguments                                                             |
 ------------------------------------------------------------------------------------------
 GetBackends        <user_name>  <user_pass>  <$SUPPORTEDLANGS>

 NewNamespace       <user_name>  <user_pass>  <namespace_name> <$SUPPORTEDLANGS>
 GetNamespaces      <user_name>  <user_pass>

 NewDataset         <user_name>  <user_pass>  <dataset_name>   <$SUPPORTEDDSETS>
 GetDatasets        <user_name>  <user_pass>

 GetDataClayID    
 GetExtDataClayID   <dc_host> <dc_port>
 RegisterDataClay   <dc_host> <dc_port>

 SetMetadataBackup  <leader_address>  <backup_address>

EOF
exit 0
}
errorMsg() {
	echo ""
	echo "[dataClay] [tool ERROR] $1"
	echo ""
	exit -1
}

shopt -s nocasematch # ignore case in case or if clauses

TOOLNAME=$0
SUPPORTEDLANGS="python | java"
SUPPORTEDDSETS="public | private"

# Classpaths
SEPPARATOR=":"
SCRIPTPATH="$(cd "$(dirname "$0")" && pwd -P)"
if [[ "$OSTYPE" == "cygwin" ]]; then
        # POSIX compatibility layer and Linux environment emulation for Windows
        SEPPARATOR=";"
        SCRIPTPATH=$(echo "$SCRIPTPATH" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "msys" ]]; then
        # Lightweight shell and GNU utilities compiled for Windows (part of MinGW)
        SEPPARATOR=";"
        SCRIPTPATH=$(echo "$SCRIPTPATH" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "win32" ]]; then
        # I'm not sure this can happen.
        SEPPARATOR=";"
        SCRIPTPATH=$(echo "$SCRIPTPATH" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
fi
LIBPATH=$SCRIPTPATH/lib
CLIENTJAR=$SCRIPTPATH/dataclayclient.jar
CLASSPATH=${CLIENTJAR}${SEPPARATOR}${LIBPATH}/*${SEPPARATOR}${CLASSPATH}


if [ ! -z $DATACLAY_JAR ]; then
  # Environment set means we are in Mare or in some already-prepared computing Environment
  # Assume everything is ok.
  rm -f $CLIENTJAR
  ln -s $DATACLAY_JAR $CLIENTJAR
else
  # No predefined DATACLAY_JAR, so assuming docker env
  type docker >/dev/null 2>&1 || { errorMsg "If DATACLAY_JAR is not installed, docker is required; but it is not installed. Aborting."; }
  DOCKER_BASE=`docker inspect --format='{{.Id}}' \`docker ps | grep logicmodule | head -1 | cut -d" " -f1\``
  if [ -z $DOCKER_BASE ]; then
	errorMsg "Could not inspect a running logicmodule docker."  
  fi
  DOCKER_DCLIB="$DOCKER_BASE:/usr/src/dataclay/lib"
  DOCKER_DCJAR="$DOCKER_BASE:/usr/src/dataclay/dataclay.jar"
  touch $SCRIPTPATH/.dockerid
  # In case of using dockers, try to find lib there 
  DOCKER_ID=`docker inspect -f "{{.Id}}" $DOCKER_BASE`
  if [ ! -f $CLIENTJAR ]; then
    UPDATE_LIB=1
  elif [ -z "$(ls -A $LIBPATH)" ]; then
  	UPDATE_LIB=1
  elif [ -z "`grep $DOCKER_ID $SCRIPTPATH/.dockerid`" ]; then
	UPDATE_LIB=1
  fi
  if [ ! -z $UPDATE_LIB ]; then
	rm -f $CLIENTJAR
	rm -Rf $LIBPATH
    docker cp $DOCKER_DCLIB "$SCRIPTPATH"
    docker cp $DOCKER_DCJAR "$CLIENTJAR"
	echo $DOCKER_ID > $SCRIPTPATH/.dockerid
	echo "[dataClay] [tool LOG] Retrieved $CLIENTJAR from $DOCKER_DCJAR"
  fi
fi

if [ ! -f $CLIENTJAR ]; then
	errorMsg "Cannot resolve dataClay jar library. Possible causes: 
                   - DATACLAY_JAR=\"$DATACLAY_JAR\" is not defined or is not a valid path
                   - a dataClay docker environment cannot be found."
fi

# Base ops commands
JAVA_OPSBASE="java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $CLASSPATH"
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

# Federation
GET_DATACLAYID="$JAVA_OPSBASE dataclay.tool.GetCurrentDataClayID"
GET_EXT_DATACLAYID="$JAVA_OPSBASE dataclay.tool.GetExternalDataClayID"
REG_EXT_DATACLAY="$JAVA_OPSBASE dataclay.tool.NewDataClayInstance"

# LM backup
SET_METADATA_BACKUP="$JAVA_OPSBASE dataclay.tool.SetMetadataBackup"

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
		if [ $# -lt 6 ]; then
			errorMsg "Missing arguments. Usage: NewModel <user_name> <user_pass> <namespace_name> <class_path> <$SUPPORTEDLANGS>"
		fi
		if [ ! -d $FOLDER ]; then
			errorMsg "Model path $FOLDER is not a valid directory."
		fi
		case $6 in
			'java')
				$NEW_NAMESPACE $2 $3 $4 java
				if [ $? -ge 0 ]; then
					INIT_PARAMS="$2 $3 $4 $5"
					if [ $# -gt 6 ]; then
						INIT_PARAMS="$INIT_PARAMS ${@:7}"
					fi
					$JAVA_NEW_MODEL $INIT_PARAMS
				fi
				;;
			'python')
				$NEW_NAMESPACE $2 $3 $4 python
				if [ $? -ge 0 ]; then
					if [ $# -gt 6 ]; then
						errorMsg "Prefetching is only supported in Java applications."
					fi
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
	'GetDataClayID')
		$GET_DATACLAYID $2
		;;
	'RegisterDataClay')
		$REG_EXT_DATACLAY $2 $3
		;;
	'GetExtDataClayID')
		$GET_EXT_DATACLAYID $2 $3
		;;
	'SetMetadataBackup')
		$SET_METADATA_BACKUP $2 $3
		;;
	*)
		echo "[ERROR]: Operation $1 is not supported."
		usage
		exit -1
		;;
esac
