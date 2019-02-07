#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
TOOLSBASE="$SCRIPTDIR/../../Tools"
DOCKERSPATH="$SCRIPTDIR/../../Environments/Dockers"
TOOLSPATH="$TOOLSBASE/dClayTool.sh"
DATACLAYJARPATH="$SCRIPTDIR/../target/dataClay-1.0.jar"
DCLIB="$TOOLSBASE/dataclayclient.jar"
SRCPATH="$SCRIPTDIR/model/src/CIMI"
NAMESPACE="CimiNS"
USER="mf2c"
PASS="p4ssw0rd"
DATASET="mf2c"
STUBSPATH="$SCRIPTDIR/wrapper/stubs"
DATACLAY_MF2C_STUBS_JAR="dataClayMf2cStubs.jar"
URL_DATACLAY_MAVEN_REPO="https://github.com/bsc-ssrg/dataclay-maven"
URL_DATACLAY_MF2C_MAVEN_REPO="https://github.com/bsc-ssrg/dataclay-maven-mf2c-wrapper.git"
if [[ "$OSTYPE" == "cygwin" ]]; then
        # POSIX compatibility layer and Linux environment emulation for Windows
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "msys" ]]; then
        # Lightweight shell and GNU utilities compiled for Windows (part of MinGW)
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
elif [[ "$OSTYPE" == "win32" ]]; then
        # I'm not sure this can happen.
        SCRIPTDIR=$(echo "$SCRIPTDIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')
fi

function usage {
	echo " USAGE: $0  [--push dataclay-maven local repository mf2c-wrapper local repository]"
	echo "		--push Indicates to push in GitHub, DockerHub, Maven, Pypi and all remote repositories "
	echo "			Remember to clone in local repository folder: $URL_DATACLAY_MAVEN_REPO "
	echo "			Remember to clone in local repository folder:  $URL_DATACLAY_MF2C_MAVEN_REPO  "
	echo ""
}

function docker-push-trunk {
	TAG=trunk
	docker push bscdataclay/logicmodule:${TAG}
	docker push bscdataclay/logicmodule:${TAG}-openjdk8
	docker push bscdataclay/dsjava:${TAG}
	docker push bscdataclay/dsjava:${TAG}-openjdk8
	docker push bscdataclay/dspython:${TAG}
	docker push bscdataclay/dspython:${TAG}-py3.6
	docker push bscdataclay/dspython:${TAG}-py2.7
}  

function docker-push-mf2c {
	TAG=mf2c
	docker push bscdataclay/logicmodule:${TAG}
	docker push bscdataclay/logicmodule:${TAG}-openjdk8
	docker push bscdataclay/dsjava:${TAG}
	docker push bscdataclay/dsjava:${TAG}-openjdk8
}  

function maven_install_trunk {  
	JARPATH=$1
	if [ $# -eq 1 ]; then
		echo "Installing dataclay trunk in m2 repository"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay \
		   -DartifactId=dataclay \
		   -Dversion=trunk \
		   -Dpackaging=jar \
		   -DpomFile=$SCRIPTDIR/pom-trunk.xml \
		   -DcreateChecksum=true
	elif [ $# -eq 2 ]; then
		LOCALREPOSITORY=$2
		echo "Installing dataclay trunk local repository"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay \
		   -DartifactId=dataclay \
		   -Dversion=trunk \
		   -Dpackaging=jar \
		   -DlocalRepositoryPath=$LOCALREPOSITORY \
		   -DpomFile=$SCRIPTDIR/pom-trunk.xml \
		   -DcreateChecksum=true
	fi
}

function maven_install_wrapper { 
	JARPATH=$1
	echo "Installing dataclay mf2c stubs in m2 repository "
	mvn install:install-file \
	   -Dfile=$JARPATH \
	   -DgroupId=dataclay.mf2c \
	   -DartifactId=stubs \
	   -Dversion=trunk \
	   -Dpackaging=jar \
	   -DpomFile=$SCRIPTDIR/pom-stubs-wrapper.xml \
	   -DcreateChecksum=true
	
	if [ $# -eq 1 ]; then
		echo "Installing dataclay mf2c stubs local repository"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay.mf2c \
		   -DartifactId=stubs \
		   -Dversion=trunk \
		   -Dpackaging=jar \
		   -DpomFile=$SCRIPTDIR/pom-stubs-wrapper.xml \
		   -DcreateChecksum=true
	
		pushd $SCRIPTDIR/wrapper
		echo "Building dataclay mf2c wrapper local repository"
		mvn package javadoc:jar
		popd
		
		JARPATH=$SCRIPTDIR/wrapper/target/wrapper-trunk.jar
		echo "Installing dataclay mf2c wrapper local repository"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay.mf2c \
		   -DartifactId=wrapper \
		   -Dversion=trunk \
		   -Dpackaging=jar \
		   -DpomFile=$SCRIPTDIR/wrapper/pom.xml \
		   -Djavadoc=$SCRIPTDIR/wrapper/target/wrapper-trunk-javadoc.jar \
		   -DcreateChecksum=true
		   
	elif [ $# -eq 2 ]; then
		LOCALREPOSITORY=$2
		echo "Installing dataclay mf2c stubs local repository $LOCALREPOSITORY"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay.mf2c \
		   -DartifactId=stubs \
		   -Dversion=trunk \
		   -Dpackaging=jar \
		   -DlocalRepositoryPath=$LOCALREPOSITORY \
		   -DpomFile=$SCRIPTDIR/pom-stubs-wrapper.xml \
		   -DcreateChecksum=true
	
		pushd $SCRIPTDIR/wrapper
		echo "Building dataclay mf2c wrapper local repository $LOCALREPOSITORY"
		mvn package javadoc:jar
		popd
		
		JARPATH=$SCRIPTDIR/wrapper/target/wrapper-trunk.jar
		echo "Installing dataclay mf2c wrapper local repository $LOCALREPOSITORY"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay.mf2c \
		   -DartifactId=wrapper \
		   -Dversion=trunk \
		   -Dpackaging=jar \
		   -DlocalRepositoryPath=$LOCALREPOSITORY \
		   -DpomFile=$SCRIPTDIR/wrapper/pom.xml \
		   -Djavadoc=$SCRIPTDIR/wrapper/target/wrapper-trunk-javadoc.jar \
		   -DcreateChecksum=true
	fi
}


echo " Welcome! this script is intended to: "
echo "		- Build new JAR of dataClay version trunk "
echo "		- Build new docker images of dataClay version trunk "
echo "		- Build new docker images for mf2c with already registered model, contracts, ..." 
echo "		- If push option selected: Publish dataClay version trunk dockers in DockerHub "
echo "		- If push option selected: Publish dataClay version mf2c dockers in DockerHub "
echo "		- Install in maven (local or remote repository) dataclay trunk "
echo "		- Install in maven (local or remote repository) mf2c stubs for CIMI model "
echo "		- Install in maven (local or remote repository) mf2c wrapper "

if [ "$#" -ne 0 ] && [ "$#" -ne 3 ]; then
	usage
    exit -1
fi

# Check parameters
PUSH=false
PUSH_PARAM=""
DATACLAY_MAVEN_REPO=""
DATACLAY_MF2C_MAVEN_REPO=""

echo " ===== Cleaning ====="
rm -f psql_dump.sql
rm -f $DATACLAY_MF2C_STUBS_JAR
rm -rf $SCRIPTDIR/execClasses
rm -f $SCRIPTDIR/LM.sqlite


# Check push repositories
if [ "$#" -eq 3 ]; then
	PUSH_PARAM=$1
	DATACLAY_MAVEN_REPO=$2
	DATACLAY_MF2C_MAVEN_REPO=$3 
	
	if [ "$PUSH_PARAM" != "--push" ]; then 
		usage
		exit -1
	else 
		PUSH=true
	fi
	
	# Check repository 
	pushd $DATACLAY_MAVEN_REPO 
	URL=`git config --get remote.origin.url`
	if [ "$URL" != "$URL_DATACLAY_MAVEN_REPO" ]; then
		echo "[ERROR] Repository provided $DATACLAY_MAVEN_REPO do not refer to proper GitHub $URL_DATACLAY_MAVEN_REPO. Aborting"
		exit -1
	else 
		echo "Going to install dataclay maven trunk in $DATACLAY_MAVEN_REPO"
	fi
	popd
	
	pushd $DATACLAY_MF2C_MAVEN_REPO 
	URL=`git config --get remote.origin.url`
	if [ "$URL" != "$URL_DATACLAY_MF2C_MAVEN_REPO" ]; then
		echo "[ERROR] Repository provided $DATACLAY_MF2C_MAVEN_REPO do not refer to proper GitHub $URL_DATACLAY_MF2C_MAVEN_REPO. Aborting"
		exit -1
	else 
		echo "Going to install dataclay maven mf2c trunk in $DATACLAY_MF2C_MAVEN_REPO"
	fi
	popd
fi

# Prepare client.properties
TMPDIR=`mktemp -d`
printf "HOST=127.0.0.1\nTCPPORT=11034" > $TMPDIR/client.properties
export DATACLAYCLIENTCONFIG=$TMPDIR/client.properties

# Build and start dataClay
pushd $SCRIPTDIR/..
echo " ===== Building new JAR of dataClay version trunk and starting dataClay for registering CIMI model ===== "
bash dataClayFullStart.sh --onlyjava --numdcs 1 --nodes 1 --sqlite
popd 

echo " ===== Register $USER account ====="
$TOOLSPATH NewAccount $USER $PASS

echo " ===== Create dataset $DATASET and grant access to it ====="
$TOOLSPATH NewDataContract $USER $PASS $DATASET $USER

echo " ===== Register model in $SRCPATH  ====="
TMPDIR=`mktemp -d`
javac -cp $DCLIB $SRCPATH/*.java -d $TMPDIR
$TOOLSPATH NewModel $USER $PASS $NAMESPACE $TMPDIR java
rm -Rf $TMPDIR

echo " ===== Get stubs into $STUBSPATH  ====="
mkdir -p $STUBSPATH
$TOOLSPATH GetStubs $USER $PASS $NAMESPACE $STUBSPATH

echo " ===== Creating stubs JAR  ====="
rm -f $DATACLAY_MF2C_STUBS_JAR
jar cvf $DATACLAY_MF2C_STUBS_JAR -C $STUBSPATH .

echo " ===== Retrieving execution classes into $SCRIPTDIR/execClasses  ====="
# Copy execClasses from dsjava docker
rm -rf $SCRIPTDIR/execClasses
mkdir -p $SCRIPTDIR/execClasses
docker cp dockers_ds1java1_1:/usr/src/dataclay/execClasses/ $SCRIPTDIR


echo " ===== Retrieving SQLITE LM into $SCRIPTDIR/LM.sqlite  ====="
rm -f $SCRIPTDIR/LM.sqlite
bash $SCRIPTDIR/export_federate_model.sh dockers_logicmodule1_1 "$SCRIPTDIR/LM.sqlite" SQLITE


# Now we can build the docker images 
echo " ===== Building docker bscdataclay/dsjava:mf2c-openjdk8 ====="
docker build --build-arg DATACLAY_JDK="openjdk8" -f DockerfileDSMf2c -t bscdataclay/dsjava:mf2c-openjdk8 .

echo " ===== Building docker bscdataclay/logicmodule:mf2c-openjdk8 ====="
docker build --build-arg DATACLAY_JDK="openjdk8" -f DockerfileLMMf2c -t bscdataclay/logicmodule:mf2c-openjdk8 .


echo " ===== Building docker bscdataclay/logicmodule:mf2c  ====="
docker tag bscdataclay/logicmodule:mf2c-openjdk8 bscdataclay/logicmodule:mf2c 

echo " ===== Building docker bscdataclay/dsjava:mf2c ====="
docker tag bscdataclay/dsjava:mf2c-openjdk8 bscdataclay/dsjava:mf2c

echo " ===== NOT Building docker bscdataclay/postgres:lm-mf2c (mf2c will use SQLITE) ====="

echo " ===== Stopping dataClay ====="
pushd $DOCKERSPATH
docker-compose down
popd

if [ "$PUSH" = true ] ; then
	echo " ===== Pushing dockers dataclay trunk dockers DockerHub ====="
	docker-push-trunk
	
	echo " ===== Pushing dockers dataclay mf2c dockers DockerHub ====="
	docker-push-mf2c
else 
	echo " ===== NOT Pushing any docker into DockerHub ====="
fi

if [ "$PUSH" = true ] ; then
	echo " ===== Installing dataClay maven trunk ====="
	maven_install_trunk $DATACLAYJARPATH $DATACLAY_MAVEN_REPO
	echo " ===== Pushing  dataClay maven trunk into GitHub ====="
	pushd $DATACLAY_MAVEN_REPO
	git add -A .
	git commit -m "Updating dataclay maven trunk with current development" 
	git push origin repository
	popd
else 
	echo " ===== Installing dataClay maven trunk ====="
	maven_install_trunk $DATACLAYJARPATH
	echo " ===== NOT Pushing  dataClay maven trunk into GitHub ====="
fi

if [ "$PUSH" = true ] ; then
	echo " ===== Installing dataClay mf2c wrapper trunk ====="
	maven_install_wrapper $DATACLAY_MF2C_STUBS_JAR $DATACLAY_MF2C_MAVEN_REPO

	echo " ===== Pushing  dataClay maven mf2c wrapper into GitHub ====="
	pushd $DATACLAY_MF2C_MAVEN_REPO
	git add -A .
	git commit -m "Updating dataclay maven mf2c wrapper with current development" 
	git push origin repository
	popd
else 
	echo " ===== Installing dataClay mf2c wrapper trunk ====="
	maven_install_wrapper $DATACLAY_MF2C_STUBS_JAR
	echo " ===== NOT Pushing  dataClay maven mf2c wrapper into GitHub ====="
fi

