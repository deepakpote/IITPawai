from rest_framework.decorators import detail_route, list_route
from rest_framework.response import Response
from rest_framework import status
from rest_framework import viewsets,permissions
from rest_framework.permissions import IsAuthenticated
import re
import string
from time import strftime
import os,time
from django.db import connection
from contents.serializers import teachingAidSerializer , contentSerializer , selfLearningSerializer
from users.authentication import TokenAuthentication
from contents.models import content , contentResponse  , contentGrade , contentDetail
from commons.models import code
from users.models import userSubject, user, userGrade, userTopic , userContent
from mitraEndPoints import constants , utils
from commons.views import getCodeIDs, getArrayFromCommaSepString, getUserIDFromAuthToken
from pip._vendor.requests.api import request


class ContentViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to search the contents.
    """
    queryset = content.objects.all().order_by('contentTitle')
    serializer_class = contentSerializer

    http_method_names = ['get', 'post']
    
    """
    API to search the content
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny] )
    def searchTeachingAid(self,request):
        # get inputs
        fileTypeCodeID = request.data.get('fileTypeCodeID')
        
        subjectCodeIDs = request.data.get('subjectCodeIDs') 
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        pageNumber = request.data.get('pageNumber')
        
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID')
        statusCodeID = request.data.get('statusCodeID')
        
        # On web portal, user no need to login to watch the video's so authentication is removed (commented) for now.
        #authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        #userID = getUserIDFromAuthToken(authToken)
                
        # Check if userID/languageID is passed in post param
#         if not userID:
#             return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
#                              "data": []},
#                              status = status.HTTP_401_UNAUTHORIZED)
         
        # If userID parameter is passed, then check user is exists or not
