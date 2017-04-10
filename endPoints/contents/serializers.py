from django.contrib.auth.models import User 
from rest_framework import serializers
from contents.models import content , contentGrade , contentDetail , chapter , chapterDetail
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
    
    contentTitle = serializers.CharField()
    instruction = serializers.CharField()
    author = serializers.CharField() 
    requirementCodeIDs = serializers.CharField() 
    createdOn = serializers.CharField() 
    modifiedOn = serializers.CharField() 
    chapterID = serializers.CharField() 
  

    class Meta:
        model = content
        fields = ('contentID', 'contentTitle', 'contentType' ,'gradeCodeIDs','subject', 'chapterID' ,'topic','requirementCodeIDs','instruction','fileType','fileName','author','objectives','language','createdOn','modifiedOn',)

class contentSerializer(serializers.ModelSerializer):
     
     
    class Meta:
        model = content
        fields = ('contentID', 'contentTitle' ,'contentType', 'subject' , 'topic' , 'requirement', 'instruction', 'fileType', 'fileName','author','objectives','language',)

class selfLearningSerializer(serializers.ModelSerializer):
    # Access custom field.
    contentID = serializers.CharField()
    topic = serializers.CharField()
    contentType = serializers.CharField()
    #subject = serializers.CharField()
    topic = serializers.CharField()
    fileType = serializers.CharField()
    language = serializers.CharField()
    requirementCodeIDs = serializers.CharField()
    fileName = serializers.CharField()
    objectives = serializers.CharField()
    createdOn = serializers.CharField() 
    modifiedOn = serializers.CharField() 
    
    class Meta:
        model = contentDetail
        fields = ( 'contentID', 'contentTitle' , 'contentType', 'topic' , 'requirementCodeIDs', 'instruction', 'fileType', 'fileName','author','objectives','language','createdOn','modifiedOn',)
 
class chapterDetailSerializer(serializers.ModelSerializer):
     
    class Meta:
        model = chapterDetail
        fields = ('chapter', 'chapterEng' ,'chapterMar',)
 