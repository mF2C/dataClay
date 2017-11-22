from __future__ import absolute_import

from unittest import TestCase

from model_mf2c.classes import SharingModelInfo

test_instance = SharingModelInfo(RAM_size=2, storage_size=128, CPU_percent=50, bandwidth_size=10)

class TestSharingModelInfo(TestCase):
    def setUp(self):
        self.info = test_instance

    def test_init(self):
        self.assertEqual(2, self.info.shared_RAM_size)
        self.assertEqual(128, self.info.shared_storage_size)
        self.assertEqual(50, self.info.shared_CPU_percent)
        self.assertEqual(10, self.info.shared_bandwidth_size)

    def test_to_dict(self):
        to_dict = self.info.to_dict()

        self.assertEqual(2, to_dict['shared_RAM_size'])
        self.assertEqual(128, to_dict['shared_storage_size'])
        self.assertEqual(50, to_dict['shared_CPU_percent'])
        self.assertEqual(10, to_dict['shared_bandwidth_size'])
        
        self.assertEquals(4, len(to_dict))
