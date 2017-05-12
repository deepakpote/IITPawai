from django.contrib.auth.models import User
from rest_framework import serializers
from commons.models import code , news , newsDetail#district 
from mitraEndPoints import constants, settings, utils
 
# class districtSerializer(serializers.HyperlinkedModelSerializer):
#     class Meta:
#         model = district
#         fields = ('districtID', 'districtName')
        
class codeSerializer(serializers.ModelSerializer):
    class Meta:
        model = code
        fields = ('codeID', 'codeGroup','codeNameEn','codeNameMr','displayOrder')    
        
class newsSerializer(serializers.ModelSerializer):
    imageURL = serializers.CharField()
    department = serializers.IntegerField(source= 'department_id')
    class Meta:
        model = news
        fields = ('newsID', 'newsTitle','author','imageURL','pdfFileURL','department','publishDate','content','createdOn','modifiedOn')
        
class customNewsSerializer(serializers.ModelSerializer):

    department = serializers.CharField(source= 'news.department.codeID') 
    newsCategory = serializers.CharField(source= 'news.newsCategory.codeID')
    newsImportance = serializers.CharField(source= 'news.newsImportance.codeID')
    publishDate = serializers.CharField(source= 'news.publishDate')
    pdfFileURL =  serializers.CharField(source= 'news.pdfFileURL')
    status = serializers.CharField(source= 'news.status.codeID') 
    
    class Meta:
        model = newsDetail
        fields = ('news','newsTitle','author','content','tags','department','newsCategory','newsImportance','publishDate','pdfFileURL','status')

#Commented for now
# class newsTagSerializer(serializers.ModelSerializer):
#      
#     class Meta:
#         model = newsTag
#         fields = ('tagName')