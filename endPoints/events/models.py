from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from users.models import user 

"""
# event model
"""
class eventModel(models.Model):
    eventID = models.CharField(primary_key = True,max_length=255)
    #refUser = models.ForeignKey('users.user', db_column = 'refUser', null = False, blank = False, related_name='event_refUser')
    
    class Meta:
        db_table = 'usr_event'