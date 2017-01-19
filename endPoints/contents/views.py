from rest_framework.decorators import detail_route, list_route
from rest_framework.response import Response
from rest_framework import status
from rest_framework import viewsets,permissions
from rest_framework.permissions import IsAuthenticated
from contents.serializers import contentSerializer 
from users.authentication import TokenAuthentication
from contents.models import content , contentResponse  , contentGrade
from commons.models import code
from users.models import userSubject, user, userGrade, userTopic , userContent
from mitraEndPoints import constants , utils
from commons.views import getCodeIDs, getArrayFromCommaSepString, getUserIDFromAuthToken


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
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication] )
    def searchTeachingAid(self,request):
        # get inputs
        fileTypeCodeID = request.data.get('fileTypeCodeID')
        
        subjectCodeIDs = request.data.get('subjectCodeIDs') 
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        pageNumber = request.data.get('pageNumber')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
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
            return Response({"response_message": constants.messages.teaching_aid_search_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
            
        # Check if fileTypeCodeID is passed in post param
        if not fileTypeCodeID:
            return Response({"response_message": constants.messages.teaching_aid_search_filetype_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED)  
         
         
        #declare count for fro to fetch the records.
        fromRecord = 0
            
        #If pageNumber param is not set then fetch the default no of rows from the content
        if not pageNumber or pageNumber == 0:
            pageNumber = constants.contentSearchRecords.default
        else:
            fromRecord = constants.contentSearchRecords.default
            pageNumber = content.objects.all().count()
        
        #Get the applicable subject list for the respective user.    
        arrSubjectCodeIDs = getSearchContentApplicableSubjectCodeIDs(subjectCodeIDs , objUser)         

        #Get the applicable grade list for the respective user.
        arrGradeCodeIDs = getSearchContentApplicableGradeCodeIDs(gradeCodeIDs , objUser)       
        
        #Get the applicable content for GradeCodeID.
        arrContentID = contentGrade.objects.filter(grade__in = arrGradeCodeIDs).values_list('content').distinct()
        
        #Get the query set using filter on filetype, subject, grade     
        contentQuerySet = content.objects.filter(fileType = fileTypeCodeID, 
                                                  contentType = constants.mitraCode.teachingAids,
                                                  subject__in = arrSubjectCodeIDs, 
                                                  contentID__in = arrContentID).order_by('-contentID')[fromRecord:pageNumber]
        
        #Check for the no of records fetched.
        if not contentQuerySet:
            return Response({"response_message": constants.messages.teaching_aid_search_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK) 
        
        #Set query string to the contentSerializer
        objContentserializer = contentSerializer(contentQuerySet, many = True)
        
        #Set serializer data to the response 
        response = objContentserializer.data
        
        #Return the response
        return Response({"response_message": constants.messages.success, "data": response})
    
    """
    API to search the self learning content
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def searchSelfLearning(self,request):
        # get inputs

        userID = request.data.get('userID') 
        languageCodeIDs = request.data.get('languageCodeIDs')
        topicCodeIDs = request.data.get('topicCodeIDs') 
        pageNumber = request.data.get('pageNumber')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
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
         
        #Get Language
        arrLanguageCodeID = []
        if not languageCodeIDs:
            # Get all the languages
            arrLanguageCodeID = getCodeIDs(constants.mitraCodeGroup.language)
        else:
            #Get the array from comma sep string of languageCodeIDs.
            arrLanguageCodeID = getArrayFromCommaSepString(languageCodeIDs)     

        #declare count for from which record number to fetch the records.
        fromRecord = 0
            
        #If pageNumber param is not set then fetch the default no of rows from the content
        if not pageNumber or pageNumber == 0:
            pageNumber = constants.contentSearchRecords.default
        else:
            fromRecord = constants.contentSearchRecords.default
            pageNumber = content.objects.all().count()
        
        #Get the applicable topic list for the respective user.    
        arrTopicCodeIDs = getSearchContentApplicableTopicCodeIDs(topicCodeIDs , objUser)  
        
        #Get the query set using filter on filetype, topic & language     
        contentQuerySet = content.objects.filter(language__in = arrLanguageCodeID,
                                                  contentType = constants.mitraCode.selfLearning, 
                                                  topic__in = arrTopicCodeIDs).order_by('-contentID')[fromRecord:pageNumber]
        
        #Check for the no of records fetched.
        if not contentQuerySet:
            return Response({"response_message": constants.messages.self_learning_search_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK) 
        
        #Set query string to the contentSerializer
        objContentserializer = contentSerializer(contentQuerySet, many = True)
        
        #Set serializer data to the response 
        response = objContentserializer.data
        
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
    API to upload the content.
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def uploadContent(self,request):
        # get inputs
        contentTitle = request.data.get('contentTitle')
        contentType = request.data.get('contentType')
        subjectCodeID = request.data.get('subjectCodeID')
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        topicCodeID = request.data.get('topicCodeID')
        requirement = request.data.get('requirement')
        instruction = request.data.get('instruction')
        fileType = request.data.get('fileType')
        fileName = request.data.get('fileName')
        author = request.data.get('author')
        objectives = request.data.get('objectives')
        language = request.data.get('language')
        
        #Get user token
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if contentTitle is passed in post param
        if not contentTitle:
            return Response({"response_message": constants.messages.uploadContent_contentTitle_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if contentType is passed in post param
        if not contentType:
            return Response({"response_message": constants.messages.uploadContent_contentType_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
        
        # Check if fileType is passed in post param
        if not fileType:
            return Response({"response_message": constants.messages.uploadContent_fileType_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if fileName is passed in post param
        if not fileName:
            return Response({"response_message": constants.messages.uploadContent_fileName_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED)
            
        #Validate youtube URL.
        ObjResponse = validateYoutubeURL(fileName)
        
        #If Youtube URL is Invaild 
        if ObjResponse is None:
            return Response({"response_message": constants.messages.uploadContent_fileName_invaild,
                     "data": []},
                     status = status.HTTP_400_BAD_REQUEST)
            
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.uploadContent_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
            
        #Declare empty object for subject,Grade and topic
        objSubject = None
        objGrade = None
        objTopic = None
        arrGradeCodeIDs = None
        
        # Check content type of uploaded file.    
        if contentType == constants.mitraCode.teachingAids:
            # If content type is teaching Aid then subjetCodeID & gradeCodeIDs can not be empty.
            if not subjectCodeID:
                return Response({"response_message": constants.messages.uploadContent_subjectCodeID_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED)
            if not gradeCodeIDs:
                return Response({"response_message": constants.messages.uploadContent_gradeCodeID_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED)
                
            # Get the respective instance of subject and grade
            objSubject = code.objects.get(codeID = subjectCodeID)
            #objGrade = code.objects.get(codeID = gradeCodeID)
            
            # Build array from comma seprated string (Comma seprated GradeCodeIDs)
            arrGradeCodeIDs = getArrayFromCommaSepString(gradeCodeIDs)
        # If content type is self learning.
        elif contentType == constants.mitraCode.selfLearning:
            # If content type is selfLearning then topicCodeID can not be empty.
            if not topicCodeID:
                return Response({"response_message": constants.messages.uploadContent_topicCodeID_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED)
            else:
                # Get the respective instance topic.
                objTopic = code.objects.get(codeID = topicCodeID)
        # Invalid content type.
        else:
            return Response({"response_message": constants.messages.uploadContent_contentType_invalid,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED)
        
        # If contentType parameter is passed, then check contentType exists or not
        try:
            objContentType = code.objects.get(codeID = contentType)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.uploadContent_contentType_does_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If fileType parameter is passed, then check fileType exists or not    
        try:
            objFileType = code.objects.get(codeID = fileType)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.uploadContent_fileType_does_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If language parameter is passed, then check language exists or not        
        try:
            objLanguage = code.objects.get(codeID = language)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.uploadContent_language_does_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If any response for content exists or not.
        try:
            # Save the content.
            ObjRec =content.objects.create(contentTitle = contentTitle, 
                    contentType = objContentType, 
                    subject = objSubject,
                    topic = objTopic,
                    requirement = requirement,
                    instruction = instruction,
                    fileType = objFileType,
                    fileName= fileName,
                    author = author,
                    objectives = objectives,
                    language = objLanguage,
                    createdBy = objUser,
                    modifiedBy = objUser)
            
            ObjRec.save()        
            
            # Check content type of uploaded file.If teachingAids then save GradeCodeIDs     
            if contentType == constants.mitraCode.teachingAids:  
                if content.objects.filter(contentID = ObjRec.contentID).exists():
                    objContent = content.objects.get(contentID = ObjRec.contentID)
                    for objGrade in arrGradeCodeIDs:    
                        objGradeCode = code.objects.get(codeID = objGrade)  
                        # Save the grades
                        contentGrade(grade = objGradeCode , content = objContent).save()
            
        except Exception as e:
            # Error occured while uploading the content.
            print e
            return Response({"response_message": constants.messages.uploadContent_content_upload_failed,
                     "data": []},
                     status = status.HTTP_400_BAD_REQUEST)

        #Return the response
        return Response({"response_message": constants.messages.success, "data": []})
        
        
def getSearchContentApplicableSubjectCodeIDs(subjectCodeIDs, objUser):
    # If subjectCodeIDs parameter is passed, split it into an array
    if subjectCodeIDs:
        arrSubjectCodeIDs = subjectCodeIDs.split(',')
        return arrSubjectCodeIDs
    
    # If subjectCodeIDs parameter is NOT passed, then fetch the subject set in profile info of that user
    objUserSubjectList = userSubject.objects.filter(user = objUser)
    
    # Initialize the array for storing subject code ids
    arrSubjectCodeIDs = []
    
    # Iterate through the subject list to build the array of subject code ids
    for objUserSubject in objUserSubjectList:
        arrSubjectCodeIDs.append(objUserSubject.subject.codeID)
    
    if len(arrSubjectCodeIDs) > 0:
        return arrSubjectCodeIDs
    
    # If no subjects are found, under user profile, 
    # then the content must be searched across all the 
    # subjects available in the system
    objCodeList = code.objects.filter(codeGroup = constants.mitraCodeGroup.subject)
    for objCode in objCodeList:
        arrSubjectCodeIDs.append(objCode.codeID)
        
    if len(arrSubjectCodeIDs) > 0:
        return arrSubjectCodeIDs
    
def getSearchContentApplicableGradeCodeIDs(gradeCodeIDs, objUser):
    # If gradeCodeIDs parameter is passed, split it into an array
    if gradeCodeIDs:
        arrGradeCodeIDs = gradeCodeIDs.split(',')
        return arrGradeCodeIDs
    
    # If gradeCodeIDs parameter is NOT passed, then fetch the grade set in profile info of that user
    objUserGradeList = userGrade.objects.filter(user = objUser)
    
    # Initialize the array for storing grade code ids
    arrGradeCodeIDs = []
    
    # Iterate through the grade list to build the array of grade code ids
    for objUserGrade in objUserGradeList:
        arrGradeCodeIDs.append(objUserGrade.grade.codeID)
    
    if len(arrGradeCodeIDs) > 0:
        return arrGradeCodeIDs
    
    # If no Grade are found, under user profile, 
    # then the content must be searched across all the 
    # Grade available in the system
    objCodeList = code.objects.filter(codeGroup = constants.mitraCodeGroup.grade)
    for objCode in objCodeList:
        arrGradeCodeIDs.append(objCode.codeID)
        
    if len(arrGradeCodeIDs) > 0:
        return arrGradeCodeIDs
    
def getSearchContentApplicableTopicCodeIDs(topicCodeIDs, objUser):
    # If topicCodeIDs parameter is passed, split it into an array
    if topicCodeIDs:
        arrTopicCodeIDs = topicCodeIDs.split(',')
        return arrTopicCodeIDs
    
    # If topicCodeIDs parameter is NOT passed, then fetch the topic set in profile info of that user
    objUserTopicList = userTopic.objects.filter(user = objUser)
    
    # Initialize the array for storing topic code ids
    arrTopicCodeIDs = []
    
    # Iterate through the grade list to build the array of topic code ids
    for objUserTopic in objUserTopicList:
        arrTopicCodeIDs.append(objUserTopic.topic.codeID)
    
    if len(arrTopicCodeIDs) > 0:
        return arrTopicCodeIDs
    
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
            objConfileName = content.objects.filter(contentID = objContent.contentID)
            return objConfileName[0].fileName
        # If response exists then update the response.
        objContentResponse.downloadCount += 1
        objContentResponse.save()
        # Get the content file name.
        objConfileName = content.objects.filter(contentID = objContent.contentID)
        return objConfileName[0].fileName
    
    # Content response for share.
    elif contentResponseType == constants.mitraCode.share :
                
        try:
            #Check response for content exists by respective user.
            objContentResponse = contentResponse.objects.get(content = objContent , user = objUser)
        except contentResponse.DoesNotExist:
        #If not exists then make entry for content response
            contentResponse(user = objUser , content = objContent , sharedCount = 1 ).save()
            # Get the content file name.
            objConfileName = content.objects.filter(contentID = objContent.contentID)
            return objConfileName[0].fileName
        # If response exists then update the response.
        objContentResponse.sharedCount += 1
        objContentResponse.save()
        # Get the content file name.
        objConfileName = content.objects.filter(contentID = objContent.contentID)
        return objConfileName[0].fileName

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
function to validate the youtube URL.
"""    
def validateYoutubeURL(url):
    try:
        # regex for validating youtube URL.
        youtube_regex = (
            r'(https?://)?(www\.)?'
        '(youtube|youtu|youtube-nocookie)\.(com|be)/'
        '(watch\?v=|embed/|v/|.+\?v=)?([^&=%\?]{11})')
    
        # Match youtube URL againest regex
        youtube_regex_match = re.match(youtube_regex, url)
        
        if youtube_regex_match:
            return youtube_regex_match.group(6)
          
        return youtube_regex_match
    except:
        return None