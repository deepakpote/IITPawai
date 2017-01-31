from django.contrib.auth.models import User 
from rest_framework import serializers
from contents.models import content , contentGrade
from django.core.validators import RegexValidator


"""
teachingAidSerializer serializer 
"""
class teachingAidSerializer(serializers.ModelSerializer):
     
    # Access custom field.
    gradeCodeIDs = serializers.CharField()
    contentType = serializers.CharField()
    subject = serializers.CharField()
    topic = serializers.CharField()
    fileType = serializers.CharField()
    language = serializers.CharField()
  

    class Meta:
        model = content
        fields = ('contentID', 'contentTitle', 'contentType' ,'gradeCodeIDs','subject','topic','requirement','instruction','fileType','fileName','author','objectives','language')

class contentSerializer(serializers.ModelSerializer):
     
     
    class Meta:
        model = content
        fields = ('contentID', 'contentTitle' ,'contentType', 'subject' , 'topic' , 'requirement', 'instruction', 'fileType', 'fileName','author','objectives','language',)
                