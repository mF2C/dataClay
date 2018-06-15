FROM openjdk:8-jdk
MAINTAINER SixSq <support@sixsq.com>
LABEL copyright="Copyright (c) 2018, SixSq Sarl \
Licensed under the Apache License, Version 2.0 (the "License"); \
you may not use this file except in compliance with the License. \
You may obtain a copy of the License at \
    http://www.apache.org/licenses/LICENSE-2.0 \
Unless required by applicable law or agreed to in writing, software \
distributed under the License is distributed on an "AS IS" BASIS, \
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or \
implied.  See the License for the specific language governing \
permissions and limitations under the License."

ADD cfgfiles /root/dataclay/cfgfiles
ADD stubs /root/dataclay/stubs
ADD bin /root/dataclay/bin
ADD cfglog /root/dataclay/cfglog
ADD src /root/dataclay/src
ADD buildApp.sh /root/dataclay/
ADD registerModel_v2.sh /root/dataclay/
ADD run.sh /root/dataclay/
ADD tool /root/tool
ADD tool/lib/dataclayclient.jar /root/dataclay/

ADD ./target/proxy-0.1.0-SNAPSHOT-standalone.jar /root/dataclay/proxy.jar

ENV DATACLAY_JAR=/root/dataclay/dataclayclient.jar

WORKDIR /root/dataclay

EXPOSE 6472

CMD ["sh", "/root/dataclay/run.sh"]

