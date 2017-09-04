
-- Insert com codeGroup..

SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `com_codeGroup` 
VALUES (100,'Messages',now(),now(),1,1),
(101,'Content Language',now(),now(),1,1),
(102,'Districts',now(),now(),1,1),
(103,'Subjects',now(),now(),1,1),
(104,'Grades',now(),now(),1,1),
(105,'Topics',now(),now(),1,1),
(106,'Skills',now(),now(),1,1),
(107,'Content Types',now(),now(),1,1),
(108,'File Types',now(),now(),1,1),
(109,'User Types',now(),now(),1,1),
(110,'Authentication Types',now(),now(),1,1),
(111,'Content Response Types',now(),now(),1,1),
(200,'Webportal Messages',now(),now(),1,1);

SET FOREIGN_KEY_CHECKS = 1;

-- sample user..
SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `usr_user`
(`userID`,
`phoneNumber`,
`userName`,
`photoUrl`,
`udiseCode`,
`emailID`,
`createdOn`,
`modifiedOn`,
`createdBy`,
`districtCodeID`,
`modifiedBy`,
`preferredLanguageCodeID`,
`userTypeCodeID`,
`departmentCodeID`)
VALUES
(1,
'+919975512171',
'Dipak Pote',
'',
'',
'Deepakpote.007@gmail.com',
now(),
now(),
1,
102100,
1,
101100,
109101,
null);

SET FOREIGN_KEY_CHECKS = 1;





SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `com_code` VALUES (100100,'Success','',NULL,NULL,now(),now(),100,1,1),
(100101,'Phone number is mandatory',' फोन नंबर अनिवार्य आहे',NULL,NULL,now(),now(),100,1,1),
(100102,'Invalid phone number',' अवैध फोन नंबर',NULL,NULL,now(),now(),100,1,1),
(100103,'Invalid OTP',' अवैध OTP',NULL,NULL,now(),now(),100,1,1),
(100104,'User validation failed',' वापरकर्ता अवैध्य',NULL,NULL,now(),now(),100,1,1),
(100105,'User cannot be authenticated',' वापरकर्ता अप्रमाणित',NULL,NULL,now(),now(),100,1,1),
(100106,'User cannot be authenticated',' वापरकर्ता अप्रमाणित',NULL,NULL,now(),now(),100,1,1),
(100107,'UserID cannot be empty',' वापरकर्ता नोंदणी आवश्यक',NULL,'user',now(),now(),100,1,1),
(100108,'Content type cannot be empty',' सामग्री निवड आवश्यक',NULL,'Content',now(),now(),100,1,1),
(100109,'File type cannot be empty',' फाईल निवड आवश्यक',NULL,'Content',now(),now(),100,1,1),
(100110,'User not Registered',' नोंदणी करणे आवश्यक',NULL,NULL,now(),now(),100,1,1),
(100111,'User already exists. Please Sign In',' वापरकर्ता आधीपासूनच उपलब्ध आहे. साइन इन करा',NULL,NULL,now(),now(),100,1,1),
(100112,'Authentication Type is mandatory',' प्रमाणीकरण अनिवार्य आहे',NULL,NULL,now(),now(),100,1,1),
(100113,'Language cannot be empty',' भाषा निवड आवश्यक',NULL,'Language',now(),now(),100,1,1),
(100114,'No records found',' काहीच उपलब्ध नाही',NULL,'content',now(),now(),100,1,1),
(100115,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'content',now(),now(),100,1,1),
(100116,'File type cannot be empty',' फाईल निवड आवश्यक',NULL,'Self learning content search',now(),now(),100,1,1),
(100117,'Language cannot be empty',' भाषा निवड आवश्यक',NULL,'Self learning content search',now(),now(),100,1,1),
(100118,'No records found',' काहीच उपलब्ध नाही',NULL,'Self learning content search',now(),now(),100,1,1),
(100119,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'Self learning content search',now(),now(),100,1,1),
(100120,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'Save like',now(),now(),100,1,1),
(100121,'Select the content for like',' सामग्री निवड आवश्यक',NULL,'Save like',now(),now(),100,1,1),
(100122,'Content being liked, no longer exists',' सामग्री अस्तित्वात नाही',NULL,'Save like',now(),now(),100,1,1),
(100123,'HasLiked connot be empty',' HasLiked आवश्यक',NULL,'Save like',now(),now(),100,1,1),
(100124,'Value of HasLiked must be boolean',' ',NULL,'Save like',now(),now(),100,1,1),
(100125,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'update user profile',now(),now(),100,1,1),
(100126,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'get user detail',now(),now(),100,1,1),
(100127,'Mobile device could not be registered for push notifications',' नोटीफिकेशन साठी ह्या मोबाईल ची नोंदणी नाही',NULL,'user registration',now(),now(),100,1,1),
(100128,'Mobile device could not be registered for push notifications',' नोटीफिकेशन साठी ह्या मोबाईल ची नोंदणी नाही',NULL,'user signin',now(),now(),100,1,1),
(100129,'Select the content to be downloaded',' डाऊनलोड करण्यासाठी सामग्री निवडा',NULL,'Save download response',now(),now(),100,1,1),
(100130,'The content that you are trying to downloaded, no longer exists',' तुम्ही डाऊनलोड करत असलेली सामग्री उपलब्ध नाही',NULL,'Save download response',now(),now(),100,1,1),
(100131,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'Save download response',now(),now(),100,1,1),
(100132,'Select the content to be shared',' शेअर करण्यासाठीची सामग्री निवडा',NULL,'Save content share response',now(),now(),100,1,1),
(100133,'The content that you are trying to share, no longer exists',' तुम्ही शेअर करत असलेली सामग्री उपलब्ध नाही',NULL,'Save content share response',now(),now(),100,1,1),
(100134,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'Save content share response',now(),now(),100,1,1),
(100135,'Select the content to be saved',' जतन करण्यासाठीची सामग्री निवडा',NULL,'save user content',now(),now(),100,1,1),
(100136,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'save user content',now(),now(),100,1,1),
(100137,'The content that you are trying to save, no longer exists',' तुम्ही जातन करू इच्छित असलेली सामग्री उपलब्ध नाही',NULL,'save user content',now(),now(),100,1,1),
(100138,'ContentID cannot be empty',' सामग्री ला id देणे आवश्यक आहे',NULL,'Get content response',now(),now(),100,1,1),
(100139,'Content not exists',' सामग्री उपलब्ध नाही',NULL,'Get content response',now(),now(),100,1,1),
(100140,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'Get content response',now(),now(),100,1,1),
(100141,'You have already saved this content',' तुम्ही हि सामग्री आधीच जतन केलेली आहे',NULL,'save user content',now(),now(),100,1,1),
(100142,'There was a problem fetching training events',' प्रशिक्षण कार्यक्रम प्राप्त होत नाही',NULL,'list of user content',now(),now(),100,1,1),
(100143,'Invalid inputs, cannot fetch calendar events',' चुकीच्या आज्ञावलीमुळे दिनदर्शिका कार्यक्रम प्राप्त होत नाही',NULL,'calendar event list',now(),now(),100,1,1),
(100144,'Cannot create new event',' नवीन कार्यक्रम सुरु करू शकत नाही',NULL,'calendar event create',now(),now(),100,1,1),
(100145,'Event to be attended, no longer exists',' तुम्ही उपस्थित राहू इच्छित असणारा कार्यक्रम उपलब्ध नाही',NULL,'calendar event attend',now(),now(),100,1,1),
(100146,'User attending event was not found',' वापरकर्ता उपस्थित राहू इच्छित असणारा कार्यक्रम उपलब्ध नाही',NULL,'calendar event attend',now(),now(),100,1,1),
(100147,'You are already attending this event',' तुम्ही या आधीच हा कार्यक्रमाला उपस्थित होता',NULL,'calendar event attend',now(),now(),100,1,1),
(100148,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'list of user content','2016-12-13 16:34:03','2016-12-13 16:34:03',100,1,1),
(100149,'Select Teaching Aid / Self learning',' शैक्षणिक साहित्य/ स्वयंअध्ययन निवडा',NULL,'list of user content',now(),now(),100,1,1),
(100150,'Select the content to be deleted',' सामग्री काढून टाकण्यासाठी निवडा',NULL,'delete user content',now(),now(),100,1,1),
(100151,'The content that you are trying to delete, no longer exists',' तुम्ही जी सामग्री काढून टाकण्यासाठी प्रयत्न करत आहात ती अस्तित्वात नाही',NULL,'delete user content',now(),now(),100,1,1),
(100152,'Select your preferred langauage',' तुमच्या आवडीची भाषा निवडा',NULL,'Save language',now(),now(),100,1,1),
(100153,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'Save language','2016-12-13 16:34:03','2016-12-13 16:34:03',100,1,1),
(100154,'The profile photo chosen for upload, looks broken',' तुम्ही टाकू इच्छित असलेल्या प्रोफाईल फोटोला समस्या येत आहे',NULL,'save user photo',now(),now(),100,1,1),
(100155,'User does not exists',' वापरकर्ता अस्तित्वात नाही',NULL,'save user photo',now(),now(),100,1,1),
(100156,'No records found',' काहीच उपलब्ध नाही',NULL,'User content',now(),now(),100,1,1),
(100157,'Code version number must be an integer',' आवृतीचा नंबर पूर्णांकात असावा',NULL,'Code List',now(),now(),100,1,1),
(100158,'Invalid Code version number',' आवृतीचा नंबर चुकीचा आहे',NULL,'Code List',now(),now(),100,1,1),
(101100,'English',' इंग्रजी',NULL,NULL,now(),now(),101,1,1),
(101101,'Marathi',' मराठी',NULL,NULL,now(),now(),101,1,1),
(101102,'Hindi','हिंदी',NULL,'',now(),now(),101,1,1),
(101103,'Urdu','उर्दू',NULL,'',now(),now(),101,1,1),
(102100,'Ahmednagar',' अहमदनगर',NULL,NULL,now(),now(),102,1,1),
(102101,'Akola',' अकोला',NULL,NULL,now(),now(),102,1,1),
(102102,'Amravati',' अमरावती',NULL,NULL,now(),now(),102,1,1),
(102103,'Aurangabad',' औरंगाबाद ',NULL,NULL,now(),now(),102,1,1),
(102104,'Beed',' बीड ',NULL,NULL,now(),now(),102,1,1),
(102105,'Bhandara',' भंडारा',NULL,NULL,now(),now(),102,1,1),
(102106,'Buldhana',' बुलढाणा',NULL,NULL,now(),now(),102,1,1),
(102107,'Chandrapur',' चंद्रपूर',NULL,NULL,now(),now(),102,1,1),
(102108,'Dhule',' धुळे',NULL,NULL,now(),now(),102,1,1),
(102109,'Gadchiroli',' गडचिरोली',NULL,NULL,now(),now(),102,1,1),
(102110,'Gondia',' गोंदिया',NULL,NULL,now(),now(),102,1,1),
(102111,'Hingoli',' हिंगोली',NULL,NULL,now(),now(),102,1,1),
(102112,'Jalgaon',' जळगाव',NULL,NULL,now(),now(),102,1,1),
(102113,'Jalna',' जालना',NULL,NULL,now(),now(),102,1,1),
(102114,'Kolhapur',' कोल्हापूर',NULL,NULL,now(),now(),102,1,1),
(102115,'Latur',' लातूर',NULL,NULL,now(),now(),102,1,1),
(102116,'Mumbai City',' मुंबई शहर',NULL,NULL,now(),now(),102,1,1),
(102117,'Mumbai Suburban',' मुंबई उपनगर',NULL,NULL,now(),now(),102,1,1),
(102118,'Nagpur',' नागपूर',NULL,NULL,now(),now(),102,1,1),
(102119,'Nanded',' नांदेड',NULL,NULL,now(),now(),102,1,1),
(102120,'Nandurbar',' नंदुरबार',NULL,NULL,now(),now(),102,1,1),
(102121,'Nashik',' नाशिक',NULL,NULL,now(),now(),102,1,1),
(102122,'Osmanabad',' उस्मानाबाद',NULL,NULL,now(),now(),102,1,1),
(102123,'Parbhani',' परभणी',NULL,NULL,now(),now(),102,1,1),
(102124,'Pune',' पुणे',NULL,NULL,now(),now(),102,1,1),
(102125,'Raigad',' रायगड',NULL,NULL,now(),now(),102,1,1),
(102126,'Ratnagiri',' रत्नागिरी',NULL,NULL,now(),now(),102,1,1),
(102127,'Sangli',' सांगली',NULL,NULL,now(),now(),102,1,1),
(102128,'Satara',' सातारा',NULL,NULL,now(),now(),102,1,1),
(102129,'Sindhudurg',' सिंधुदुर्ग',NULL,NULL,now(),now(),102,1,1),
(102130,'Solapur',' सोलापूर',NULL,NULL,now(),now(),102,1,1),
(102131,'Thane',' ठाणे',NULL,NULL,now(),now(),102,1,1),
(102132,'Wardha',' वर्धा',NULL,NULL,now(),now(),102,1,1),
(102133,'Washim',' वाशिम',NULL,NULL,now(),now(),102,1,1),
(102134,'Yavatmal',' यवतमाळ',NULL,NULL,now(),now(),102,1,1),
(102135,'Palghar',' पालघर',NULL,NULL,now(),now(),102,1,1),
(103100,'Algebra',' बीजगणित',NULL,NULL,now(),now(),103,1,1),
(103101,'Geometry',' भूमिती',NULL,NULL,now(),now(),103,1,1),
(104100,'6',' ६',NULL,NULL,now(),now(),104,1,1),
(104101,'7','७',NULL,NULL,now(),now(),104,1,1),
(104102,'8',' ८',NULL,NULL,now(),now(),104,1,1),
(104103,'9','९',NULL,NULL,now(),now(),104,1,1),
(104104,'10','१०',NULL,NULL,now(),now(),104,1,1),
(105100,'Computer','कॉम्पुटर',NULL,NULL,now(),now(),105,1,1),
(105101,'Youtube','युटूब',NULL,NULL,now(),now(),105,1,1),
(105102,'Blog','ब्लॉग',NULL,'',now(),now(),105,1,1),
(105103,'Mobile','मोबाईल',NULL,'',now(),now(),105,1,1),
(105104,'Internet','इंटरनेट',NULL,'',now(),now(),105,1,1),
(105105,'Mathematics','गणित',NULL,'',now(),now(),105,1,1),
(105106,'Literacy','साक्षरता',NULL,'',now(),now(),105,1,1),
(105107,'Tech Savvy basic','',NULL,'',now(),now(),105,1,1),
(105108,'Marathi','मराठी',NULL,'',now(),now(),105,1,1),
(105109,'Science','विज्ञान',NULL,'',now(),now(),105,1,1),
(105110,'English','इंग्रजी',NULL,'',now(),now(),105,1,1),
(105111,'Environment study','परिसर अभ्यास',NULL,'',now(),now(),105,1,1),
(106100,'Skill1','',NULL,NULL,now(),now(),106,1,1),
(106101,'Skill2','',NULL,NULL,now(),now(),106,1,1),
(107100,'Teaching aids',' शैक्षणिक साधने',1,NULL,now(),now(),107,1,1),
(107101,'Self learning',' स्वअध्ययन',2,NULL,now(),now(),107,1,1),
(107102,'Trainings',' प्रशिक्षणे',3,NULL,now(),now(),107,1,1),
(108100,'Video',' व्हिडीओ',1,NULL,now(),now(),108,1,1),
(108101,'PDF',' ऑडिओ',2,NULL,now(),now(),108,1,1),
(108102,'Flipbook',' फ्लिपबुक',3,NULL,now(),now(),108,1,1),
(109100,'Teacher',' शिक्षक',NULL,NULL,now(),now(),109,1,1),
(110100,'Registration',' नोंदणी',NULL,NULL,now(),now(),110,1,1),
(110101,'SignIn',' साइन इन',NULL,NULL,now(),now(),110,1,1),
(111100,'Like','',NULL,'Used for content response',now(),now(),111,1,1),
(111101,'Download','',NULL,'Used for content response',now(),now(),111,1,1),
(111102,'Share','',NULL,'Used for content response',now(),now(),111,1,1),
(111103,'Rating','',NULL,'Used for content response',now(),now(),111,1,1),
(200100,'User token is invalid, please try with valid token','वापरकर्ता टोकन अवैध आहे, वैध टोकन प्रयत्न करा',NULL,'user',now(),now(),200,1,1),
(200101,'Phone number is not registered with the system','फोन नंबर प्रणाली सह नोंदणीकृत नाही',NULL,'user',now(),now(),200,1,1),
(200102,'Invalid pass key','अवैध पास की',NULL,'user',now(),now(),200,1,1),
(200103,'Valid user for sending notifications','सूचना पाठवणे वैध वापरकर्ता',NULL,'user',now(),now(),200,1,1);
SET FOREIGN_KEY_CHECKS = 1;



select * from com_codegroup;
-- Insert code group for department.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 112 as codeGroupID, 'Department' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- Insert into code - department
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 112100 as codeID, 112 as codeGroupID, 'Department 1' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 112101 as codeID, 112 as codeGroupID, 'Department 2' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

