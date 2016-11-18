from __future__ import unicode_literals

from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone

# district model
class district(models.Model):
    districtID = models.IntegerField(primary_key = True)
    districtName = models.CharField(max_length = 255, null = False, unique = True)
    
     
    class Meta:
        db_table = 'com_District'
        get_latest_by = 'districtName'