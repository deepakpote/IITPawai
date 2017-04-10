from __future__ import unicode_literals
from django.db import models
from commons.models import code

"""
content model
"""                 
class content(models.Model):
    contentID = models.AutoField(primary_key = True)
    # column contentTitle,instruction and author has been moved to the new table con_contentDetail table.
    #contentTitle = models.CharField(null = False, unique = False, max_length = 255)
    contentType = models.ForeignKey('commons.code', db_column='contentTypeCodeID', related_name='content_contentTypeCodeID')
    subject = models.ForeignKey('commons.code', db_column='subjectCodeID', related_name='content_subjectCodeID', null = True)
    #grade = models.ForeignKey('commons.code', db_column='gradeCodeID', related_name='content_gradeCodeID', null = True)
    topic = models.ForeignKey('commons.code', db_column='topicCodeID', related_name='content_topicCodeID', null = True)
    chapter = models.ForeignKey('chapter', db_column='chapterID', related_name='content_chapterID', null = True)
       
    requirement = models.TextField(null = True)
    #instruction = models.TextField(null = True)
    
    fileType = models.ForeignKey('commons.code', db_column='fileTypeCodeID', related_name='content_fileTypeCodeID')
    fileName = models.CharField(null = False, max_length = 255)
    
    #author = models.CharField(null = False, max_length = 255)
    objectives = models.TextField(null = True)
    language = models.ForeignKey('commons.code', db_column='languageCodeID', related_name='content_languageCodeID')
    status = models.ForeignKey('commons.code', db_column='statusCodeID', related_name='content_statusCodeID')
     
    createdBy = models.ForeignKey('users.user', related_name='content_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='content_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
     
    class Meta:
        db_table = 'con_content'
        
     
"""
content detail model
"""                 
class contentDetail(models.Model):
    contentDetailID = models.AutoField(primary_key = True)
    content = models.ForeignKey('content', db_column = 'contentID', null = False, related_name="contentDetail_contentID")
    appLanguage = models.ForeignKey('commons.code', db_column='appLanguageCodeID', related_name='contentDetail_appLanguageCodeID')
    contentTitle = models.CharField(null = False, unique = False, max_length = 255)
    instruction = models.TextField(null = True)
    author = models.CharField(null = True, max_length = 255)
       
    class Meta:
        db_table = 'con_contentDetail'
        unique_together = ('content', 'appLanguage')
        
"""
content grade model
"""                 
class contentGrade(models.Model):
    contentGradeID = models.AutoField(primary_key = True)
    grade = models.ForeignKey('commons.code', db_column = 'gradeCodeID', null = False, related_name="contentGrade_gradeCodeID")
    content = models.ForeignKey('content', db_column = 'contentID', null = False, related_name="contentGrade_contentID")
    
    class Meta:
        db_table = 'con_contentGrade'
          
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
        db_table = 'con_contentResponse'
        
"""
chapter model
"""                 
class chapter(models.Model):
    chapterID = models.AutoField(primary_key = True)
    subject = models.ForeignKey('commons.code', db_column='subjectCodeID', related_name='chapter_subjectCodeID')
    grade = models.ForeignKey('commons.code', db_column='gradeCodeID', related_name='chapter_gradeCodeID')
    displayOrder = models.IntegerField(null = True, blank = True)
    
    createdBy = models.ForeignKey('users.user', related_name='chapter_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='chapter_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
       
    class Meta:
        db_table = 'con_chapter'
        
"""
chapter detail model
"""                 
class chapterDetail(models.Model):
    chapterDetailID = models.AutoField(primary_key = True)
    chapter = models.ForeignKey('chapter', db_column = 'chapterID', null = False, related_name="chapterDetail_chapterID")
    chapterEng = models.TextField(null = False)
    chapterMar = models.TextField(null = False)
       
    class Meta:
        db_table = 'con_chapterDetail'
