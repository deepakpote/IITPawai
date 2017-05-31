from users.models import token, user
from rest_framework import authentication,exceptions


# authentication of users registering for the first time is done in the register call itself.


# This is used for authentication of signed in users.
# This checks for a static token that was served to the user while registering
class TokenAuthentication(authentication.BaseAuthentication):
    def authenticate(self, request):
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Chek authToken 
        if not authToken:
            return None

        #Declare token
        token1 = None;
        
        # check token exists or not
        try:
            token1 = token.objects.get(token=authToken)
        except token.DoesNotExist:
            raise exceptions.AuthenticationFailed('Authentication failed.')
            #return None
            
        #Get the use
        user = token1.user
        
        
        if user:
            return user, None
        else:
            return None