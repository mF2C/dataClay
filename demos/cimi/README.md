Contents
========

- `src/model`: class model for a distributed index of text objects.
- `src/app`: main application for this CIMI filtering demo.

- `cfgfiles`: folder containing session.properties for apps plus client.properties as example to connect to (dockerized) dataClay.
- `*.sh`: scripts sorted by execution order for a full test.

Notice that these scripts will generate a 'bin' directory (for compiled apps and model) 
and a 'stubs' directory to store downloaded stubs after model registration.

Execution example
=================

`1_RegisterModel.sh`
- Registers the class model in dataClay

`2_BuildApps.sh`
- Builds the the main demo application

`3_DataGenerator.sh`
- Stores the basic data (objects) for the demo in dataClay.
- It requires two paths: JSON devices path and JSON agents path. 
- json directory contains two valid examples.

`4_BindingMockup.sh`
- Runs the application.
- It requires a numeric value for the filter of devices.

