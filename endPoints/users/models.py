from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from commons.models import district, code 
from idlelib.IOBinding import blank_re



# user model
class user(models.Model):
    userID = models.AutoField(primary_key = True)
     
    phoneRegex = RegexValidator(regex=r'^\+?1?\d{10,15}$', message="Phone number must be entered in the format: '+999999999'. Up to 15 digits allowed.")
    phoneNumber = models.CharField(null = False, unique = True, max_length = 15, validators = [phoneRegex]) 
     
    userName = models.CharField(max_length = 100, null = False)
    photoUrl = models.CharField(max_length = 255, null = True, blank = True)
    udiseCode = models.CharField(max_length = 255, null = True, blank = True)
    emailID = models.EmailField(max_length = 100, null = True, blank = True)
     
    #preferredLanguage = models.ForeignKey('language', db_column = 'preferredLanguageID', null = False, related_name='user_preferredLanguage')
    district = models.ForeignKey('commons.district', db_column = 'districtID', null = False, related_name="user_district")
     
    createdBy = models.ForeignKey('user', null = True, related_name='user_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('user', null = True, related_name='user_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now_add=True)

    class Meta:
        db_table = 'usr_user'


class userAuth(models.Model):
    userauthid = models.AutoField(db_column='userAuthID', primary_key=True)  # Field name made lowercase.
    loginid = models.IntegerField(db_column='loginID', unique=True)  # Field name made lowercase.
    password = models.CharField(max_length=256, blank=True, null=True)
    sessiontoken = models.CharField(db_column='sessionToken', max_length=256, blank=True, null=True)  # Field name made lowercase.
    lastloggedinon = models.DateTimeField(db_column='lastLoggedInOn')  # Field name made lowercase.
     
    createdon = models.DateTimeField(db_column='createdOn')  # Field name made lowercase.
    modifiedon = models.DateTimeField(db_column='modifiedOn')  # Field name made lowercase.
    createdby = models.ForeignKey(user,  related_name='userAuth_createdBy', db_column='createdBy')  # Field name made lowercase.
    modifiedby = models.ForeignKey(user,  related_name='userAuth_modifiedby', db_column='modifiedBy')  # Field name made lowercase.
 
    class Meta:
        db_table = 'usr_userauth'

class language(models.Model):
    languageID = models.AutoField(primary_key = True)
    languageName =  models.CharField(max_length = 256)
    resourceCode = models.CharField(max_length = 3)
     
#     createdBy = models.ForeignKey('user', related_name = 'language_createdBy', db_column = 'createdBy')
#     createdOn = models.DateTimeField(auto_now_add=True)
     
    class Meta:
        db_table = 'com_Language'
        get_latest_by = 'languageName'





class userSubject(models.Model):
    userSubjectID = models.AutoField(primary_key=True)  # Field name made lowercase.
    subjectCodeID = models.ForeignKey('commons.code', related_name='userSubject_subjectcodeid', db_column='subjectCodeID')  # Field name made lowercase.
    userID = models.ForeignKey('user', related_name='userSubject_userid', db_column='userID')  # Field name made lowercase.
 
    class Meta:
        db_table = 'usr_userSubject'

class userSkill(models.Model):
    userSkillID = models.AutoField(primary_key=True)  # Field name made lowercase.
    skillCodeID = models.ForeignKey('commons.code', related_name='userSkill_skillcodeid', db_column='skillCodeID')  # Field name made lowercase.
    userID = models.ForeignKey('user', related_name='userSkill_userid', db_column='userID')  # Field name made lowercase.
 
    class Meta:
        db_table = 'use_userSkill'
 
class userTopic(models.Model):
    userTopicID = models.AutoField(primary_key=True)  # Field name made lowercase.
    topicCodeID = models.ForeignKey('commons.code', related_name='userTopic_topiccodeid', db_column='topicCodeID')  # Field name made lowercase.
    userid = models.ForeignKey('user', related_name='userTopic_userid', db_column='userID')  # Field name made lowercase.
 
    class Meta:
        db_table = 'usr_userTopic'
         
class userGrade(models.Model):
    userGradeID = models.AutoField(primary_key=True)  # Field name made lowercase.
    gradeCodeID = models.ForeignKey('commons.code', related_name='userGrade_gradeCodeID', db_column='gradeCodeID')  # Field name made lowercase.
    userID = models.ForeignKey('user', related_name='userGrade_userid', db_column='userID')  # Field name made lowercase.
 
    class Meta:
        db_table = 'usr_userGrade'        
