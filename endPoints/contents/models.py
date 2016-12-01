from __future__ import unicode_literals

from django.db import models
from commons.models import code 
from pip.cmdoptions import requirements

"""
content model
"""                 
class content(models.Model):
    contentID = models.AutoField(primary_key = True)
    contentTitle = models.CharField(null = False, unique = True, max_length = 255)
    contentTypeCodeID = models.ForeignKey('commons.code', db_column='contentTypeCodeID', related_name='content_contentTypeCodeID')
    subjectCodeID = models.ForeignKey('commons.code', db_column='subjectCodeID', related_name='content_subjectCodeID')
    gradeCodeID = models.ForeignKey('commons.code', db_column='gradeCodeID', related_name='content_gradeCodeID',null = True)
    
    requirement = models.TextField(null = True)
    instruction = models.TextField(null = True)
    
    fileTypeCodeID = models.ForeignKey('commons.code', db_column='fileTypeCodeID', related_name='content_fileTypeCodeID')
    fileName = models.CharField(null = False, max_length = 255)
    
    author = models.ForeignKey('users.user', related_name='content_author', db_column = 'author')
    objectives = models.TextField(null = True)
    
    languageCodeID = models.ForeignKey('commons.code', db_column='languageCodeID', related_name='content_languageCodeID')
     
    createdBy = models.ForeignKey('users.user', related_name='content_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='content_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now=True)
     
    class Meta:
        db_table = 'con_Content'
        get_latest_by = 'contentTitle' 
