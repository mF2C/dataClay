#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
TOOLSBASE="$SCRIPTDIR/../tool"
TOOLSPATH="$TOOLSBASE/dClayTool.sh"
DCLIB="$TOOLSBASE/dataclayclient.jar"
SRCPATH="$SCRIPTDIR/model/src/CIMI"
DATACLAY_TAG="2.0.dev4"
NAMESPACE="CimiNS"
USER="mf2c"
PASS="p4ssw0rd"
DATASET="mf2c"
STUBSPATH="$SCRIPTDIR/wrapper/stubs"
DATACLAY_MF2C_STUBS_JAR="dataClayMf2cStubs.jar"
URL_DATACLAY_MF2C_MAVEN_REPO="https://github.com/bsc-ssrg/dataclay-maven-mf2c-wrapper.git"
PROXY="$SCRIPTDIR/../dataclay-proxy"

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
	echo " USAGE: $0 proxy_tag [--push <mf2c-wrapper local repository>]"
	echo "		--push Indicates to push to DockerHub and Maven "
	echo "			Remember to clone in local repository folder:  $URL_DATACLAY_MF2C_MAVEN_REPO  "
	echo ""
}

function docker-push-mf2c {
	TAG=$1
	docker push mf2c/dataclay-logicmodule:${TAG}
	docker push mf2c/dataclay-dsjava:${TAG}
	docker push mf2c/dataclay-logicmodule:latest
	docker push mf2c/dataclay-dsjava:latest
	
}  

