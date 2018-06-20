
DEPRECATED
==========

Python is not being used in mF2C, so orchestration and examples have been focused to the Java execution.

dataClay keeps having support for Python, but no mF2C documentation is included. Consider this document
deprecated and untested from now on.


Installation of Python client libraries
=======================================

The package is in the PyPI. Install it with `pip` with:

    $> pip install dataClay

We strongly suggest to do that inside a python virtual environment. Python 2.7.9 and newer are supported. Python 3.x is currently not supported.

Registration
============

The user, dataset and data model must be registered prior to the application
execution. In this examples, we provide a simple script which automates this
process.

Remember that this registration should be done with the dataClay
services up and running.

Perform the registration with:

    $> bash register.sh

Currently, plain passwords are used to ease development, debugging and testing. Of course,
this will work differently in the production version.

You can check the register.sh or adapt it to your needs. Take note that the following 
files are provided by dataClay and are intended to be used just as they are:

  - tool/lib/dataclayclient.jar
  - tool/dClayTool.sh

If there are some errors or you would like to adapt something, contact dataClay 
developers.

Note that for size reasons in GitHub, the `dataclayclient.jar` is not in the
repository and instead will be automatically retrieved by the dataClay tool
script. If everything works as expected, no special action is required.


Configuration files
===================

There is a folder called `cfgfiles` in which you can find two files:

  - **`client.properties`** which is provided and you should change it if you
change the orchestration of the service.
  - **`session.properties`** which is generated during the registration step.

You are not required to change anything in that folder. Just check that the 
`session.properties` has been correctly populated.


Application execution
=====================

The provided example is prepared to use the persistence framework. To execute
the application just call it:

    $> python demo/main.py

Note that the relatives paths are important for the dataClay framework to find
the `cfgfiles` folder.

**Standard error can be redirected to prevent logging messages on main screen**
