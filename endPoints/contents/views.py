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
from contents.serializers import teachingAidSerializer , contentSerializer , selfLearningSerializer , chapterDetailSerializer
from users.authentication import TokenAuthentication
from contents.models import content , contentResponse  , contentGrade , contentDetail, chapter, chapterDetail
from commons.models import code
from users.models import userSubject, user, userGrade, userTopic , userContent, userRole
from mitraEndPoints import constants , utils
from commons.views import getCodeIDs, getArrayFromCommaSepString, getUserIDFromAuthToken, getCodeIDsAndCodeName, shouldFilterFor
from pip._vendor.requests.api import request
import requests
from fileinput import filename

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
        chapterID = request.data.get('chapterID')
        pageNumber = request.data.get('pageNumber')
        
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID')
        statusCodeID = request.data.get('statusCodeID')
        uploadedBy = request.data.get('uploadedBy')
        authToken = request.META.get('HTTP_AUTHTOKEN')
  
        
        # On web portal, user no need to login to watch the video's so authentication is removed (commented) for now.
        #authToken = request.META.get('HTTP_AUTHTOKEN')
        
        myDraftCheck = ''
        
        if authToken:
            #Get userID from authToken
            userID = getUserIDFromAuthToken(authToken)
                     
             #Check if userID/languageID is passed in post param
            if not userID:
                 return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                                  "data": []},
                                  status = status.HTTP_401_UNAUTHORIZED)
              
             #If userID parameter is passed, then check user is exists or not
            try:
                 objUser = user.objects.get(userID = userID)
            except user.DoesNotExist:
                 return Response({"response_message": constants.messages.teaching_aid_search_user_not_exists,
                                  "data": []},
                                 status = status.HTTP_404_NOT_FOUND)
                 
            roleID = None
            #Chek any role assigned or not
            try:
                 objUserRole= userRole.objects.get(user = objUser)
                 roleID = objUserRole.role.roleID
            except userRole.DoesNotExist:
                objUserType = user.objects.get(userID = userID)
                roleID = objUserType.userType.codeID
                 
                 
            if roleID == constants.role.admin:
                if statusCodeID == constants.mitraCode.created:
                    #contentIDs = getDraftContentID(objUser)
                    #if contentIDs:
                    myDraftCheck = '( CV.createdBy = ' + str(userID) + ' AND CV.statusCodeID = ' + str('114100 ) AND ')
                elif statusCodeID == None or not statusCodeID:
                    myDraftCheck = '((CV.createdBy = ' + str(userID) + ' AND CV.statusCodeID = ' + str('114100) ') + 'OR statusCodeID IN (114101,114102)) AND '
            elif roleID == constants.role.teacher or roleID == constants.mitraCode.userType_teacher:
                if statusCodeID == constants.mitraCode.created:
                    #contentIDs = getDraftContentID(objUser)
                    #if contentIDs:
                    myDraftCheck = '( CV.createdBy = ' + str(userID) + ' AND CV.statusCodeID = ' + str('114100 ) AND ')
                elif statusCodeID == None or not statusCodeID:
                    myDraftCheck = '((CV.createdBy = ' + str(userID) + ' AND CV.statusCodeID = ' + str('114100) ') + 'OR statusCodeID IN (114102)) AND '
            
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
            
            if not authToken:
                statusCodeID.remove(constants.mitraCode.created)
            
            arrStatusCodeID = tuple(map(int, statusCodeID))
    
            #If the length of statusCodeID is 1 then remove last comma.
            if len(arrStatusCodeID) == 1:
                arrStatusCodeID =  '(%s)' % ', '.join(map(repr, arrStatusCodeID))
                            
            #statusCodeID = constants.mitraCode.published
        else:
            # If statusCodeID parameter is passed, then check user is exists or not
            try:
                objStatusCode = code.objects.get(codeID = statusCodeID)
                
                if not authToken and statusCodeID == constants.mitraCode.created:
                    #This is not a correct way to do...Need to modify.
                    arrStatusCodeID = '(1141000)'
                else:
                    arrStatusCodeID = '('+ str(statusCodeID) + ')'
            except code.DoesNotExist:
                return Response({"response_message": constants.messages.search_content_status_not_exists,
                                 "data": []},
                                status = status.HTTP_404_NOT_FOUND)
                
        if fileTypeCodeID == constants.mitraCode.ekStep:
             return getContentFromEkStepAPI(subjectCodeIDs, gradeCodeIDs)
            
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
            
        uploadedByCheck = ''
            
        # If uploadedBy is empty then don't add the check of uploadedBy.
        if not uploadedBy or uploadedBy is None:
            uploadedByCheck = ''
        else:
            # Added the check for uploadedBy.
            uploadedByCheck = 'CV.createdBy = ' + str(uploadedBy) + ' AND '
        
        chapterCheck = ''
        # If subjectCodeIDs and gradeCodeIDs are provided then check for 'chapterID'
        if subjectCodeIDs and gradeCodeIDs and chapterID:
            # Added check for Chapter. 
            chapterCheck = 'CV.chapterID = ' + str(chapterID) + ' AND '
            
                 
        # Connection
        cursor = connection.cursor()  
        
        # Create object of common class
        objCommon = utils.common()
        
        # SQL Query
        searchTeachingAidQuery = """ select CV.contentID,
                                            CV.contentTitle,
                                            CV.requirement,
                                            CV.instruction,
                                            CASE CV.fileTypeCodeID
                                                WHEN 108100 THEN  CV. fileName
                                                WHEN 108101 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentAudioDir)) + """',CV.fileName) 
                                                WHEN 108102 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentPPTDir)) + """',CV.fileName) 
                                                WHEN 108103 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentWorksheet)) + """',CV.fileName) 
                                                WHEN 108104 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentPDF)) + """',CV.fileName) 
                                                WHEN 108105 THEN  CV. fileName
                                                ELSE NULL
                                                END as fileName,
                                            CV.author,
                                            CV.objectives,
                                            CV.contentTypeCodeID,
                                            CV.fileTypeCodeID,
                                            CV.languageCodeID,
                                            CV.subjectCodeID,
                                            CV.topicCodeID,
                                            group_concat(CG.gradeCodeID) as gradeCodeIDs,
                                            CV.createdOn,
                                            CV.modifiedOn,
                                            CV.chapterID
                                            from vw_con_contentDetail CV 
                                            INNER JOIN con_contentGrade CG ON CV.contentID = CG.contentID 
                                            where """ + uploadedByCheck + myDraftCheck + chapterCheck +  """CV.appLanguageCodeID = %s
                                            and CV.fileTypeCodeID IN %s 
                                            and CV.statusCodeID IN %s
                                            and CV.contentTypeCodeID = %s 
                                            and CV.subjectCodeID IN %s 
                                            and CG.gradeCodeID IN %s
                                            group by CV.contentID,
                                            CV.contentTitle,
                                            CV.requirement,
                                            CV.instruction,
                                            CV.fileName,
                                            CV.author,
                                            CV.objectives,
                                            CV.contentTypeCodeID,
                                            CV.fileTypeCodeID,
                                            CV.languageCodeID,
                                            CV.subjectCodeID,
                                            CV.topicCodeID order by CV.contentID limit %s,%s"""%(appLanguageCodeID,str(arrFileTypeCodeID),str(arrStatusCodeID),constants.mitraCode.teachingAids,str(arrSubjectCodeIDs),str(arrGradeCodeIDs),fromRecord,pageNumber)
                       
        #print "searchTeachingAidQuery:",searchTeachingAidQuery            
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
                                    'language':         item[9],
                                    'createdOn':        item[13],
                                    'modifiedOn':       item[14],
                                    'chapterID':        item[15]
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
        uploadedBy = request.data.get('uploadedBy')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        # On web portal, user no need to login to watch the video's so authentication  is removed (commented) for now.
