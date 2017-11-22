
# dataClay

Welcome! Here you will find some information in order to bootstrap
a "dataClay environment". You can use it to familiarize that with
dataClay, to start developing applications, to explore the different
components, etc.

## Preflight check

You need:

  - **docker** Get it through https://www.docker.com/ > Get Docker
  - **docker-compose** Quickest way to get the latest release https://github.com/docker/compose/releases
  - **Python 2.7.x**
  - **Java 8**

Also:
  - A python **virtualenv** for the previous interpreter is strongly suggested, but not required.


## Installation of Python client libraries

The package is in the PyPI. Install it with `pip` with:

    $> pip install dataClay

We strongly suggest to do that inside a python virtual environment. Python 2.7.9 and newer are supported. Python 3.x is currently not supported.


## Initializing the dataClay services

A `docker-compose.yml` is provided to ease the process. You can simply do:

    $> cd orchestration
    $> docker-compose rm  # to clean the previous containers, if exist
    $> docker-compose up

That will download and orchestrate the different images needed. Note that, 
by default, the dataClay LogicModule will be listening to port 11034.

You have to wait for initialization to finish before going on. Typically, 
one of the last messages will be the registration of Python DataService, 
which will be something like:

    [LOGICMODULE] Registered ExecutionEnvironment [EE/172.23.0.2:2127] for language `LANG_PYTHON` as 4a98e888-0927-45e9-bf0b-acd468aaad5b

(The important part of last message is **LANG_PYTHON**).

**Do not Ctrl+C the process**. If you want the `docker-compose` to be in the
background, add the flag `-d` (detached mode). Otherwise, just leave that 
opened and proceed into a new terminal. For further options on `docker-compose`
process, read its documentation.


## Registration

The user, dataset and data model must be registered prior to the application
execution. In this examples, we provide a simple script which automates this
process.

Remember that this registration should be done with the dataClay
services up and running.

Perform the registration with:

    $> bash register.sh

You can check the register.sh or adapt it to your needs. Take note that the following 
files are provided by dataClay and are intended to be used just as they are:

  - dataclayclient.jar
  - dataClayTools.jar
  - dClayTool.sh

If there are some errors or you would like to adapt something, contact dataClay 
developers.


### Configuration files

There is a folder called `cfgfiles` in which you can find two files:

  - **`client.properties`** which is provided and you should change it if you
change the orchestration of the service.
  - **`session.properties`** which is generated during the registration step.

You are not required to change anything in that folder. Just check that the 
`session.properties` has been correctly populated.


## Application execution

The provided example is prepared to use the persistence framework. To execute
the application just call it:

    $> python demo/main.py

Note that the relatives paths are important for the dataClay framework to find
the `cfgfiles` folder.

