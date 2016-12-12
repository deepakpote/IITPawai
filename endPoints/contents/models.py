from __future__ import unicode_literals
from django.db import models
from commons.models import code 

"""
content model
"""                 
class content(models.Model):
    contentID = models.AutoField(primary_key = True)
    contentTitle = models.CharField(null = False, unique = True, max_length = 255)
    contentType = models.ForeignKey('commons.code', db_column='contentTypeCodeID', related_name='content_contentTypeCodeID')
    subject = models.ForeignKey('commons.code', db_column='subjectCodeID', related_name='content_subjectCodeID')
    grade = models.ForeignKey('commons.code', db_column='gradeCodeID', related_name='content_gradeCodeID')
    topic = models.ForeignKey('commons.code', db_column='topicCodeID', related_name='content_topicCodeID', null = True)
    
    requirement = models.TextField(null = True)
    instruction = models.TextField(null = True)
    
    fileType = models.ForeignKey('commons.code', db_column='fileTypeCodeID', related_name='content_fileTypeCodeID')
    fileName = models.CharField(null = False, max_length = 255)
    
    author = models.CharField(null = False, max_length = 255)
    objectives = models.TextField(null = True)
    
    language = models.ForeignKey('commons.code', db_column='languageCodeID', related_name='content_languageCodeID')
     
    createdBy = models.ForeignKey('users.user', related_name='content_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='content_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
     
    class Meta:
        db_table = 'con_Content'
        get_latest_by = 'contentTitle' 
        
"""
content response model
"""                 
class contentResponse(models.Model):
    contentResponseID = models.AutoField(primary_key = True)
    user = models.ForeignKey('users.user', db_column = 'userID', null = False, related_name="contentResponse_userID")
    content = models.ForeignKey('content', db_column = 'contentID', null = False, related_name="contentResponse_contentID")
    
    hasLiked = models.NullBooleanField(null = True , default = False,)
    downloadCount = models.IntegerField(null = True , default = 0)
    sharedCount = models.IntegerField(null = True , default = 0)
     
    class Meta:
        db_table = 'con_ContentResponse'