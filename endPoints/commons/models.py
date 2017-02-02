from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
from pyexpat import model


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
    newsTitle = models.CharField(null = False , max_length = 255)
    author = models.CharField(null = False, max_length = 255)
    content = models.TextField(null = True)
    department = models.ForeignKey('commons.code', db_column = 'departmentCodeID', null = True, blank = False, related_name = 'news_departmentCodeID')
    publishDate = models.DateTimeField(auto_now_add = True, null = False)
    pdfFileURL = models.CharField(null = True, max_length = 255)
    
    createdBy = models.ForeignKey('users.user', related_name='news_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='news_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
     
    class Meta:
        db_table = 'com_news'
        get_latest_by = 'createdOn'
 
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
        db_table = 'com_newsImage'        
               
"""
configuration model
"""
class configuration(models.Model):
    configurationID = models.AutoField(primary_key = True)
    key = models.CharField(null = False, max_length = 255)
    value = models.CharField(max_length = 1000)
     
    class Meta:
        db_table = 'com_configuration'
