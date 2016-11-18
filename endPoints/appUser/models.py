from django.db import models
from django.core.validators import RegexValidator
from django.utils import timezone
 
# user model
class User(models.Model):
    id = models.AutoField(primary_key = True)
     
    phone_regex = RegexValidator(regex=r'^\+?1?\d{9,15}$', message="Phone number must be entered in the format: '+999999999'. Up to 15 digits allowed.")
    phone_number = models.CharField(validators = [phone_regex], null = False, blank=False, unique = True, max_length = 15)
     
    name = models.CharField(max_length = 100)
    photo_url = models.CharField(max_length = 255, null = True)
    udise_code = models.CharField(max_length = 255, null = True)
    email = models.EmailField(max_length = 100, null = True)
    preferred_language = models.ForeignKey('Language', null = True, related_name="preferred_language")

    created_at = models.DateTimeField(default = timezone.now)
    modified_at = models.DateTimeField(default = timezone.now)

    USERNAME_FIELD = 'phone_number'
    REQUIRED_FIELDS = ['name',]

    def is_anonymous(self):
        """
        Always return False. This is a way of comparing User objects to
        anonymous users.
        """
        return False

    def is_authenticated(self):
        """
        Always return True. This is a way to tell if the user has been
        authenticated in templates.
        """
        return True

    class Meta:
        db_table = 'users'
        get_latest_by = "name"

class Otp(models.Model):
    id = models.AutoField(primary_key=True)
    phone_number = models.CharField(max_length=15)
    otp = models.CharField(max_length=6)

    created_at = models.DateTimeField(auto_now=False, auto_now_add=True)
    modified_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'otps'

class Token(models.Model):
    id = models.AutoField(primary_key=True)
    user = models.ForeignKey(User)
    token = models.CharField(max_length=255, unique=True)

    created_at = models.DateTimeField(auto_now=False, auto_now_add=True)
    modified_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'tokens'


# not needed
# class UserAuth(models.Model):
#     userAuthID = models.AutoField(primary_key = True)
#     loginID = models.IntegerField(unique = True)
#     passwordOTP = models.CharField(max_length = 6, null = True)
#     password = models.CharField(max_length = 256, null = True)
#     sessionToken = models.CharField(max_length = 256, null = True)
#     lastLoggedInOn = models.DateTimeField(default = timezone.now)
#
#     createdBy = models.ForeignKey('user', related_name="userAuth_createdBy", db_column = 'createdBy')
#     createdOn = models.DateTimeField(default = timezone.now)
#     modifiedBy = models.ForeignKey('user', related_name="userAuth_modifiedBy", db_column = 'modifiedBy')
#     modifiedOn = models.DateTimeField(default = timezone.now)
#
#     class Meta:
#         db_table = 'usr_userAuth'
     
class Language(models.Model):
    languageID = models.AutoField(primary_key = True)
    languageName =  models.CharField(max_length = 256)
    resourceCode = models.CharField(max_length = 3)
     
    createdBy = models.ForeignKey('User', related_name = 'language_createdBy', db_column = 'createdBy')
    createdOn = models.DateTimeField(default = timezone.now)
     
    class Meta:
        db_table = 'com_Language'
        get_latest_by = "userName"
