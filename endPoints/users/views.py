from django.contrib.auth.models import User
from users.models import user, userSubject, userSkill,userTopic,userGrade
from rest_framework import viewsets
from users.serializers import userSerializer
from rest_framework import viewsets,permissions
from rest_framework.decorators import list_route
from rest_framework.response import Response
from rest_framework import status
from commons.models import code
 
class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = user.objects.all().order_by('userName')
    serializer_class = userSerializer
 
    http_method_names = ['get', 'post']    
  
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def userProfileSave(self, request):            
        # Get input data
        phone_number = request.data.get('phoneNumber')
        userName_string = request.data.get('userName')
        photoUrl_string  = request.data.get('photoUrl')
        udiseCode_string = request.data.get('udiseCode')
        emailID_string = request.data.get('emailID')
        #preferredLanguageID_number = request.data.get('preferredLanguageID')
        districtID_number = request.data.get('district')
        subjectID_string = request.data.get('subjectCodeIDs')
        skillID_string = request.data.get('skillCodeIDs')
        topicID_string = request.data.get('topicCodeIDs')
        gradeID_string = request.data.get('gradeCodeIDs')

        # validate user information
#             user = userSerializer(data = request.data)
#             if not user.is_valid():
#                return Response(user.errors, status = status.HTTP_400_BAD_REQUEST)
        
        # Get existing user object.
        #userObj = user.objects.filter(phoneNumber=phone_number)
        userObj = user.objects.get(phoneNumber=phone_number)
        #Update the user profile
        userObj.userName = userName_string
        userObj.photoUrl = photoUrl_string
        userObj.udiseCode = udiseCode_string
        userObj.emailID = emailID_string
        #userObj.preferredLanguageID = preferredLanguageID_number
        userObj.districtID = districtID_number
#           userObj.modifiedBy = userObj.userID
        userObj.modifiedOn = datetime.now()
            
        # save it
        userObj.save()
        
        #Save user subject
        usrSubIbj = self.userSubjectSave(subjectID_string,userObj)
        # Save user skill
        self.userSkillSave(skillID_string,userObj)
        # save user grade.
        self.userTopicSave(topicID_string,userObj)
        # save user topics
        self.gradeID_string(gradeID_string,userObj)
        
        # Response
        response = request.data
        return Response({"message": "User profile saved successfully", "data": [response]})
            
    
    """
    API to save user subjects
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def userSubjectSave(self,subjectCodeIDs,userObj):
        if not subjectCodeIDs:
            return Response({"message": "subjects are not selected", "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # save the user subject.
        subjectList = subjectCodeIDs.split(',')
        for subject in subjectList:
             codeObj = code.objects.get(codeID=subject)
             usrSubject = userSubject(subjectCodeID= codeObj,userID=userObj)
             usrSubject.save()
        
        return Response({"message": "Subjects saved successfully", "data":[]})
    
    """
    API to save user Skill
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def userSkillSave(self,skillCodeIDs,userObj):
        if not skillCodeIDs:
            return Response({"message": "Skills are not selected", "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
            
        # save the user skills.
        skillList = skillCodeIDs.split(',')
        for skill in skillList:
             codeObj = code.objects.get(codeID=skill)
             usrSkill = userSkill(skillCodeID=codeObj,userID=userObj)
             usrSkill.save()
        
        return Response({"message": "Skills saved successfully", "data":[]})
            
    """
    API to save user topics
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def userTopicSave(self,topicCodeIDs,userObj):
        if not topicCodeIDs:
            return Response({"message": "Topic are not selected", "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        
        # save the user topics.
        topicList = topicCodeIDs.split(',')
        for topic in topicList:
             codeObj = code.objects.get(codeID=topic)
             usrTopic = userTopic(topicCodeID=codeObj,userID=userObj )
             usrTopic.save()
        
        return Response({"message": "Topic saved successfully", "data":[]})
    
    """
    API to save user Grade
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def userGradeSave(self,gradeCodeIDs,userObj):
        if not gradeCodeIDs:
            return Response({"message": "Grade are not selected", "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        
        # save the user Grade.
        gradeList = gradeCodeIDs.split(',')
        for grade in gradeList:
             codeObj = code.objects.get(codeID=grade)
             usrGrade = userGrade(gradeCodeID=codeObj,userid=userObj)
             usrGrade.save()
        
        return Response({"message": "Grade saved successfully", "data":[]})    