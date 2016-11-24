from django.contrib.auth.models import User
from rest_framework import serializers
from users.models import user 
 
class userSerializer(serializers.ModelSerializer):
    class Meta:
        model = user
        fields = ('userID', 'phoneNumber', 'userName', 'photoUrl', 'udiseCode', 'emailID','district')
      