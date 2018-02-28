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

Dockers example (1 backend)
===========================

With Global Requirements satisfied and dockers orchestrated with the provided docker-compose file, 
you can proceed with the following steps to execute a Wordcount test:

`1_FilesToDocker.sh`
- Exports files directory to dataClay docker. 

`2_RegisterModel.sh`
- Registers the class model in dataClay

`3_BuildApps.sh`
- Builds the producer/consumer applications

`4_TextGeneration.sh`
- Runs producer application. 
- An alias for the index of texts is provided. 

`5_WordCount.sh`
- Runs consumer application. 
- The alias used in script 3 must be provided.

Further considerations
======================

- Scenarios with more than one backend would require deploying files (`1_FilesToDocker.sh`) to other backends. To do so, nowadays, would imply changing the script properly.

- Input directory of text files is currently hardcoded in `1_FilesToDocker.sh` script, but it can be changed editing the script properly.
