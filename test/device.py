from __future__ import absolute_import

from unittest import TestCase

from model_mf2c.classes import Device

from .user import test_instance as owner
from .static_info import test_instance as static_info
from .dynamic_info import test_instance as dynamic_info
from .network_info import test_instance as network_info
from .behaviour_info import test_instance as behaviour_info
from .security_info import test_instance as security_info
from .component import test_instance as component
from .sharing_model_info import test_instance as sharing_model_info

test_instance = Device(device_id="id", owner=owner, static_info=static_info, dynamic_info=dynamic_info, network_info=network_info, behaviour_info=behaviour_info, security_info=security_info, attached_components=[component, component], sharing_model_info=sharing_model_info)

class TestDevice(TestCase):
    def setUp(self):
        self.device = test_instance

    def test_init(self):
        self.assertEqual('id', self.device.device_id)
        self.assertIs(static_info, self.device.static_info)
        self.assertIs(dynamic_info, self.device.dynamic_info)
        self.assertIs(network_info, self.device.network_info)
        self.assertIs(behaviour_info, self.device.behaviour_info)
        self.assertIs( security_info, self.device.security_info)
        self.assertListEqual([component, component], self.device.attached_components)
        self.assertIs(sharing_model_info, self.device.sharing_model_info)
