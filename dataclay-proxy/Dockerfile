FROM ubuntu:18.04

# Install packages:
# - sqlite libraries
RUN apt-get update \
        && apt-get install --no-install-recommends -y --allow-unauthenticated default-jdk \
        && rm -rf /var/lib/apt/lists/*

ADD cfgfiles /root/dataclay/cfgfiles
ADD src /root/dataclay/src
ADD proxy.jar /root/dataclay/proxy.jar
ADD health_check /root/dataclay/health_check
WORKDIR /root/dataclay
EXPOSE 6472

ENTRYPOINT ["java", "-cp", "proxy.jar", "com.sixsq.dataclay.proxy"]

