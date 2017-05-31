from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from pyexpat import model

from datetime import datetime


# codeGroup model
class codeGroup(models.Model):
    codeGroupID = models.IntegerField(primary_key = True)
    codeGroupName = models.CharField(max_length = 255, null = False,unique = True)
    createdBy = models.ForeignKey('users.user', related_name='codeGroup_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='codeGroup_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'com_codeGroup'
        get_latest_by = 'codeGroupName'   
        
# code model
class code(models.Model):
    codeID = models.IntegerField(primary_key = True)
    codeGroup = models.ForeignKey('codeGroup', db_column = 'codeGroupID', null = False, related_name="code_codeGroupID")
    codeNameEn = models.CharField(max_length = 255, null = False)
    codeNameMr = models.CharField(max_length = 255, null = True)
    displayOrder = models.IntegerField(null = True, blank = True)
    comment = models.CharField(max_length = 255,null = True)
    createdBy = models.ForeignKey('users.user', related_name='code_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='code_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'com_code'
        get_latest_by = 'codeNameEn'  
        
"""
news model
"""                 
class news(models.Model):
    newsID = models.AutoField(primary_key = True)
    department = models.ForeignKey('commons.code', db_column = 'departmentCodeID', null = True, blank = False, related_name = 'news_departmentCodeID')
    newsCategory = models.ForeignKey('commons.code', db_column = 'newsCategoryCodeID', related_name = 'news_newsCategoryCodeID')
    newsImportance = models.ForeignKey('commons.code', db_column = 'newsImportanceCodeID', related_name = 'news_newsImportanceCodeID')
    publishDate = models.DateTimeField(default=datetime.now, null = False)
    pdfFileURL = models.CharField(null = True, max_length = 255)
    status = models.ForeignKey('commons.code', db_column = 'statusCodeID', related_name = 'news_statusCodeID')
     
    createdBy = models.ForeignKey('users.user', related_name='news_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='news_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
     
    class Meta:
        db_table = 'com_news'
        get_latest_by = 'createdOn'
        
"""
news detail model
"""                 
class newsDetail(models.Model):
    newsDetailID = models.AutoField(primary_key = True)
    news = models.ForeignKey('news', db_column = 'newsID', related_name = 'newsDetail_newsID')
    appLanguage = models.ForeignKey('commons.code', db_column = 'appLanguageCodeID', related_name = 'newsDetail_appLanguageCodeID')
    newsTitle = models.CharField(null = False , max_length = 255)
    author = models.CharField(null = True, max_length = 255)
    content = models.TextField(null = True)
    tags = models.TextField(null = True)
         
    class Meta:
        unique_together = ("news", "appLanguage")
        db_table = 'com_newsDetail'
 
"""
news image model
"""      
class newsImage(models.Model):
    newsImageID = models.AutoField(primary_key = True)
    news = models.ForeignKey('news', db_column = 'newsID', null = False, related_name = 'newsImage_newsID')
    imageURL = models.CharField(null = False, max_length = 255)
    
    createdBy = models.ForeignKey('users.user', related_name='newsImage_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        unique_together = ("news", "imageURL")
        db_table = 'com_newsImage'       

#Commented for now.        
# """
# news tag model
# """      
# class newsTag(models.Model):
#     newsTagID = models.AutoField(primary_key = True)
#     news = models.ForeignKey('news', db_column = 'newsID', null = False, related_name = 'newsTag_newsID')
#     tagName = models.TextField(null = True)
#     
#     createdBy = models.ForeignKey('users.user', related_name='newsTag_createdBy', db_column = 'createdBy')
#     createdOn = models.DateTimeField(auto_now_add=True)
#     
#     class Meta:
#         unique_together = ("news", "tagName")
#         db_table = 'com_newsTag'     

"""
user news
"""      
class userNews(models.Model):
    userNewsID = models.AutoField(primary_key = True)
    news = models.ForeignKey('news', db_column = 'newsID', null = False, related_name = 'userNews_newsID')   
    user = models.ForeignKey('users.user', db_column = 'userID', null = False, related_name = 'userNews_userID')   
    createdOn = models.DateTimeField(auto_now_add = True)   
    
    class Meta:
        unique_together = ("news", "user")
        db_table = 'com_userNews'
        
"""
configuration model
"""
class configuration(models.Model):
    configurationID = models.AutoField(primary_key = True)
    key = models.CharField(null = False, max_length = 255)
    value = models.CharField(max_length = 1000)
     
    class Meta:
        db_table = 'com_configuration'
