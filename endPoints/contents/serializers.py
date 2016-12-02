from django.contrib.auth.models import User 
from rest_framework import serializers
from contents.models import content 
from django.core.validators import RegexValidator


"""
content serializer class
"""
class contentSerializer(serializers.ModelSerializer):
    class Meta:
        model = content
        fields = ('contentID', 'contentTitle', 'contentType', 'subject' , 'grade' , 'topic' , 'requirement', 'instruction', 'fileType', 'fileName','author','objectives','language')
             