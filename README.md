# dataClay

Welcome! Here you will find some information in order to bootstrap
a "dataClay environment". You can use it to familiarize that with
dataClay, to start developing applications, to explore the different
components, etc.

To read the usage manual, open the pdf file. To bootstrap your environment
and start testing and hacking, keep reading.

## Preflight check

You need:

  - **docker** Get it through https://www.docker.com/ > Get Docker
  - **docker-compose** Quickest way to get the latest release https://github.com/docker/compose/releases
  - **Python 2.7.x**
  - **Java 8**

Also:
  - A python **virtualenv** for the previous interpreter is strongly suggested, but not required.


## Initializing the dataClay services

A `docker-compose.yml` is provided to ease the process. You can simply do:

    $> cd orchestration
    $> docker-compose down # stop and clean previous containers
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

## Executing demo examples

At this point, you can follow specific README files included in the different demos available from the corresponding folder. 
