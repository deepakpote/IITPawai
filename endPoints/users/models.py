from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from commons.models import code 
#from commons.models import district 
"""
# user model
"""
class user(models.Model):
    userID = models.AutoField(primary_key = True)
     
    phoneRegex = RegexValidator(regex=r'^\+?1?\d{10,15}$', message="Phone number must be entered in the format: '+999999999'. Up to 15 digits allowed.")
    phoneNumber = models.CharField(null = False, unique = True, max_length = 15, validators = [phoneRegex]) 
     
    userName = models.CharField(max_length = 100, null = False)
    photoUrl = models.CharField(max_length = 255, null = True, blank = True)
    udiseCode = models.CharField(max_length = 255, null = True, blank = True)
    emailID = models.EmailField(max_length = 100, null = True, blank = True)
    
    # For district and language, set null = true, so as to allow inserting of admin user - with blank values for these 2 fields. Later on update these 2 fields for admin
    # Further set blank = false, so as to NOT allow blank inputs
    preferredLanguage = models.ForeignKey('commons.code', db_column = 'preferredLanguageCodeID', null = True, blank = False, related_name='user_preferredLanguageCodeID')
    district = models.ForeignKey('commons.code', db_column = 'districtCodeID', null = True, blank = False, related_name="user_districtCodeID")
    userType = models.ForeignKey('commons.code', db_column = 'userTypeCodeID', null = True, blank = False, related_name="user_userTypeCodeID")
     
    createdBy = models.ForeignKey('user', null = True, related_name='user_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('user', null = True, related_name='user_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)

    
    USERNAME_FIELD = 'phoneNumber'
    REQUIRED_FIELDS = ['name',]
 
    def is_anonymous(self):
        """
        Always return False. This is a way of comparing User objects to
        anonymous users.
        """
        return False
    
    def is_authenticated(self):
        """
        Always return True. This is a way to tell if the user has been
        authenticated in templates.
        """
        return True
    
    class Meta:
        db_table = 'usr_User'
        get_latest_by = 'userName'

"""
OTP model
"""
class otp(models.Model):
    otpID = models.AutoField(primary_key=True)
    phoneRegex = RegexValidator(regex=r'^\+?1?\d{10,15}$', message="Phone number must be entered in the format: '+999999999'. Up to 15 digits allowed.")
    phoneNumber = models.CharField(validators = [phoneRegex], null = False, max_length = 15)
    otp = models.CharField(max_length=6, null = False, blank = True) # cannot be stored as null into DB, but input can be blank, as this is autogenerated

    createdOn = models.DateTimeField(auto_now=False, auto_now_add=True)
    modifiedOn = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'usr_Otp'

"""
Token model
"""
class token(models.Model):
    tokenID = models.AutoField(primary_key=True)
    user = models.ForeignKey('user', related_name='token_user', db_column = 'userID')
    token = models.CharField(max_length = 255, unique=True)

    createdOn = models.DateTimeField(auto_now=False, auto_now_add=True)
    modifiedOn = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'usr_Token'

"""
user authentication model
"""                 
class userAuth(models.Model):
    userAuthID = models.AutoField(primary_key = True)
    loginID = models.CharField(null = False, unique = True, max_length = 255)
    password = models.CharField(max_length = 255, null = True)
    authToken = models.CharField(max_length = 255, null = True)
    lastLoggedInOn = models.DateTimeField(auto_now=True)
     
    createdBy = models.ForeignKey('user', related_name='userAuth_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('user', related_name='userAuth_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
     
    class Meta:
        db_table = 'usr_userAuth'

"""
user subject model
"""  
class userSubject(models.Model):
    userSubjectID = models.AutoField(primary_key=True)
    subject = models.ForeignKey('commons.code', db_column='subjectCodeID', related_name='userSubject_subjectCodeID')
    user = models.ForeignKey('user', db_column='userID', related_name='userSubject_userID')
    
    createdOn = models.DateTimeField(auto_now=False, auto_now_add=True)
    modifiedOn = models.DateTimeField(auto_now=True)
 
    class Meta:
        db_table = 'usr_userSubject'
"""
user skill model
"""  
class userSkill(models.Model):
    userSkillID = models.AutoField(primary_key=True)
    skill = models.ForeignKey('commons.code', db_column='skillCodeID', related_name='userSkill_skillCodeID')
    user = models.ForeignKey('user', db_column='userID', related_name='userSkill_userid')
    
    createdOn = models.DateTimeField(auto_now=False, auto_now_add=True)
    modifiedOn = models.DateTimeField(auto_now=True)
 
    class Meta:
        db_table = 'usr_userSkill'
"""
user topic model
"""   
class userTopic(models.Model):
    userTopicID = models.AutoField(primary_key=True)
    topic = models.ForeignKey('commons.code', db_column='topicCodeID', related_name='userTopic_topicCodeID')
    user = models.ForeignKey('user', db_column='userID', related_name='userTopic_userid')
    
    createdOn = models.DateTimeField(auto_now=False, auto_now_add=True)
    modifiedOn = models.DateTimeField(auto_now=True)
 
    class Meta:
        db_table = 'usr_userTopic'
"""
user grade model
"""          
class userGrade(models.Model):
    userGradeID = models.AutoField(primary_key=True)  
    grade = models.ForeignKey('commons.code', db_column='gradeCodeID', related_name='userGrade_gradeCodeID')
    user = models.ForeignKey('user', db_column='userID', related_name='userGrade_userID')
    
    createdOn = models.DateTimeField(auto_now=False, auto_now_add=True)
    modifiedOn = models.DateTimeField(auto_now=True)
 
    class Meta:
        db_table = 'usr_userGrade'        

#      
# class language(models.Model):
#     languageID = models.AutoField(primary_key = True)
#     languageName =  models.CharField(max_length = 255)
#     resourceCode = models.CharField(max_length = 3)
#     
#     class Meta:
#         db_table = 'com_Language'
#         get_latest_by = 'userName'
#         

