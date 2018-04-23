Contents
========

- `src/CIMI`: dataClay classes implementing CIMI resources
- `src/api`: methods (create, read, update, delete, query) to be called from clojure to interact with dataClay. It exports objects as JSON strings.
- `src/demo`: a small sample application calling the previous methods.
- `demo` : folder with scripts and JSON files with sample data. 
- `cfgfiles`: folder containing session.properties for apps plus client.properties as example to connect to (dockerized) dataClay.
- `registerModel.sh`: script that registers the class model in dataClay

Notice that these scripts will generate a 'bin' directory (for compiled apps and model) 
and a 'stubs' directory to store downloaded stubs after model registration.

Execution example
=================

Steps to execute the sample application in `src/demo`:

`bash demo/buildApp.sh `

`bash demo/runApp.sh demo/jsons/Services.json demo/jsons/Devices.json demo/jsons/DeviceDynamics.json demo/jsons/ServiceInstances.json`
