from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
#from users.models import user

# district model
class district(models.Model):
    districtID = models.IntegerField(primary_key = True)
    districtName = models.CharField(max_length = 255, null = False, unique = True)
    
    class Meta:
        db_table = 'com_District'
        get_latest_by = 'districtName'

# codeGroup model
class codeGroup(models.Model):
    codeGroupID = models.IntegerField(primary_key = True)
    codeGroupName = models.CharField(max_length = 255, null = False,unique = True)
    createdBy = models.ForeignKey('users.user', related_name='codeGroup_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='codeGroup_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now_add=True)

    class Meta:
        db_table = 'com_CodeGroup'
        get_latest_by = 'codeGroupName'   
        
# code model
class code(models.Model):
    codeID = models.IntegerField(primary_key = True)
    codeGroupID = models.ForeignKey('codeGroup', db_column = 'codeGroupID', null = False, related_name="code_codeGroupID")
    codeNameEn = models.CharField(max_length = 255, null = True)
    codeNameMr = models.CharField(max_length = 255, null = True)
    displayOrder = models.IntegerField()
    createdBy = models.ForeignKey('users.user', related_name='code_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(auto_now_add=True)
    modifiedBy = models.ForeignKey('users.user', related_name='code_modifiedBy', db_column = 'modifiedBy')
    modifiedOn = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'com_Code'
        get_latest_by = 'codeNameEn'        