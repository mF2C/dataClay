# Proxy Service for DataClay

A service that acts as a simple proxy between the SixSq CIMI server
and DataClay.


## Usage

You must have the DataClay system running before starting this
proxy. To do that use the Docker compose file in the ``orchestration``
directory of this repository.  From there, the system can be started
with ``docker-compose up``.

Next you will have to compile the mF2C resources into a DataClay model
and register them.  Run the following script from this directory:

```sh
$ bash registerModel.sh
```

Then you must compile the DataClayWrapper that acts as an interface
between DataClay and the CIMI SCRUD actions.

```sh
$ bash buildApp.sh
```

At this point, you can actually start the DataClay proxy service.
Just do the following: 

```sh
$ lein run
```

When this starts, it will provide an endpoint at http://localhost:8080
that will an edn-encoded string, run the appropriate DataClay SCRUD
action, and return the results as an edn-encoded string.

The format of the edn-encoded request is:

```clojure
{
 :action "action-name"
 :args ["argument1", "argument-2", ...]
}
```

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
