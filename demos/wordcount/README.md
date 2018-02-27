Contents
========

- `src/model`: class model for a distributed index of text objects
- `src/producer`: producer application that creates persistent text objects from files stored in backends
- `src/consumer`: consumer application that computes Wordcount considering all indexed text objects. Itf available for COMPSs.

- `cfgfiles`: folder containing session.properties for apps plus client.properties as example to connect to (dockerized) dataClay.
- `*.sh`: scripts sorted by execution order for a full test.

Notice that these scripts will generate a 'bin' directory (for compiled apps and model) 
and a 'stubs' directory to store downloaded stubs after model registration.

Apps Requirements
=================

**It is expected that environment variable called COMPSSLIB is defined, pointing to the path of a recent compss-engine.jar lib.**

Both TextGeneration and WordCount scripts will ask for the alias for the Index of files. This will serve as an entry point to find all texts.

Also, TextGeneration script asks for a remote path (a folder with files, or a single file), the distributed index will read them to create 
the actual Text objects in dataClay. 

This path must be found from any dataClay backend. For instance, in MN a shared filesystem can be used to share a specific location.

 
Dockers example (1 backend)
===========================

With Global Requirements satisfied and dockers orchestrated with the provided docker-compose file, 
you can proceed with the following steps to execute a Wordcount test:

`0_FilesToDocker.sh`
- exports files directory to dataClay docker

`1_RegisterModel.sh`
- registers the class model in dataClay

`2_BuildApps.sh`
- builds the producer/consumer applications

`3_TextGeneration.sh`
- runs producer application

`4_WordCount.sh`
- runs consumer application

Notice that having extra backends would require deploying files (`0_FilesToDocker.sh`) to other backends.
The script can modified accordingly to do it.
