FROM openjdk:8-jdk

ADD cfgfiles /root/dataclay/cfgfiles
ADD stubs /root/dataclay/stubs
ADD bin /root/dataclay/bin
ADD cfglog /root/dataclay/cfglog
ADD src /root/dataclay/src

ADD waitForBackends.sh /root/dataclay/
ADD buildApp.sh /root/dataclay/
ADD registerModel_v2.sh /root/dataclay/
ADD run.sh /root/dataclay/

ADD tool /root/tool

ADD ./target/proxy-0.1.0-SNAPSHOT-standalone.jar /root/dataclay/proxy.jar

ENV DATACLAY_JAR=/root/tool/lib/dataclayclient.jar

WORKDIR /root/dataclay

EXPOSE 6472

CMD ["sh", "/root/dataclay/run.sh"]

