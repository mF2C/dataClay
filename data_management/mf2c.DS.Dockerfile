ARG DATACLAY_TAG
FROM bscdataclay/dsjava:${DATACLAY_TAG}
MAINTAINER dataClay team <support-dataclay@bsc.es>

COPY ./execClasses execClasses

ENTRYPOINT ["java", "-cp", "dataclay.jar:lib/*", "-Dlog4j.configurationFile=/usr/src/dataclay/log4j2.xml", "dataclay.dataservice.server.DataServiceSrv"]
# Don't use CMD in order to keep compatibility with singularity container's generator
