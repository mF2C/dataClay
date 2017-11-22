from __future__ import absolute_import
from unittest import TestCase

from model_mf2c.classes import Agent

from .device import test_instance as device

test_instance = Agent(my_device=device)

class TestAgent(TestCase):
    def setUp(self):
        self.agent = test_instance

    def _cloud(self, method, arg=None):
        try:
            self.agent.is_cloud = True
            method()
            self.fail()
        except:
            pass
        finally:
            self.agent.is_cloud = False

    def test_init(self):
        self.assertEqual(device.device_id, self.agent.dev.device_id)
        self.assertIs(device, self.agent.dev)
        self.assertFalse(self.agent.is_leader)
        self.assertFalse(self.agent.is_cloud)
        self.assertFalse(self.agent.multicloud_federation)
        self.assertIs(self.agent, self.agent.aggregated_resource_info.agent)

    def test_get_static_info(self):
        info = self.agent.get_static_info()

        self.assertIs(self.agent.dev.owner, info[0])
        self.assertIs(self.agent.dev.static_info, info[1])

        self._cloud(self.agent.get_dynamic_info)

    def test_get_dynamic_info(self):
        info = self.agent.get_dynamic_info()

        self.assertIs(self.agent.dev.owner, info[0])
        self.assertIs(self.agent.dev.static_info.operating_system, info[1])
        self.assertIs(self.agent.dev.static_info.system_architecture, info[2])
        self.assertIs(self.agent.dev.dynamic_info, info[3])

        self._cloud(self.agent.get_dynamic_info)

    def test_get_attached_component_info(self):
        info = self.agent.get_attached_component_info()

        self.assertEqual(self.agent.dev.attached_components, info)

        self._cloud(self.agent.get_attached_component_info)

    def test_get_attached_component_info_as_list(self):
        info = self.agent.get_attached_component_info_as_list()

        self.assertEqual(2, len(self.agent.get_attached_component_info_as_list()))

        self._cloud(self.agent.get_attached_component_info_as_list)