function maven_install_wrapper { 
	JARPATH=$1
	VERSION=$2
	rm -rf $SCRIPTDIR/wrapper/target
	if [ -f $SCRIPTDIR/wrapper/pom.xml.orig ]; then
		mv $SCRIPTDIR/wrapper/pom.xml.orig $SCRIPTDIR/wrapper/pom.xml #sanity check
	fi
	cp $SCRIPTDIR/wrapper/pom.xml $SCRIPTDIR/wrapper/pom.xml.orig
	sed -i "s/trunk/$VERSION/g" $SCRIPTDIR/wrapper/pom.xml
	sed -i '/<dependency>/ {
		:start
		N
		/<\/dependency>$/!b start
		/<artifactId>dataclay<\/artifactId>/ {
		s/\(<version>\)'"$VERSION"'\(<\/version>\)/\1trunk\2/
		}
	}' $SCRIPTDIR/wrapper/pom.xml
	
	echo "Installing dataclay mf2c stubs in m2 repository "
	mvn install:install-file \
	   -Dfile=$JARPATH \
	   -DgroupId=dataclay.mf2c \
	   -DartifactId=stubs \
	   -Dversion=$VERSION \
	   -Dpackaging=jar \
	   -DpomFile=$SCRIPTDIR/pom-stubs-wrapper.xml \
	   -DcreateChecksum=true
	
	if [ $# -eq 2 ]; then
		echo "Installing dataclay mf2c stubs local repository"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay.mf2c \
		   -DartifactId=stubs \
		   -Dversion=$VERSION \
		   -Dpackaging=jar \
		   -DpomFile=$SCRIPTDIR/pom-stubs-wrapper.xml \
		   -DcreateChecksum=true
	
		pushd $SCRIPTDIR/wrapper
		echo "Building dataclay mf2c wrapper local repository"
		mvn package javadoc:jar
		popd
		
		JARPATH=$SCRIPTDIR/wrapper/target/wrapper-${PROXY_TAG}.jar
		echo "Installing dataclay mf2c wrapper local repository"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay.mf2c \
		   -DartifactId=wrapper \
		   -Dversion=$VERSION \
		   -Dpackaging=jar \
		   -DpomFile=$SCRIPTDIR/wrapper/pom.xml \
		   -Djavadoc=$SCRIPTDIR/wrapper/target/wrapper-${PROXY_TAG}-javadoc.jar \
		   -DcreateChecksum=true
		   
	elif [ $# -eq 3 ]; then
		LOCALREPOSITORY=$3
		echo "Installing dataclay mf2c stubs local repository $LOCALREPOSITORY"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay.mf2c \
		   -DartifactId=stubs \
		   -Dversion=$VERSION \
		   -Dpackaging=jar \
		   -DlocalRepositoryPath=$LOCALREPOSITORY \
		   -DpomFile=$SCRIPTDIR/pom-stubs-wrapper.xml \
		   -DcreateChecksum=true
	
		pushd $SCRIPTDIR/wrapper
		echo "Building dataclay mf2c wrapper local repository $LOCALREPOSITORY"
		mvn clean
		mvn package javadoc:jar
		popd
		
		JARPATH=$SCRIPTDIR/wrapper/target/wrapper-${PROXY_TAG}.jar
		echo "Installing dataclay mf2c wrapper local repository $LOCALREPOSITORY"
		mvn install:install-file \
		   -Dfile=$JARPATH \
		   -DgroupId=dataclay.mf2c \
		   -DartifactId=wrapper \
		   -Dversion=$VERSION \
		   -Dpackaging=jar \
		   -DlocalRepositoryPath=$LOCALREPOSITORY \
		   -DpomFile=$SCRIPTDIR/wrapper/pom.xml \
		   -Djavadoc=$SCRIPTDIR/wrapper/target/wrapper-${PROXY_TAG}-javadoc.jar \
		   -DcreateChecksum=true
	fi
	
	mv $SCRIPTDIR/wrapper/pom.xml.orig $SCRIPTDIR/wrapper/pom.xml
	
}


echo " Welcome! this script is intended to: "
echo "		- Build new docker images for mf2c with already registered model, contracts, ..." 
echo "		- If push option selected: Publish dataClay version mf2c dockers in DockerHub "
echo "		- Install in maven (local or remote repository) mf2c stubs for CIMI model "
echo "		- Install in maven (local or remote repository) mf2c wrapper "

if [ "$#" -ne 1 ] && [ "$#" -ne 3 ]; then
	usage
    exit -1
fi

# Check parameters
PUSH=false
PUSH_PARAM=""
DATACLAY_MF2C_MAVEN_REPO=""
PROXY_TAG=$1

echo " ===== Cleaning ====="
rm -f psql_dump.sql
rm -f $DATACLAY_MF2C_STUBS_JAR
rm -rf $SCRIPTDIR/execClasses
rm -f $SCRIPTDIR/LM.sqlite


# Check push repositories
if [ "$#" -eq 3 ]; then
	PUSH_PARAM=$2
	DATACLAY_MF2C_MAVEN_REPO=$3
	
	if [ "$PUSH_PARAM" != "--push" ]; then 
		usage
		exit -1
	else 
		PUSH=true
	fi
	
	pushd $DATACLAY_MF2C_MAVEN_REPO 
	URL=`git config --get remote.origin.url`
	if [ "$URL" != "$URL_DATACLAY_MF2C_MAVEN_REPO" ]; then
		echo "[ERROR] Repository provided $DATACLAY_MF2C_MAVEN_REPO do not refer to proper GitHub $URL_DATACLAY_MF2C_MAVEN_REPO. Aborting"
		exit -1
	else 
		echo "Going to install dataclay maven mf2c ${PROXY_TAG} in $DATACLAY_MF2C_MAVEN_REPO"
	fi
	popd
fi

# Prepare client.properties
TMPDIR=`mktemp -d`
printf "HOST=127.0.0.1\nTCPPORT=1034" > $TMPDIR/client.properties
export DATACLAYCLIENTCONFIG=$TMPDIR/client.properties

# Build and start dataClay
pushd $SCRIPTDIR/dockers

echo " ==== Set dataClay version $DATACLAY_TAG in docker-compose.yml ==== " 
if [ -f docker-compose.yml.orig ]; then
	mv docker-compose.yml.orig docker-compose.yml # sanity check if script was interrupted 
fi
cp docker-compose.yml docker-compose.yml.orig
sed -i "s/trunk/$DATACLAY_TAG/g" docker-compose.yml

echo " ===== Starting dataClay ===== "
docker-compose down #sanity check
docker-compose up -d
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
rm -rf $STUBSPATH
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
TABLES="account credential contract interface ifaceincontract opimplementations datacontract dataset accessedimpl accessedprop type java_type python_type memoryfeature cpufeature langfeature archfeature prefetchinginfo implementation python_implementation java_implementation annotation property java_property python_property operation java_operation python_operation metaclass java_metaclass python_metaclass namespace"
for table in $TABLES;
do
	docker exec -t dockers_logicmodule1_1 sqlite3 "//tmp/dataclay/LM" ".dump $table" >> $SCRIPTDIR/LM.sqlite
done

echo " ===== Stopping dataClay ====="
pushd $SCRIPTDIR/dockers
docker-compose down
mv docker-compose.yml.orig docker-compose.yml

popd

# Now we can build the docker images 
echo " ===== Building docker mf2c/dataclay-dsjava:${PROXY_TAG} ====="
docker build --build-arg DATACLAY_TAG="${DATACLAY_TAG}" --build-arg DATACLAY_JDK="openjdk8" -f DockerfileDSMf2c -t mf2c/dataclay-dsjava:${PROXY_TAG} .

echo " ===== Building docker mf2c/dataclay-logicmodule:${PROXY_TAG} ====="
docker build --build-arg DATACLAY_TAG="${DATACLAY_TAG}" --build-arg DATACLAY_JDK="openjdk8" -f DockerfileLMMf2c -t mf2c/dataclay-logicmodule:${PROXY_TAG} .

echo " ===== Building docker mf2c/dataclay-logicmodule:latest  ====="
docker tag mf2c/dataclay-logicmodule:${PROXY_TAG} mf2c/dataclay-logicmodule:latest 

echo " ===== Building docker mf2c/dataclay-dsjava:latest ====="
docker tag mf2c/dataclay-dsjava:${PROXY_TAG} mf2c/dataclay-dsjava:latest 

echo " ===== Building docker bscdataclay/logicmodule:mf2c   ====="
docker tag mf2c/dataclay-logicmodule:${PROXY_TAG} bscdataclay/logicmodule:mf2c 

echo " ===== Building docker bscdataclay/dsjava:mf2c  ====="
docker tag mf2c/dataclay-dsjava:${PROXY_TAG} bscdataclay/dsjava:mf2c 

if [ "$PUSH" = true ] ; then
	echo " ===== Pushing dockers dataclay mf2c dockers DockerHub ====="
	docker-push-mf2c ${PROXY_TAG}
else 
	echo " ===== NOT Pushing any docker into DockerHub ====="
fi

if [ "$PUSH" = true ] ; then
	echo " ===== Installing dataClay mf2c wrapper $PROXY_TAG ====="
	maven_install_wrapper $DATACLAY_MF2C_STUBS_JAR $PROXY_TAG $DATACLAY_MF2C_MAVEN_REPO

	echo " ===== Pushing  dataClay maven mf2c wrapper into GitHub ====="
	pushd $DATACLAY_MF2C_MAVEN_REPO
	git add -A .
	git commit -m "Updating dataclay maven mf2c wrapper with current development" 
	git push origin repository
	popd
else 
	echo " ===== Installing dataClay mf2c wrapper $PROXY_TAG ====="
	maven_install_wrapper $DATACLAY_MF2C_STUBS_JAR $PROXY_TAG
	echo " ===== NOT Pushing  dataClay maven mf2c wrapper into GitHub ====="
fi

echo " ===== Installing dataclay-proxy with tag $PROXY_TAG ====="
pushd $PROXY
bash release.sh $PROXY_TAG
popd

if [ "$PUSH" = true ] ; then
	echo " ===== Pushing  dataclay proxy in DockerHub ====="
	docker push mf2c/dataclay-proxy:${PROXY_TAG}
	docker push mf2c/dataclay-proxy:latest
else 
	echo " ===== NOT Pushing dataclay-proxy ====="
fi


