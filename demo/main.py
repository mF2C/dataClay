# Initialize dataClay
import os
from dataclay import api

api.init()

from mf2c_mock_functions import *
from model_mf2c.classes import *


def run_mf2c_as_leader(my_agent):
    # invented syntax for the messages
    discovered_id = send_welcome_message()

    if my_agent.check_child_exists(discovered_id):
        send_ack(discovered_id)
    else:
        provide_keys(discovered_id)
        ### The Agent should have been made persistent on its own,
        ### so you can get_by_alias it and do whatever you need with that

        # new_agent = Agent.get_by_alias(discovered_id) #remote get
        # my_agent.children.add(new_agent) #add reference to children list
        # new_agent.aggregated_resource_info.new_replica("LOCAL")


def run_mf2c_as_normal(my_agent):
    # ...(respond to welcomes, keep-alives, etc) - needs to do nothing about data
    pass


if __name__ == "__main__":

    # Create and store user. This would be done in the cloud, but I do it here for testing
    user_key = "id-user"
    my_user = User(user_id=user_key, email="email", name="name")
    print my_user.getID()
    #user.make_persistent(alias=user_key)
    my_user.make_persistent(alias=user_key)

    # Identification module
    # user_key should be get from the local device
    # seed should be asked to the user
    seed = "1"
    device_id = generate_device_id(user_key, seed)

    owner = User.get_by_alias(user_key)  # The user is already stored in the cloud (User Management has created it)
    # owner.new_replica("LOCAL") # Create a local copy of the user in this agent to avoid remote accesses

    # Categorization module
    static_info = collect_static_info()
    dynamic_info = collect_dynamic_info()
    network_info = collect_network_info()
    components = collect_components_info() # this one is a list

    my_device = Device(device_id, my_user, static_info, dynamic_info, network_info, None, None, components, None)
    # my_device.make_persistent()

    my_agent = Agent(my_device) # The agent is not persistent now
    # my_agent.make_persistent(my_agent.id, api.LOCAL) # my_agent.id is the alias to recover the object
    my_agent.make_persistent(my_agent.id) # Without "LOCAL" for testing. It must be recursive

    device = my_agent.dev
    assert my_device == device 

    user = device.owner
    assert my_user == user

    #owner = User.get_by_alias(user_key)
    #assert my_user == owner

    # Some other component(s) will do:
    behaviour_info = collect_behaviour_info()
    security_info = collect_security_info()
    sharing_info = collect_sharing_model_info()

    my_agent.set_behaviour_info(behaviour_info)
    my_agent.set_security_info(security_info)
    my_agent.set_sharing_model_info(sharing_info)

    my_agent.is_leader = True
    # You may want to test that instead... note that it will fail the get_static_info => that's by design!
    # my_agent.is_cloud = True

    # Test all
    test_all_not_cloud(my_agent)

    # Invented skeleton. I am not sure how the Cloud Agent should be managed
    if my_agent.is_leader:
        run_mf2c_as_leader(my_agent)
    else:
        run_mf2c_as_normal(my_agent)

    # Testing methods as cloud agent
    my_agent.is_cloud = True
    test_all_cloud(my_agent)

    api.finish()