#         authToken = request.META.get('HTTP_AUTHTOKEN')

        myDraftCheck = ''
        
        if authToken: 
            #Get userID from authToken
            userID = getUserIDFromAuthToken(authToken)
                     
            # Check if userID/languageID is passed in post param
            if not userID:
                return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                                 "data": []},
                                 status = status.HTTP_401_UNAUTHORIZED)
              
            # If userID parameter is passed, then check user is exists or not
            try:
                objUser = user.objects.get(userID = userID)
            except user.DoesNotExist:
                return Response({"response_message": constants.messages.self_learning_search_user_not_exists,
                                 "data": []},
                                status = status.HTTP_404_NOT_FOUND)
                
            roleID = None
            #Chek any role assigned or not
            try:
                 objUserRole= userRole.objects.get(user = objUser)
                 roleID = objUserRole.role.roleID
            except userRole.DoesNotExist:
                objUserType = user.objects.get(userID = userID)
                roleID = objUserType.userType.codeID
                 
                 
            if roleID == constants.role.admin:
                if statusCodeID == constants.mitraCode.created:
                    #contentIDs = getDraftContentID(objUser)
                    #if contentIDs:
                    myDraftCheck = '( CV.createdBy = ' + str(userID) + ' AND CV.statusCodeID = ' + str('114100 ) AND ')
                elif statusCodeID == None or not statusCodeID:
                    myDraftCheck = '((CV.createdBy = ' + str(userID) + ' AND CV.statusCodeID = ' + str('114100) ') + 'OR statusCodeID IN (114101,114102)) AND '
            elif roleID == constants.role.teacher or roleID == constants.mitraCode.userType_teacher:
                if statusCodeID == constants.mitraCode.created:
                    #contentIDs = getDraftContentID(objUser)
                    #if contentIDs:
                    myDraftCheck = '( CV.createdBy = ' + str(userID) + ' AND CV.statusCodeID = ' + str('114100 ) AND ')
                elif statusCodeID == None or not statusCodeID:
                    myDraftCheck = '((CV.createdBy = ' + str(userID) + ' AND CV.statusCodeID = ' + str('114100) ') + 'OR statusCodeID IN (114102)) AND '

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
            
            if not authToken:
                statusCodeID.remove(constants.mitraCode.created)
            
            arrStatusCodeID = tuple(map(int, statusCodeID))
    
            #If the length of statusCodeID is 1 then remove last comma.
            if len(arrStatusCodeID) == 1:
                arrStatusCodeID =  '(%s)' % ', '.join(map(repr, arrStatusCodeID))
        else:
            # If statusCodeID parameter is passed, then check user is exists or not
            try:
                objStatusCode = code.objects.get(codeID = statusCodeID)
                
                if not authToken and statusCodeID == constants.mitraCode.created:
                    #This is not a correct way to do...Need to modify.
                    arrStatusCodeID = '(1141000)'
                else:    
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
        
        
        # If uploadedBy is empty then don't add the check of uploadedBy.
        if not uploadedBy or uploadedBy is None:
            uploadedByCheck = ''
        else:
            # Added the check for uploadedBy.
            uploadedByCheck = 'CV.createdBy = ' + str(uploadedBy) + ' AND '
            
        # Connection
        cursor = connection.cursor()  
        
        # Create object of common class
        objCommon = utils.common()
         
        # SQL Query
        searchSelfLearningQuery = """ select CV.contentID, CV.contentDetailID,
                                            CV.contentTitle ,
                                            CV.requirement,
                                            CV.instruction  ,
                                            CASE CV.fileTypeCodeID
                                                WHEN 108100 THEN  CV. fileName
                                                WHEN 108101 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentAudioDir)) + """',CV.fileName) 
                                                WHEN 108102 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentPPTDir)) + """',CV.fileName) 
                                                WHEN 108103 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentWorksheet)) + """',CV.fileName) 
                                                WHEN 108104 THEN   CONCAT('""" + str(objCommon.getBaseURL(constants.uploadedContentDir.contentPDF)) + """',CV.fileName) 
                                                WHEN 108105 THEN  CV. fileName
                                                ELSE NULL
                                                END as fileName,
                                            CV.author ,
                                            CV.objectives,
                                            CV.contentTypeCodeID,
                                            CV.fileTypeCodeID,
                                            CV.languageCodeID,
                                            CV.subjectCodeID,
                                            CV.topicCodeID,
                                            CV.createdOn,
                                            CV.modifiedOn
                                            from vw_con_contentDetail CV
                                            where """ + uploadedByCheck + myDraftCheck +""" CV.languageCodeID IN %s 
                                            and CV.contentTypeCodeID = %s 
                                            and CV.statusCodeID IN %s
                                            and CV.topicCodeID IN %s 
                                            and CV.appLanguageCodeID = %s 
                                            order by CV.contentID limit %s,%s"""%(arrLanguageCodeID,constants.mitraCode.selfLearning,str(arrStatusCodeID),str(arrTopicCodeIDs),appLanguageCodeID,fromRecord,pageNumber)
         
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
                                'topic' :           item[12],
                                'createdOn':        item[13],
                                'modifiedOn':       item[14]
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
                      'marInstruction':         objContentDetails.marInstruction,
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
                      'statusCodeID':           objContentDetails.status.codeID,
                      'chapterID':              (objContentDetails.chapter.chapterID if objContentDetails.chapter != None else None)
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

        if request.data.get('subjectCodeID'):
            subjectCodeID = int(request.data.get('subjectCodeID'))
        else:
            subjectCodeID = None
            
        
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        
        if request.data.get('chapterID'):
            chapterID = int(request.data.get('chapterID'))
        else:
            chapterID = None
            
        if request.data.get('topicCodeID'):
            topicCodeID = int(request.data.get('topicCodeID'))
        else:
            topicCodeID = None
            
        requirementCodeIDs = request.data.get('requirementCodeIDs')
        
        if request.data.get('fileTypeCodeID'):
            fileTypeCodeID = int(request.data.get('fileTypeCodeID'))
        else:
            fileTypeCodeID = None
            
        if request.data.get('contentLanguageCodeID') :
            languageCodeID = int(request.data.get('contentLanguageCodeID'))
        else:
            languageCodeID = None
             
        if request.data.get('statusCodeID'):      
            statusCodeID = int(request.data.get('statusCodeID'))
        else:
            statusCodeID = None
            
        fileName = None
        
        if(contentID == 0):
            uploadedFile = request.FILES['uploadedFile'] if 'uploadedFile' in request.FILES else None
                         
            if uploadedFile:
                if request.data.get('fileName'):
                    print "IFFF"
                    return statusHttpUnauthorized(constants.messages.uploadContent_upload_file_or_give_filename)
                else:
                    try:
                        uploadedFile = request.FILES['uploadedFile']
                    except Exception as e:
                        return statusHttpUnauthorized(constants.messages.uploadContent_upload_a_valid_file)
        
            elif request.data.get('fileName'):
                fileName = request.data.get('fileName')
        
            else:
                if statusCodeID != constants.mitraCode.created:
                    print "NOT CREATED"
                    return statusHttpUnauthorized(constants.messages.uploadContent_upload_file_or_give_filename)
        
        if(contentID > 0):
            uploadedFile = request.FILES['uploadedFile'] if 'uploadedFile' in request.FILES else None
        

        
        #This is not getting used any where in the webportal as well as app so commented for now.
        #objectives = request.data.get('objectives')
        #author = request.data.get('author')
        
        #Get user token
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        responseMessage = None
        objContentType = None
        objFileType = None
        objLanguage = None
        
        # Check if contentType CodeID is passed in post param
        if not contentTypeCodeID and contentTypeCodeID != 0:
            return statusHttpUnauthorized(constants.messages.uploadContent_contentType_cannot_be_empty)
        
#         # Check if fileType is passed in post param
#         if not fileTypeCodeID and fileTypeCodeID != 0:
#             return statusHttpUnauthorized(constants.messages.uploadContent_fileType_cannot_be_empty)
#                 
#         # Check if language is passed in post param
#         if not languageCodeID:
#             return statusHttpUnauthorized(constants.messages.uploadContent_languageCodeID_cannot_be_empty)
            
        # Check if statusCodeID is passed in post param
        if not statusCodeID:
            return statusHttpUnauthorized(constants.messages.uploadContent_statusCodeID_cannot_be_empty)
        
        if statusCodeID != constants.mitraCode.created:
            responseMessage = validateRequest(userID, engContentTitle, marContentTitle, engInstruction, marInstruction, 
                                   contentTypeCodeID, fileTypeCodeID, languageCodeID, statusCodeID,engAuthor ,marAuthor, )
        
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
         
        if fileTypeCodeID:        
            # If fileType parameter is passed, then check fileType exists or not    
            try:
                objFileType = code.objects.get(codeID = fileTypeCodeID)
            except code.DoesNotExist:
                if statusCodeID != constants.mitraCode.created:
                    return statusHttpNotFound(constants.messages.uploadContent_fileType_does_not_exists)
        
        if languageCodeID:
            # If language parameter is passed, then check language exists or not        
            try:
                objLanguage = code.objects.get(codeID = languageCodeID)
            except code.DoesNotExist:
                if statusCodeID != constants.mitraCode.created:
                    return statusHttpNotFound(constants.messages.uploadContent_language_does_not_exists)  
                
        if fileTypeCodeID:
            # If the filetype is video or ekStep then validate the URL.
            if isVideoOrEkStep(fileTypeCodeID) == True:  
                if contentID == 0: 
                # Check if fileName is passed in post param
                    if not fileName:
                        if statusCodeID != constants.mitraCode.created:
                            return statusHttpUnauthorized(constants.messages.uploadContent_fileName_cannot_be_empty)
                    
                    if int(fileTypeCodeID) == int(constants.mitraCode.video):
                        if fileName:
                            #Validate youtube URL.
                            isValidYoutubeURL = validateYoutubeURL(fileName)
                        
                            #If Youtube URL is Invaild 
                            if not isValidYoutubeURL:
                                return statusHttpBadRequest(constants.messages.uploadContent_fileName_invaild)
                
                else:
                    fileName = request.data.get('fileName')
    
            else:
                fileName = "upload_pending"
        
        #Declare empty object for subject,Grade and topic
        objSubject = None
        objGrade = None
        objTopic = None
        arrGradeCodeIDs = None
        objChapter = None
        
        # Check content type of uploaded file.    
        if contentTypeCodeID == int(constants.mitraCode.teachingAids):
            if statusCodeID != constants.mitraCode.created:
                # If content type is teaching Aid then subjetCodeID & gradeCodeIDs can not be empty.
                if not subjectCodeID or subjectCodeID == 0:
                    return statusHttpUnauthorized(constants.messages.uploadContent_subjectCodeID_cannot_be_empty)
                
                if not gradeCodeIDs:
                    return statusHttpUnauthorized(constants.messages.uploadContent_gradeCodeID_cannot_be_empty)
                
                if subjectCodeID and gradeCodeIDs:
                
                    if not chapterID:
                        return statusHttpUnauthorized(constants.messages.uploadContent_chapterID_cannot_be_empty)
            
            if chapterID:
                # If chapter parameter is passed, then check chapter exists or not        
                try:
                    objChapter = chapter.objects.get(chapterID = chapterID)
                except chapter.DoesNotExist:
                    if statusCodeID != constants.mitraCode.created:
                        return statusHttpNotFound(constants.messages.uploadContent_chapter_does_not_exists) 
            
            if subjectCodeID:
                # Get the respective instance of subject and grade
                objSubject = code.objects.get(codeID = subjectCodeID)
            
            if gradeCodeIDs:
                # Build array from comma seprated string (Comma seprated GradeCodeIDs)
                arrGradeCodeIDs = getArrayFromCommaSepString(gradeCodeIDs)
            
                if len(arrGradeCodeIDs) > 1:
                    return statusHttpUnauthorized(constants.messages.uploadContent_select_single_grade)
        # If content type is self learning.
        elif contentTypeCodeID == constants.mitraCode.selfLearning:
            if statusCodeID != constants.mitraCode.created:
                # If content type is selfLearning then topicCodeID can not be empty.
                if not topicCodeID:
                    return statusHttpUnauthorized(constants.messages.uploadContent_topicCodeID_cannot_be_empty)
            if topicCodeID:
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
                                                chapter = objChapter,
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
                                                                  contentTitle = engContentTitle,
                                                                  instruction = engInstruction , 
                                                                  author = engAuthor),
                                                    contentDetail(content = objRec,
                                                                  appLanguage = objAppLanguageMar ,
                                                                  contentTitle = marContentTitle, 
                                                                  instruction = marInstruction , 
                                                                  author = marAuthor),
                                                    ]
                                                 )
             
                contentID =  objRec.contentID 
                
                if fileTypeCodeID and uploadedFile and fileTypeCodeID and contentID:
                    if (isVideoOrEkStep(fileTypeCodeID) == False):
                        saveUploadedFile(uploadedFile, fileTypeCodeID, contentID)
            
            else:
                # If contentID parameter is passed, then check contentID exists or not and update the content details.       
                try:
                    objcontent = content.objects.get(contentID = contentID)
                except content.DoesNotExist:
                    return statusHttpNotFound(constants.messages.uploadContent_contentID_does_not_exists)
                 
                # If contentID valid, update the details.
                content.objects.filter(contentID = contentID).update(contentType = objContentType, 
                                                                     subject = objSubject,
                                                                     chapter = objChapter,
                                                                     topic = objTopic,
                                                                     requirement = requirementCodeIDs,
                                                                     fileType = objFileType,
                                                                     status = objStatus,
                                                                     language = objLanguage,
                                                                     modifiedBy = objUser)
                try:
                    objContentDetail = contentDetail.objects.get(content = objcontent , appLanguage = objAppLanguageEng)
                    # update content details for english language.
                    contentDetail.objects.filter(content = objcontent,
                                                 appLanguage = objAppLanguageEng).update(contentTitle = engContentTitle,  
                                                                                        instruction = engInstruction,
                                                                                        author = engAuthor)
                except contentDetail.DoesNotExist:
                    # Save the content detail for English language.
                    objEngConDetail = contentDetail.objects.create(content = objcontent,
                                                              appLanguage = objAppLanguageEng,
                                                              contentTitle = engContentTitle,
                                                              instruction = engInstruction , 
                                                              author = engAuthor)
                    objEngConDetail.save()
                        
                try: 
                    objContentDetail = contentDetail.objects.get(content = objcontent , appLanguage = objAppLanguageMar)
                    # update content details for marathi language.
                    contentDetail.objects.filter(content = objcontent,
                                                 appLanguage = objAppLanguageMar).update(contentTitle = marContentTitle,  
                                                                                         instruction = marInstruction,
                                                                                         author = marAuthor)
                except contentDetail.DoesNotExist:
                     # Save the content detail for Marathi language.
                        objMarConDetail = contentDetail.objects.create(content = objcontent,
                                                                  appLanguage = objAppLanguageMar ,
                                                                  contentTitle = marContentTitle, 
                                                                  instruction = marInstruction , 
                                                                  author = marAuthor)
                        objMarConDetail.save()
                 
                if fileTypeCodeID: 
                    if (isVideoOrEkStep(fileTypeCodeID) == False and uploadedFile):
                        removePreviouslyUploadedFile(contentID)
                        saveUploadedFile(uploadedFile, fileTypeCodeID, contentID)
                    
                    elif(isVideoOrEkStep(fileTypeCodeID) == True and fileName):
                        if fileName:
                            updateFileName(fileName, contentID)
            
            # Check content type of uploaded file.If teachingAids then save GradeCodeIDs     
            if contentTypeCodeID == constants.mitraCode.teachingAids:
                if arrGradeCodeIDs:
                    saveContentGrade(arrGradeCodeIDs , contentID)
            
#             if (isVideoOrEkStep(fileTypeCodeID) == False):
#                 saveUploadedFile(uploadedFile, fileTypeCodeID, contentID)
               
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
    
    """
    API to get content authors list
    """   
    @list_route(methods=['POST'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def getContentAuthorList(self, request):
        # get inputs
        authToken = request.META.get('HTTP_AUTHTOKEN')
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID')

        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
    
        # check userID exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.author_list_user_does_not_exist, 
                             "data": []}, status = status.HTTP_404_NOT_FOUND)
            
        # check appLanguageCodeID 
        if not appLanguageCodeID or appLanguageCodeID == 0:
            return Response({"response_message": constants.messages.author_list_appLanguageCodeID_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
            
        # check appLanguageCodeID exists or not
        try:
            objAppLanguage = code.objects.get(codeID = appLanguageCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.author_list_appLanguageCodeID_does_not_exist, 
                             "data": []}, status = status.HTTP_404_NOT_FOUND)
        
        # Get all author list for respective app language.
        objContentAuthorList = contentDetail.objects.filter(appLanguage = objAppLanguage).values('author','appLanguage',).distinct()
        
        # Remove empty and null authors
        objContentAuthorList = objContentAuthorList.exclude(author__isnull=True).exclude(author__exact='')
        
        if not objContentAuthorList:
            return Response({"response_message": constants.messages.author_list_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK)
        
        return Response({"response_message": constants.messages.success, "data": objContentAuthorList})
    
    """
    API to get content uploaded user's list. 
    """   
    @list_route(methods=['POST'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def getContentUploadedByList(self, request):
        # get inputs
        authToken = request.META.get('HTTP_AUTHTOKEN')

        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
    
        # check userID exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.content_uploadedBy_list_user_does_not_exist, 
                             "data": []}, status = status.HTTP_404_NOT_FOUND)
                    
        # Get all distinct users list who has uploaded single content.
        objContentUploadedByList = content.objects.all().values_list('createdBy' , flat=True).distinct()
        
        #Get actual user information LIST
        objUploadedBy = list(user.objects.filter(userID__in = objContentUploadedByList).values('userID','userName'))
        
        #If the list contains logged in user's userID then replace its userName with "Me".
        for objuser in objUploadedBy:
            if objuser['userID'] == userID:
                objCode = code.objects.get(codeID = constants.mitraCode.content_or_news_uploaded_by_user_me)
                objuser['userName'] = objCode.codeNameEn
                
        #If no records found.
        if not objUploadedBy:
            return Response({"response_message": constants.messages.content_uploadedBy_list_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK)
        
        return Response({"response_message": constants.messages.success, "data": objUploadedBy})
    
    """
    API to Add the Chapter
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def addChapter(self,request):
        # get inputs
        chapterID = request.data.get('chapterID')
        subjectCodeID = request.data.get('subjectCodeID')
        gradeCodeID = request.data.get('gradeCodeID')
        chapterEng = request.data.get('chapterEng')
        chapterMar = request.data.get('chapterMar')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is valid or not.
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
        # check userID exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.addChapter_user_does_not_exist, 
                             "data": []}, status = status.HTTP_404_NOT_FOUND)
            
        # Check if subjectCodeID is passed in post param
        if not subjectCodeID or subjectCodeID is None:
            return Response({"response_message": constants.messages.addChapter_subjectCodeID_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if gradeCodeID is passed in post param
        if not gradeCodeID or gradeCodeID is None:
            return Response({"response_message": constants.messages.addChapter_gradeCodeID_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if chapterEng is passed in post param
        if not chapterEng  or chapterEng.isspace() or chapterEng is None:
            return Response({"response_message": constants.messages.addChapter_chapterEng_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if chapterMar is passed in post param
        if not chapterMar or chapterMar.isspace() or chapterMar is None:
            return Response({"response_message": constants.messages.addChapter_chapterMar_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # If subjectCodeID parameter is passed, then check subjectCodeID exists or not
        try:
            objSubject = code.objects.get(codeID = subjectCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.addChapter_subject_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
            
        # If gradeCodeID parameter is passed, then check gradeCodeID exists or not
        try:
            objGrade = code.objects.get(codeID = gradeCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.addChapter_grade_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
            
        if not chapterID or chapterID == 0:
            # Save chapter
            objChapter = chapter.objects.create(
                                            subject = objSubject,       # Subject
                                            grade = objGrade,           # Grade
                                            createdBy = objUser,
                                            modifiedBy = objUser
                                            )
    
            objChapter.save()
            
            # Save chapter details
            objChapterDetail = chapterDetail.objects.create(
                                            chapter = objChapter,       # Chpter
                                            chapterEng = chapterEng,    # Chapter name in English
                                            chapterMar = chapterMar     # Chapter name in Marathi
                                            )
    
            objChapterDetail.save()
        else:
            
            # If chapterID parameter is passed, then check chapter exists or not
            try:
                objChapter = chapter.objects.get(chapterID = chapterID)
            except chapter.DoesNotExist:
                return Response({"response_message": constants.messages.addChapter_chapter_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
            
            #Update chapter
            chapter.objects.filter(chapterID = chapterID).update(subject = objSubject, grade = objGrade,modifiedBy = objUser)
            
            #Update chapter details
            chapterDetail.objects.filter(chapter = objChapter).update(chapterEng = chapterEng,chapterMar = chapterMar)
            
        #Return the response
        return Response({"response_message": constants.messages.success, "data": []})  
    
    """
    Get Chapter list
    """   
    @list_route(methods=['POST'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def chapterList(self, request):
        subjectCodeID = request.data.get('subjectCodeID')
        gradeCodeID = request.data.get('gradeCodeID')
        authToken = request.META.get('HTTP_AUTHTOKEN')

        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
    
        # check userID exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.chapterList_user_does_not_exist, 
                             "data": []}, status = status.HTTP_404_NOT_FOUND)
            
        chapterDetailQuerySet = None
            
        # if subjectCodeID and gradeCodeID are passed then fetch chapters for passed subject & grade, else fetch all the chapters
        if subjectCodeID or gradeCodeID:
            
            # Check if subjectCodeID is passed in post param
            if not subjectCodeID or subjectCodeID is None:
                return Response({"response_message": constants.messages.chapterList_subjectCodeID_cannot_be_empty,
                         "data": []},
                         status = status.HTTP_401_UNAUTHORIZED) 
                
            # Check if gradeCodeID is passed in post param
            if not gradeCodeID or gradeCodeID is None:
                return Response({"response_message": constants.messages.chapterList_gradeCodeID_cannot_be_empty,
                         "data": []},
                         status = status.HTTP_401_UNAUTHORIZED) 
                
            # If subjectCodeID parameter is passed, then check subjectCodeID exists or not
            try:
                objSubject = code.objects.get(codeID = subjectCodeID)
            except code.DoesNotExist:
                return Response({"response_message": constants.messages.chapterList_subject_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
                
            # If gradeCodeID parameter is passed, then check gradeCodeID exists or not
            try:
                objGrade = code.objects.get(codeID = gradeCodeID)
            except code.DoesNotExist:
                return Response({"response_message": constants.messages.chapterList_grade_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
            
            chapterDetailQuerySet = chapterDetail.objects.filter(chapter__grade = objGrade, chapter__subject = objSubject)
          
        # Fetch all the chapters    
        else:
            
            chapterDetailQuerySet = chapterDetail.objects.all()
            
        # if no records found
        if not chapterDetailQuerySet:
            return Response({"response_message": constants.messages.chapterList_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK)
            
        #Chapter detail serializer.            
        serializer = chapterDetailSerializer(chapterDetailQuerySet, many = True)
        
        return Response({"response_message": constants.messages.success, "data": serializer.data})
        
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
def removePreviouslyUploadedFile(contentID):
    
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
                               contentTypeCodeID, fileTypeCodeID, languageCodeID, statusCodeID , engAuthor , marAuthor):
    
        # Check if userID is passed in post param
        if not userID:
            return constants.messages.user_userid_cannot_be_empty
                   
        # Check if contentTitle for english is passed in post param
        if not engContentTitle or engContentTitle is None or engContentTitle.isspace():
            return constants.messages.uploadContent_contentTitle_english_cannot_be_empty
            
         # Check if contentTitle for marathi is passed in post param
        if not marContentTitle or marContentTitle is None or marContentTitle.isspace():
            return constants.messages.uploadContent_contentTitle_marathi_cannot_be_empty
        
        # Check if engInstruction for english is passed in post param
        if not engInstruction or engInstruction is None or engInstruction.isspace():
            return constants.messages.uploadContent_instruction_english_cannot_be_empty
            
         # Check if contentTitle for marathi is passed in post param
        if not marInstruction or marInstruction is None or marInstruction.isspace():
            return constants.messages.uploadContent_instruction_marathi_cannot_be_empty
        
        # Check if engAuthor for english is passed in post param
        if not engAuthor or engAuthor is None or engAuthor.isspace():
            return constants.messages.uploadContent_author_english_cannot_be_empty
            
         # Check if marAuthor for marathi is passed in post param
        if not marAuthor or marAuthor is None or marAuthor.isspace():
            return constants.messages.uploadContent_author_marathi_cannot_be_empty
            
        # Check if contentType CodeID is passed in post param
        if not contentTypeCodeID and contentTypeCodeID != 0:
            return constants.messages.uploadContent_contentType_cannot_be_empty
         
        # Check if fileType is passed in post param
        if not fileTypeCodeID and fileTypeCodeID != 0:
            return constants.messages.uploadContent_fileType_cannot_be_empty
                 
        # Check if language is passed in post param
        if not languageCodeID:
            return constants.messages.uploadContent_languageCodeID_cannot_be_empty
#             
#         # Check if statusCodeID is passed in post param
#         if not statusCodeID:
#             return constants.messages.uploadContent_statusCodeID_cannot_be_empty

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
Function to get draft content from userID.
"""   
def getDraftContentID(objUser):
    
    # Initialize the array for storing content ids with status 'Created'
    arrDraftContentIDs = []
    
    objContentStatusCreated = code.objects.get(codeID = constants.mitraCode.created)
    objContentList = content.objects.filter(status = objContentStatusCreated,createdBy = objUser)
    for objContent in objContentList:
        arrDraftContentIDs.append(objContent.contentID)
        
    if len(arrDraftContentIDs) > 0:
        return arrDraftContentIDs    
    
"""
Function to update the video or ekStep URL in DB
"""      
def updateFileName(fileName, contentID):
    content.objects.filter(contentID = contentID).update(fileName = fileName)
     
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
        
'''
Function to fetch data from ekStep
'''
def getContentFromEkStepAPI(subjectCodeIDs, gradeCodeIDs):
    url = constants.ekStep.url
    headers = {'Content-Type': 'application/json',
               'Authorization' : constants.ekStep.apiKey
               }
      
    filters = {
            "contentType":["Story","Worksheet","Game","Collection","Textbook"],
            "objectType":["Content"],
            "status":["Live"],
            "tags":["MAA"],
            "domain": ["literacy","numeracy"],
            }
    # if no inputs for gradeCodeIDs then fetch content for grade 1 to 4
    if not gradeCodeIDs or gradeCodeIDs == None:
        filters["gradeLevel"] =  ["Grade 1","Grade 2","Grade 3","Grade 4"]
        
    # if no inputs for subjectCodeID then fetch contents for english, marathi and hindi language only
    if not subjectCodeIDs or subjectCodeIDs == None:
          filters["language"] = ["English","Marathi","Hindi"]
          
     
    requestBody = {
                    "request":
                        {
                            "filters": filters,
                            "limit": "1000"
                        }
                   }
        
    responseData = []    
    try :
#         call ekstep to get a response
        ekStepResponse = requests.post(url, headers=headers, json=requestBody)
        count = 0;
        
        print "ekStepResponse:",ekStepResponse
        
#         process every entry for key - result which has an array of content
        for entry, value in ekStepResponse.json().iteritems():
            if entry == "result":                
                contentArray = value['content']
        
#         construct a response for mitra
        for entry in contentArray:
            responseDataSingle = {
                        'contentID' : 0,
                        'contentTitle' : entry['name'],
                        'contentType' : constants.mitraCode.teachingAids,
                        'gradeCodeIDs' : mapGrades(entry['gradeLevel']),
                        'subject' : mapSubject(entry['domain'], entry['language']),
                        'topic' : None,
                        'requirementCodeIDs': "",
                        'instruction': "",
                        'fileType' : constants.mitraCode.ekStep,
                        'fileName': getFileNameFromEkStep(entry['identifier']),
                        'author': "",
                        'objectives' : "",
                        'language': mapLanguage(entry['language']),
                        'createdOn': entry['createdOn'],
                        'modifiedOn': entry['lastUpdatedOn'],
                        'chapterID': ""
                    }
#            filters for subject and grade  
            if  (shouldFilterFor(responseDataSingle['gradeCodeIDs'], gradeCodeIDs) == False and
                 shouldFilterFor(responseDataSingle['subject'], subjectCodeIDs) == False): 
                    responseData.append(responseDataSingle)
                    
        print "ekStepResponse:",ekStepResponse
                     
    except Exception as e:
            print "Exception", e
    
    if not responseData:
            return Response({"response_message": constants.messages.teaching_aid_search_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK) 
            
    #Set query string to the contentSerializer
    objContentSerializer = teachingAidSerializer(responseData, many = True)
    #Set serializer data to the response 
    response = objContentSerializer.data
    
    
    #Return the response
    return Response({"response_message": constants.messages.success, "data": response})

'''
function to map ekStep grades with mitra grade codes
'''
def mapGrades(gradeLevel):
    
    ekStepGradesToMitraGradesDict = {
                                     'Grade 1' : constants.grade.gradeOne,
                                     'Grade 2' : constants.grade.gradeTwo,
                                     'Grade 3' : constants.grade.gradeThree,
                                     'Grade 4' : constants.grade.gradeFour
                                    }
      
    gradesOut = ""
    for grade in gradeLevel:
        if grade == 'Kindergarten' or grade == 'Other' or grade == 'Grade 5':
            continue
        gradesOut = gradesOut + str(ekStepGradesToMitraGradesDict[grade]) + "," 
#     return the entire string except the last comma
    return gradesOut[:-1]    

'''
function to map ekStep domain and language to mitra's subject
'''
def mapSubject(domainArray, languageArray):
    
    subjectIdNameDict = getCodeIDsAndCodeName(constants.mitraCodeGroup.subject)
    subjectOut = ""
    
#     if domainArray[0] == constants.ekStepCodes.numeracy:
#         subjectOut = subjectOut + str(subjectIdNameDict['Maths']) + ","
#     elif domainArray[0] == constants.ekStepCodes.literacy and languageArray[0] == constants.ekStepCodes.marathi:
#         subjectOut = subjectOut + str(subjectIdNameDict['Marathi']) + ","  
#     elif domainArray[0] == constants.ekStepCodes.literacy and languageArray[0] == constants.ekStepCodes.english:
#         subjectOut = subjectOut + str(subjectIdNameDict['English']) + ","
    #     return the entire string except the last comma
    
    if constants.ekStepCodes.numeracy in domainArray:
        subjectOut = subjectOut + str(subjectIdNameDict['Maths']) + ","
    elif constants.ekStepCodes.literacy in domainArray and languageArray[0] == constants.ekStepCodes.marathi:
        subjectOut = subjectOut + str(subjectIdNameDict['Marathi']) + ","  
    elif constants.ekStepCodes.literacy in domainArray and languageArray[0] == constants.ekStepCodes.english:
        subjectOut = subjectOut + str(subjectIdNameDict['English']) + ","
    else:
        subjectOut = None
    return subjectOut[:-1] 

'''
function to map ekStep language to mitra language codes
'''    
def mapLanguage(languageArray):
    
    ekStepLanguageToCodeDict = {
                                constants.ekStepCodes.marathi : constants.language.english,
                                constants.ekStepCodes.english : constants.language.marathi
                                }
    
    languageOut = ""
    for language in languageArray:
        languageOut = languageOut + str(ekStepLanguageToCodeDict[language]) + ","
#     return the entire string except the last comma   
    return languageOut[:-1]
    
'''
function to cnstruct a preview URL of ekstep content
'''       
def getFileNameFromEkStep(identifier):
    contextPath = constants.ekStep.contentPreviewUrl
    return contextPath + str(identifier)     


    