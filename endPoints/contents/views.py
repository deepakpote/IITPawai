from rest_framework.decorators import detail_route, list_route
from rest_framework.response import Response
from rest_framework import status
from rest_framework import viewsets,permissions
from contents.serializers import contentSerializer

from contents.models import content
from commons.models import code
from users.models import userSubject, user, userGrade
from mitraEndPoints import constants

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
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def searchTeachingAid(self,request):
        # get inputs
#       contentType = request.data.get('contentTypeCodeID')
        userID = request.data.get('userID') 
        fileTypeCodeID = request.data.get('fileTypeCodeID')
        languageCodeID = request.data.get('languageCodeID')
        
        subjectCodeIDs = request.data.get('subjectCodeIDs') 
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        pageNumber = request.data.get('pageNumber')
                
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
         
        # Check if languageCodeID is passed in post param    
        if not languageCodeID:
            return Response({"response_message": constants.messages.teaching_aid_search_language_cannot_be_empty,
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
        
        #Get the query set using filter on filetype, subject, grade     
        contentQuerySet = content.objects.filter(language = languageCodeID,
                                                  fileType = fileTypeCodeID, 
                                                  subject__in = arrSubjectCodeIDs, 
                                                  grade__in = arrGradeCodeIDs).order_by('-contentID')[fromRecord:pageNumber]
        
        #Check for the no of records fetched.
        if not contentQuerySet:
            return Response({"response_message": constants.messages.teaching_aid_search_no_records_found,
                    "data": []},
                    status = status.HTTP_404_NOT_FOUND) 
        
        #Set query string to the contentSerializer
        objContentserializer = contentSerializer(contentQuerySet, many = True)
        
        #Set serializer data to the response 
        response = objContentserializer.data
        
        #Return the response
        return Response({"response_message": constants.messages.success, "data": response})
        
        
        
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
    