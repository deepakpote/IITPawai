-- use mitraDev;
-- Insert admin user
Insert into usr_user(phoneNumber, userName, createdOn, modifiedOn)
values( 1234567890, 'admin', now(), now());

-- Insert code group
Insert into com_codegroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 100 as codeGroupID, 'Messages' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 101 as codeGroupID, 'Language' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 102 as codeGroupID, 'Districts' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 103 as codeGroupID, 'Subjects' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 104 as codeGroupID, 'Grades' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 105 as codeGroupID, 'Topics' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 106 as codeGroupID, 'Skills' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 107 as codeGroupID, 'Content Types' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 108 as codeGroupID, 'File Types' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;


-- Insert into code
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
-- Language
Select 101100 as codeID, 101 as codeGroupID, 'English' as codeNameEn, 'इंग्रजी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 101101 as codeID, 101 as codeGroupID, 'Marathi' as codeNameEn, 'मराठी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 

-- Districts
Select 102100 as codeID, 102 as codeGroupID, 'Ahmednagar' as codeNameEn, 'अहमदनगर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102101 as codeID, 102 as codeGroupID, 'Akola' as codeNameEn, 'अकोला' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102102 as codeID, 102 as codeGroupID, 'Amravati' as codeNameEn, 'अमरावती' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102103 as codeID, 102 as codeGroupID, 'Aurangabad' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102104 as codeID, 102 as codeGroupID, 'Beed' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102105 as codeID, 102 as codeGroupID, 'Bhandara' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102106 as codeID, 102 as codeGroupID, 'Buldhana' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102107 as codeID, 102 as codeGroupID, 'Chandrapur' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102108 as codeID, 102 as codeGroupID, 'Dhule' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102109 as codeID, 102 as codeGroupID, 'Gadchiroli' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102110 as codeID, 102 as codeGroupID, 'Gondia' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102111 as codeID, 102 as codeGroupID, 'Hingoli' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102112 as codeID, 102 as codeGroupID, 'Jalgaon' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102113 as codeID, 102 as codeGroupID, 'Jalna' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102114 as codeID, 102 as codeGroupID, 'Kolhapur' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102115 as codeID, 102 as codeGroupID, 'Latur' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102116 as codeID, 102 as codeGroupID, 'Mumbai City' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102117 as codeID, 102 as codeGroupID, 'Mumbai Suburban' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102118 as codeID, 102 as codeGroupID, 'Nagpur' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102119 as codeID, 102 as codeGroupID, 'Nanded' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102120 as codeID, 102 as codeGroupID, 'Nandurbar' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102121 as codeID, 102 as codeGroupID, 'Nashik' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102122 as codeID, 102 as codeGroupID, 'Osmanabad' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102123 as codeID, 102 as codeGroupID, 'Parbhani' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102124 as codeID, 102 as codeGroupID, 'Pune' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102125 as codeID, 102 as codeGroupID, 'Raigad' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102126 as codeID, 102 as codeGroupID, 'Ratnagiri' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102127 as codeID, 102 as codeGroupID, 'Sangli' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102128 as codeID, 102 as codeGroupID, 'Satara' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102129 as codeID, 102 as codeGroupID, 'Sindhudurg' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102130 as codeID, 102 as codeGroupID, 'Solapur' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102131 as codeID, 102 as codeGroupID, 'Thane' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102132 as codeID, 102 as codeGroupID, 'Wardha' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102133 as codeID, 102 as codeGroupID, 'Washim' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102134 as codeID, 102 as codeGroupID, 'Yavatmal' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 102135 as codeID, 102 as codeGroupID, 'Palghar' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 

;

-- Update the language and district for admin user
Update usr_user set preferredLanguageCodeID = 101100, districtCodeID = 102100 where userID = 1;


-- Insert into code - error messages
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 100100 as codeID, 100 as codeGroupID, 'Success' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 100101 as codeID, 100 as codeGroupID, 'Phone number is mandatory' as codeNameEn, 'फोन नंबर अनिवार्य आहे' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 100102 as codeID, 100 as codeGroupID, 'Invalid phone number' as codeNameEn, 'अवैध फोन नंबर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 100103 as codeID, 100 as codeGroupID, 'Invalid OTP' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
;


-- delete from com_code where codeGroupID = 100;

/*Insert into com_district
Select 1 as districtID, 'Ahmednagar' as districtName union 
Select 2 as districtID, 'Akola' as districtName union 
Select 3 as districtID, 'Amravati' as districtName union 
Select 4 as districtID, 'Aurangabad' as districtName union 
Select 5 as districtID, 'Beed' as districtName union 
Select 6 as districtID, 'Bhandara' as districtName union 
Select 7 as districtID, 'Buldhana' as districtName union 
Select 8 as districtID, 'Chandrapur' as districtName union 
Select 9 as districtID, 'Dhule' as districtName union 
Select 10 as districtID, 'Gadchiroli' as districtName union 
Select 11 as districtID, 'Gondia' as districtName union 
Select 12 as districtID, 'Hingoli' as districtName union 
Select 13 as districtID, 'Jalgaon' as districtName union 
Select 14 as districtID, 'Jalna' as districtName union 
Select 15 as districtID, 'Kolhapur' as districtName union 
Select 16 as districtID, 'Latur' as districtName union 
Select 17 as districtID, 'Mumbai City' as districtName union 
Select 18 as districtID, 'Mumbai Suburban' as districtName union 
Select 19 as districtID, 'Nagpur' as districtName union 
Select 20 as districtID, 'Nanded' as districtName union 
Select 21 as districtID, 'Nandurbar' as districtName union 
Select 22 as districtID, 'Nashik' as districtName union 
Select 23 as districtID, 'Osmanabad' as districtName union 
Select 24 as districtID, 'Parbhani' as districtName union 
Select 25 as districtID, 'Pune' as districtName union 
Select 26 as districtID, 'Raigad' as districtName union 
Select 27 as districtID, 'Ratnagiri' as districtName union 
Select 28 as districtID, 'Sangli' as districtName union 
Select 29 as districtID, 'Satara' as districtName union 
Select 30 as districtID, 'Sindhudurg' as districtName union 
Select 31 as districtID, 'Solapur' as districtName union 
Select 32 as districtID, 'Thane' as districtName union 
Select 33 as districtID, 'Wardha' as districtName union 
Select 34 as districtID, 'Washim' as districtName union 
Select 35 as districtID, 'Yavatmal' as districtName union 
Select 36 as districtID, 'Palghar' as districtName; 
*/