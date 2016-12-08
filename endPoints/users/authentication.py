from users.models import token, user
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
        token1 = token.objects.filter(token=token_string).first()
        user = token1.user
        if user:
            return user, None
        else:
            return None