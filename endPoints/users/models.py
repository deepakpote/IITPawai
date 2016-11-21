from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from commons.models import district 

# user model
class user(models.Model):
    userID = models.AutoField(primary_key = True)
     
    phoneRegex = RegexValidator(regex=r'^\+?1?\d{9,15}$', message="Phone number must be entered in the format: '+999999999'. Up to 15 digits allowed.")
    phoneNumber = models.CharField(validators = [phoneRegex], null = False, unique = True, max_length = 15)
     
    userName = models.CharField(max_length = 100, null = False)
    photoUrl = models.CharField(max_length = 255, null = True)
    udiseCode = models.CharField(max_length = 255, null = True)
    emailID = models.EmailField(max_length = 100, null = True)
     
    preferredLanguageID = models.ForeignKey('language', db_column = 'languageID', null = False, related_name='user_preferredLanguageID')
    districtID = models.ForeignKey('commons.district', db_column = 'districtID', null = False, related_name="user_districtID")
     
    createdBy = models.ForeignKey('user', null = True, related_name='user_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('user', null = True, related_name='user_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now_add=True)
     
    class Meta:
        db_table = 'usr_User'
        get_latest_by = 'userName'
         
class userAuth(models.Model):
    userAuthID = models.AutoField(primary_key = True)
    loginID = models.IntegerField(unique = True)
    password = models.CharField(max_length = 256, null = True)
    sessionToken = models.CharField(max_length = 256, null = True)
    lastLoggedInOn = models.DateTimeField(auto_now_add=True)
     
    createdBy = models.ForeignKey('user', related_name='userAuth_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('user', related_name='userAuth_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now_add=True)
     
    class Meta:
        db_table = 'usr_userAuth'
     
class language(models.Model):
    languageID = models.AutoField(primary_key = True)
    languageName =  models.CharField(max_length = 256)
    resourceCode = models.CharField(max_length = 3)
     
#     createdBy = models.ForeignKey('user', related_name = 'language_createdBy', db_column = 'createdBy')
#     createdOn = models.DateTimeField(auto_now_add=True)
     
    class Meta:
        db_table = 'com_Language'
        get_latest_by = 'userName'
        
