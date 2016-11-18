from django.contrib.auth.models import User
from users.models import user 
from rest_framework import viewsets
from users.serializers import userSerializer
 
 
class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = user.objects.all().order_by('username')
    serializer_class = userSerializer