# Proxy Service for DataClay

A service that acts as a simple proxy between the SixSq CIMI server
and DataClay.

## Build DataClay Proxy

To build the Docker image which contains the DataClay proxy service,
you'll find the `release.sh` script which will:
 
 1. deploy DataClay
 2. wait for it to be ready
 3. generate a random user password
 4. run registerModel
 5. run buildApp
 6. build an uberjar with for the proxy
 7. adapt the cfgfiles to user the DC container service name:port, and
    update the user password
 8. build the Docker image, adding all the created and modified files
    into the image
 9. tag the created image to latest
 10. cleanup the local environment

The script accepts the Docker image tag as an argument, so all you
have to do from this directory is:

```bash
export TAG=1.1 # for example
./release.sh ${TAG}    
```

After this you should end up with `mf2c/dataclay-proxy:${TAG}` and
`mf2c/dataclay-proxy:latest` in your local docker repository. You can
then use `docker push` to upload them to the mF2C public Docker Hub.

## Deploying the proxy

If DataClay is already running, to deploy the proxy all you need to
know is the Docker network where DataClay is running (`docker network
ls`).  Let's assume this is `orchestration_default`. Then:

```bash
export DC_NET=orchestration_default
export TAG=1.1
docker run --net $DC_NET -p 6472:6472 mf2c/dataclay-proxy:${TAG}
```

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
