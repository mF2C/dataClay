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
- Exports files directory to dataClay docker. 
- A target directory path (**remote\_path** for step 3) of backend docker must be provided.

`1_RegisterModel.sh`
- Registers the class model in dataClay

`2_BuildApps.sh`
- Builds the producer/consumer applications

`3_TextGeneration.sh`
- Runs producer application. 
- An alias for the index of texts (**text\_col\_alias**) and **remote\_path** from step 0 are provided.

`4_WordCount.sh`
- Runs consumer application. 
- The **text\_col\_alias** is provided.

Notice that having extra backends would require deploying files (`0_FilesToDocker.sh`) to other backends.
