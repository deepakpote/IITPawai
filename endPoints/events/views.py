from django.shortcuts import render

from rest_framework import viewsets,permissions
from rest_framework.response import Response
from rest_framework.decorators import detail_route, list_route
from rest_framework import status
from rest_framework.permissions import IsAuthenticated
from users.authentication import TokenAuthentication

from mitraEndPoints import constants
from events.serializers import eventQuerySerializer,eventSerializer,userEventModelSerializer
from events.CalenderService import EventsCalender
from users.models import user
from events.models import userEvent
from commons.views import getUserIDFromAuthToken

class EventViewSet(viewsets.ViewSet):
    """
    API endpoint to fetch list of events from calender
    """
    serializer_class = userEventModelSerializer
    calender = EventsCalender()
    
    http_method_names = ['get', 'post']
    
    """
    API to Fetch list of events
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def listEvents(self, request):
        queryParameters = eventQuerySerializer(data=request.data)
        
        if not queryParameters.is_valid():
            return Response({"response_message": constants.messages.event_list_invalid_input, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        lstEvents=EventViewSet.calender.listEvents(**(queryParameters.data))
        return Response({"response_message": constants.messages.success, "data":lstEvents})
    
    """
    API to add event
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def addEvent(self, request):        
        objEventSerializer=eventSerializer(data=request.data)
        if not objEventSerializer.is_valid():
            return Response({"response_message": constants.messages.event_add_invalid_input,"data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        result=EventViewSet.calender.addEvent(objEventSerializer.data)
        return Response({"response_message": constants.messages.success, "data":result})
    
    """
    API to attend event
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def attendEvent(self, request):
        eventID = request.data.get('eventID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        # Check if userID is passed in post param
        if not eventID:
            return Response({"response_message": constants.messages.event_attend_eventid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
        
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
         
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.event_attend_user_does_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
            
#         objUserEventSerializer = userEventModelSerializer(data = request.data)
#         if not objUserEventSerializer.is_valid():
#             return Response({"response_message": constants.messages.event_attend_parameters_not_valid,"data": []},
#                             status=status.HTTP_401_UNAUTHORIZED)
        if userEvent.objects.filter(event = eventID, user = objUser).exists():
            return Response({"response_message": constants.messages.event_attend_user_already_attending_event,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
        
        objUserEvent = userEvent(event = eventID, user = objUser)
        objUserEvent.save()
        
        #objUserEvent = objUserEventSerializer.create(objUserEventSerializer.data)
        return Response({"response_message": constants.messages.success, "data":[]})
        