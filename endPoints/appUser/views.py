from appUser.models import User, Otp, Token
from django.http import HttpResponse
from rest_framework import viewsets,permissions
from rest_framework.decorators import detail_route, list_route
from rest_framework.response import Response
from rest_framework import status
from .serializers import UserSerializer


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer

    http_method_names = ['get', 'post']

    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def requestotp(self,request):
        phone_number = request.data.get('phone_number')
        if not phone_number:
            return Response({"message": "phone number not specified", "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        otp = Otp(phone_number=phone_number,otp='1234')
        otp.save()
        # TODO:
        # make call to plivo here
        return Response({"message": "otp sent to " + phone_number, "data":[]})


    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def register(self,request):
        phone_number = request.data.get('phone_number')
        otp_string = request.data.get('otp')

        otp = Otp.objects.filter(phone_number=phone_number,otp=otp_string).first()
        if not otp:
            return Response({"message": "invalid otp", "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)
        else:
            user = UserSerializer(data=request.data)
            if user.is_valid():
                user.save()
                token_string = "qqqqwwwweeee"
                token = Token(user=user.instance,token=token_string).save()
                response = user.data
                response['token'] = token_string
                return Response({"message": "", "data": [response]})
            else:
                return Response(user.errors, status=status.HTTP_400_BAD_REQUEST)

    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def verifyotp(self,request):
        phone_number = request.data.get('phone_number')
        otp_string = request.data.get('otp')

        otp = Otp.objects.filter(phone_number=phone_number, otp=otp_string).first()
        if not otp:
            return Response({"message": "invalid otp", "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        else:
            return Response({"message": "valid otp", "data": []})


    @list_route(methods=['get','post'], permission_classes=[permissions.AllowAny])
    def opentoall(self,request):
        return Response({"hello"})

    @list_route(methods=['get','post'])
    def myinfo(self,request):
        print request.user
        return Response(UserSerializer(request.user).data)