#         try:
#             objUser = user.objects.get(userID = userID)
#         except user.DoesNotExist:
#             return Response({"response_message": constants.messages.teaching_aid_search_user_not_exists,
#                              "data": []},
#                             status = status.HTTP_404_NOT_FOUND)

        
        # Check if appLanguageCodeID is passed in header
        if not appLanguageCodeID:
            return Response({"response_message": constants.messages.contentSearch_appLanguageCodeID_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
          
        # If appLanguageCodeID parameter is passed, then check appLanguageCodeID is exists or not
        try:
            objAppLanguageCode = code.objects.get(codeID = appLanguageCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.contentSearch_appLanguageCodeID_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
            
        # Check if fileTypeCodeID is passed in post param
#         if not fileTypeCodeID:
#             return Response({"response_message": constants.messages.teaching_aid_search_filetype_cannot_be_empty,
#                      "data": []},
#                      status = status.HTTP_401_UNAUTHORIZED)  
        arrFileTypeCodeID = []    
        
        # Check if fileTypeCodeID is passed in header
        if not fileTypeCodeID or fileTypeCodeID is None:
            
             # Get all fileTypeCodeIDs.
            fileTypeCodeID = getCodeIDs(constants.mitraCodeGroup.fileType)
            
            arrFileTypeCodeID = tuple(map(int, fileTypeCodeID))
    
            #If the length of filetypecodeID is 1 then remove last comma.
            if len(arrFileTypeCodeID) == 1:
                arrFileTypeCodeID =  '(%s)' % ', '.join(map(repr, arrFileTypeCodeID))
                            
        else:
            # If fileTypeCodeID parameter is passed, then check fileType is exists or not
            try:
                objFileType = code.objects.get(codeID = fileTypeCodeID)
                arrFileTypeCodeID = '('+ str(fileTypeCodeID) + ')'
            except code.DoesNotExist:
                return Response({"response_message": constants.messages.teaching_aid_search_filetype_not_exists,
                                 "data": []},
                                status = status.HTTP_404_NOT_FOUND)
         
         
        #declare count for fro to fetch the records.
        fromRecord = 0
            
        #If pageNumber param is not set then fetch the default no of rows from the content
        if not pageNumber or pageNumber == 0:
            pageNumber = constants.contentSearchRecords.default
        else:
            fromRecord = constants.contentSearchRecords.default
            pageNumber = content.objects.all().count()
        
        arrStatusCodeID = []    
        
        # Check if statusCodeID is passed in header
        if not statusCodeID or statusCodeID is None:
            
             # Get all statuscodeIDs.
            statusCodeID = getCodeIDs(constants.mitraCodeGroup.content_News_TrainingCreation_Status)
            
            arrStatusCodeID = tuple(map(int, statusCodeID))
    
            #If the length of filetypecodeID is 1 then remove last comma.
            if len(arrStatusCodeID) == 1:
                arrStatusCodeID =  '(%s)' % ', '.join(map(repr, arrStatusCodeID))
                            
            #statusCodeID = constants.mitraCode.published
        else:
            # If statusCodeID parameter is passed, then check user is exists or not
            try:
                objStatusCode = code.objects.get(codeID = statusCodeID)
                arrStatusCodeID = '('+ str(statusCodeID) + ')'
            except code.DoesNotExist:
                return Response({"response_message": constants.messages.search_content_status_not_exists,
                                 "data": []},
                                status = status.HTTP_404_NOT_FOUND)
                

        #Get the applicable subject list for the respective user.    
        arrSubjectCodeIDs = getSearchContentApplicableSubjectCodeIDs(subjectCodeIDs)         

        arrSubjectCodeIDs = tuple(map(int, arrSubjectCodeIDs))
        
        #print "arrSubjectCodeIDs : ",arrSubjectCodeIDs
        
        if len(arrSubjectCodeIDs) == 1:
            arrSubjectCodeIDs =  '(%s)' % ', '.join(map(repr, arrSubjectCodeIDs))
        
        #Get the applicable grade list for the respective user.
        arrGradeCodeIDs = getSearchContentApplicableGradeCodeIDs(gradeCodeIDs) 
        
        arrGradeCodeIDs = tuple(map(int, arrGradeCodeIDs))
        
        if len(arrGradeCodeIDs) == 1:
            arrGradeCodeIDs =  '(%s)' % ', '.join(map(repr, arrGradeCodeIDs))
        
        # Connection
        cursor = connection.cursor()  
        
        # Create object of common class
        objCommon = utils.common()
        
        # SQL Query
        searchTeachingAidQuery = """ select CC.contentID,
                                            CCG.contentTitle,
                                            CC.requirement,
                                            CCG.instruction,
                                            CASE CC.fileTypeCodeID
                                                WHEN 108100 THEN  CC. fileName
                                                WHEN 108101 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentAudioDir)) + """',CC.fileName) 
                                                WHEN 108102 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentPPTDir)) + """',CC.fileName) 
                                                WHEN 108103 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentWorksheet)) + """',CC.fileName) 
                                                WHEN 108104 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentPDF)) + """',CC.fileName) 
                                                WHEN 108105 THEN  CC. fileName
                                                ELSE NULL
                                                END as fileName,
                                            CCG.author,
                                            CC.objectives,
                                            CC.contentTypeCodeID,
                                            CC.fileTypeCodeID,
                                            CC.languageCodeID,
                                            CC.subjectCodeID,
                                            CC.topicCodeID,
                                            group_concat(CG.gradeCodeID) as gradeCodeIDs
                                            from con_content CC 
                                            INNER JOIN con_contentGrade CG ON CC.contentID = CG.contentID 
                                            INNER JOIN con_contentDetail CCG ON CC.contentID = CCG.contentID
                                            where CCG.appLanguageCodeID = %s
                                            and CC.fileTypeCodeID IN %s 
                                            and CC.statusCodeID IN %s
                                            and CC.contentTypeCodeID = %s 
                                            and CC.subjectCodeID IN %s 
                                            and CG.gradeCodeID IN %s 
                                            group by CC.contentID,
                                            CCG.contentTitle,
                                            CC.requirement,
                                            CCG.instruction,
                                            CC.fileName,
                                            CCG.author,
                                            CC.objectives,
                                            CC.contentTypeCodeID,
                                            CC.fileTypeCodeID,
                                            CC.languageCodeID,
                                            CC.subjectCodeID,
                                            CC.topicCodeID order by CC.contentID limit %s,%s"""%(appLanguageCodeID,str(arrFileTypeCodeID),str(arrStatusCodeID),constants.mitraCode.teachingAids,str(arrSubjectCodeIDs),str(arrGradeCodeIDs),fromRecord,pageNumber)
                                            
        cursor.execute(searchTeachingAidQuery)
    
        #Queryset
        contentQuerySet = cursor.fetchall()
        
        response_data = []
        
        for item in contentQuerySet:
            objResponse_data = {
                                    'contentID':        item[0], 
                                    'contentTitle':     item[1], 
                                    'contentType':      item[7],
                                    'gradeCodeIDs':     str(item[12]),
                                    'subject':          item[10],
                                    'topic' :           item[11],
                                    'requirementCodeIDs':      item[2],
                                    'instruction':      item[3],
                                    'fileType' :        item[8],
                                    'fileName':         item[4],
                                    'author':           item[5],
                                    'objectives' :      item[6],
                                    'language':         item[9]
                                }
            response_data.append(objResponse_data)

        #Check for the no of records fetched.
        if not contentQuerySet:
            return Response({"response_message": constants.messages.teaching_aid_search_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK) 
        
        #Set query string to the contentSerializer
        objContentSerializer = teachingAidSerializer(response_data, many = True)
        
        #Set serializer data to the response 
        response = objContentSerializer.data
        
        #Return the response
        return Response({"response_message": constants.messages.success, "data": response})
    
    """
    API to search the self learning content
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def searchSelfLearning(self,request):
        # get inputs

        #userID = request.data.get('userID') 
        languageCodeIDs = request.data.get('languageCodeIDs')
        topicCodeIDs = request.data.get('topicCodeIDs') 
        pageNumber = request.data.get('pageNumber')
        
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID') 
        
        statusCodeID = request.data.get('statusCodeID') 
        # On web portal, user no need to login to watch the video's so authentication  is removed (commented) for now.
#         authToken = request.META.get('HTTP_AUTHTOKEN')
#         
#         #Get userID from authToken
#         userID = getUserIDFromAuthToken(authToken)
#                 
#         # Check if userID/languageID is passed in post param
#         if not userID:
#             return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
#                              "data": []},
#                              status = status.HTTP_401_UNAUTHORIZED)
#          
#         # If userID parameter is passed, then check user is exists or not
#         try:
#             objUser = user.objects.get(userID = userID)
#         except user.DoesNotExist:
#             return Response({"response_message": constants.messages.self_learning_search_user_not_exists,
#                              "data": []},
#                             status = status.HTTP_404_NOT_FOUND)

        # Check if appLanguageCodeID is passed in header
        if not appLanguageCodeID:
            return Response({"response_message": constants.messages.contentSearch_appLanguageCodeID_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
          
        # If appLanguageCodeID parameter is passed, then check appLanguageCodeID is exists or not
        try:
            objAppLanguageCode = code.objects.get(codeID = appLanguageCodeID)
        except code.DoesNotExist:
                return Response({"response_message": constants.messages.contentSearch_appLanguageCodeID_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
            
         
        #Get Language
        arrLanguageCodeID = []
        if not languageCodeIDs:
            # Get all the languages
            arrLanguageCodeID = getCodeIDs(constants.mitraCodeGroup.language)
        else:
            #Get the array from comma sep string of languageCodeIDs.
            arrLanguageCodeID = getArrayFromCommaSepString(languageCodeIDs)    
            
        arrLanguageCodeID = tuple(map(int, arrLanguageCodeID))
         
         
        if len(arrLanguageCodeID) ==1:
            arrLanguageCodeID =  '(%s)' % ', '.join(map(repr, arrLanguageCodeID))    
         

        #declare count for from which record number to fetch the records.
        fromRecord = 0
            
        #If pageNumber param is not set then fetch the default no of rows from the content
        if not pageNumber or pageNumber == 0:
            pageNumber = constants.contentSearchRecords.default
        else:
            fromRecord = constants.contentSearchRecords.default
            pageNumber = content.objects.all().count()
            
        arrStatusCodeID = [] 
        
        # Check if statusCodeID is passed in header
        if not statusCodeID or statusCodeID is None:
            #statusCodeID = constants.mitraCode.published
            
            # Get all statuscodeIDs.
            statusCodeID = getCodeIDs(constants.mitraCodeGroup.content_News_TrainingCreation_Status)
            
            arrStatusCodeID = tuple(map(int, statusCodeID))
    
            #If the length of filetypecodeID is 1 then remove last comma.
            if len(arrStatusCodeID) == 1:
                arrStatusCodeID =  '(%s)' % ', '.join(map(repr, arrStatusCodeID))
        else:
            # If statusCodeID parameter is passed, then check user is exists or not
            try:
                objStatusCode = code.objects.get(codeID = statusCodeID)
                arrStatusCodeID = '('+ str(statusCodeID) + ')'
            except code.DoesNotExist:
                return Response({"response_message": constants.messages.search_content_status_not_exists,
                                 "data": []},
                                status = status.HTTP_404_NOT_FOUND)
        
        #Get the applicable topic list for the respective user.    
        arrTopicCodeIDs = getSearchContentApplicableTopicCodeIDs(topicCodeIDs)  
        
        arrTopicCodeIDs = tuple(map(int, arrTopicCodeIDs))
         
         
        if len(arrTopicCodeIDs) ==1:
            arrTopicCodeIDs =  '(%s)' % ', '.join(map(repr, arrTopicCodeIDs))
        
        
        #Get the query set using filter on filetype, topic & language     
#         contentQuerySet = content.objects.filter(language__in = arrLanguageCodeID,
#                                                   contentType = constants.mitraCode.selfLearning, 
#                                                   topic__in = arrTopicCodeIDs,
#                                                   contentID = contentDetail_contentID__content).order_by('-contentID')[fromRecord:pageNumber]


        # New code.
        # Connection
        cursor = connection.cursor()  
        
        # Create object of common class
        objCommon = utils.common()
         
        # SQL Query
        searchSelfLearningQuery = """ select CC.contentID, CCG.contentDetailID,
                                            CCG.contentTitle ,
                                            CC.requirement,
                                            CCG.instruction  ,
                                            CASE CC.fileTypeCodeID
                                                WHEN 108100 THEN  CC. fileName
                                                WHEN 108101 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentAudioDir)) + """',CC.fileName) 
                                                WHEN 108102 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentPPTDir)) + """',CC.fileName) 
                                                WHEN 108103 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentWorksheet)) + """',CC.fileName) 
                                                WHEN 108104 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentPDF)) + """',CC.fileName) 
                                                WHEN 108105 THEN  CC. fileName
                                                ELSE NULL
                                                END as fileName,
                                            CCG.author ,
                                            CC.objectives,
                                            CC.contentTypeCodeID,
                                            CC.fileTypeCodeID,
                                            CC.languageCodeID,
                                            CC.subjectCodeID,
                                            CC.topicCodeID
                                            from con_content CC 
                                            INNER JOIN con_contentDetail CCG ON CC.contentID = CCG.contentID
                                            where CC.languageCodeID IN %s 
                                            and CC.contentTypeCodeID = %s 
                                            and CC.statusCodeID IN %s
                                            and CC.topicCodeID IN %s 
                                            and CCG.appLanguageCodeID = %s 
                                            order by CC.contentID limit %s,%s"""%(arrLanguageCodeID,constants.mitraCode.selfLearning,str(arrStatusCodeID),str(arrTopicCodeIDs),appLanguageCodeID,fromRecord,pageNumber)
         
        cursor.execute(searchSelfLearningQuery)
        
         
        #Queryset
        contentQuerySet = cursor.fetchall()
         
        response_data = []
         
        for item in contentQuerySet:
            objResponse_data = {
                                'contentID':        item[0], 
                                'contentTitle':     item[2], 
                                'requirementCodeIDs':      item[3], 
                                'instruction':      item[4], 
                                'fileName':         item[5],
                                'author':           item[6], 
                                'objectives' :      item[7], 
                                'contentType':      item[8],
                                'fileType' :        item[9],
                                'language':         item[10],
                                'subject':          item[11],
                                'topic' :           item[12]
                                }
             
            response_data.append(objResponse_data)

        #Check for the no of records fetched.
        if not contentQuerySet:
            return Response({"response_message": constants.messages.self_learning_search_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK) 
        
        #Set query string to the contentSerializer
        objContentserializer = selfLearningSerializer(response_data, many = True)
        
        #Set serializer data to the response 
        response = objContentserializer.data
        #print(objContentserializer.data)
        
        #Return the response
        return Response({"response_message": constants.messages.success, "data": response})
    
    """
    API to save the content response: Like
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def like(self,request):
        # get inputs
        contentID = request.data.get('contentID')
        hasLiked = request.data.get('hasLiked')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if contentID is passed in post param
        if not contentID:
            return Response({"response_message": constants.messages.save_like_contentid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
        
            
        # Check hasLiked param is passed or not.
        if hasLiked is None:
            return Response({"response_message": constants.messages.save_like_hasLiked_cannot_be_empty,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
        
        # If contentID parameter is passed, then check content exists or not
        try:
            objContent = content.objects.get(contentID = contentID)
        except content.DoesNotExist:
            return Response({"response_message": constants.messages.save_like_content_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.save_like_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
        
        # Create object of common class 
        objCommon = utils.common()
        
        # Check value of  hasLiked is boolean or NOT.
        if not objCommon.isBool(hasLiked):
            return Response({"response_message": constants.messages.save_like_hasLike_value_must_be_boolean,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED) 
                               
        # Save content like response.
        saveContentResponse(objContent, objUser, constants.mitraCode.like , objCommon.getBoolValue(hasLiked))

        #Return the response
        return Response({"response_message": constants.messages.success, "data": []})
    
    """
    API to save the content response: Download
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def download(self,request):
        # get inputs
        contentID = request.data.get('contentID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if contentID is passed in post param
        if not contentID:
            return Response({"response_message": constants.messages.content_download_response__contentid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
               
        # If contentID parameter is passed, then check content exists or not
        try:
            objContent = content.objects.get(contentID = contentID)
        except content.DoesNotExist:
            return Response({"response_message": constants.messages.content_download_response_content_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.content_download_response_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                               
        # Save content download response and return file name of that content.
        contentFileName = saveContentResponse(objContent, objUser, constants.mitraCode.download , None)

        # set file name to the response.
        response = { 'fileName' : contentFileName }
        
        #Return the response
        return Response({"response_message": constants.messages.success, "data": [response]})
    
    """
    API to save the content response: Share
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def share(self,request):
        # get inputs
        contentID = request.data.get('contentID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if contentID is passed in post param
        if not contentID:
            return Response({"response_message": constants.messages.content_share_response__contentid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
               
        # If contentID parameter is passed, then check content exists or not
        try:
            objContent = content.objects.get(contentID = contentID)
        except content.DoesNotExist:
            return Response({"response_message": constants.messages.content_share_response_content_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.content_share_response_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                               
        # Save content share response and return file name of that content.
        contentFileName = saveContentResponse(objContent, objUser, constants.mitraCode.share , None)

        #set the file name to the response.
        response = { 'fileName' : contentFileName }
        
        #Return the response
        return Response({"response_message": constants.messages.success, "data": [response]})
    
    """
    API to get the content response : Liked
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def getContentResponse(self,request):
        # get inputs
        contentID = request.data.get('contentID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if contentID is passed in post param
        if not contentID:
            return Response({"response_message": constants.messages.get_content_response_contentid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
               
        # If contentID parameter is passed, then check content exists or not
        try:
            objContent = content.objects.get(contentID = contentID)
        except content.DoesNotExist:
            return Response({"response_message": constants.messages.get_content_response_content_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.get_content_response_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                               
        # Save content share response and return file name of that content.
        objContentResponse = getContentResponseDetails(objContent, objUser)
                    
        #set the file name to the response.
        response = { 'hasLiked' : objContentResponse.hasLiked ,
                     'hasSaved' : objContentResponse.hasSaved  }
        
        #Return the response
        return Response({"response_message": constants.messages.success, "data": [response]})
    
    
    """
    API to get the content details in all app languages
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def contentDetail(self,request):
        # get inputs
        contentID = request.data.get('contentID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        objContent = None
        gradeCodeIDs = None
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if contentID is passed in post param
        if not contentID:
            return Response({"response_message": constants.messages.get_contentdetail_contentid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
               
        # If contentID parameter is passed, then check content exists or not
        try:
            objContent = content.objects.get(contentID = contentID)
        except content.DoesNotExist:
            return Response({"response_message": constants.messages.get_contentdetail_content_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.get_contentdetail_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                               
        # Get content details in english as wellas marathi language.
        objContentDetails = getContentDetails(contentID)
        #Get actual fileName.
        objContentFileName = getContentFileName(objContent)
        
        #If content type is teachingAids
        if objContentDetails.contentType.codeID == constants.mitraCode.teachingAids:
            subjectCodeID = objContentDetails.subject.codeID 
            topicCodeID = None
        # If content type is self learning.
        elif objContentDetails.contentType.codeID == constants.mitraCode.selfLearning:
            subjectCodeID = None
            topicCodeID = objContentDetails.topic.codeID 
        
        #Get comma sep list of gradeCodeID from contentID
        gradeCodeIDs = ",".join(str(con.grade.codeID) for con in contentGrade.objects.filter(content = objContent))
        
        #Build collection manually for both languages
        response = {  'engContentTitle':        objContentDetails.engContentTitle,
                      'marContentTitle':        objContentDetails.marContentTitle,
                      'engInstruction':         objContentDetails.engInstruction ,
                      'marInstruction ':        objContentDetails.marInstruction,
                      'engAuthor':              objContentDetails.engAuthor,
                      'marAuthor':              objContentDetails.marAuthor,
                      'contentTypeCodeID':      objContentDetails.contentType.codeID,
                      'subjectCodeID':          subjectCodeID,
                      'gradeCodeIDs':           gradeCodeIDs,
                      'topicCodeID':            topicCodeID,
                      'requirementCodeIDs':     objContentDetails.requirement,
                      'objectives':             objContentDetails.objectives ,
                      'fileTypeCodeID':         objContentDetails.fileType.codeID,
                      'fileName':               objContentFileName,
                      'contentLanguageCodeID':  objContentDetails.language.codeID,
                      'statusCodeID':           objContentDetails.status.codeID
                    }
        
       
        #Return the response
        return Response({"response_message": constants.messages.success, "data": [response]})
    
    """
    API to upload the content.
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def uploadContent(self,request):
        # get inputs
        if request.data.get('contentID'):
            contentID = int(request.data.get('contentID'))
        else:
            contentID = 0
        
        engContentTitle = request.data.get('engContentTitle')
        marContentTitle = request.data.get('marContentTitle')
        
        engInstruction = request.data.get('engInstruction')
        marInstruction = request.data.get('marInstruction')
        
        engAuthor = request.data.get('engAuthor')
        marAuthor = request.data.get('marAuthor')
        
        if request.data.get('contentTypeCodeID'):
            contentTypeCodeID = int(request.data.get('contentTypeCodeID'))
        else:
            contentTypeCodeID = None

        subjectCodeID = request.data.get('subjectCodeID')
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        topicCodeID = request.data.get('topicCodeID')
        requirementCodeIDs = request.data.get('requirementCodeIDs')

        fileTypeCodeID = request.data.get('fileTypeCodeID')
        uploadedFile = request.FILES['uploadedFile'] if 'uploadedFile' in request.FILES else None
                         
        if uploadedFile:
            if request.data.get('fileName'):
                return statusHttpUnauthorized(constants.messages.uploadContent_upload_file_or_give_filename)
            else:
                try:
                    uploadedFile = request.FILES['uploadedFile']
                except Exception as e:
                    return statusHttpUnauthorized(constants.messages.uploadContent_upload_a_valid_file)
        
        elif request.data.get('fileName'):
            fileName = request.data.get('fileName')
        
        else:
            return statusHttpUnauthorized(constants.messages.uploadContent_upload_file_or_give_filename)
        
        languageCodeID = request.data.get('contentLanguageCodeID')       
        statusCodeID = request.data.get('statusCodeID')
        
        #This is not getting used any where in the webportal as well as app so commented for now.
        #objectives = request.data.get('objectives')
        #author = request.data.get('author')
        
        #Get user token
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        responseMessage = validateRequest(userID, engContentTitle, marContentTitle, engInstruction, marInstruction, 
                               contentTypeCodeID, fileTypeCodeID, languageCodeID, statusCodeID)
        
        if responseMessage != 0:
            return statusHttpUnauthorized(responseMessage)
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:         
            return statusHttpNotFound(constants.messages.uploadContent_user_not_exists)
                   
        # If statusCodeID parameter is passed, then check status exists or not
        try:
            objStatus = code.objects.get(codeID = statusCodeID)
        except code.DoesNotExist:
            return statusHttpNotFound(constants.messages.uploadContent_status_not_exists)
        
        # If contentType parameter is passed, then check contentType CodeID exists or not
        try:
            objContentType = code.objects.get(codeID = contentTypeCodeID)
        except code.DoesNotExist:
            return statusHttpNotFound(constants.messages.uploadContent_contentType_does_not_exists)
         
        # If fileType parameter is passed, then check fileType exists or not    
        try:
            objFileType = code.objects.get(codeID = fileTypeCodeID)
        except code.DoesNotExist:
            return statusHttpNotFound(constants.messages.uploadContent_fileType_does_not_exists)
         
        # If language parameter is passed, then check language exists or not        
        try:
            objLanguage = code.objects.get(codeID = languageCodeID)
        except code.DoesNotExist:
            return statusHttpNotFound(constants.messages.uploadContent_language_does_not_exists)  
                
        # If the filetype is video or ekStep then validate the URL.
        if isVideoOrEkStep(fileTypeCodeID) == True:   
            # Check if fileName is passed in post param
            if not fileName:
                return statusHttpUnauthorized(constants.messages.uploadContent_fileName_cannot_be_empty)
            
            if int(fileTypeCodeID) == int(constants.mitraCode.video):
                #Validate youtube URL.
                isValidYoutubeURL = validateYoutubeURL(fileName)
                
                #If Youtube URL is Invaild 
                if not isValidYoutubeURL:
                    return statusHttpBadRequest(constants.messages.uploadContent_fileName_invaild)

        else:
            fileName = "upload_pending"
        
        #Declare empty object for subject,Grade and topic
        objSubject = None
        objGrade = None
        objTopic = None
        arrGradeCodeIDs = None
        
        # Check content type of uploaded file.    
        if contentTypeCodeID == int(constants.mitraCode.teachingAids):
            # If content type is teaching Aid then subjetCodeID & gradeCodeIDs can not be empty.
            if not subjectCodeID or subjectCodeID == 0:
                return statusHttpUnauthorized(constants.messages.uploadContent_subjectCodeID_cannot_be_empty)
            
            if not gradeCodeIDs:
                return statusHttpUnauthorized(constants.messages.uploadContent_gradeCodeID_cannot_be_empty)
                
            # Get the respective instance of subject and grade
            objSubject = code.objects.get(codeID = subjectCodeID)
            
            # Build array from comma seprated string (Comma seprated GradeCodeIDs)
            arrGradeCodeIDs = getArrayFromCommaSepString(gradeCodeIDs)
        # If content type is self learning.
        elif contentTypeCodeID == constants.mitraCode.selfLearning:
            # If content type is selfLearning then topicCodeID can not be empty.
            if not topicCodeID:
                return statusHttpUnauthorized(constants.messages.uploadContent_topicCodeID_cannot_be_empty)
            else:
                # Get the respective instance topic.
                objTopic = code.objects.get(codeID = topicCodeID)
        # Invalid content type.
        else:
            return statusHttpUnauthorized(constants.messages.uploadContent_contentType_invalid)
        
        # Get app language instances for english and marathi.
        objAppLanguageEng = code.objects.get(codeID = constants.appLanguage.english)
        objAppLanguageMar = code.objects.get(codeID = constants.appLanguage.marathi)
        
        try:
            # Check contentID is provided or not.
            if not contentID or contentID == 0:
                # Save the content.
                objRec = content.objects.create(contentType = objContentType, 
                                                subject = objSubject,
                                                topic = objTopic,
                                                requirement = requirementCodeIDs,
                                                fileType = objFileType,
                                                fileName= fileName,
                                                #objectives = null,
                                                language = objLanguage,
                                                status = objStatus,
                                                createdBy = objUser,
                                                modifiedBy = objUser)
                
                objRec.save()
                
                #Save the content details for multiple language.
                contentDetail.objects.bulk_create(
                                                    [
                                                    contentDetail(content = objRec,
                                                                  appLanguage = objAppLanguageEng,
                                                                  contentTitle = engContentTitle.strip(),
                                                                  instruction = engInstruction , 
                                                                  author = engAuthor),
                                                    contentDetail(content = objRec,
                                                                  appLanguage = objAppLanguageMar ,
                                                                  contentTitle = marContentTitle.strip(), 
                                                                  instruction = marInstruction , 
                                                                  author = marAuthor),
                                                    ]
                                                 )
             
                contentID =  objRec.contentID 
            
            else:
                # If contentID parameter is passed, then check contentID exists or not and update the content details.       
                try:
                    objcontent = content.objects.get(contentID = contentID)
                except content.DoesNotExist:
                    return statusHttpNotFound(constants.messages.uploadContent_contentID_does_not_exists)
                 
                # If contentID valid, update the details.
                content.objects.filter(contentID = contentID).update(contentType = objContentType, 
                                                                     subject = objSubject,
                                                                     topic = objTopic,
                                                                     requirement = requirementCodeIDs,
                                                                     fileType = objFileType,
                                                                     fileName = fileName,
                                                                     #objectives = objectives,
                                                                     status = objStatus,
                                                                     language = objLanguage,
                                                                     modifiedBy = objUser)
                
                # update content details for english language.
                contentDetail.objects.filter(content = objcontent,
                                             appLanguage = objAppLanguageEng).update(contentTitle = engContentTitle.strip(),  
                                                                                    instruction = engInstruction.strip(),
                                                                                    author = engAuthor)
                # update content details for marathi language.
                contentDetail.objects.filter(content = objcontent,
                                             appLanguage = objAppLanguageMar).update(contentTitle = marContentTitle.strip(),  
                                                                                     instruction = marInstruction.strip(),
                                                                                     author = marAuthor)
                
                if (isVideoOrEkStep(fileTypeCodeID) == False):
                    removeUploadedFile(contentID)
            
            # Check content type of uploaded file.If teachingAids then save GradeCodeIDs     
            if contentTypeCodeID == constants.mitraCode.teachingAids:
                saveContentGrade(arrGradeCodeIDs , contentID)
            
            if (isVideoOrEkStep(fileTypeCodeID) == False):
                saveUploadedFile(uploadedFile, fileTypeCodeID, contentID)
               
        except Exception as e:
            # Error occurred while uploading the content.
            print e
            return statusHttpBadRequest(constants.messages.uploadContent_content_upload_failed)

        #Return the response
        return Response({"response_message": constants.messages.success, "data": [{"contentID" : contentID}]})   
    
    """
    API to save the content status
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def saveContentStatus(self,request):
        # get inputs
        contentID = request.data.get('contentID')
        statusCodeID = request.data.get('statusCodeID')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if contentID is passed in post param
        if not contentID:
            return Response({"response_message": constants.messages.saveContentStatus_contentid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if statusCodeID is passed in post param
        if not statusCodeID:
            return Response({"response_message": constants.messages.saveContentStatus_statuscodeid_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
               
        # If contentID parameter is passed, then check content exists or not
        try:
            objContent = content.objects.get(contentID = contentID)
        except content.DoesNotExist:
            return Response({"response_message": constants.messages.saveContentStatus_content_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If statusCodeID parameter is passed, then check status exists or not
        try:
            objStatus = code.objects.get(codeID = statusCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.saveContentStatus_status_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.saveContentStatus_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                               
        #update content status.                  
        content.objects.filter(contentID = contentID).update(status = objStatus)  

        #Return the response
        return Response({"response_message": constants.messages.success, "data": []})  
        
def getSearchContentApplicableSubjectCodeIDs(subjectCodeIDs):
    # If subjectCodeIDs parameter is passed, split it into an array
    if subjectCodeIDs:
        arrSubjectCodeIDs = subjectCodeIDs.split(',')
        return arrSubjectCodeIDs
    
    # Initialize the array for storing subject code ids
    arrSubjectCodeIDs = []
    
    # If subjectCodeIDs is empty then, no need to make search on user profile (i.e on user subjects). Bring all subjectCodeIDs
    #objUserSubjectList = userSubject.objects.filter(user = objUser)
    
    # Iterate through the subject list to build the array of subject code ids
#     for objUserSubject in objUserSubjectList:
#         arrSubjectCodeIDs.append(objUserSubject.subject.codeID)
#     
#     if len(arrSubjectCodeIDs) > 0:
#         return arrSubjectCodeIDs
    
    # If no subjects are found, under user profile, 
    # then the content must be searched across all the 
    # subjects available in the system
    objCodeList = code.objects.filter(codeGroup = constants.mitraCodeGroup.subject)
    for objCode in objCodeList:
        arrSubjectCodeIDs.append(objCode.codeID)
        
    if len(arrSubjectCodeIDs) > 0:
        return arrSubjectCodeIDs
    
def getSearchContentApplicableGradeCodeIDs(gradeCodeIDs):
    # If gradeCodeIDs parameter is passed, split it into an array
    if gradeCodeIDs:
        arrGradeCodeIDs = gradeCodeIDs.split(',')
        return arrGradeCodeIDs
    
    # Initialize the array for storing grade code ids
    arrGradeCodeIDs = []
    
    # If gradeCodeIDs is empty then, no need to make search on user profile (i.e on user grades). Bring all gradeCodeIDs
    #objUserGradeList = userGrade.objects.filter(user = objUser)
    
    # Iterate through the grade list to build the array of grade code ids
#     for objUserGrade in objUserGradeList:
#         arrGradeCodeIDs.append(objUserGrade.grade.codeID)
#     
#     if len(arrGradeCodeIDs) > 0:
#         return arrGradeCodeIDs
    
    # If no Grade are found, under user profile, 
    # then the content must be searched across all the 
    # Grade available in the system
    objCodeList = code.objects.filter(codeGroup = constants.mitraCodeGroup.grade)
    for objCode in objCodeList:
        arrGradeCodeIDs.append(objCode.codeID)
        
    if len(arrGradeCodeIDs) > 0:
        return arrGradeCodeIDs
    
def getSearchContentApplicableTopicCodeIDs(topicCodeIDs):
    # If topicCodeIDs parameter is passed, split it into an array
    if topicCodeIDs:
        arrTopicCodeIDs = topicCodeIDs.split(',')
        return arrTopicCodeIDs
    
    # Initialize the array for storing topic code ids
    arrTopicCodeIDs = []
    
    # If topicCodeIDs is empty then, no need to make search on user profile (i.e on userTopic). Bring all topicCodeIDs
    #objUserTopicList = userTopic.objects.filter(user = objUser)
    
    # Iterate through the grade list to build the array of topic code ids
#     for objUserTopic in objUserTopicList:
#         arrTopicCodeIDs.append(objUserTopic.topic.codeID)
#     
#     if len(arrTopicCodeIDs) > 0:
#         return arrTopicCodeIDs
    
    # If no topic are found, under user profile, 
    # then the content must be searched across all the 
    # Topic available in the system
    objCodeList = code.objects.filter(codeGroup = constants.mitraCodeGroup.topic)
    for objCode in objCodeList:
        arrTopicCodeIDs.append(objCode.codeID)
        
    if len(arrTopicCodeIDs) > 0:
        return arrTopicCodeIDs
    
"""
Common function to save content response for Like/download/share.
"""
def saveContentResponse(objContent , objUser, contentResponseType , hasLiked):
    #If content response for like.
    if contentResponseType == constants.mitraCode.like:
        if hasLiked != None:
            # If any response for content exists or not.
            try:
                #Check response for content exists by respective user.
                objContentResponse = contentResponse.objects.get(content = objContent , user = objUser)
            except contentResponse.DoesNotExist:
                #If not exists then make entry for content response
                contentResponse(user = objUser , content = objContent , hasLiked = hasLiked ).save()
                return 
            # If response exists then update the response.
            objContentResponse.hasLiked =  hasLiked
            objContentResponse.save()
                
        return
    # Content response for download.
    elif contentResponseType == constants.mitraCode.download :
        
        try:
            #Check response for content exists by respective user.
            objContentResponse = contentResponse.objects.get(content = objContent , user = objUser)
        except contentResponse.DoesNotExist:
            #If not exists then make entry for content response
            contentResponse(user = objUser , content = objContent , downloadCount = 1 ).save()
            # Get the content file name.
            #objConfileName = content.objects.filter(contentID = objContent.contentID)
            return getContentFileName(objContent)
        # If response exists then update the response.
        objContentResponse.downloadCount += 1
        objContentResponse.save()
        # Get the content file name.
        #objConfileName = content.objects.filter(contentID = objContent.contentID)
        return getContentFileName(objContent)
    
    # Content response for share.
    elif contentResponseType == constants.mitraCode.share :
                
        try:
            #Check response for content exists by respective user.
            objContentResponse = contentResponse.objects.get(content = objContent , user = objUser)
        except contentResponse.DoesNotExist:
        #If not exists then make entry for content response
            contentResponse(user = objUser , content = objContent , sharedCount = 1 ).save()
            # Get the content file name.
            #objConfileName = content.objects.filter(contentID = objContent.contentID)
            return getContentFileName(objContent)
        # If response exists then update the response.
        objContentResponse.sharedCount += 1
        objContentResponse.save()
        # Get the content file name.
        #objConfileName = content.objects.filter(contentID = objContent.contentID)
        return getContentFileName(objContent)


"""
Common function to get the actual file name of content.
"""
def getContentFileName(objContent):
    
    # Check content exists or not.
    try:
        #Check content exists.
        objContent = content.objects.get(contentID = objContent.contentID)
    except content.DoesNotExist:
        #If not exists then return
        return 
    
    # Create object of common class
    objCommon = utils.common()
    
    print "objContent.fileType:",objContent.fileType
    #If content is video.
    if objContent.fileType.codeID == constants.mitraCode.video:
        return objContent.fileName
    # If audio.
    elif objContent.fileType.codeID == constants.mitraCode.audio :
        return str(objCommon.getBaseURL(constants.uploadedContentDir.contentAudioDir) + objContent.fileName )
    # if ppt.
    elif objContent.fileType.codeID == constants.mitraCode.ppt :
        return str(objCommon.getBaseURL(constants.uploadedContentDir.contentPPTDir) + objContent.fileName )
    # if worksheet
    elif objContent.fileType.codeID == constants.mitraCode.worksheet :
        return str(objCommon.getBaseURL(constants.uploadedContentDir.contentWorksheet) + objContent.fileName )
    # if contentWorksheet
    elif objContent.fileType.codeID == constants.mitraCode.pdf :
        return str(objCommon.getBaseURL(constants.uploadedContentDir.contentPDF) + objContent.fileName )
    # if contentPDF
    elif objContent.fileType.codeID == constants.mitraCode.ekStep :
        return objContent.fileName

"""
function to get the content response for Like/download/share/Saved.
"""
def getContentResponseDetails(objContent, objUser):
        
        #Check any response for the content exists or not.
        try:
            objContentResponse = contentResponse.objects.get(content = objContent , user = objUser)
        except contentResponse.DoesNotExist:
            #If not exists.It means no single response has been made for this content.
            objContentResponse = contentResponse()
            objContentResponse.hasLiked = False
            objContentResponse.downloadCount = 0
            objContentResponse.sharedCount = 0
        
        #Check any response for the user content exists or not.
        objContentResponse.hasSaved = userContent.objects.filter(content = objContent , user = objUser).exists()

        return objContentResponse
    
"""
function to get the content details.
"""
def getContentDetails(contentID):
    
        objContentDetails = None
        #Get details from content models.
        objContentDetails = content.objects.get(contentID = contentID)
                
        # Get app language instances for english and marathi.
        objAppLanguageEng = code.objects.get(codeID = constants.appLanguage.english)
        objAppLanguageMar = code.objects.get(codeID = constants.appLanguage.marathi)
        
        #Check content details for english language..
        try:
            objContentEngDetails = contentDetail.objects.get(content = objContentDetails , appLanguage = objAppLanguageEng)
            objContentDetails.engContentTitle = objContentEngDetails.contentTitle 
            objContentDetails.engInstruction = objContentEngDetails.instruction   
            objContentDetails.engAuthor = objContentEngDetails.author 
        except contentDetail.DoesNotExist:
            #If not exists.It means no content is not uploaded for english language..
            objContentDetails.engContentTitle = ''
            objContentDetails.engInstruction = ''
            objContentDetails.engAuthor = ''
            
        #Check content details for marathi language..
        try:
            objContentMarDetails = contentDetail.objects.get(content = objContentDetails , appLanguage = objAppLanguageMar)
            objContentDetails.marContentTitle = objContentMarDetails.contentTitle    
            objContentDetails.marInstruction = objContentMarDetails.instruction    
            objContentDetails.marAuthor = objContentMarDetails.author 
        except contentDetail.DoesNotExist:
            #If not exists.It means no content is not uploaded for Marathi language..
            objContentDetails.marContentTitle = ''  
            objContentDetails.marInstruction = ''   
            objContentDetails.marAuthor = ''
        
        return objContentDetails
"""
function to validate the youtube URL.
"""    
def validateYoutubeURL(url):
        # regex for validating youtube URL.
        youtube_regex = (
            r'(https?://)?(www\.)?'
        '(youtube|youtu|youtube-nocookie)\.(com|be)/'
        '(watch\?v=|embed/|v/|.+\?v=)?([^&=%\?]{11})')
    
        # Match youtube URL against regex
        youtube_regex_match = re.match(youtube_regex, url)
        
        if not youtube_regex_match:
            return False
        else:
            return True
"""
Function to save the content Grade.
"""
def saveContentGrade(arrGradeCodeIDs , contentID): 
    objContent = content.objects.get(contentID = contentID)
    # Delete all the contentGrade of respective content from contentGrade.
    contentGrade.objects.filter(content = objContent).delete()
    
    if not arrGradeCodeIDs:
        return
    
    for objGrade in arrGradeCodeIDs:    
        objGradeCode = code.objects.get(codeID = objGrade)  
        # Save the grades
        contentGrade(grade = objGradeCode , content = objContent).save()
    
    return

"""
Function to construct file names of uploaded files with time stamp and content ID
"""
def constructFileName(contentID, fileExtension):
    currentDateTime = strftime("%y%m%d%H%M%S", time.localtime())
    
    return (str(contentID) + "_" + currentDateTime + fileExtension)

"""
Function to remove an already stored file in case of an edit
"""
def removeUploadedFile(contentID):
    
    #under the static/content directory, search for file that starts with the given contentID 
    for root, dirs, files in os.walk(constants.uploadedContentDir.baseDir, topdown=False):
        for name in files:
            #if the file is found, remove it
            if(name.startswith(str(contentID))):
                os.remove(os.path.join(root, name)) 

"""
Function to validate and send back response messages for several request parameters
"""                
def validateRequest(userID, engContentTitle, marContentTitle, engInstruction, marInstruction, 
                               contentTypeCodeID, fileTypeCodeID, languageCodeID, statusCodeID):
    
        # Check if userID is passed in post param
        if not userID:
            return constants.messages.user_userid_cannot_be_empty
                   
        # Check if contentTitle for english is passed in post param
        if not engContentTitle or engContentTitle is None or engContentTitle.isspace():
            return constants.messages.uploadContent_contentTitle_english_cannot_be_empty
            
         # Check if contentTitle for marathi is passed in post param
        if not marContentTitle or marContentTitle is None or marContentTitle.isspace():
            return constants.messages.uploadContent_contentTitle_marathi_cannot_be_empty
            
        # Check if contentType CodeID is passed in post param
        if not contentTypeCodeID and contentTypeCodeID != 0:
            return constants.messages.uploadContent_contentType_cannot_be_empty
        
        # Check if fileType is passed in post param
        if not fileTypeCodeID and fileTypeCodeID != 0:
            return constants.messages.uploadContent_fileType_cannot_be_empty
                
        # Check if language is passed in post param
        if not languageCodeID:
            return constants.messages.uploadContent_languageCodeID_cannot_be_empty
            
        # Check if statusCodeID is passed in post param
        if not statusCodeID:
            return constants.messages.uploadContent_statusCodeID_cannot_be_empty

        return 0
 
"""
Function to save the uploaded file
"""   
def saveUploadedFile(uploadedFile, fileTypeCodeID, contentID):
    
    tempFileName, fileExtension = os.path.splitext(uploadedFile.name) 
    baseDir = None
    
    if int(fileTypeCodeID) == int(constants.mitraCode.pdf):
        baseDir = constants.uploadedContentDir.pdfDir
        uploadedFileName = constructFileName(contentID, fileExtension) 
                
    elif int(fileTypeCodeID) == int(constants.mitraCode.ppt):
        baseDir = constants.uploadedContentDir.pptDir
        uploadedFileName = constructFileName(contentID, fileExtension) 
                
    elif int(fileTypeCodeID) == int(constants.mitraCode.worksheet):
        baseDir = constants.uploadedContentDir.worksheetDir
        uploadedFileName = constructFileName(contentID, fileExtension) 
                
    elif int(fileTypeCodeID) == int(constants.mitraCode.audio):  
        baseDir = constants.uploadedContentDir.audioDir 
        uploadedFileName = constructFileName(contentID, fileExtension) 
                                  
    #path where the file should be stored
    fileName = str(baseDir) + str(uploadedFileName)
            
    #open the file in chunks and write it the to the destination
    with open(fileName, 'wb+') as destination:
        for chunk in uploadedFile.chunks():
            destination.write(chunk)
    
    #updating the corresponding entry in the database                    
    content.objects.filter(contentID = contentID).update(fileName = uploadedFileName)  
     
"""
Function to check if the file type is video or ekStep
"""    
def isVideoOrEkStep(fileTypeCodeID):
    if int(fileTypeCodeID) == int(constants.mitraCode.video) or int(fileTypeCodeID) == int(constants.mitraCode.ekStep):
        return True
    else:
        return False         
 
"""
Function to return HTTP 404
"""              
def statusHttpNotFound(responseMessage):
        return Response({"response_message": responseMessage,
                                 "data": []},
                                status = status.HTTP_404_NOT_FOUND)
        
"""
Function to return HTTP 401
"""         
def statusHttpUnauthorized(responseMessage):
        return Response({"response_message": responseMessage,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED)

"""
Function to return HTTP 400
""" 
def statusHttpBadRequest(responseMessage):
        return Response({"response_message": responseMessage,
                     "data": []},
                     status = status.HTTP_400_BAD_REQUEST)
    