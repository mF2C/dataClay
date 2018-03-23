Contents
========

- `src/model`: basic class model representing CIMI resources of types agent and device.
- `src/app: mockup agent binding app that behaves differently depending on whether the agent is a leader or a normal agent.
- `mobile`: working directory for the normal agent with its own configuration files to connect to dataClay 1.
- `leader`: working directory for the leader with its own configuration files to connect to dataClay 2
- `.sh`: scripts sorted by execution order for a full test. These scripts are executed separatedly for both leader and mobile from their wokring dirs.

Notice that these scripts will generate a 'bin' directory (for compiled apps and model) 
and a 'stubs' directory to store downloaded stubs after model registration.

Dockers example
===============

After dockers orchestrated with the provided docker-compose file, 
you can proceed with the following steps to execute a federation test:

From leader workingcopy:
`../1_RegisterModel.sh`
`../2_BuildApps.sh`
`../3_AgentMockup.sh`
- This will keep waiting until a federated object is received from mobile side.

From mobile workingcopy:
`../1_RegisterModel.sh`
`../2_BuildApps.sh`

At this point, we simulate the discovery of the leader by running:
`./discoverLeader.sh`
- This script will show the leader dataClay info to be passed in next step.

`../3_AgentMockup.sh <info_from_previous_step>`
- This last script will federate its device info with the leader after a simulated process of discovery.

Synchronization
===============

Once the object is federated, the output will be as follows:

`Hwloc before set: null`
`CPUInfo before set: null`

Now:
- Press "ENTER" in mobile agent to simulate a change in the info:

`Hwloc after set: This Hwloc has been set in agent 4`
`CPUInfo after set: This CPUInfo has been set in agent 4`

- Press "ENTER" in leader agent to check info is available from leader dataClay as well:

`Hwloc after set: This Hwloc has been set in agent 4`
`CPUInfo after set: This CPUInfo has been set in agent 4`
