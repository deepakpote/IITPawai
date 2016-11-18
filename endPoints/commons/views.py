from django.contrib.auth.models import User
from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework.decorators import list_route

from commons.models import district 
from commons.serializers import districtSerializer
 
 
class DistrictViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = district.objects.all().order_by('districtName')
    serializer_class = districtSerializer
