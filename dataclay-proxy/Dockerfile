FROM openjdk:11-jdk
ADD cfgfiles /root/dataclay/cfgfiles
ADD src /root/dataclay/src
ADD proxy.jar /root/dataclay/proxy.jar
WORKDIR /root/dataclay
EXPOSE 6472

ENTRYPOINT ["java", "-cp", "proxy.jar", "com.sixsq.dataclay.proxy"]

