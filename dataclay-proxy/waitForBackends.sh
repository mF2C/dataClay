#!/bin/bash
#Check params
DATACLAY_ADMIN_USER=admin
DATACLAY_ADMIN_PASSWORD=admin
LANGUAGE=java
NUMBER_OF_SL=1
NUMBER_OF_EE_PER_SL=1
DATACLAY_TOOL=../tool/dClayTool.sh

DSCOUNTER=1
while [ $DSCOUNTER -le $NUMBER_OF_SL ]; do
  NUMBER_OF_EE=`$DATACLAY_TOOL GetBackends $DATACLAY_ADMIN_USER $DATACLAY_ADMIN_PASSWORD $LANGUAGE | grep -e "^DS$DSCOUNTER" | wc -l`
  if [ ${NUMBER_OF_EE:-0} -lt $NUMBER_OF_EE_PER_SL ]; then
    sleep 2
  else
    echo "[`date`] *** DataService DS$DSCOUNTER for language $LANGUAGE is ready"
    let DSCOUNTER+=1
  fi
done
