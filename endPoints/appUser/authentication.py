from appUser.models import Token, User
from rest_framework import authentication,exceptions


class TokenAuthentication(authentication.BaseAuthentication):
    def authenticate(self, request):
        authorization = request.META.get('HTTP_AUTHORIZATION')
        if not authorization:
            return None
        if not authorization.split()[0] == 'CUSTOM-ANDROID-AUTH':
            return None

        assert authorization.split()[1].split('=')[0] == "token"
        token_string = authorization.split()[1].split('=')[1]
        print token_string
        token = Token.objects.filter(token=token_string).first()
        user = token.user
        return user, None
        # except Exception as e:
        #     # This means token is invalid.
        #     print "error" + e.message
        #     raise exceptions.AuthenticationFailed('Android token authentication attempted and failed')