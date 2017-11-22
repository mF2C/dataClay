from __future__ import absolute_import

from unittest import TestCase

from model_mf2c.classes import StaticInfo

test_instance = StaticInfo(os='Linux', arch='x86', proc_maker='intel', proc_arch='amd64', num_CPUs=4, CPU_speed=1, total_cores=6, RAM_size=2, storage_size=128, limited=True, graphics_card='nvidia gtx')

class TestStaticInfo(TestCase):
    def setUp(self):
        self.info = test_instance

    def test_init(self):
        self.assertEqual('Linux', self.info.operating_system)
        self.assertEqual('x86', self.info.system_architecture)
        self.assertEqual('intel', self.info.processor_maker_name)
        self.assertEqual('amd64', self.info.processor_architecture)
        self.assertEqual(4, self.info.num_CPUs)
        self.assertEqual(1, self.info.CPU_clock_speed)
        self.assertEqual(6, self.info.total_num_cores)
        self.assertEqual(2, self.info.total_RAM_size)
        self.assertEqual(128, self.info.total_storage_size)
        self.assertTrue(self.info.limited_power)
        self.assertEqual('nvidia gtx', self.info.graphics_card_info)

    def test_to_dict(self):
        to_dict = self.info.to_dict()

        self.assertEqual('Linux', to_dict['operating_system'])
        self.assertEqual('x86', to_dict['system_architecture'])
        self.assertEqual('intel', to_dict['processor_maker_name'])
        self.assertEqual('amd64', to_dict['processor_architecture'])
        self.assertEqual(4, to_dict['num_CPUs'])
        self.assertEqual(1, to_dict['CPU_clock_speed'])
        self.assertEqual(6, to_dict['total_num_cores'])
        self.assertEqual(2, to_dict['total_RAM_size'])
        self.assertEqual(128, to_dict['total_storage_size'])
        self.assertTrue(to_dict['limited_power'])
        self.assertEqual('nvidia gtx', to_dict['graphics_card_info'])

        self.assertEquals(11, len(to_dict))
