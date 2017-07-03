from django.shortcuts import render

from rest_framework import viewsets,permissions
from rest_framework.response import Response
from rest_framework.decorators import detail_route, list_route
from rest_framework import status
from rest_framework.permissions import IsAuthenticated
from django.db import connection
from django.db.models import Min
from users.authentication import TokenAuthentication
from mitraEndPoints import constants, utils
from events.serializers import eventQuerySerializer,eventSerializer,userEventModelSerializer,trainingListSerializer
from commons.serializers import codeSerializer
from events.CalenderService import EventsCalender
from users.models import user
from commons.models import code
from events.models import userEvent, eventInfo , event, eventDetail, districtBlockMapping
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
            print "objEventSerializer error:",objEventSerializer.errors
            return Response({"response_message": constants.messages.event_add_invalid_input,"data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        result=EventViewSet.calender.addEvent(objEventSerializer.data)
        
        addEvent()
        
        return Response({"response_message": constants.messages.success, "data":result})
    
    """
    API to update the event
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def updateEvent(self, request):        
        objEventSerializer=eventSerializer(data=request.data)
        eventID = request.data.get('eventID')
        
        if not objEventSerializer.is_valid(): 
            print "objEventSerializer error:",objEventSerializer.errors
            return Response({"response_message": constants.messages.event_add_invalid_input,"data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        result=EventViewSet.calender.updateEvent(objEventSerializer.data, eventID)
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
                            status = status.HTTP_200_OK)
        
        objUserEvent = userEvent(event = eventID, user = objUser)
        objUserEvent.save()
        
        #objUserEvent = objUserEventSerializer.create(objUserEventSerializer.data)
        return Response({"response_message": constants.messages.success, "data":[]})
    
# news API start here

    """
    Get training list
    """   
    @list_route(methods=['POST'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def trainingList(self, request):
        categoryCodeID = request.data.get('categoryCodeID') 
        date = request.data.get('date')
        districtCodeID = request.data.get('districtCodeID')
        blockCodeID = request.data.get('blockCodeID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID')

        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
        
        # Connection
        cursor = connection.cursor()  
        
        # Create object of common class
        objCommon = utils.common()
        
        # SQL Query
        searchTrainingListQuery = """ select  
                                    E.eventID, 
                                    EI.eventInfoID,
                                    ED.eventDetailID,
                                    E.categoryCodeID,
                                    EI.appLanguageCodeID, 
                                    EI.eventTitle, 
                                    EI.eventDescription,
                                    ED.date, 
                                    ED.districtCodeID,
                                    ED.blockCodeID,
                                    ED.engLocation,
                                    ED.marLocation,
                                    ED.engTrainer, 
                                    ED.marTrainer,
                                    ED.statusCodeID,
                                    E.createdBy,
                                    E.createdOn         
                            from evt_event E 
                            inner join evt_eventinfo EI on E.eventID = EI.eventID
                            join (    select min(ED.date) as date, ED.eventID 
                                    from evt_eventdetail ED 
                                    group by ED.eventID) A 
                                    on A.eventID = E.eventID
                            join evt_eventdetail ED on ED.eventID = E.eventID and ED.date = A.date
                            where EI.appLanguageCodeID = 113100 """
        
        if categoryCodeID:
            searchTrainingListQuery = searchTrainingListQuery + str(" AND E.categoryCodeID = " + str(categoryCodeID))
            
        if date:
            searchTrainingListQuery = searchTrainingListQuery + str(" AND ED.date = '" + str(date) + "'")
            
        if districtCodeID:
            searchTrainingListQuery = searchTrainingListQuery + str(" AND ED.districtCodeID = " + str(districtCodeID))
            
        if blockCodeID:
            searchTrainingListQuery = searchTrainingListQuery + str(" AND ED.blockCodeID = " + str(blockCodeID))
        
        
        #(appLanguageCodeID,str(arrFileTypeCodeID),str(arrStatusCodeID),constants.mitraCode.teachingAids,str(arrSubjectCodeIDs),str(arrGradeCodeIDs),fromRecord,pageNumber)
                       
        print "searchTrainingListQuery:",searchTrainingListQuery            
        cursor.execute(searchTrainingListQuery)
    
        #Query set
        TrainingListQuerySet = cursor.fetchall()
        
        response_data = []
        
        for item in TrainingListQuerySet:
            objResponse_data = {
                                    'eventID':          item[0], 
                                    'eventInfoID':      item[1], 
                                    'eventDetailID':    item[2],
                                    'categoryCodeID':   item[3],
                                    'appLanguageCodeID':item[4],
                                    'eventTitle' :           item[5],
                                    'eventDescription':      item[6],
                                    'date':                 item[7],
                                    'districtCodeID' :        item[8],
                                    'blockCodeID':         item[9],
                                    'engLocation':           item[10],
                                    'marLocation' :      item[11],
                                    'engTrainer':         item[12],
                                    'marTrainer':        item[13],
                                    'statusCodeID':       item[14],
                                    'createdBy':        item[15],
                                    'createdOn':        item[16]
                                }
            response_data.append(objResponse_data)
            
        #Check for the no of records fetched.
        if not TrainingListQuerySet:
            return Response({"response_message": constants.messages.training_list_search_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK) 
        
        return Response({"response_message": constants.messages.success, "data": [response_data]})
    
    
        """
    Get training list
    """   
    @list_route(methods=['POST'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def alternateTrainingList(self, request):
        eventID = request.data.get('eventID') 
        categoryCodeID = request.data.get('categoryCodeID') 
        date = request.data.get('date')
        districtCodeID = request.data.get('districtCodeID')
        blockCodeID = request.data.get('blockCodeID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID')

        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        objEvent = None
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
            
        # check eventID is not null 
        if not eventID or eventID == 0:
            return Response({"response_message": constants.messages.alternate_event_list_eventid_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
            
        # If eventID parameter is passed, then check eventID exists or not
        try:
            objEvent = event.objects.get(eventID = eventID)
        except event.DoesNotExist:
            return Response({"response_message": constants.messages.alternate_event_list_event_does_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
        
        #Get event details for the event.
        objEventDetail = getMainEventDetailsFromEventID(objEvent)
        
        
        # Connection
        cursor = connection.cursor()  
        
        # Create object of common class
        objCommon = utils.common()
        
        # SQL Query
        searchTrainingListQuery = """ select  
                                    E.eventID, 
                                    EI.eventInfoID,
                                    ED.eventDetailID,
                                    E.categoryCodeID,
                                    EI.appLanguageCodeID, 
                                    EI.eventTitle, 
                                    EI.eventDescription,
                                    ED.date, 
                                    ED.districtCodeID,
                                    ED.blockCodeID,
                                    ED.engLocation,
                                    ED.marLocation,
                                    ED.engTrainer, 
                                    ED.marTrainer,
                                    ED.statusCodeID,
                                    E.createdBy,
                                    E.createdOn         
                            from evt_event E 
                            inner join evt_eventinfo EI on E.eventID = EI.eventID
                            inner join evt_eventdetail ED on ED.eventID = E.eventID 
                            where EI.appLanguageCodeID = 113100 AND E.eventID = """ + str(eventID) + """ AND ED.eventDetailID <> """ + str(objEventDetail[0]) 
        
        if categoryCodeID:
            searchTrainingListQuery = searchTrainingListQuery + str(" AND E.categoryCodeID = " + str(categoryCodeID))
            
        if date:
            searchTrainingListQuery = searchTrainingListQuery + str(" AND ED.date = '" + str(date) + "'")
            
        if districtCodeID:
            searchTrainingListQuery = searchTrainingListQuery + str(" AND ED.districtCodeID = " + str(districtCodeID))
            
        if blockCodeID:
            searchTrainingListQuery = searchTrainingListQuery + str(" AND ED.blockCodeID = " + str(blockCodeID))
        
        
        #(appLanguageCodeID,str(arrFileTypeCodeID),str(arrStatusCodeID),constants.mitraCode.teachingAids,str(arrSubjectCodeIDs),str(arrGradeCodeIDs),fromRecord,pageNumber)
                       
        print "searchTrainingListQuery:",searchTrainingListQuery            
        cursor.execute(searchTrainingListQuery)
    
        #Query set
        TrainingListQuerySet = cursor.fetchall()
        
        response_data = []
        
        for item in TrainingListQuerySet:
            objResponse_data = {
                                    'eventID':          item[0], 
                                    'eventInfoID':      item[1], 
                                    'eventDetailID':    item[2],
                                    'categoryCodeID':   item[3],
                                    'appLanguageCodeID':item[4],
                                    'eventTitle' :           item[5],
                                    'eventDescription':      item[6],
                                    'date':                 item[7],
                                    'districtCodeID' :        item[8],
                                    'blockCodeID':         item[9],
                                    'engLocation':           item[10],
                                    'marLocation' :      item[11],
                                    'engTrainer':         item[12],
                                    'marTrainer':        item[13],
                                    'statusCodeID':       item[14],
                                    'createdBy':        item[15],
                                    'createdOn':        item[16]
                                }
            response_data.append(objResponse_data)
            
        #Check for the no of records fetched.
        if not TrainingListQuerySet:
            return Response({"response_message": constants.messages.training_list_search_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK) 
        
        return Response({"response_message": constants.messages.success, "data": [response_data]})
    
    """
    API to save the training status
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def saveTrainingStatus(self,request):
        # get inputs
        eventDetailID = request.data.get('eventDetailID')
        statusCodeID = request.data.get('statusCodeID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if eventDetailID is passed in post param
        if not eventDetailID:
            return Response({"response_message": constants.messages.saveTrainingStatus_eventDetailid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if statusCodeID is passed in post param
        if not statusCodeID:
            return Response({"response_message": constants.messages.saveTrainingStatus_statuscodeid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
               
        # If eventDetailID parameter is passed, then check event exists or not
        try:
            objEventDetail = eventDetail.objects.get(eventDetailID = eventDetailID)
        except eventDetail.DoesNotExist:
            return Response({"response_message": constants.messages.saveTrainingStatus_event_doesnot_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If statusCodeID parameter is passed, then check status exists or not
        try:
            objStatus = code.objects.get(codeID = statusCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.saveTrainingStatus_status_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.saveTrainingStatus_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                               
        #update event status.                  
        eventDetail.objects.filter(eventDetailID = eventDetailID).update(status = objStatus)  

        #Return the response
        return Response({"response_message": constants.messages.success, "data": []}) 
    
    """
    API to add the MASTER event.
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def addEvent(self,request):
        # get inputs
        eventID = request.data.get('eventID')
        categoryCodeID = request.data.get('categoryCodeID')
        authToken = request.META.get('HTTP_AUTHTOKEN') 
        marEventTitle = request.data.get('marEventTitle')
        engEventTitle = request.data.get('engEventTitle')
        
        
        engEventDescription = request.data.get('engEventDescription')
        marEventDescription = request.data.get('marEventDescription')
        
        date = request.data.get('date')
        
        districtCodeID = request.data.get('districtCodeID')
        blockCodeID = request.data.get('blockCodeID')
        
        engLocation = request.data.get('engLocation')
        marLocation = request.data.get('marLocation')
        
        engTrainer = request.data.get('engTrainer')
        marTrainer = request.data.get('marTrainer')
        
        statusCodeID = request.data.get('statusCodeID')
                
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userImmnmjnmjjD is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if categoryCodeID is passed in post param
        if not categoryCodeID:
            return Response({"response_message": constants.messages.add_event_categoryCodeID_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
        
            
        # Check marEventTitle param is passed or not.
        if not marEventTitle:
            return Response({"response_message": constants.messages.add_event_marEventTitle_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
        
        # Check marEventTitle param is passed or not.
        if not engEventTitle:
            return Response({"response_message": constants.messages.add_event_engEventTitle_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
        
        # Check engEventDescription param is passed or not.
        if not engEventDescription:
            return Response({"response_message": constants.messages.add_event_engEventDescription_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
        
        # Check marEventDescription param is passed or not.
        if not marEventDescription:
            return Response({"response_message": constants.messages.add_event_marEventDescription_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 

        # Check marEventDescription param is passed or not.
        if not date:
            return Response({"response_message": constants.messages.add_event_date_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
        
        # Check districtCodeID param is passed or not.
        if not districtCodeID:
            return Response({"response_message": constants.messages.add_event_districtCodeID_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check blockCodeID param is passed or not.
        if not blockCodeID:
            return Response({"response_message": constants.messages.add_event_blockCodeID_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check engLocation param is passed or not.
        if not engLocation:
            return Response({"response_message": constants.messages.add_event_engLocation_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check marLocation param is passed or not.
        if not marLocation:
            return Response({"response_message": constants.messages.add_event_marLocation_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            

        # Check marTrainer param is passed or not.
        if not marTrainer:
            return Response({"response_message": constants.messages.add_event_marTrainer_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check engTrainer param is passed or not.
        if not engTrainer:
            return Response({"response_message": constants.messages.add_event_engTrainer_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check statusCodeID param is passed or not.
        if not statusCodeID:
            return Response({"response_message": constants.messages.add_event_statusCodeID_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.add_event_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                               
        # Save content like response.
        objeEvent = saveEvent(eventID, categoryCodeID, marEventTitle, engEventTitle, engEventDescription, marEventDescription , objUser)

        saveEventDetail(objeEvent, date, districtCodeID, blockCodeID, engLocation, marLocation, marTrainer, engTrainer, statusCodeID, objUser)
        #Return the response
        return Response({"response_message": constants.messages.success, "data": []})
    
    
    """
    API to add the MASTER event.
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def addAlternateEvent(self,request):
        # get inputs
        eventID = request.data.get('eventID')
        date = request.data.get('date')
        
        districtCodeID = request.data.get('districtCodeID')
        blockCodeID = request.data.get('blockCodeID')
        
        engLocation = request.data.get('engLocation')
        marLocation = request.data.get('marLocation')
        
        engTrainer = request.data.get('engTrainer')
        marTrainer = request.data.get('marTrainer')
        
        statusCodeID = request.data.get('statusCodeID')
        authToken = request.META.get('HTTP_AUTHTOKEN') 
                
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # If eventID parameter is passed, then check event exists or not
        try:
            objEvent = event.objects.get(eventID = eventID)
        except event.DoesNotExist:
            return Response({"response_message": constants.messages.add_event_event_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                 
        # Check marEventDescription param is passed or not.
        if not date:
            return Response({"response_message": constants.messages.add_event_date_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
        
        # Check districtCodeID param is passed or not.
        if not districtCodeID:
            return Response({"response_message": constants.messages.add_event_districtCodeID_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check blockCodeID param is passed or not.
        if not blockCodeID:
            return Response({"response_message": constants.messages.add_event_blockCodeID_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check engLocation param is passed or not.
        if not engLocation:
            return Response({"response_message": constants.messages.add_event_engLocation_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check marLocation param is passed or not.
        if not marLocation:
            return Response({"response_message": constants.messages.add_event_marLocation_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            

        # Check marTrainer param is passed or not.
        if not marTrainer:
            return Response({"response_message": constants.messages.add_event_marTrainer_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check engTrainer param is passed or not.
        if not engTrainer:
            return Response({"response_message": constants.messages.add_event_engTrainer_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check statusCodeID param is passed or not.
        if not statusCodeID:
            return Response({"response_message": constants.messages.add_event_statusCodeID_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.add_event_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                               
        # Save event details.
        saveEventDetail(objEvent, date, districtCodeID, blockCodeID, engLocation, marLocation, marTrainer, engTrainer, statusCodeID, objUser)
        #Return the response
        return Response({"response_message": constants.messages.success, "data": []})
    
    """
    Get Block list from District
    """   
    @list_route(methods=['POST'], permission_classes=[permissions.AllowAny])
    def getBlockListFromDistrict(self, request):
        districtCodeID = request.data.get('districtCodeID')

        blockListQueryset = None
                
        # Check districtCodeID is passed or not.
        if  not districtCodeID:
            return Response({"response_message": constants.messages.blockList_districtCodeID_cannot_be_empty,
                         "data": []},
                         status = status.HTTP_401_UNAUTHORIZED) 
                    
        # If districtCodeID parameter is passed, then check districtCodeID exists or not
        try:
            objDistrict = code.objects.get(codeID = districtCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.blockList_district_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
            
        # Get all blockID from districtCodeID.
        objBlockList = districtBlockMapping.objects.filter(district = objDistrict).values_list('block' , flat=True)
        
        #Get blockCodeID list
        blockListQueryset = code.objects.filter(codeID__in = objBlockList)
        
        # if no records found
        if not blockListQueryset:
            return Response({"response_message": constants.messages.blockList_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK)
               
        serializer = codeSerializer(blockListQueryset, many = True)
        
        return Response({"response_message": constants.messages.success, "data": serializer.data})
    
    """
    API to get trainer list
    """   
    @list_route(methods=['POST'], permission_classes=[permissions.AllowAny])
    def getTrainerList(self, request):
        # get inputs

        # Get all trainer list.
        objTrainerList = eventDetail.objects.values('eventDetailID','engTrainer','marTrainer').distinct()
        
        # Remove empty and null authors
        objTrainerList = objTrainerList.exclude(engTrainer__isnull=True).exclude(engTrainer__exact='')
        
        if not objTrainerList:
            return Response({"response_message": constants.messages.author_list_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK)
        
        return Response({"response_message": constants.messages.success, "data": objTrainerList})
        
        
"""
Common function to event info.
"""
def saveEvent(eventID, categoryCodeID, marEventTitle, engEventTitle, engEventDescription, marEventDescription, objUser):
    
    #Check categoryCodeID exists or not
    try:
        objCategory = code.objects.get(codeID = categoryCodeID)
    except code.DoesNotExist:
        return
    
    #Save event
    objEvent = event.objects.create(category = objCategory, createdBy = objUser)
    objEvent.save()
        
    # Get app language instances for English and Marathi.
    objAppLanguageEng = code.objects.get(codeID = constants.appLanguage.english)
    objAppLanguageMar = code.objects.get(codeID = constants.appLanguage.marathi)
        
    #Save the training info for multiple language.
    eventInfo.objects.bulk_create(
                                        [
                                        eventInfo(
                                                event = objEvent,
                                                appLanguage = objAppLanguageMar,
                                                eventTitle = marEventTitle, 
                                                eventDescription = marEventDescription
                                                ),
                                         eventInfo(
                                                event = objEvent,
                                                appLanguage = objAppLanguageEng,
                                                eventTitle = engEventTitle, 
                                                eventDescription = engEventDescription),
                                        ]
                                     )
    #return event object.
    return objEvent

"""
Common function to event details for Master Event.
"""
def saveEventDetail(objEvent, date,districtCodeID, blockCodeID, engLocation, marLocation, marTrainer, engTrainer, statusCodeID, objUser):
    
    #Check categoryCodeID exists or not
    try:
        objDistrict = code.objects.get(codeID = districtCodeID)
        objBlock = code.objects.get(codeID = blockCodeID) 
        objStatus = code.objects.get(codeID = statusCodeID)
    except code.DoesNotExist:
        return
    
    #Save event details.
    eventDetail(event = objEvent,
                date = date,
                district = objDistrict,
                block = objBlock,
                engLocation = engLocation,
                marLocation = marLocation,
                marTrainer = marTrainer,
                engTrainer = engTrainer,
                status = objStatus,
                createdBy = objUser, 
                modifiedBy = objUser).save()

    return

"""
Common function to delete event details.
"""
def deleteAlternateEventDetail(objEventDetail):
    
    #First delete event from user events,
    userEvent.objects.filter(eventDetail = objEventDetail).delete()
    #Delete event details.
    eventDetail.objects.filter(eventDetailID = objEvent).delete()

    return

"""
Common function to get main EventDetailID from eventID.
"""
def getMainEventDetailsFromEventID(objEvent):
    
    objEventDetail = None
    #Get minimum value field name using aggregation min. i.e find min date records for the event.
    objEventDetail = eventDetail.objects.filter(event = objEvent).values_list('eventDetailID').annotate(Min('date')).order_by('date')[0]

    return objEventDetail

