from django.contrib.auth.models import User
from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework.decorators import list_route

from commons.models import district,code 
from commons.serializers import districtSerializer,codeNameSerializer
 
 
class DistrictViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = district.objects.all().order_by('districtName')
    serializer_class = districtSerializer

class CodeNameViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    #queryset = code.objects.filter(codeGroupID='10')
    queryset = code.objects.all().order_by('codeNameEn')
    serializer_class = codeNameSerializer