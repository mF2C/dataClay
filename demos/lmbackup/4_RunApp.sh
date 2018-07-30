#!/bin/bash

LIBPATH="../../tool/lib"
CLASSPATH="stubs:bin:$LIBPATH/dataclayclient.jar:$LIBPATH/dependencies/*"

APP="demo.LMBackupDemo"
BASECP="stubs:bin"
LOG4J2="-Dlog4j.configurationFile=file:cfglog/log4j2.xml"
echo ""
echo "Executing: "
echo ""
echo " java $LOG4J -cp $CLASSPATH $APP"
echo ""

# Make pipe
mkfifo demo_pipe

# Running demo in background 
java -cp $CLASSPATH $LOG4J2 $APP > output.txt 2>&1 <demo_pipe &
exec 6>demo_pipe

# Wait until process requires input
while ! grep -q "Input something" output.txt; do sleep 2;done

# Stop non-desirable dockers 
docker stop orchestration_ds1java1_1 >/dev/null 2>&1
docker stop orchestration_logicmodule1_1 >/dev/null 2>&1

# Notify everything stopped
echo "Hi">&6;

# Wait to finish
while ! grep -q "Demo finished" output.txt;do echo "Waiting demo to finish..."; sleep 10; done

# Print output 
echo "#### Demo output ####"
cat output.txt

# Clean
rm -f demo_pipe
rm -f output.txt
