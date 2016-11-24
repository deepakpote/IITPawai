from django.contrib.auth.models import User
from rest_framework import serializers
from commons.models import code #district 
 
# class districtSerializer(serializers.HyperlinkedModelSerializer):
#     class Meta:
#         model = district
#         fields = ('districtID', 'districtName')
        
class codeSerializer(serializers.ModelSerializer):
    class Meta:
        model = code
        fields = ('codeID', 'codeGroup','codeNameEn','codeNameMr','displayOrder')        

