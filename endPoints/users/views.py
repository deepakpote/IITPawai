from django.utils.crypto import get_random_string
from rest_framework.decorators import detail_route, list_route
from rest_framework.response import Response
from rest_framework.authtoken.models import Token
from rest_framework import status
#from users.models import user 
from rest_framework import viewsets,permissions
from users.serializers import userSerializer, otpSerializer
from users.models import user, otp, token, userSubject, userSkill, userTopic, userGrade
from commons.models import code
from mitraEndPoints import constants
import random
import plivo

 
class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = user.objects.all().order_by('userName')
    serializer_class = userSerializer

    http_method_names = ['get', 'post']

    """
    API to send OTP
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def requestOtp(self,request):
        phoneNumber = request.data.get('phoneNumber')
        if not phoneNumber:
            return Response({"response_message": constants.messages.registration_phone_number_cannot_be_empty, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
            
        objOtp = otpSerializer(data = request.data)
        if not objOtp.is_valid():
            return Response({"response_message": constants.messages.registration_phone_number_is_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        generatedOTP = random.randint(100000, 999999)
        objOtp = otp(phoneNumber = phoneNumber, otp = generatedOTP)
        objOtp.save()
        
        # Send OTP SMS Call
        sendOtpSms(phoneNumber, generatedOTP, 101100)
        # make call to plivo here
        return Response({"response_message": constants.messages.success, "data":[]})

    """
    API to verify OTP
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def verifyOtp(self,request):
        # get inputs
        phoneNumber = request.data.get('phoneNumber')
        otp_string = request.data.get('otp')
        
        # Check if phone # is passed in post param
        if not phoneNumber:
            return Response({"response_message": constants.messages.registration_phone_number_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
        
        # Validate phone #
        objOtp = otpSerializer(data = request.data)
        if not objOtp.is_valid():
            return Response({"response_message": constants.messages.registration_phone_number_is_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)

        otpList = otp.objects.filter(phoneNumber=phoneNumber, otp=otp_string).first()
        if not otpList:
            return Response({"response_message": constants.messages.registration_otp_is_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        else:
            return Response({"response_message": constants.messages.success, "data": []})

    """
    API to register user
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def register(self,request):
        # Get input data
        phoneNumber = request.data.get('phoneNumber')
        otp_string = request.data.get('otp')

        # validate user information
        user = userSerializer(data = request.data)#, context={'request': request})
        if not user.is_valid():
            return Response(user.errors, status=status.HTTP_400_BAD_REQUEST)
        
        # validate OTP
        otpList = otp.objects.filter(phoneNumber = phoneNumber,otp = otp_string).first()
        if not otpList:
            return Response({"response_message": constants.messages.registration_otp_is_invalid, "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # If user information is valid, save it
        user.save()
        
        # Generate auth token for user
        tokenAreadyExists = True
        while tokenAreadyExists:
            # Generate token
            token_string = get_random_string(length = 32)#"qqqqwwwweeee" + str(user.instance.userID)
            
            # Check if that token is already taken up. 
            # If yes re-generate the token in while loop
            # If NO then the generated token is a good pick, break out of while loop
            if token.objects.filter(token = token_string).count() == 0:
                tokenAreadyExists = False
        
        # Save the auth generated token
        objToken = token(user=user.instance, token = token_string).save()
        
        # Add user data, along with the generated token to the response
        response = user.data
        response['token'] = token_string
        return Response({"response_message": constants.messages.success, "data": [response]})
    
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

#     @list_route(methods=['get','post'], permission_classes=[permissions.AllowAny])
#     def opentoAll(self,request):
#         return Response({"hello"})

#     @list_route(methods=['get','post'])
#     def myinfo(self,request):
#         print request.user
#         return Response(UserSerializer(request.user).data)
def sendOtpSms(recepientPhoneNumber, generatedOtp, languageCodeID):
    
    print ("Entered SMS OTP")
    #Verify the Plivo account
    objPlivo = plivo.RestAPI(constants.sms.authId, constants.sms.authToken)
    
    #Set the SMS Parameters
    params = {
        'src': constants.sms.srcPhoneNumber, # Sender's phone number with country code
        'dst' : recepientPhoneNumber, # Receiver's phone Number with country code
        'text' : constants.sms.registrationMessage + str(generatedOtp), # Your SMS Text Message - English
        'url' : "http://example.com/report/", # The URL to which with the status of the message is sent
        'method' : 'POST' # The method used to call the url
    }
        
    #Send SMS Call
    response = objPlivo.send_message(params)
    
    #Print Response
    print (response)
