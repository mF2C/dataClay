from __future__ import absolute_import

from unittest import TestCase

from model_mf2c.classes import BehaviourInfo

test_instance = BehaviourInfo(mobile=True, reliability="reliable", trust="trustable", leader_capable=False)

class TestBehaviourInfo(TestCase):
    def setUp(self):
        self.info = test_instance

    def test_init(self):
        self.assertTrue(self.info.mobile)
        self.assertEqual("reliable", self.info.reliability)
        self.assertEqual("trustable", self.info.trust)
        self.assertFalse(self.info.leader_capable)

    def test_to_dict(self):
        to_dict = self.info.to_dict()

        self.assertTrue(to_dict['mobile'])
        self.assertEqual("reliable", to_dict['reliability'])
        self.assertEqual("trustable", to_dict['trust'])
        self.assertFalse(to_dict['leader_capable'])

        self.assertEquals(4, len(to_dict))   
