from django.contrib.auth.models import User
from rest_framework import serializers
from commons.models import code , news #district 
 
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
