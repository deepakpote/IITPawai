from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from users.models import user 
from commons.models import code 
from contents.models import content

from datetime import datetime

"""
# user event model
"""
# class userEvent(models.Model):
#     userEventID = models.AutoField(primary_key = True)
#     #event = models.CharField(db_column ='eventID', max_length=255)
#     eventDetail = models.ForeignKey('eventDetail', db_column = 'eventDetailID', null = False, related_name="userEvent_eventDetailID")
# 
#     user = models.ForeignKey('users.user', null = False, blank = False, db_column ='userID', related_name = 'userEvent_userID')
#     createdOn = models.DateTimeField(auto_now_add=True)
#     
#     class Meta:
#         #unique_together = ("eventDetail", "user")
#         db_table = 'evt_userEvent'
        
"""
# user event model
"""
class usersEvent(models.Model):
    userEventID = models.AutoField(primary_key = True)
    eventDetail = models.ForeignKey('eventDetail', db_column = 'eventDetailID', null = False, related_name="userEvent_eventDetailID")
 
    user = models.ForeignKey('users.user', null = False, blank = False, db_column ='userID', related_name = 'userEvent_userID')
    createdOn = models.DateTimeField(auto_now_add=True)
     
    class Meta:
        #unique_together = ("eventDetail", "user")
        db_table = 'evt_usersEvent'
        
      
"""
event  model
"""                 
class event(models.Model):
    eventID = models.AutoField(primary_key = True)
    category = models.ForeignKey('commons.code', db_column='categoryCodeID', related_name='event_categoryCodeID')

    createdBy = models.ForeignKey('users.user', related_name='event_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
     
    class Meta:
        db_table = 'evt_event'

"""
event info  model
"""                 
class eventInfo(models.Model):
    eventInfoID = models.AutoField(primary_key = True)
    event = models.ForeignKey('event', db_column = 'eventID', null = False, related_name="eventInfo_eventID")
    appLanguage = models.ForeignKey('commons.code', db_column='appLanguageCodeID', related_name='eventInfo_appLanguageCodeID')
    eventTitle = models.CharField(null = True, max_length = 2555)
    eventDescription = models.TextField(null = True)
       
    class Meta:
        db_table = 'evt_eventInfo'
        
"""
event details model
"""                 
class eventDetail(models.Model):
    eventDetailID = models.AutoField(primary_key = True)
    event = models.ForeignKey('event', db_column = 'eventID', null = False, related_name="eventDetail_eventID")
    date = models.DateTimeField(default=datetime.now, null = False)
    district = models.ForeignKey('commons.code', db_column='districtCodeID', related_name='eventDetail_districtCodeID')
    block = models.ForeignKey('commons.code', db_column='blockCodeID', related_name='eventDetail_blockCodeID')
    state = models.ForeignKey('commons.code', db_column='stateCodeID', related_name='eventDetail_stateCodeID')
    engLocation = models.CharField(null = True, max_length = 255)
    marLocation = models.CharField(null = True, max_length = 255)
    engTrainer = models.CharField(null = True, max_length = 255)
    marTrainer = models.CharField(null = True, max_length = 255)
    status = models.ForeignKey('commons.code', db_column = 'statusCodeID', related_name = 'eventDetail_statusCodeID')
    
    createdBy = models.ForeignKey('users.user', related_name='eventDetail_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='eventDetail_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'evt_eventDetail'
        
"""
District-Block mapping  model 
"""                 
class districtBlockMapping(models.Model):
    districtBlockMappingID = models.AutoField(primary_key = True)
    district = models.ForeignKey('commons.code', db_column='districtCodeID', related_name='districtBlockMapping_districtCodeID')
    block = models.ForeignKey('commons.code', db_column='blockCodeID', related_name='districtBlockMapping_blockCodeID')
    state = models.ForeignKey('commons.code', db_column='stateCodeID', related_name='districtBlockMapping_stateCodeID')
       
    class Meta:
        unique_together = ('district', 'block','state')
        db_table = 'evt_districtBlockMapping'