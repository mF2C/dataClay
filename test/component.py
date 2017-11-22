from __future__ import absolute_import

from unittest import TestCase

from model_mf2c.classes import Component

test_instance = Component(sensor_type="sensor", actuator_type="act", comm_info="comm", device_type="type", location="loc")

class TestComponent(TestCase):
    def setUp(self):
        self.component = test_instance

    def test_init(self):
        self.assertEqual('sensor', self.component.sensor_type)
        self.assertEqual('act', self.component.actuator_type)
        self.assertEqual('comm', self.component.communication_info)
        self.assertEqual('type', self.component.device_type)
        self.assertEqual('loc', self.component.location)

    def test_to_dict(self):
        to_dict = self.component.to_dict()

        self.assertEqual('sensor', to_dict['sensor_type'])
        self.assertEqual('act', to_dict['actuator_type'])
        self.assertEqual('comm', to_dict['communication_info'])
        self.assertEqual('type', to_dict['device_type'])
        self.assertEqual('loc', to_dict['location'])
        
        self.assertEquals(5, len(to_dict))
