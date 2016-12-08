from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from users.models import user 

"""
# user event model
"""
class userEvent(models.Model):
    eventID = models.CharField(primary_key = True,max_length=255)
    userID = models.CharField(max_length=255)
    #userID = models.ForeignKey('users.user', on_delete = models.CASCADE, null = False, blank = False,db_column ='userID',related_name = 'userEvent_userID')
    
    class Meta:
        unique_together = ("eventID", "userID")
        db_table = 'usr_event'