from __future__ import absolute_import

from unittest import TestCase

from model_mf2c.classes import NetworkInfo

test_instance = NetworkInfo(io="I/O", eth_info="eth", eth_address="0.0.0.0", wifi_info="wifi", wifi_address="1.1.1.1", bandwidth="50Mb/s", standard="std")

class TestNetworkInfo(TestCase):
    def setUp(self):
        self.info = test_instance

    def test_init(self):
        self.assertEqual('I/O', self.info.network_io)
        self.assertEqual('eth', self.info.ethernet_info)
        self.assertEqual('0.0.0.0', self.info.ethernet_address)
        self.assertEqual('wifi', self.info.wifi_info)
        self.assertEqual('1.1.1.1', self.info.wifi_address)
        self.assertEqual('50Mb/s', self.info.bandwidth_capacity)
        self.assertEqual('std', self.info.standard_info)

    def test_to_dict(self):
        to_dict = self.info.to_dict()

        self.assertEqual('I/O', to_dict['network_io'])
        self.assertEqual('eth', to_dict['ethernet_info'])
        self.assertEqual('0.0.0.0', to_dict['ethernet_address'])
        self.assertEqual('wifi', to_dict['wifi_info'])
        self.assertEqual('1.1.1.1', to_dict['wifi_address'])
        self.assertEqual('50Mb/s', to_dict['bandwidth_capacity'])
        self.assertEqual('std', to_dict['standard_info'])

        self.assertEquals(7, len(to_dict))
