from dataclay import dclayMethod
from storage.api import StorageObject


class ToDictMixin(object):
    @dclayMethod(fields="list<str>", return_="dict")
    def _to_dict(self, fields):
        return {field: self.__getattribute__(field) for field in fields}


class User(StorageObject, ToDictMixin):
    """
    @ClassField id_key str
    @ClassField email str
    @ClassField name str
    @ClassField service_consumer bool
    @ClassField resource_contributor bool
    @ClassField allowed_services list
    """

    @dclayMethod(user_id="str", email="str", name="str")
    def __init__(self, user_id, email, name):
        self.id_key = user_id
        self.email = email
        self.name = name
        self.service_consumer = False
        self.resource_contributor = False
        self.allowed_services = []

    @dclayMethod(return_="dict")
    def get_user_profile(self):
        fields = ["id_key", "email", "name", "service_consumer", "resource_contributor"]
        return self._to_dict(fields)


class StaticInfo(StorageObject, ToDictMixin):
    """
    @ClassField operating_system str
    @ClassField system_architecture str
    @ClassField processor_maker_name str
    @ClassField processor_architecture str
    @ClassField num_CPUs int
    @ClassField CPU_clock_speed int
    @ClassField total_num_cores int
    @ClassField total_RAM_size int
    @ClassField total_storage_size int
    @ClassField limited_power bool
    @ClassField graphics_card_info str
    """

    @dclayMethod(os="str", arch="str", proc_maker="str", proc_arch="str", num_CPUs="int", CPU_speed="int", total_cores="int", RAM_size="int", storage_size="int", limited="bool", graphics_card="str")
    def __init__(self, os, arch, proc_maker, proc_arch, num_CPUs, CPU_speed, total_cores, RAM_size, storage_size, limited, graphics_card):
        self.operating_system = os
        self.system_architecture = arch
        self.processor_maker_name = proc_maker
        self.processor_architecture = proc_arch
        self.num_CPUs = num_CPUs
        self.CPU_clock_speed = CPU_speed
        self.total_num_cores = total_cores
        self.total_RAM_size = RAM_size
        self.total_storage_size = storage_size
        self.limited_power = limited
        self.graphics_card_info = graphics_card

    @dclayMethod(return_="dict")
    def to_dict(self):
        fields = [
            "operating_system",
            "system_architecture",
            "processor_maker_name",
            "processor_architecture",
            "num_CPUs",
            "CPU_clock_speed",
            "total_num_cores",
            "total_RAM_size",
            "total_storage_size",
            "limited_power",
            "graphics_card_info"
        ]
        return self._to_dict(fields)


class DynamicInfo(StorageObject, ToDictMixin):
    """
    @ClassField available_RAM_size int
    @ClassField available_RAM_percent int
    @ClassField available_storage_size int
    @ClassField available_storage_percent int
    @ClassField available_CPU_percent int
    @ClassField remaining_power_seconds int
    @ClassField remaining_power_percent int
    @ClassField location str
    """

    @dclayMethod(RAM_size="int", RAM_percent="int", storage_size="int", storage_percent="int", CPU_percent="int", power_seconds="int", power_percent="int", location="str")
    def __init__(self, RAM_size, RAM_percent, storage_size, storage_percent,  CPU_percent, power_seconds, power_percent, location):
        self.available_RAM_size = RAM_size
        self.available_RAM_percent = RAM_percent
        self.available_storage_size = storage_size
        self.available_storage_percent = storage_percent
        self.available_CPU_percent = CPU_percent
        self.remaining_power_seconds = power_seconds
        self.remaining_power_percent = power_percent
        self.location = location

    @dclayMethod(return_="dict")
    def to_dict(self):
        fields = [
            "available_RAM_size",
            "available_RAM_percent",
            "available_storage_size",
            "available_storage_percent",
            "available_CPU_percent",
            "remaining_power_seconds",
            "remaining_power_percent",
            "location"
        ]
        return self._to_dict(fields)


class NetworkInfo(StorageObject, ToDictMixin):
    """
    @ClassField network_io str
    @ClassField ethernet_info str
    @ClassField ethernet_address str
    @ClassField wifi_info str
    @ClassField wifi_address str
    @ClassField bandwidth_capacity int
    @ClassField standard_info str
    """

    @dclayMethod(io="str", eth_info="str", eth_address="str", wifi_info="str", wifi_address="str", bandwidth ="str", standard="str")
    def __init__(self, io, eth_info, eth_address, wifi_info, wifi_address, bandwidth, standard):
        self.network_io = io
        self.ethernet_info = eth_info
        self.ethernet_address = eth_address
        self.wifi_info = wifi_info
        self.wifi_address = wifi_address
        self.bandwidth_capacity = bandwidth
        self.standard_info = standard

    @dclayMethod(return_="dict")
    def to_dict(self):
        fields = [
            "network_io",
            "ethernet_info",
            "ethernet_address",
            "wifi_info",
            "wifi_address",
            "bandwidth_capacity",
            "standard_info"
        ]
        return self._to_dict(fields)


