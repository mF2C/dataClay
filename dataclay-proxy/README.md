# Proxy Service for DataClay

A service that acts as a simple proxy between the SixSq CIMI server
and DataClay.


## Start DataClay

You must have the DataClay system running before starting this
proxy. To do that use the Docker compose file in the ``orchestration``
directory of this repository.  From there, the system can be started
with ``docker-compose up``.

## Mac OS X

If you're not on an Apple machine, then you can skip to the next step
to initialize DataClay.  If you're using an Apple machine then, you
must run the DataClay proxy from another machine or from within a
Linux container.

**WARNING: The dataClay client does NOT work on Mac OS X.**

If you'll run the DataClay proxy from a container, you must odify the
`client.properties` configuration file.  The file should contain the
values:

```
HOST=logicmodule1
TCPPORT=1034
```

This will point to the service inside of the Docker network.

You can then start the container.  You'll want to mount the repository
workspace in the container and probably also your SSH credentials.

```bash
$ docker run -it \
    -v /Users/loomis/Documents/code/SixSq/dataClay:/root/dataClay \
    -v /Users/loomis/.ssh/:/root/.ssh \
    --network orchestration_default \
    --publish 6472:6472 \
    openjdk:8-jdk
```

You will have to modify the file system mounts and you may need to
modify the value of the `--network` parameter.

From within the container, set the `DATACLAY_JAR` environmental
variable to point to the `dataclayclient.jar` file.  Download this if
necessary.  The automated extraction of this with Docker will not work
within a Docker container.

You will also have to download Leiningen to run the service.  Follow
the [Leiningen installation
instructions](https://leiningen.org/#install).  This is essentially
the following:

```bash
$ curl -o /usr/bin/lein \
    https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein 
$ chmod a+x /usr/bin/lein 
$ lein --help 

```

Now, you can continue with the steps below.

## Initialize DataClay

Next you will have to compile the mF2C resources into a DataClay model
and register them.  Run the following script from the `dataclay-proxy`
subdirectory of the repository:

```sh
$ bash registerModel.sh
```

Then you must compile the DataClayWrapper that acts as an interface
between DataClay and the CIMI SCRUD actions.

```sh
$ bash buildApp.sh
```

## Start DataClay Proxy

Before starting the service, make sure that you've provided your
repository credentials in the file `~/.lein/profiles.clj`.  You will
need this to download the DataClay client jar file.

At this point, you can actually start the DataClay proxy service.
Just do the following: 

```sh
$ lein run
```

If it starts successfully, you'll see a message like:

```
INFO: dataClay proxy successfully started on port 6472
```

on the console.

## Proxy Usage

The running service provides an endpoint at http://localhost:6472 that
will take an edn-encoded command, run the appropriate DataClay SCRUD
action, and return the results as an edn-encoded string.

The format of the edn-encoded request is:

```clojure
[:action arg1 arg2]
```

where the action is `:add`, `:delete`, etc. with the arguments
following.  The number and type of arguments must match the function
signatures.

The response will be of the form:

```clojure
{
 :status 200
 :body "edn-encoded response"
}
```

The status can be any valid HTTP status code.  The content of the body
will depend on the action being called.

## License

**NOTE: The changes from the upstream repository are owned by SixSq
Sarl and subject to the Apache 2 License.  The code from the upstream
repository is owned by the original contributor(s) and subject to
unknown licenses.  You must contact the original contributor(s) to
determine if you can use their code.**

Copyright (c) 2018, SixSq Sarl

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
