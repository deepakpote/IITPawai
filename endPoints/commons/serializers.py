from django.contrib.auth.models import User
from rest_framework import serializers
from commons.models import district 
 
class districtSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = district
        fields = ('districtID', 'districtName')

