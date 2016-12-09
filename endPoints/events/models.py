from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from users.models import user 

"""
# user event model
"""
class userEvent(models.Model):
    userEventID = models.AutoField(primary_key = True)
    event = models.CharField(db_column ='eventID', max_length=255)
    #userID = models.CharField(max_length=255)
    user = models.ForeignKey('users.user', null = False, blank = False, db_column ='userID', related_name = 'userEvent_userID')
    createdOn = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        unique_together = ("event", "user")
        db_table = 'usr_userEvent'