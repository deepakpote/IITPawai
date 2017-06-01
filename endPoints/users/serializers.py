from django.contrib.auth.models import User 
import re
from rest_framework import serializers
from users.models import user, userRole
from django.core.validators import RegexValidator
from users.models import user, otp

"""
Function to validate phone number
"""
def validatePhoneNumber(value):
    print '-----------------------------------------------------------------------'
#     phoneRegex = RegexValidator(regex=r'^\+?1?\d{9,15}$', message="Phone number must be entered in the format: '+999999999'. Up to 15 digits allowed.")
#     if re.match(r'^\+?1?\d{9,15}$', value) == False:
#        raise ValidationError('Invalid phone number')
    print value

"""
user serializer class
"""
class userSerializer(serializers.ModelSerializer):
    phoneNumber = serializers.CharField(required=False)
    class Meta:
        model = user
        fields = ('userID', 'phoneNumber', 'userName', 'userType' , 'preferredLanguage' , 'photoUrl', 'udiseCode', 'emailID', 'district','department')

"""
OTP serializer class
"""
class otpSerializer(serializers.ModelSerializer):
#     phoneNumber = serializers.CharField(validators=[validatePhoneNumber], min_length = 10, max_length = 15)
#     otp = serializers.CharField(min_length = 6, max_length = 6)
#     def validate(self, data):
#         print '**********************************************************************'
#         #validatePhoneNumber(data['phoneNumber'])
#         return data
    class Meta:
        model = otp
        fields = ('phoneNumber', 'otp')
        
"""
user role Serializer
"""
class userRoleSerializer(serializers.ModelSerializer):
    
    roleName = serializers.CharField(source='role.roleName')
    roleID = serializers.CharField(source='role.roleID')
    
    class Meta:
        model = userRole
        fields = ('roleID','roleName')

