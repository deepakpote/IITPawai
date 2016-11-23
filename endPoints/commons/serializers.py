from django.contrib.auth.models import User
from rest_framework import serializers
from commons.models import district,code 
 
class districtSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = district
        fields = ('districtID', 'districtName')
        
class codeNameSerializer(serializers.ModelSerializer):
    class Meta:
        model = code
        fields = ('codeID', 'codeGroupID','codeNameEn','codeNameMr','displayOrder')        

