from __future__ import absolute_import

from unittest import TestCase
import unittest

from model_mf2c.classes import User

test_instance = User(user_id='id', email='a@b.c', name='name')

class TestUser(TestCase):
    def setUp(self):
        self.user = test_instance

    def test_init(self):
        self.assertEqual('id', self.user.id_key)
        self.assertEqual('a@b.c',self.user.email)
        self.assertEqual('name',self.user.name)
        self.assertFalse(self.user.service_consumer)
        self.assertFalse(self.user.resource_contributor)
        self.assertListEqual([], self.user.allowed_services)

    def test_get_user_profile(self):
        profile = self.user.get_user_profile()

        self.assertEquals('id', profile['id_key'])
        self.assertEquals('a@b.c', profile['email'])
        self.assertEquals('name', profile['name'])
        self.assertFalse(profile['service_consumer'])
        self.assertFalse(profile['resource_contributor'])

        self.assertEquals(5, len(profile))
