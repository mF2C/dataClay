from pprint import pprint

from model_mf2c.classes import (
    StaticInfo, DynamicInfo, NetworkInfo, BehaviourInfo, SecurityInfo,
    SharingModelInfo, Component, User, Device, Agent)

def calculate_agent_value(my_agent):
    #do whatever
    return my_agent.dev.static_info.total_storage_size * 0.5


def send_welcome_message():
    return "id1"


def send_ack(discovered_id):
    pass


def provide_keys(discovered_id):
    pass


def generate_device_id(user_key, seed):
    return user_key + seed


def collect_static_info():
    # do whatever and create StaticInfo instance
    si = StaticInfo("OS", "System arch", "Proc maker", "Proc arch", 4, 1024, 16, 1024, 1024, False, "Graphics card")
    si.make_persistent()
    return si


def collect_dynamic_info():
    # do whatever and create DynamicInfo instance
    di = DynamicInfo(1024, 100, 1024, 100, 100, 1000, 100, "location")
    di.make_persistent()
    return di


def collect_network_info():
    # do whatever and create NetworkInfo instance
    ni = NetworkInfo("IO", "Eth info", "Eth addr", "Wifi info", "Wifi addr", 1024, "standard")
    ni.make_persistent()
    return ni


def collect_behaviour_info():
    bi = BehaviourInfo(True, "reliability", "trust", True)
    bi.make_persistent()
    return bi


def collect_security_info():
    si = SecurityInfo(True, True, True)
    si.make_persistent()
    return si


def collect_sharing_model_info():
    smi = SharingModelInfo(1024, 1024, 100, 1024)
    smi.make_persistent()
    return smi


def collect_components_info():
    components = [Component("sensor type", "actuator type", "communication info", "device type", "location")]
    for c in components:
        c.make_persistent()
    return components


# Other tests

def test_all_not_cloud(agent):
    # Testing all agent functions
    # Static info
    print "Static info (not modifiable):"
    pprint(agent.get_static_info())
    # Dynamic info
    print "Dynamic info (before update):"
    pprint(agent.get_dynamic_info())
    agent.set_available_RAM_size(1)
    agent.set_available_RAM_percent(2)
    agent.set_available_storage_size(3)
    agent.set_available_storage_percent(4)
    agent.set_available_CPU_percent(5)
    agent.set_remaining_power_seconds(6)
    agent.set_remaining_power_percent(7)
    agent.set_location("location2")
    print "Dynamic info (after update):"
    pprint(agent.get_dynamic_info())
    # Network info
    print "Network info (before update):"
    pprint(agent.get_network_info().to_dict())
    agent.set_network_io("io2")
    agent.set_ethernet_info("eth-info2")
    agent.set_ethernet_address("eth-addr2")
    agent.set_wifi_info("wifi-info2")
    agent.set_wifi_address("wifi-addr2")
    agent.set_bandwidth_capacity(1)
    agent.set_standard_info("std-info2")
    print "Network info (after update):"
    pprint(agent.get_network_info().to_dict())
    # Security info
    print "Security info (before update):"
    pprint(agent.get_security_info().to_dict())
    agent.set_data_privacy(False)
    agent.set_network_security(False)
    agent.set_device_security(False)
    print "Security info (after update):"
    pprint(agent.get_security_info().to_dict())
    # Behaviour info
    print "Behaviour info (before update):"
    pprint(agent.get_behaviour_info().to_dict())
    agent.set_mobility(False)
    agent.set_reliability("Reliability2")
    agent.set_trust("Trust2")
    agent.set_leader_capability(False)
    print "Behaviour info (after update):"
    pprint(agent.get_behaviour_info().to_dict())
    # Sharing info
    print "Sharing info (before update):"
    pprint(agent.get_sharing_model_info().to_dict())
    agent.set_shared_RAM_size(1)
    agent.set_shared_storage_size(2)
    agent.set_shared_CPU_percent(3)
    agent.set_shared_bandwidth_size(4)
    print "Sharing info (after update):"
    pprint(agent.get_sharing_model_info().to_dict())
    # Get resource
    usr, stt, dyn, net, beh, sec = agent.get_resource_info()
    print "Get resource info-User id: %s, name: %s, email: %s" % (usr.id_key, usr.email, usr.name)
    print "Get resource info-Static: "
    pprint(stt.to_dict())
    print "Get resource info-Dynamic:"
    pprint(dyn.to_dict())
    print "Get resource info-Network:"
    pprint(net.to_dict())
    print "Get resource info-Behaviour"
    pprint(beh.to_dict())
    print "Get resource info-Security"
    pprint(sec.to_dict())
    print "Get resource info as dict: "
    pprint(agent.get_resource_info_as_dict())
    # Attached Components
    print "Get attached component info: "
    pprint(agent.get_attached_component_info_as_list())
    new_comp = Component("new_sensor_type", "new_actuator_type", "new_comm_info", "new_dev_type", "new_location")
    new_comp.make_persistent()

    agent.add_attached_component(new_comp)
    print "Get attached component info (after add)"
    pprint(agent.get_attached_component_info_as_list())
    agent.remove_attached_component(new_comp)
    print "Get attached component info (after remove)"
    pprint(agent.get_attached_component_info_as_list())
    print "Trying to remove unexisting component..."
    agent.remove_attached_component(new_comp)
    # Cloud
    multi = agent.get_multicloud_federation()
    print "Multi-cloud federation (before update)", multi
    print "Multi-cloud federation (after update)", not multi


def test_all_cloud(agent):
    # Children
    agent.is_cloud = True
    print "Get all children info: "
    pprint(agent.get_all_children_info())
    static_info = collect_static_info()
    dynamic_info = collect_dynamic_info()
    network_info = collect_network_info()
    behaviour_info = collect_behaviour_info()
    security_info = collect_security_info()
    components = collect_components_info()  # this one is a list
    sharing_info = collect_sharing_model_info()  # this one is a list
    owner = User("id-user2", "email2", "name2")
    new_device = Device("New-child-id", owner, static_info, dynamic_info, network_info, behaviour_info, security_info, components, sharing_info)
    new_agent = Agent(new_device)
    new_agent.make_persistent()

    agent.add_child(new_agent)
    print "Check new child exists: "
    pprint(agent.check_child_exists(new_agent.id))
    print "Check invented child exists: "
    pprint(agent.check_child_exists("invented_id"))
    print "Get all children info (after add_child): "
    pprint(agent.get_all_children_info())
    agent.remove_child(new_agent)
    print "Check removed child exists: "
    pprint(agent.check_child_exists(new_agent.id))
    print "Get all children info (after remove_child): "
    pprint(agent.get_all_children_info())
    print "Trying to remove unexisting child..."
    agent.remove_child(new_agent)
