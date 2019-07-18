# mF2c dataClay

Welcome! Here you will find some information in order to bootstrap
a "dataClay environment". You can use it to familiarize that with
mF2c dataClay, to start developing applications, to explore the different
components, etc.

To read the usage manual, open the pdf file. To bootstrap your environment
and start testing and hacking, keep reading.

## Preflight check

You need:

  - **docker** Get it through https://www.docker.com/ > Get Docker
  - **docker-compose** Quickest way to get the latest release https://github.com/docker/compose/releases
  - **Python 3.5.x or 2.7.x**
  - **Java 8**

Also:
  - A python **virtualenv** for the previous interpreter is strongly suggested, but not required.


## Initializing the dataClay services

A `docker-compose.yml` is provided to ease the process. You can simply do:

    $> cd data_management/dockers
    $> docker-compose down # stop and clean previous containers
    $> docker-compose up

That will download and orchestrate the different images needed. Note that, 
by default, the dataClay LogicModule will be listening to port 1034.

**Do not Ctrl+C the process**. If you want the `docker-compose` to be in the
background, add the flag `-d` (detached mode). Otherwise, just leave that 
opened and proceed into a new terminal. For further options on `docker-compose`
process, read its documentation.

## Executing dataClay demo examples

At this point, you can follow specific README files included in the different demos available from the corresponding folder. 

## data management module 

This module contains the mf2C implementation for dataClay. Here we will find the mF2C model (classes 
that represents mF2C resources) and the mF2c dm_app responsible to react to any CIMI request. For 
instance, when a CIMI get arrives, the dm_app performs it using dataClay objects.  
((documentation of section in progress...))

## dataclay proxy module

This module contains necessary code to integrate dataClay with mF2C components. In particular, here
we will find specific code to translate CIMI requests to requests to dataClay dm_app. 
((documentation of section in progress...))

# mF2c developers guide 

https://github.com/mF2C/dataClay/wiki/dataClay-Mf2c--a-developers-guide-for-CIMI-resources
