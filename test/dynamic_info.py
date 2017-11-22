from __future__ import absolute_import

from unittest import TestCase

from model_mf2c.classes import DynamicInfo

test_instance = DynamicInfo(RAM_size=2, RAM_percent=50, storage_size=128, storage_percent=10, CPU_percent=25, power_seconds=2, power_percent=40, location='loc')

class TestDynamicInfo(TestCase):
    def setUp(self):
        self.info = test_instance

    def test_init(self):
        self.assertEqual(2, self.info.available_RAM_size)
        self.assertEqual(50, self.info.available_RAM_percent)
        self.assertEqual(128, self.info.available_storage_size)
        self.assertEqual(10, self.info.available_storage_percent)
        self.assertEqual(25, self.info.available_CPU_percent)
        self.assertEqual(2, self.info.remaining_power_seconds)
        self.assertEqual(40, self.info.remaining_power_percent)
        self.assertEqual('loc', self.info.location)

    def test_to_dict(self):
        to_dict = self.info.to_dict()

        self.assertEqual(2, to_dict['available_RAM_size'])
        self.assertEqual(50, to_dict['available_RAM_percent'])
        self.assertEqual(128, to_dict['available_storage_size'])
        self.assertEqual(10, to_dict['available_storage_percent'])
        self.assertEqual(25, to_dict['available_CPU_percent'])
        self.assertEqual(2, to_dict['remaining_power_seconds'])
        self.assertEqual(40, to_dict['remaining_power_percent'])
        self.assertEqual('loc', to_dict['location'])

        self.assertEquals(8, len(to_dict))
