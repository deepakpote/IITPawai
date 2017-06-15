from django.utils.crypto import get_random_string
from rest_framework.decorators import detail_route, list_route
from rest_framework.response import Response
from rest_framework.authtoken.models import Token
from rest_framework import status
from django.core import signing
#from users.models import user 
from rest_framework import viewsets,permissions
from users.serializers import userSerializer, otpSerializer, userRoleSerializer

from rest_framework.permissions import IsAuthenticated
from users.authentication import TokenAuthentication
from users.models import user, otp, token, userSubject, userSkill, userTopic, userGrade, userAuth, device, userContent, role, userRole
from commons.models import code
from mitraEndPoints import constants , utils, settings 
import random
import plivo
import requests
import json
import base64
import os,time
import string
from django.db import connection
from datetime import datetime, timedelta
from django.utils import timezone
from contents.models import content , contentGrade
from contents.views import getSearchContentApplicableSubjectCodeIDs , getSearchContentApplicableGradeCodeIDs , getSearchContentApplicableTopicCodeIDs
from time import gmtime, strftime
from contents.serializers import contentSerializer , teachingAidSerializer , selfLearningSerializer
from commons.views import getCodeIDs, getArrayFromCommaSepString, getUserIDFromAuthToken
from contents.serializers import contentSerializer , teachingAidSerializer
from commons.views import getCodeIDs, getArrayFromCommaSepString, getUserIDFromAuthToken
from commons.models import code 
from pyfcm import FCMNotification



