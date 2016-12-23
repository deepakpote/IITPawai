from users.models import token, user
from rest_framework import authentication,exceptions



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