class BehaviourInfo(StorageObject, ToDictMixin):
    """
    @ClassField mobile bool
    @ClassField reliability str
    @ClassField trust str
    @ClassField leader_capable bool
    """

    @dclayMethod(mobile="bool", reliability="str", trust="str", leader_capable="bool")
    def __init__(self, mobile, reliability, trust, leader_capable):
        self.mobile = mobile
        self.reliability = reliability
        self.trust = trust
        self.leader_capable = leader_capable

    @dclayMethod(return_="dict")
    def to_dict(self):
        fields = [
            "mobile",
            "reliability",
            "trust",
            "leader_capable"
        ]
        return self._to_dict(fields)


class SecurityInfo(StorageObject, ToDictMixin):
    """
    @ClassField data_privacy bool
    @ClassField network_security bool
    @ClassField device_security bool
    """

    @dclayMethod(data_privacy="bool", network_security="bool", device_security="bool")
    def __init__(self, data_privacy, network_security, device_security):
        self.data_privacy = data_privacy
        self.network_security = network_security
        self.device_security = device_security

    @dclayMethod(return_="dict")
    def to_dict(self):
        fields = [
            "data_privacy",
            "network_security",
            "device_security"
        ]
        return self._to_dict(fields)


class SharingModelInfo(StorageObject, ToDictMixin):
    """
    @ClassField shared_RAM_size int
    @ClassField shared_storage_size int
    @ClassField shared_CPU_percent int
    @ClassField shared_bandwidth_size int
    """

    @dclayMethod(RAM_size="int", storage_size="int", CPU_percent="int", bandwidth_size="int")
    def __init__(self, RAM_size, storage_size, CPU_percent, bandwidth_size):
        self.shared_RAM_size = RAM_size
        self.shared_storage_size = storage_size
        self.shared_CPU_percent = CPU_percent
        self.shared_bandwidth_size = bandwidth_size

    @dclayMethod(return_="dict")
    def to_dict(self):
        fields = [
            "shared_RAM_size",
            "shared_storage_size",
            "shared_CPU_percent",
            "shared_bandwidth_size"
        ]
        return self._to_dict(fields)


class Component(StorageObject, ToDictMixin):
    """
    @ClassField sensor_type str
    @ClassField actuator_type str
    @ClassField communication_info str
    @ClassField device_type str
    @ClassField location str
    """

    @dclayMethod(sensor_type="str", actuator_type="str", comm_info="str", device_type="str", location="str")
    def __init__(self, sensor_type, actuator_type, comm_info, device_type, location):
        self.sensor_type = sensor_type
        self.actuator_type = actuator_type
        self.communication_info = comm_info
        self.device_type = device_type
        self.location = location

    @dclayMethod(return_="dict")
    def to_dict(self):
        fields = [
            "sensor_type",
            "actuator_type",
            "communication_info",
            "device_type",
            "location"
        ]
        return self._to_dict(fields)

    @dclayMethod(other="model_mf2c.classes.Component", return_="bool")
    def __eq__(self, other):
        if not isinstance(other, Component):
            return False

        return all((
            self.sensor_type == other.sensor_type,
            self.actuator_type == other.actuator_type,
            self.communication_info == other.communication_info,
            self.device_type == other.device_type,
            self.location == other.location
        ))


class Device(StorageObject):
    """
    @ClassField device_id str
    @ClassField owner model_mf2c.classes.User
    @ClassField static_info model_mf2c.classes.StaticInfo
    @ClassField dynamic_info model_mf2c.classes.DynamicInfo
    @ClassField network_info model_mf2c.classes.NetworkInfo
    @ClassField behaviour_info model_mf2c.classes.BehaviourInfo
    @ClassField security_info model_mf2c.classes.SecurityInfo
    @ClassField attached_components list<model_mf2c.classes.Component>
    @ClassField sharing_model_info model_mf2c.classes.SharingModelInfo
    """

    # Not setting security, behaviour and sharing model in the constructor since they will be set later by the user
    @dclayMethod(device_id="str", owner="model_mf2c.classes.User", static_info="model_mf2c.classes.StaticInfo", dynamic_info="model_mf2c.classes.DynamicInfo", network_info="model_mf2c.classes.NetworkInfo", behaviour_info="model_mf2c.classes.BehaviourInfo", security_info="model_mf2c.classes.SecurityInfo", attached_components="list<model_mf2c.classes.Component>", sharing_model_info="model_mf2c.classes.SharingModelInfo")
    def __init__(self, device_id, owner, static_info, dynamic_info, network_info, behaviour_info, security_info, attached_components, sharing_model_info):
        self.device_id = device_id
        self.owner = owner
        self.static_info = static_info
        self.dynamic_info = dynamic_info
        self.network_info = network_info
        self.behaviour_info = behaviour_info
        self.security_info = security_info
        self.attached_components = attached_components
        self.sharing_model_info = sharing_model_info


