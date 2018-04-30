Contents
========

- `src/model`: basic class model for the demo.
- `src/demo`: main application for the demo.
- `cfgfiles`: folder containing session.properties for apps plus client.properties as example to connect to (dockerized) dataClay.
- `*.sh`: scripts sorted by execution order for a full test.

Notice that these scripts will generate a 'bin' directory (for compiled apps and model) 
and a 'stubs' directory to store downloaded stubs after model registration.

Execution example
=================

`1_SetBackup.sh`
- Registers a secondary logic module as a backup of the primary one.

`2_RegisterModel.sh`
- Registers the class model for the demol.

`3_BuildApps.sh`
- Builds demo application.

`4_RunApp.sh`
- Runs the application.
	- Creates an object on a DS node.
	- Replicates the object on another DS node.
	- Simulates a shutdown in primary Logic Module and one DS node.
	- The system recovers autonomously.
