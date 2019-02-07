FROM openjdk:8-jdk
ADD cfgfiles /root/dataclay/cfgfiles
ADD src /root/dataclay/src
ADD ./target/proxy-2.4-standalone.jar /root/dataclay/proxy.jar
WORKDIR /root/dataclay
EXPOSE 6472

ENTRYPOINT ["java", "-Dlog4j.configurationFile=file:cfglog/log4j2.xml", "-cp", "proxy.jar", "com.sixsq.dataclay.proxy"]

