from django.contrib.auth.models import User
from rest_framework import serializers
from users.models import user 
 
class userSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = user
        fields = ('userID', 'phoneNumber', 'username', 'photoUrl', 'udiseCode', 'emailID', 'language')

