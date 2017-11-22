from __future__ import absolute_import

from unittest import TestCase

from model_mf2c.classes import SecurityInfo

test_instance = SecurityInfo(data_privacy=True, network_security=True, device_security=False)

class TestSecurityInfo(TestCase):
    def setUp(self):
        self.info = test_instance

    def test_init(self):
        self.assertTrue(self.info.data_privacy)
        self.assertTrue(self.info.network_security)
        self.assertFalse(self.info.device_security)

    def test_to_dict(self):
        to_dict = self.info.to_dict()

        self.assertTrue(to_dict['data_privacy'])
        self.assertTrue(to_dict['network_security'])
        self.assertFalse(to_dict['device_security'])

        self.assertEquals(3, len(to_dict))   