class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = user.objects.all().order_by('userName')
    serializer_class = userSerializer

    http_method_names = ['get', 'post']
    
    """
    API to send non data push, display, notification to all the devices registered in the system
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def sendDisplayNotificationsToAll(self,request):
        userTokenToVerify = request.data.get('utoken')
        result = []
        response_message = ""
        #Verify of the user token of the webPortal admin is matching with the one registered with the system 
        if(token.objects.filter(token=userTokenToVerify).exists()):
            #Fetch all the devices from usr_device table to send push notifications to
            sqlQuery = "select UD.* from usr_device UD join usr_user UU on UU.phoneNumber = UD.phoneNumber"
            objDevices = []
            objDevicesObjects = device.objects.raw(sqlQuery)
            for objDeviceInstance in objDevicesObjects:
                objDevices.append(objDeviceInstance.fcmDeviceID)
            api_key = constants.fcm.FCM_SERVERKEY
            push_service = FCMNotification(api_key=api_key)
            title = request.data.get('title')
            body = request.data.get('body')
            if(not title):
                title = constants.fcm.DATA_NOTIFICATION_TITLE
            if(not body):
                body = "Sample message body"
            #send push notifications to multiple registered devices at a time        
            result = push_service.notify_multiple_devices(registration_ids=objDevices,
                                                      message_title=title,
                                                      message_body=body)
            response_message = str(result['failure']) + " notifications failed, and " + str(result['success']) + " notification sent successfully."
        else:
            response_message = code.objects.get(codeID = constants.webportalmessages.web_admin_invalid_token).codeNameEn
        
        #print response_message
        #print result     
        return Response({"response_message": response_message, "data":result})
    
    """
    API to send data push notifications to all devices having FCM id
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def sendDataNotificationsToAll(self,request):
        userTokenToVerify = request.data.get('utoken')
        result = []
        response_message = ""
        #Verify of the user token of the webPortal admin is matching with the one registered with the system
        if(token.objects.filter(token=userTokenToVerify).exists()):
            #Fetch all the devices from usr_device table to send push notifications to
            sqlQuery = "select UD.* from usr_device UD join usr_user UU on UU.phoneNumber = UD.phoneNumber"
            objDevices = []
            objDevicesObjects = device.objects.raw(sqlQuery)
            for objDeviceInstance in objDevicesObjects:
                objDevices.append(objDeviceInstance.fcmDeviceID)
            api_key = constants.fcm.FCM_SERVERKEY
            push_service = FCMNotification(api_key=api_key)
            title = request.data.get('title')
            body = request.data.get('body')
            if(not title):
                title = constants.fcm.DATA_NOTIFICATION_TITLE
            if(not body):
                body = "Sample message body"
            data = {"title" : title,"body": body}
            #send push notifications to multiple registered devices at a time
            result = push_service.notify_multiple_devices(registration_ids=objDevices,
                                                      data_message=data)
            response_message = str(result['failure']) + " notifications failed, and " + str(result['success']) + " notification sent successfully."
        else:
            response_message = code.objects.get(codeID = constants.webportalmessages.web_admin_invalid_token).codeNameEn
        
        #print response_message
        #print result     
        return Response({"response_message": response_message, "data":result})
    
    """
    API to send OTP
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def validateWebAdmin(self,request):
        userPhoneNo = request.data.get('phoneno')
        userPassKey = request.data.get('passkey')
        result = []
        if(user.objects.filter(phoneNumber = userPhoneNo).exists()):
            #this is a hard coded passkey to be used for verifying the admin user for webPortal
            if userPassKey == constants.webportal.AdminPassword:
                response_message = code.objects.get(codeID = constants.webportalmessages.web_admin_valid_user).codeNameEn
                userObj = user.objects.filter(phoneNumber = userPhoneNo).first()
                userTokenObject = token.objects.get(user = userObj)
                result = {"error": "false", "usertoken":userTokenObject.token}
            else:
                response_message = code.objects.get(codeID = constants.webportalmessages.web_admin_invalid_pass_key).codeNameEn
                result = {"error":"true"}
        else:
            result = {"error":"true"}
            response_message = code.objects.get(codeID = constants.webportalmessages.web_admin_phoneno_not_registered).codeNameEn    
        return Response({"response_message": response_message, "data":result})

    """
    API to send OTP
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def requestOtp(self,request):
        phoneNumber = request.data.get('phoneNumber')
        authenticationType = request.data.get('authenticationType')
        
        if not phoneNumber:
            return Response({"response_message": constants.messages.registration_phone_number_cannot_be_empty, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
            
        objOtp = otpSerializer(data = request.data)
        if not objOtp.is_valid():
            return Response({"response_message": constants.messages.registration_phone_number_is_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # Check if authentication type is not empty
        if not authenticationType:
            return Response ({"response_message": constants.messages.authentication_type_cannot_be_empty, "data": []},
                            status=status.HTTP_406_NOT_ACCEPTABLE)
        
        # Check if the authentication type is 'Registration' or 'SignIn'
        isPhoneNumberRegistered = user.objects.filter(phoneNumber=phoneNumber).exists()
        
        if(isPhoneNumberRegistered and authenticationType == constants.authenticationTypes.registration):
            return Response({"response_message": constants.messages.registration_user_already_registered, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
            
        if (not isPhoneNumberRegistered and authenticationType == constants.authenticationTypes.signIn):
            return Response({"response_message": constants.messages.sign_in_user_not_registered, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        isOTPtimecorrect = None
        
        #Check, If Last OTP is sent before 3 min,then don't send the OTP,else,Send the OTP
        isOTPtimecorrect = checkOTPTimer(phoneNumber)
        
        if isOTPtimecorrect:
            print "OTP saved and sent"
            generatedOTP = random.randint(100000, 999999)
            objOtp = otp(phoneNumber = phoneNumber, otp = generatedOTP)
            objOtp.save()
            
            if authenticationType == constants.authenticationTypes.registration:
                otpMessage = constants.sms.registrationMessage
            else:
                otpMessage = constants.sms.signInMessage
            
            # Send OTP SMS Call
            sendOtpSms(phoneNumber, generatedOTP, constants.language.english, otpMessage)
        else:
            return Response({"response_message": constants.messages.sign_in_user_not_registered, "data": []},
                            status=status.HTTP_400_BAD_REQUEST)
        
        # make call to plivo here
        return Response({"response_message": constants.messages.success, "data":[]})

    """
    API to verify OTP
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def verifyOtp(self,request):
        # get inputs
        phoneNumber = request.data.get('phoneNumber')
        authenticationType = request.data.get('authenticationType')
        otp_string = request.data.get('otp')
        fcmDeviceID = request.data.get('fcmDeviceID')
        fcmRegistrationRequired = request.data.get('fcmRegistrationRequired')
        
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
            
        # Check if fcmRegistrationRequired # is passed in post param
        if not fcmRegistrationRequired:
            return Response({"response_message": constants.messages.registration_fcmRegistrationRequired_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Create object of common class 
        objCommon = utils.common()     
        objUser = None

        # Check value of registration is boolean or NOT.
        if not objCommon.isBool(fcmRegistrationRequired):
            return Response({"response_message": constants.messages.registration_fcmRegistrationRequired_value_must_be_boolean,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED)   
            
        #Get boolean value of fcmRegistrationRequired
        isfcmRegistrationRequired = objCommon.getBoolValue(fcmRegistrationRequired)
        
        if isfcmRegistrationRequired == True:
            # validate FCM device id. This is later on used for sending push notifications
            if not fcmDeviceID:
                return Response({"response_message": constants.messages.registration_fcm_device_id_cannot_be_empty, "data":[]},
                                status=status.HTTP_401_UNAUTHORIZED)
        
        # Check if authentication type is not empty
        if not authenticationType:
            return Response ({"response_message": constants.messages.authentication_type_cannot_be_empty, "data": []},
                            status=status.HTTP_406_NOT_ACCEPTABLE)
        
        # Check if user with given phone number exists or not
        isPhoneNumberRegistered = user.objects.filter(phoneNumber=phoneNumber).exists()
        if authenticationType == constants.authenticationTypes.registration:
            if isPhoneNumberRegistered:
                return Response({"response_message": constants.messages.registration_user_already_registered, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # If verification is for Sign In, then check if the user with given phone number is registered or not 
        if authenticationType == constants.authenticationTypes.signIn:
            if not isPhoneNumberRegistered:
                return Response({"response_message": constants.messages.sign_in_user_not_registered, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
                
        if isPhoneNumberRegistered:
            # validate user information
            try:
                objUser = user.objects.get(phoneNumber=phoneNumber)
            except user.DoesNotExist:
                return Response({"response_message": constants.messages.sign_in_user_not_registered,
                             "data": []},
                            status = status.HTTP_401_UNAUTHORIZED)
                
        
        # Check if the OTP is generated in the last 24 hours    
        currentDate = timezone.now()
        otpList = otp.objects.filter(phoneNumber=phoneNumber, otp=otp_string).order_by('-createdOn').first()
        if not otpList:
            return Response({"response_message": constants.messages.registration_otp_is_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # If OTP is generated before 24 hours, return error
        if(otpList.createdOn < (currentDate - timedelta(hours=constants.authenticationTypes.otpValidityHours))):
            return Response({"response_message": constants.messages.registration_otp_is_invalid, "data": []},
                        status=status.HTTP_401_UNAUTHORIZED)
        
        if isfcmRegistrationRequired == True and isPhoneNumberRegistered:
            #Finally save the user device id, required for push notifications
            userDeviceSave(objUser, fcmDeviceID)
        
        # For registration call, do not send auth token
        if authenticationType == constants.authenticationTypes.registration:
            return Response({"response_message": constants.messages.success, "data": []})
        
        # For sign-in call, send auth token in response
        if authenticationType == constants.authenticationTypes.signIn:   
            objSignedInUser = user.objects.get(phoneNumber = phoneNumber)
            objToken = token.objects.get(user = objSignedInUser)
            response = { 'userID' : objSignedInUser.userID,
                        'token' : objToken.token }
            return Response({"response_message": constants.messages.success, "data": [response]})
        
    """
    API to set user password.
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def setPassword(self,request):
        # Get input data
        password = request.data.get('password') 
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        # validate user information
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.setPassword_user_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
            
        # validate user information
        try:
            objToken = token.objects.get(user = objUser)
        except token.DoesNotExist:
            return Response({"response_message": constants.messages.setPassword_user_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
            
        # Password must contain six character and not more then 255 character.    
        if ( (len(password) < 6) or (len(password) > 16)):
            return Response({"response_message": constants.messages.setPassword_password_cannot_be_empty_it_must_be_greater_then_six_characters_and_lessThen_16_characters,
                         "data": []},
                        status = status.HTTP_401_UNAUTHORIZED)
        
        # Check for white space in password.
        if ' ' in password:
            return Response({"response_message": constants.messages.setPassword_password_should_not_contain_space,
                         "data": []},
                        status = status.HTTP_401_UNAUTHORIZED)
        
        # Encrypt password.
        encrptedPassword = signing.dumps({settings.SECRET_KEY  : password })
        
        # If user valid, update user password.
        token.objects.filter(token = authToken , user = objUser).update(
                                                                        password = encrptedPassword.strip()
                                                                       )

        return Response({"response_message": constants.messages.success, "data": []})
    
    """
    API to sign-in to web portal.
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny],authentication_classes = [TokenAuthentication])
    def webSignIn(self,request):
        # get inputs
        phoneNumber = request.data.get('phoneNumber')
        password = request.data.get('password')

        # Check if phoneNumber is passed in post param
        if not phoneNumber:
            return Response({"response_message": constants.messages.webSignIn_phone_number_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if password is passed in post param
        if not password:
            return Response({"response_message": constants.messages.webSignIn_password_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)          
                 
        # Check if phone number exists.
        objUser = user.objects.filter(phoneNumber = phoneNumber).first()
        if not objUser:
            return Response({"response_message": constants.messages.webSignIn_phone_number_is_invalid, "data": []},
                    status=status.HTTP_401_UNAUTHORIZED)

        # authenticate user phoneNumber and password.
        try:
            #authResponse = userAuth.objects.get(loginID = phoneNumber , password = password.strip())
            authResponse = token.objects.get(user = objUser)
        except token.DoesNotExist:
            return Response({"response_message": constants.messages.webSignIn_invalid_credentials,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
            
        try:
            dbPassword = signing.loads(authResponse.password)
        except (BadSignature, SignatureExpired):
            return Response({"response_message": constants.messages.webSignIn_invalid_credentials,
                         "data": []},
                        status = status.HTTP_401_UNAUTHORIZED)
            
        if password != dbPassword[settings.SECRET_KEY]:
            return Response({"response_message": constants.messages.webSignIn_invalid_credentials,
                         "data": []},
                        status = status.HTTP_401_UNAUTHORIZED)
            
        # Add user token to the response.
        response = { 'token' : authResponse.token }
            
        return Response({"response_message": constants.messages.success, "data": [response]})

    """
    API to sign in with google from app
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def signInWithGoogle(self,request):
        googleToken = request.data.get('googleToken')
        fcmDeviceID = request.data.get('fcmDeviceID')
        fcmRegistrationRequired = request.data.get('fcmRegistrationRequired')
        email = get_email_from_google_token(googleToken)
        if not email:
            return Response({"response_message": constants.messages.registration_user_validation_failed, "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)
            
        # Check if fcmRegistrationRequired # is passed in post param
        if not fcmRegistrationRequired:
            return Response({"response_message": constants.messages.registration_fcmRegistrationRequired_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Create object of common class 
        objCommon = utils.common()     

        # Check value of registration is boolean or NOT.
        if not objCommon.isBool(fcmRegistrationRequired):
            return Response({"response_message": constants.messages.registration_fcmRegistrationRequired_value_must_be_boolean,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED)
            
                #Get boolean value of fcmRegistrationRequired
        isfcmRegistrationRequired = objCommon.getBoolValue(fcmRegistrationRequired)
        
        if isfcmRegistrationRequired == True:
            # validate FCM device id. This is later on used for sending push notifications
            if not fcmDeviceID:
                return Response({"response_message": constants.messages.registration_fcm_device_id_cannot_be_empty, "data":[]},
                                status=status.HTTP_401_UNAUTHORIZED)

        userObject = user.objects.filter(emailID = email).first()
        
        if userObject:
            # user already present. similar to verifyotp sign in - send user id and token
            objToken = token.objects.filter(user = userObject).first()
            if not objToken:
                # strange case. user is present without a corresponding token. create token in that case.
                # no real need to check for duplicates in token generation since field is unique
                token_string = get_random_string(length = 32)
                objToken = token(user=userObject, token = token_string).save()
    
            if isfcmRegistrationRequired == True:
                #Finally save the user device id, required for push notifications
                userDeviceSave(userObject, fcmDeviceID)
            
            return Response({"response_message": constants.messages.success,
                     "data": [{'userID': userObject.userID, 'token' : objToken.token }]}
                    )
        else:
            #user not present. 
            return Response({"response_message": constants.messages.sign_in_user_not_registered,
                         "data": []})

    """
    API to register user
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def register(self,request):
        # Get input data
        phoneNumber = request.data.get('phoneNumber')
        otp_string = request.data.get('otp')
        department = request.data.get('department')
        userType = request.data.get('userType')
        fcmDeviceID = request.data.get('fcmDeviceID')
        fcmRegistrationRequired = request.data.get('fcmRegistrationRequired')
        email = None

        # validate user information 
        objUserSerializer = userSerializer(data = request.data)#, context={'request': request})
        if not objUserSerializer.is_valid():
            return Response({"response_message":" constants.messages.registration_user_validation_failed", "data":[ objUserSerializer.errors]},
                            status=status.HTTP_401_UNAUTHORIZED)
        if phoneNumber:
            if user.objects.filter(phoneNumber = phoneNumber).exists():
                return Response({"response_message": constants.messages.register_user_phonenumber_already_registered,
                                 "data": []},
                                status = status.HTTP_401_UNAUTHORIZED)
    
        #check user type if department is provided
        if department:
            if userType != constants.userType.Officer:
                return Response({"response_message": constants.messages.registration_user_validation_failed, "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)
                
                # Check if fcmRegistrationRequired # is passed in post param
        if not fcmRegistrationRequired:
            return Response({"response_message": constants.messages.registration_fcmRegistrationRequired_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Create object of common class 
        objCommon = utils.common()     
        objUser = None

        # Check value of registration is boolean or NOT.
        if not objCommon.isBool(fcmRegistrationRequired):
            return Response({"response_message": constants.messages.registration_fcmRegistrationRequired_value_must_be_boolean,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED)   
            
        #Get boolean value of fcmRegistrationRequired
        isfcmRegistrationRequired = objCommon.getBoolValue(fcmRegistrationRequired)
        
        if isfcmRegistrationRequired == True:
            # validate FCM device id. This is later on used for sending push notifications
            if not fcmDeviceID:
                return Response({"response_message": constants.messages.registration_fcm_device_id_cannot_be_empty, "data":[]},
                                status=status.HTTP_401_UNAUTHORIZED)
                 
        
        # check that either valid otp and phone number or valid oauth token is provided.
        otpList = otp.objects.filter(phoneNumber = phoneNumber,otp = otp_string).first()
        if not otpList:
            googleToken = request.data.get('googleToken')
            email = get_email_from_google_token(googleToken)
            objUserSerializer.emailID = email
            #print objUserSerializer.emailID
            if not email:
                #neither otp+phone number nor google token is provided
                return Response({"response_message": constants.messages.registration_user_validation_failed, "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)

        # If user information is valid, save it
        objCreatedUser = objUserSerializer.save()
        
        # set email id for newly created user
        objCreatedUser.emailID = email
        objCreatedUser.save()
        
        #Once user is saved, update it's created by and modified by fields.
        #objCreatedUser = user.objects.get(userID = objUserSerializer.userID)
        objCreatedUser.createdBy = objCreatedUser
        objCreatedUser.modifiedBy = objCreatedUser
        objCreatedUser.save()
        
        #if registred user is a Teacher, then add role "Teacher"
        #if objCreatedUser.userType.codeID == constants.mitraCode.userType_teacher or objCreatedUser.userType.codeID == constants.userType.Officer or objCreatedUser.userType.codeID == constants.userType.Other or objCreatedUser.userType.codeID == constants.userType.CenterHead:
        # get teacher role.
        objTeacherRole = role.objects.get(roleID = constants.role.teacher)
        userRole(user = objCreatedUser , role = objTeacherRole ).save()
        
        #Save user subject
        subjectCodeIDs = request.data.get('subjectCodeIDs')
        userSubjectSave(subjectCodeIDs, objUserSerializer.instance)
        
        # Save user skill
        skillCodeIDs = request.data.get('skillCodeIDs')
        userSkillSave(skillCodeIDs, objUserSerializer.instance)
         
        # save user grade.
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        userGradeSave(gradeCodeIDs, objUserSerializer.instance)
         
        # save user topics
        topicCodeIDs  = request.data.get('topicCodeIDs')
        userTopicSave(topicCodeIDs, objUserSerializer.instance)
#         
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
        objToken = token(user=objUserSerializer.instance, token = token_string).save()
        
        if isfcmRegistrationRequired == True:
            #Finally save the user device id, required for push notifications
            userDeviceSave(objCreatedUser, fcmDeviceID)
        
        # Add user data, along with the generated token to the response
        #response = objUserSerializer.data
        response = { 'userID' : objCreatedUser.userID }  #objCreatedUser.userID
        response['token'] = token_string
        return Response({"response_message": constants.messages.success, "data": [response]})

    """
    API to set email to already existing user who had originally registered with phone number
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def setNewEmail(self,request):
        
        authToken = request.META.get('HTTP_AUTHTOKEN')
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        # validate user information
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.update_profile_user_not_exists,
                                "data": []},
                                status = status.HTTP_404_NOT_FOUND)

        googleToken = request.data.get('googleToken')
        email = get_email_from_google_token(googleToken)
        if not email:
            return Response({"response_message": constants.messages.registration_user_validation_failed, "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)

        # TODO:
        # send proper message for email that already exists for somebody else

        if user.objects.filter(emailID = email).exclude(userID=userID).exists():
            return Response({"response_message": constants.messages.setNewEmail_user_emailID_already_registered, "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)
        else:
            objUser.emailID = email
            objUser.save()
            return Response({"response_message": constants.messages.success, "data": []})
        

        

    
    """
    API to update user profile.
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def updateProfile(self,request):
        # Get input data
        phoneNumber = request.data.get('phoneNumber') 
        emailID = request.data.get('emailID') 
        userName = request.data.get('userName')
        udiseCode = request.data.get('udiseCode')
        userTypeCodeID = request.data.get('userTypeCodeID')
        preferredLanguageCodeID = request.data.get('preferredLanguageCodeID') 
        districtCodeID = request.data.get('districtCodeID') 
        authToken = request.META.get('HTTP_AUTHTOKEN')
        department = request.data.get('department')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        # validate user information
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.update_profile_user_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
        
        objDepartment = None
        
        #Check department is passed.
        if department:
            #Allow to update the department when the user type is officer.
            if userTypeCodeID == constants.userType.Officer:
                objDepartment = department
            else:
                return Response({"response_message": constants.messages.update_profile_user_validation_failed, "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)
                
        if phoneNumber:
            if user.objects.filter(phoneNumber = phoneNumber).exclude(userID = userID).exists():
                return Response({"response_message": constants.messages.register_user_phonenumber_already_registered,
                             "data": []},
                            status = status.HTTP_401_UNAUTHORIZED)
        
        # If user valid, update the details.
        user.objects.filter(userID = userID).update(userName = userName , 
                                                               phoneNumber = phoneNumber ,
                                                               udiseCode = udiseCode , 
                                                               userType = userTypeCodeID , 
                                                               preferredLanguage = preferredLanguageCodeID , 
                                                               district = districtCodeID ,
                                                               department = objDepartment,
                                                               modifiedBy = userID)
        
        #Save user subject
        subjectCodeIDs = request.data.get('subjectCodeIDs')
        userSubjectSave(subjectCodeIDs, objUser)
        
        # Save user skill
        skillCodeIDs = request.data.get('skillCodeIDs')
        userSkillSave(skillCodeIDs, objUser)
         
        # save user grade.
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        userGradeSave(gradeCodeIDs, objUser)
         
        # save user topics
        topicCodeIDs  = request.data.get('topicCodeIDs')
        userTopicSave(topicCodeIDs, objUser)
#         
        return Response({"response_message": constants.messages.success, "data": []})

# Login API is no more in use. So commented.    
#     """
#     API to login
#     """
#     @list_route(methods=['post'], permission_classes=[permissions.AllowAny],authentication_classes = [TokenAuthentication])
#     def login(self,request):
#         # get inputs
#         phoneNumber = request.data.get('phoneNumber')
#         authtoken = request.data.get('token')
#         fcmDeviceID = request.data.get('fcmDeviceID')
#         
#         # Check if phoneNumber is passed in post param
#         if not phoneNumber:
#             return Response({"response_message": constants.messages.registration_phone_number_cannot_be_empty,
#                              "data": []},
#                              status = status.HTTP_401_UNAUTHORIZED)
#             
#         # Check if token is passed in post param
#         if not authtoken:
#             return Response({"response_message": constants.messages.login_token_cannot_be_empty,
#                              "data": []},
#                              status = status.HTTP_401_UNAUTHORIZED) 
#         
#         # validate FCM device id. This is later on used for sending push notifications
#         if not fcmDeviceID:
#             return Response({"response_message": constants.messages.signin_fcm_device_id_cannot_be_empty, "data":[]},
#                             status=status.HTTP_401_UNAUTHORIZED)           
#                  
#         # Check if phone number exists.
#         objUser = user.objects.filter(phoneNumber = phoneNumber).first()
#         if not objUser:
#             return Response({"response_message": constants.messages.registration_phone_number_is_invalid, "data": []},
#                     status=status.HTTP_401_UNAUTHORIZED)
# 
#         
#         #authenticate user with it's token.
#         tokenList = token.objects.filter(user = objUser , token = authtoken).first()
#         
#         # Check authentication result.
#         if not tokenList:
#             return Response({"response_message": constants.messages.login_user_token_invalid, "data": []},
#                             status=status.HTTP_401_UNAUTHORIZED)
#         
#         # Fetch userAuth for respective phoneNumber
#         objuserAuth = userAuth.objects.filter(loginID = phoneNumber).first()
#         
#         # If first time login then make entry into the userAuth
#         if not objuserAuth:
#             userAuth(loginID = phoneNumber, authToken = authtoken, createdBy = objUser , modifiedBy = objUser).save()  
#         else:
#             #Update the lastLoggedInOn and modifiedOn
# #             objuserAuth.lastLoggedInOn = datetime.datetime.now()
# #             objuserAuth.modifiedOn = datetime.datetime.now()
#             objuserAuth.save()
#         
#         #Finally save the user device id, required for push notifications
#         userDeviceSave(objUser, fcmDeviceID)
#         
#         return Response({"response_message": constants.messages.success, "data": []})

    """
    APT to get user details
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def detail(self,request):
        """ Get user details
            args:
                request : passed userid as parameter to the request object
            returns:
                Response: response_message and user details
        """
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
         
        # get user info
        userInfo = user.objects.filter(userID = userID).first()
        # If userID parameter is passed, then check user is exists or not
        if not userInfo:
            return Response({"response_message": constants.messages.user_userprofile_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)

        #Set query string to the userSerializer
        objUserSerializer = userSerializer(userInfo)
    
        #Set serializer data to the response 
        response = objUserSerializer.data
        
        # Create object of common class 
        objCommon = utils.common()
        
        if  response["photoUrl"]:
            response["photoUrl"] = objCommon.getBaseURL(constants.staticFileDir.userDir) + str(response["photoUrl"])

        userSubjectCodeID = getUserSubjectCode(userInfo)
        userGradeCodeID = getUserGradeCode(userInfo)
        userTopicCodeID = getUsertopicCode(userInfo)
        userSkillCodeID = getUserSkillCode(userInfo)

        response["subjectCodeIDs"] = userSubjectCodeID if userSubjectCodeID else None
        response["gradeCodeIDs"] = userGradeCodeID if userGradeCodeID else None
        response["topicCodeIDs"] = userTopicCodeID if userTopicCodeID else None
        response["skillCodeIDs"] = userSkillCodeID if userSkillCodeID else None

        return Response({"response_message": constants.messages.success, "data": [response]})
    """
    API to save user content
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def contentSave(self,request):
        """Save user content
        args:
            request : passed userID,contentID as parameter to request object
        returns:
            Response: Save the user content detail

        """
        contentID = request.data.get("contentID")
        saveContent = request.data.get("saveContent")
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        # check userID is passed as parameter in post
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)

        # check contentID is passed as parameter in post    
        if not contentID:
            return Response({"response_message": constants.messages.save_usercontent_contentid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
                # Create object of common class 
        objCommon = utils.common()
        
        # Check value of saveContent is boolean or NOT.
        if not objCommon.isBool(saveContent):
            return Response({"response_message": constants.messages.save_like_hasLike_value_must_be_boolean,
                            "data": []},
                            status = status.HTTP_401_UNAUTHORIZED)    

        # If contentID parameter is passed, then check content exists or not
        try:
            objContent = content.objects.get(contentID = contentID)
        except content.DoesNotExist:
            return Response({"response_message": constants.messages.save_usercontent_content_id_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.save_usercontent_user_id_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)


            
        if objCommon.getBoolValue(saveContent) == True:
            # If userIDis allready present in userContent
            objUserContentUserInfo = userContent.objects.filter(user = objUser, content = objContent)
            
            if objUserContentUserInfo:
                return Response({"response_message": constants.messages.save_usercontent_user_id_allready_exists,
                         "data": []},
                        status = status.HTTP_304_NOT_MODIFIED)
            
            # Save user content detail
            userContent(user = objUser,content = objContent).save()
        else:
            # saveContent == False mean delete the content.
            contentDelete( objUser.userID , objContent.contentID)
            

        return Response({"response_message": constants.messages.success, "data": []})

    """
    API to get user content list
    """
    @list_route(methods=['POST'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication] )
    def contentList(self,request):
        """ Get the usercontent list
        args:
            request: passed userID and contentTypeCodeID as parameter
        returns:
            Response: list of content

        """
        contentTypeCodeID = request.data.get("contentTypeCodeID")
        
        subjectCodeIDs = request.data.get('subjectCodeIDs') 
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        fileTypeCodeIDs = request.data.get('fileTypeCodeIDs')
        
        topicCodeIDs = request.data.get('topicCodeIDs') 
        languageCodeIDs = request.data.get('languageCodeIDs')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)

        # check userID is passed as parameter 
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
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

        # check contentID is passed as parameter in post    
        if not contentTypeCodeID:
            return Response({"response_message": constants.messages.usercontent_list_contenttype_codeid_cannnot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)

        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.usercontent_list_user_id_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)

        # If contentTypeCodeID parameter is passed, then check user exists or not in com_code
        try:
            objContentTypeCodeID = code.objects.get(codeID = contentTypeCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.usercontent_list_contenttype_code_id_does_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                    
        # Get list of contentIDs of login user
        objUserContent = list(userContent.objects.filter(user = objUser).values_list('content_id',flat = True))
        #print "objUserContent:",objUserContent
        
        # Declare empty user content type code.
        objUserContentTypeCode = None
        
        # Create object of common class
        objCommon = utils.common()
        
        #If content type is Teaching Aids.
        if contentTypeCodeID == constants.mitraCode.teachingAids:
                   
            #Get the applicable subject list for the respective user.    
            arrSubjectCodeIDs = getSearchContentApplicableSubjectCodeIDs(subjectCodeIDs)  
            
            arrSubjectCodeIDs = tuple(map(int, arrSubjectCodeIDs))

            if len(arrSubjectCodeIDs) == 1:
                arrSubjectCodeIDs =  '(%s)' % ', '.join(map(repr, arrSubjectCodeIDs))
                
            #Get the applicable grade list for the respective user.
            arrGradeCodeIDs = getSearchContentApplicableGradeCodeIDs(gradeCodeIDs)
            
            arrGradeCodeIDs = tuple(map(int, arrGradeCodeIDs))
        
            if len(arrGradeCodeIDs) == 1:
                arrGradeCodeIDs =  '(%s)' % ', '.join(map(repr, arrGradeCodeIDs))
                
            #Get correct/valid FileTypeCodeID 
            arrContentFileTypeCodeID = []
            if not fileTypeCodeIDs:
                # Get codeIDs related to filetype codegroup
                arrContentFileTypeCodeID = getCodeIDs(constants.mitraCodeGroup.fileType)
            else:
                #Get the array from comma sep string of fileTypeCodeIDs.
                arrContentFileTypeCodeID = getArrayFromCommaSepString(fileTypeCodeIDs)
                
            arrContentFileTypeCodeID = tuple(map(int, arrContentFileTypeCodeID))
        
            #If the length of filetypecodeID is 1 then remove last comma.
            if len(arrContentFileTypeCodeID) == 1:
                arrContentFileTypeCodeID =  '(%s)' % ', '.join(map(repr, arrContentFileTypeCodeID))
                
            #Get the applicable content for GradeCodeID.
            #arrContentID = list(contentGrade.objects.filter(grade__in = arrGradeCodeIDs).values_list('content',flat = True).distinct())
       
            # Connection
            cursor = connection.cursor()  
            
            
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
                                                group_concat(CG.gradeCodeID) as gradeCodeIDs,
                                                CC.createdOn,
                                                CC.modifiedOn,
                                                CC.chapterID
                                                from con_content CC 
                                                INNER JOIN con_contentGrade CG ON CC.contentID = CG.contentID 
                                                INNER JOIN usr_userContent UC ON CC.contentID = UC.contentID
                                                INNER JOIN con_contentDetail CCG ON CC.contentID = CCG.contentID
                                                where UC.userID = %s
                                                and CC.fileTypeCodeID IN %s 
                                                and CC.contentTypeCodeID = %s 
                                                and CC.subjectCodeID IN %s 
                                                and CG.gradeCodeID IN %s 
                                                and CCG.appLanguageCodeID = %s
                                                group by CC.contentID, CCG.contentTitle, CC.requirement, CCG.instruction, CC.fileName, CCG.author, CC.objectives, CC.contentTypeCodeID, CC.fileTypeCodeID, CC.languageCodeID, CC.subjectCodeID,
                                                CC.topicCodeID order by CC.contentID"""%(userID,arrContentFileTypeCodeID,107100,str(arrSubjectCodeIDs),str(arrGradeCodeIDs),appLanguageCodeID)
                                          
            cursor.execute(searchTeachingAidQuery)
            
            #Queryset
            contentQuerySet = cursor.fetchall()
            
            response_data = []
        
            for item in contentQuerySet:
                objResponse_data = {
                                        'contentID':    item[0], 
                                        'contentTitle': item[1], 
                                        'requirementCodeIDs':  item[2],
                                        'instruction':  item[3],
                                        'fileName':     item[4],
                                        'author':       item[5],
                                        'objectives' :  item[6],
                                        'contentType':  item[7],
                                        'fileType' :    item[8],
                                        'language':     item[9],
                                        'subject':      item[10],
                                        'topic' :       item[11],
                                        'gradeCodeIDs': str(item[12]),
                                        'createdOn':    item[13],
                                        'modifiedOn':   item[14],
                                        'chapterID':    item[15]
                                        }   
                response_data.append(objResponse_data)

            objUserContentTypeCode = response_data
            
        elif contentTypeCodeID == constants.mitraCode.selfLearning:
            #Get the applicable topic list for the respective user.    
            arrTopicCodeIDs = getSearchContentApplicableTopicCodeIDs(topicCodeIDs) 
            
            arrTopicCodeIDs = tuple(map(int, arrTopicCodeIDs))
         
         
            if len(arrTopicCodeIDs) ==1:
                arrTopicCodeIDs =  '(%s)' % ', '.join(map(repr, arrTopicCodeIDs)) 

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
            
            #Build queryset               
#             objUserContentTypeCode = content.objects.filter(contentType = objContentTypeCodeID, 
#                                                             contentID__in = objUserContent,
#                                                             topic__in = arrTopicCodeIDs,
#                                                             language__in = arrLanguageCodeID)
            # New code.
            # Connection
            cursor = connection.cursor()  
             
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
                                                CC.topicCodeID,
                                                CC.createdOn,
                                                CC.modifiedOn
                                                from con_content CC 
                                                INNER JOIN con_contentDetail CCG ON CC.contentID = CCG.contentID
                                                INNER JOIN usr_userContent UC ON CC.contentID = UC.contentID
                                                where UC.userID = %s
                                                and CC.languageCodeID IN %s 
                                                and CCG.appLanguageCodeID = %s 
                                                and CC.contentTypeCodeID = %s 
                                                and CC.topicCodeID IN %s 
                                                order by CC.contentID """%(userID,arrLanguageCodeID,appLanguageCodeID,constants.mitraCode.selfLearning,str(arrTopicCodeIDs))
     
             
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
        if not objUserContentTypeCode and not response_data:
            return Response({"response_message": constants.messages.usercontent_list_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK) 
            
        #If content type is Teaching Aids.
        if contentTypeCodeID == constants.mitraCode.teachingAids:
            # Call to the custome ContentSerializer
            objContentSerializer = teachingAidSerializer(objUserContentTypeCode, many = True)
        elif contentTypeCodeID == constants.mitraCode.selfLearning:
            # Call to the contentSerializer
            objContentSerializer = selfLearningSerializer(response_data, many = True)
            

        #Set objContentSerializer data to response
        response = objContentSerializer.data

        return Response({"response_message": constants.messages.success, "data": response})
    
    """
    API to update user language.
    """
    
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def saveLanguage(self,request):
        # Get input data
        preferredLanguageCodeID = request.data.get('preferredLanguageCodeID') 
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        # check userID is passed as parameter 
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)

        # check preferredLanguageCodeID is passed as parameter in post    
        if not preferredLanguageCodeID:
            return Response({"response_message": constants.messages.userlanguage_save_languagecode_id_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
      
        # validate user information
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.userlanguage_save_user_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
        
        # If user valid, update the details.
        user.objects.filter(userID = userID).update(preferredLanguage = preferredLanguageCodeID)
        
        # Return the success response.
        return Response({"response_message": constants.messages.success, "data": []})
    
    """
    APT to save user photo
    """
    @list_route(methods = ['POST'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def saveUserPhoto(self,request):
        """ save photo url of user 
        args:
            request : userID and image byte array passed as parameter
        returns:
            response : photo url successfully saved in database
        """
        byteArrayData = request.data.get('byteArray')
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)

        # fileName = None
        # check userID is passed as parameter in post method
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)

        # check image byteArray is passed as parameter in posty method
        if not byteArrayData:
            return Response({"response_message": constants.messages.user_uploadphoto_bytearray_data_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)

        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.user_uploadphoto_user_does_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)

        # Decode image byte array data 
        result = base64.b64decode(byteArrayData)
        
        # Set the base dirceory with static folder in app
        baseDir = constants.imageDir.baseDir
        
        # Get current date and time to set name of image file 
        objCurrentDateTime = strftime("%y%m%d%H%M%S", time.localtime())
        fileName = str(userID) + "_" + objCurrentDateTime + ".png"

        # Set folder path and file name of image
        completeFileName = str(baseDir) + fileName

        with open(completeFileName, 'wb') as f:
            f.write(result)

        # Get the cuerent image file of user
        objUserPhotoUrl = str(objUser.photoUrl)

        if len(objUserPhotoUrl) > 0:
            objUserPhotoUrlDelete =  baseDir + objUserPhotoUrl

        # If image file of user exist in directory delete it before update entry of photoUrl for user
        if os.path.isfile(objUserPhotoUrlDelete):
            os.unlink(objUserPhotoUrlDelete)

        user.objects.filter(userID = userID).update(photoUrl = fileName)

        return Response({"response_message": constants.messages.success, "data": []})
    
    """
    Get user's roles
    """   
    @list_route(methods=['GET'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def getUserRoleList(self, request):
        authToken = request.META.get('HTTP_AUTHTOKEN')

        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.userRole_list_user_id_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
    
        # check userID exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.userRole_list_user_does_not_exist, 
                             "data": []}, status = status.HTTP_404_NOT_FOUND)
        
        userRoleQuerySet = userRole.objects.filter(user = objUser)
        
        if not userRoleQuerySet:
            return Response({"response_message": constants.messages.userRole_list_no_records_found,
                    "data": []},
                    status = status.HTTP_200_OK)
                     
        serializer = userRoleSerializer(userRoleQuerySet, many = True)
        
        return Response({"response_message": constants.messages.success, "data": serializer.data})


#     @list_route(methods=['get','post'], permission_classes=[permissions.AllowAny])
#     def opentoAll(self,request):
#         return Response({"hello"})

#     @list_route(methods=['get','post'])
#     def myinfo(self,request):
#         print request.user
#         return Response(UserSerializer(request.user).data)

"""
API to delete userContent
"""
def contentDelete(userID , contentID):
    """ Delete userContent
    args:
        request: passed userID and contentID as parameter
    returns:
        response: user content deleteed successfully
    """

    # check userID is passed as parameter in post
    if not userID:
        return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                         "data": []},
                         status = status.HTTP_401_UNAUTHORIZED)

    # check contentID is passed as parameter in post    
    if not contentID:
        return Response({"response_message": constants.messages.usercontent_delete_contentid_cannot_be_empty,
                         "data": []},
                         status = status.HTTP_401_UNAUTHORIZED)

    # If contentID and userID parameters are passed, then check content exists or not in userContent table
    try:
        objUserContentID = userContent.objects.get(content = contentID, user = userID)
    except userContent.DoesNotExist:
        return Response({"response_message": constants.messages.usercontent_delete_userid_and_contentid_does_not_exists,
                 "data": []},
                status = status.HTTP_404_NOT_FOUND)

    # Delete user content based on userID and contentID
    userContent.objects.get(user = userID, content = contentID).delete()

    return

   
def sendOtpSms(recepientPhoneNumber, generatedOtp, languageCodeID, otpMessage):
    print ("Entered SMS OTP")
    
    # If send sms is set to TRUE, only then continue sending sms. 
    # Else Skip sending SMS. 
    # Send SMS must be set to TRUE ONLY on LIVE site.
    if not constants.sms.sendSMS:
        print 'Send SMS functionality is disabled for this site. No SMS will be sent'
        return
    
    #Verify the Plivo account
    objPlivo = plivo.RestAPI(constants.sms.authId, constants.sms.authToken)
    
    #Set the SMS Parameters
    params = {
        'src': constants.sms.srcPhoneNumber, # Sender's phone number with country code
        'dst' : recepientPhoneNumber, # Receiver's phone Number with country code
        'text' : otpMessage + str(generatedOtp), # Your SMS Text Message - English
        'url' : "http://example.com/report/", # The URL to which with the status of the message is sent
        'method' : 'POST' # The method used to call the url
    }
        
    #Send SMS Call
    response = objPlivo.send_message(params)
    
    #Print Response
    print (response)
"""
Function to save the user subjects.
"""
def userSubjectSave(subjectCodeIDs, objUser):
    # Delete all the subjects of respective user from userSubject.
    userSubject.objects.filter(user = objUser).delete()
    
    if not subjectCodeIDs:
        return
    # save the user subject.
    subjectCodeList = subjectCodeIDs.split(',')
    for subjectCodeID in subjectCodeList:
         objCode = code.objects.get(codeID = subjectCodeID)
         userSubject(subject = objCode, user = objUser).save()
    
    return
"""
Function to save the user skills.
"""
def userSkillSave(skillCodeIDs, userObj):
    # Delete all the skills of respective user from userSkill.
    userSkill.objects.filter(user = userObj).delete()
    
    if not skillCodeIDs:
        return
        
    # save the user skills.
    skillCodeList = skillCodeIDs.split(',')
    for skillCodeID in skillCodeList:
         objCode = code.objects.get(codeID = skillCodeID)
         userSkill(skill = objCode, user = userObj).save()
    
    return
"""
Function to save the user topics.
"""
def userTopicSave(topicCodeIDs, userObj):
        # Delete all the topics of respective user from userTopic.
    userTopic.objects.filter(user = userObj).delete()
    
    if not topicCodeIDs:
        return
        
    # save the user topics.
    topicCodeList = topicCodeIDs.split(',')
    for topicCodeID in topicCodeList:
         objCode = code.objects.get(codeID=topicCodeID)
         userTopic(topic = objCode, user = userObj ).save()
    
    return
"""
Function to save the user grades.
"""
def userGradeSave(gradeCodeIDs , userObj): 
    # Delete all the grades of respective user from userGrade.
    userGrade.objects.filter(user = userObj).delete()
    
    if not gradeCodeIDs:
        return 
    
    # save the user Grade.
    gradeCodeList = gradeCodeIDs.split(',')
    for gradeCodeID in gradeCodeList:
         objCode = code.objects.get(codeID = gradeCodeID)
         userGrade(grade = objCode, user = userObj).save()

    return

"""
Function to get the subjetcode of user
"""
def getUserSubjectCode(userInfo):
    """ get the subjectCodeID based on the user
        Args:
            userInfo(obj): user information 
        returns: 
            userSubjectCodeID(str):Comma seprated string of userSubjectCodeIDs

    """
    objUserSubjectList = userSubject.objects.filter(user = userInfo)
    arrSubjectCodeIDs = []
    userSubjectCodeID = None

    for objUserSubject in objUserSubjectList:
        arrSubjectCodeIDs.append(str(objUserSubject.subject.codeID))

    userSubjectCodeID = ",".join(arrSubjectCodeIDs)

    return userSubjectCodeID

# Function to get the gradecode of the user
def getUserGradeCode(userInfo):
    """ get the gradeCodeID based on the user 
        Args:
            userInfo(obj): user information 
        returns:
            userGradeCodeID(str): Comma seprated string of userGradeCodeIDs
    """
    objUserGradeList = userGrade.objects.filter(user = userInfo)
    arrGradeCodeIDs = []
    userGradeCodeID = None

    for objUserGrade in objUserGradeList:
        arrGradeCodeIDs.append(str(objUserGrade.grade.codeID))

    userGradeCodeID = ",".join(arrGradeCodeIDs)

    return userGradeCodeID

def getUsertopicCode(userInfo):
    """ get the topicCodeID based on the user 
        Args:
            userInfo(obj): user information 
        returns:
            userTopicCodeID(str):Comma seprated string of userTopicCodeIDs
    """
    objUserTopicList = userTopic.objects.filter(user = userInfo) 
    arrTopicCodeIDs = []
    userTopicCodeID = None

    for objUserTopic in objUserTopicList:
        arrTopicCodeIDs.append(str(objUserTopic.topic.codeID))

    userTopicCodeID = ",".join(arrTopicCodeIDs)

    return userTopicCodeID

def getUserSkillCode(userInfo):
    """ get the skill codeID based on the user
        Args:
            userInfo(obj): user information 
        returns:
            userSkillCodeID(str):Comma seprated string of userTopicCodeIDs
    """
 
    objUserSkillList = userSkill.objects.filter(user = userInfo)
    arrSkillCodeIDs = []
    userSkillCodeID = None

    for objUserSkill in objUserSkillList:
        arrSkillCodeIDs.append(str(objUserSkill.skill.codeID))

    userSkillCodeID = ",".join(arrSkillCodeIDs)

    return userSkillCodeID
"""
Save user's device ID for push notifications
"""
def userDeviceSave(objUser, fcmDeviceID):
    # Check if the given phoneNumber and device ID combination already exists
    isDeviceAlreadyRegistrered = device.objects.filter(user = objUser, fcmDeviceID = fcmDeviceID).exists()
    if isDeviceAlreadyRegistrered:
        return
    
    if objUser:
        # If the given userID and device ID combination does NOT exists, then, save
        device(user = objUser, fcmDeviceID = fcmDeviceID).save()
        return
    
    return

#checkOTPTimer
"""
Check timestamp difference of last sent otp and request for current otp
"""
def checkOTPTimer(phoneNumber):
    sendOTP = None
    secondDifference = None
    mysqlCurrentDate = None
    
    # Connection
    cursor = connection.cursor()  
    
    # SQL Query
    getCurrentDate = "select now()"
    
    #print "getCurrentDate:",getCurrentDate            
    cursor.execute(getCurrentDate)
    
    #Queryset
    datetimeQuerySet = cursor.fetchall()
    
    for item in datetimeQuerySet:
        mysqlCurrentDate = item[0]
    
    # Check if the OTP for given phoneNumber is exists or not.
    try:
        objOTP = otp.objects.filter(phoneNumber = phoneNumber).order_by('-createdOn').first()
    except otp.DoesNotExist:
        return True
    
    # i.e. User registration.
    if not objOTP:
        return True
    
    #Get current date time
    objCurrentDateTime = datetime.now()
    objLastOTPDate = objOTP.createdOn.replace(tzinfo=None)
    objCurrentDT = objCurrentDateTime.replace(tzinfo=None)
      
#     print "objCurrentDateTime:",objCurrentDT
#     print "objOTP.createdOn",objLastOTPDate
#     print "mysqlCurrentDate:",mysqlCurrentDate
    
    #If both the datetime exists, then campare the both.
    if objLastOTPDate and mysqlCurrentDate:
        # Get the datetime difference in seconds
        secondDifference = (mysqlCurrentDate - objLastOTPDate).total_seconds()
        print "secondDifference:",secondDifference
    
    #If the difference is gretter then 3min i.e. 180 seconds then send the OTP.
    if secondDifference > constants.sms.reSendSMS_IntervalInSeconds:
        return True
    else:
        return False

"""
function to get email from token if valid
"""
def get_email_from_google_token(googleToken):
    try:
        result = requests.get(
            'https://www.googleapis.com/oauth2/v1/tokeninfo',
            params = {'id_token': googleToken}
        )
        tokenResult = json.loads(result.text)
        email = tokenResult.get("email")
        assert tokenResult.get('issued_to') in ['276996264257-movh2acc81ada5tfftrjmifcg155bunm.apps.googleusercontent.com','276996264257-rindt6lhslg4f86q0ngq65gk8tmfn0t9.apps.googleusercontent.com']
        assert tokenResult.get('audience') == '276996264257-c77hctg5nldp3ed7h1b2d7fsa6nvt7ti.apps.googleusercontent.com'
        assert tokenResult.get('email_verified') == True
        return email
    except:
        return None
    

        