class AggregatedResourceInfo(StorageObject):
    """
    @ClassField agent model_mf2c.classes.Agent
    """
    @dclayMethod(agent="model_mf2c.classes.Agent")
    def __init__(self, agent):
        self.agent = agent
        self.calculate_aggregation()

    @dclayMethod()
    def calculate_aggregation(self):
        """Update own properties (to be defined).

        This takes info from the agent (device, children, other aggregated info)
        """
        pass


class Agent(StorageObject):
    """
    @ClassField id str
    @ClassField dev model_mf2c.classes.Device
    @ClassField is_leader bool
    @ClassField is_cloud bool
    @ClassField multicloud_federation bool
    @ClassField children list<model_mf2c.classes.Agent>
    @ClassField aggregated_resource_info model_mf2c.classes.AggregatedResourceInfo
    """

    @dclayMethod(my_device="model_mf2c.classes.Device")
    def __init__(self, my_device):
        self.id = my_device.device_id #For the moment I assign the same id to the device and to the agent
        self.aggregated_resource_info = AggregatedResourceInfo(self) #AggregatedResourceInfo about the associated device
        self.dev = my_device
        self.children = list()
        self.is_leader = False
        self.is_cloud = False
        self.multicloud_federation = False

    @dclayMethod(return_=None)
    def calculate_aggregation(self):
        """ Update own properties (to be defined) taking info from the agent (device, children, other aggregated info) """
        pass

    @dclayMethod(return_=None, exception_message="str")
    def _check_cloud(self, exception_message):
        if self.is_cloud:
            raise RuntimeError(exception_message)

    #Returns owner + static info
    @dclayMethod(return_="tuple<model_mf2c.classes.User, model_mf2c.classes.StaticInfo>")
    def get_static_info(self):
        self._check_cloud("Trying to get static info on a cloud agent")
        return self.dev.owner, self.dev.static_info

    # Dynamic Info setters and getters.
    # Insted of a single "set_dynamic_info" I created individual setters for the info that could change

    # Returns owner, operating system and system architecture (static) + dynamic info
    @dclayMethod(return_="tuple<model_mf2c.classes.User, str, str, model_mf2c.classes.DynamicInfo>")
    def get_dynamic_info(self):
        self._check_cloud("Trying to get dynamic info on a cloud agent")
        return self.dev.owner, self.dev.static_info.operating_system, self.dev.static_info.system_architecture, self.dev.dynamic_info

    @dclayMethod(size="int")
    def set_available_RAM_size(self, size):
        self._check_cloud("Trying to set available RAM size on a cloud agent")
        self.dev.dynamic_info.available_RAM_size = size

    @dclayMethod(percent="int")
    def set_available_RAM_percent(self, percent):
        self._check_cloud("Trying to set available RAM percent on a cloud agent")
        self.dev.dynamic_info.available_RAM_percent = percent

    @dclayMethod(size="int")
    def set_available_storage_size(self, size):
        self._check_cloud("Trying to set available storage size on a cloud agent")
        self.dev.dynamic_info.available_storage_size = size

    @dclayMethod(percent="int")
    def set_available_storage_percent(self, percent):
        self._check_cloud("Trying to set available storage percent on a cloud agent")
        self.dev.dynamic_info.available_storage_percent = percent

    @dclayMethod(percent="int")
    def set_available_CPU_percent(self, percent):
        self._check_cloud("Trying to set available CPU percent on a cloud agent")
        self.dev.dynamic_info.available_CPU_percent = percent

    @dclayMethod(seconds="int")
    def set_remaining_power_seconds(self, seconds):
        self._check_cloud("Trying to set remaining power seconds on a cloud agent")
        self.dev.dynamic_info.remaining_power_seconds = seconds

    @dclayMethod(percent="int")
    def set_remaining_power_percent(self, percent):
        self._check_cloud("Trying to set remaining power percent on a cloud agent")
        self.dev.dynamic_info.remaining_power_percent = percent

    @dclayMethod(location="str")
    def set_location(self, location):
        self._check_cloud("Trying to set location on a cloud agent")
        self.dev.dynamic_info.location = location

    # Network Info setters and getters

    @dclayMethod(return_="model_mf2c.classes.NetworkInfo")
    def get_network_info(self):
        self._check_cloud("Trying to get network info on a cloud agent")
        return self.dev.network_info

    @dclayMethod(io="str")
    def set_network_io(self, io):
        self._check_cloud("Trying to set network IO on a cloud agent")
        self.dev.network_info.network_io = io

    @dclayMethod(info="str")
    def set_ethernet_info(self, info):
        self._check_cloud("Trying to set ethernet info on a cloud agent")
        self.dev.network_info.ethernet_info = info

    @dclayMethod(address="str")
    def set_ethernet_address(self, address):
        self._check_cloud("Trying to set ethernet address on a cloud agent")
        self.dev.network_info.ethernet_address = address

    @dclayMethod(info="str")
    def set_wifi_info(self, info):
        self._check_cloud("Trying to set wifi info on a cloud agent")
        self.dev.network_info.wifi_info = info

    @dclayMethod(address="str")
    def set_wifi_address(self, address):
        self._check_cloud("Trying to set wifi address on a cloud agent")
        self.dev.network_info.wifi_address = address

    @dclayMethod(bw="int")
    def set_bandwidth_capacity(self, bw):
        self._check_cloud("Trying to set bandwidth capacity on a cloud agent")
        self.dev.network_info.bandwidth_capacity = bw

    @dclayMethod(info="str")
    def set_standard_info(self, info):
        self._check_cloud("Trying to set network standard info on a cloud agent")
        self.dev.network_info.standard_info = info

    # Attached Component setters and getters

    @dclayMethod(return_="list<model_mf2c.classes.Component>")
    def get_attached_component_info(self):
        self._check_cloud("Trying to get attached components on a cloud agent")
        return self.dev.attached_components

    @dclayMethod(return_="list")
    def get_attached_component_info_as_list(self):
        self._check_cloud("Trying to get attached components on a cloud agent")
        ret = list()
        for comp in self.get_attached_component_info():
            ret.append(comp.to_dict())
        return ret

    @dclayMethod(component="model_mf2c.classes.Component")
    def add_attached_component(self, component):
        self._check_cloud("Trying to add an attached component to a cloud agent")
        self.dev.attached_components.append(component)

    @dclayMethod(component="model_mf2c.classes.Component")
    def remove_attached_component(self, component):
        self._check_cloud("Trying to remove an attached component from a cloud agent")
        try:
            self.dev.attached_components.remove(component)
        except ValueError:
            pass

    # Security Info setters and getters

    @dclayMethod(sec_info="model_mf2c.classes.SecurityInfo")
    def set_security_info(self, sec_info):
        self._check_cloud("Trying to set security info on a cloud agent")
        self.dev.security_info = sec_info

    @dclayMethod(return_="model_mf2c.classes.SecurityInfo")
    def get_security_info(self):
        self._check_cloud("Trying to get security info on a cloud agent")
        return self.dev.security_info

    @dclayMethod(data="bool")
    def set_data_privacy(self, data):
        self._check_cloud("Trying to set data privacy on a cloud agent")
        self.dev.security_info.data_privacy = data

    @dclayMethod(network="bool")
    def set_network_security(self, network):
        self._check_cloud("Trying to set network security on a cloud agent")
        self.dev.security_info.network_security = network

    @dclayMethod(device_sec="bool")
    def set_device_security(self, device_sec):
        self._check_cloud("Trying to set device security on a cloud agent")
        self.dev.security_info.device_security = device_sec

    # Behaviour Info setters and getters

    @dclayMethod(beh_info="model_mf2c.classes.BehaviourInfo")
    def set_behaviour_info(self, beh_info):
        self._check_cloud("Trying to set behaviour info on a cloud agent")
        self.dev.behaviour_info = beh_info

    @dclayMethod(return_="model_mf2c.classes.BehaviourInfo")
    def get_behaviour_info(self):
        self._check_cloud("Trying to get behaviour info on a cloud agent")
        return self.dev.behaviour_info

    @dclayMethod(mobile="bool")
    def set_mobility(self, mobile):
        self._check_cloud("Trying to set mobility info on a cloud agent")
        self.dev.behaviour_info.mobile = mobile

    @dclayMethod(reliability="str")
    def set_reliability(self, reliability):
        self._check_cloud("Trying to set reliability info on a cloud agent")
        self.dev.behaviour_info.reliability = reliability

    @dclayMethod(trust="str")
    def set_trust(self, trust):
        self._check_cloud("Trying to set trust info on a cloud agent")
        self.dev.behaviour_info.trust = trust

    @dclayMethod(leader="bool")
    def set_leader_capability(self, leader):
        self._check_cloud("Trying to set leader capability on a cloud agent")
        self.dev.behaviour_info.leader_capable = leader

    # Sharing Model setters and getters

    @dclayMethod(sharing_info="model_mf2c.classes.SharingModelInfo")
    def set_sharing_model_info(self, sharing_info):
        self._check_cloud("Trying to set sharing model info on a cloud agent")
        self.dev.sharing_model_info = sharing_info

    @dclayMethod(return_="model_mf2c.classes.SharingModelInfo")
    def get_sharing_model_info(self):
        self._check_cloud("Trying to get sharing model info on a cloud agent")
        return self.dev.sharing_model_info

    @dclayMethod(RAM="int")
    def set_shared_RAM_size(self, RAM):
        self._check_cloud("Trying to set shared RAM size on a cloud agent")
        self.dev.sharing_model_info.shared_RAM_size = RAM

    @dclayMethod(storage="int")
    def set_shared_storage_size(self, storage):
        self._check_cloud("Trying to set shared storage size on a cloud agent")
        self.dev.sharing_model_info.shared_storage_size = storage

    @dclayMethod(CPU="int")
    def set_shared_CPU_percent(self, CPU):
        self._check_cloud("Trying to set shared CPU percent on a cloud agent")
        self.dev.sharing_model_info.shared_CPU_percent = CPU

    @dclayMethod(bw="int")
    def set_shared_bandwidth_size(self, bw):
        self._check_cloud("Trying to set shared bandwidth size on a cloud agent")
        self.dev.sharing_model_info.shared_bandwidth_size = bw

    # Get Resource Info and Aggregation

    @dclayMethod(return_="tuple<model_mf2c.classes.User, model_mf2c.classes.StaticInfo, model_mf2c.classes.DynamicInfo, model_mf2c.classes.NetworkInfo, model_mf2c.classes.BehaviourInfo, model_mf2c.classes.SecurityInfo>")
    def get_resource_info(self):
        self._check_cloud("Trying to get resource info on a cloud agent")
        self.dev.behaviour_info, self.dev.security_info
        return self.dev.owner, self.dev.static_info, self.dev.dynamic_info, self.dev.network_info, self.dev.behaviour_info, self.dev.security_info

    @dclayMethod(return_="dict")
    def get_resource_info_as_dict(self):
        self._check_cloud("Trying to get resource info on a cloud agent")
        ret = {"Owner": self.dev.owner.name}
        ret.update(self.dev.static_info.to_dict())
        ret.update(self.dev.dynamic_info.to_dict())
        ret.update(self.dev.network_info.to_dict())
        ret.update(self.dev.behaviour_info.to_dict())
        ret.update(self.dev.security_info.to_dict())
        return ret

    # Get all children info and manage children

    @dclayMethod(return_="dict<str, dict>")
    def get_all_children_info(self):
        if self.is_leader or self.is_cloud:
            return {child.id: child.get_resource_info_as_dict() for child in self.children}
        else:
            raise RuntimeError("Trying to get children on a normal agent")

    @dclayMethod(child="model_mf2c.classes.Agent")
    def add_child(self, child):
        if self.is_leader or self.is_cloud:
            self.children.append(child)
            self.aggregated_resource_info.calculate_aggregation()
        else:
            raise RuntimeError("Trying to add a child to a normal agent")

    @dclayMethod(child="model_mf2c.classes.Agent")
    def remove_child(self, child):
        if self.is_leader or self.is_cloud:
            try:
                self.children.remove(child)
            except ValueError:
                pass
            self.aggregated_resource_info.calculate_aggregation()
        else:
            raise RuntimeError("Trying to remove a child from a normal agent")

    @dclayMethod(child_id="str", return_= bool)
    def check_child_exists(self, child_id):
        if self.is_leader or self.is_cloud:
            return any((child.id == child_id for child in self.children))
        else:
            raise RuntimeError("Trying to check child on a normal agent")

    # Cloud-specific methods

    @dclayMethod(multicloud="bool")
    def set_multicloud_federation(self, multicloud):
        self._check_cloud("Trying to set multicloud federation on a non-cloud agent")
        self.multicloud_federation = multicloud

    @dclayMethod(return_="bool")
    def get_multicloud_federation(self):
        self._check_cloud("Trying to get multicloud federation on a non-cloud agent")
        return self.multicloud_federation
