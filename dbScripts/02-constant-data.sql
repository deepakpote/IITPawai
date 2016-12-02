-- Insert code group for user types.
Insert into com_codegroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 109 as codeGroupID, 'User Types' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- Insert into code - user types
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 109100 as codeID, 109 as codeGroupID, 'Teacher' as codeNameEn, 'शिक्षक' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 

-- Insert subjects
Select 103100 as codeID, 103 as codeGroupID, 'English' as codeNameEn, 'इंग्रजी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 103101 as codeID, 103 as codeGroupID, 'Marathi' as codeNameEn, 'मराठी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 103102 as codeID, 103 as codeGroupID, 'Maths' as codeNameEn, 'गणित' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 

-- Insert grades
Select 104100 as codeID, 104 as codeGroupID, '1' as codeNameEn, '१' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104101 as codeID, 104 as codeGroupID, '2' as codeNameEn, '२' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104102 as codeID, 104 as codeGroupID, '3' as codeNameEn, '३' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104103 as codeID, 104 as codeGroupID, '4' as codeNameEn, '४' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104104 as codeID, 104 as codeGroupID, '5' as codeNameEn, '५' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104105 as codeID, 104 as codeGroupID, '6' as codeNameEn, '६' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104106 as codeID, 104 as codeGroupID, '7' as codeNameEn, '७' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104107 as codeID, 104 as codeGroupID, '8' as codeNameEn, '८' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104108 as codeID, 104 as codeGroupID, '9' as codeNameEn, '९' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 104109 as codeID, 104 as codeGroupID, '10' as codeNameEn, '१०' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
;

-- Insert error message for user validation failed
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 100104 as codeID, 100 as codeGroupID, 'User validation failed' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- Insert error message for user authentication failed during login
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 100105 as codeID, 100 as codeGroupID, 'User cannot be authenticated' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 100106 as codeID, 100 as codeGroupID, 'User cannot be authenticated' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;
-- =============================================== FROM SHARWARI ==================================================================================
-- Insert in com_codeGroup Table (Authentication Types)
Insert into com_codegroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 110 as codeGroupID, 'Authentication Types' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- Insert in com_code Table (Authentication Types)
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 110100 as codeID, 110 as codeGroupID, 'Registration' as codeNameEn, 'नोंदणी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 110101 as codeID, 110 as codeGroupID, 'SignIn' as codeNameEn, 'साइन इन' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- Insert in com_Code Table (Error Codes)
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 100110 as codeID, 100 as codeGroupID, 'User not Registered' as codeNameEn, 'वापरकर्ता नोंदणी न' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 100111 as codeID, 100 as codeGroupID, 'User already exists. Please Sign In' as codeNameEn, 'वापरकर्ता आधीपासूनच विद्यमान आहे. साइन इन करा' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 100112 as codeID, 100 as codeGroupID, 'Authentication Type is mandatory' as codeNameEn, 'प्रमाणीकरण अनिवार्य आहे' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- ============================================ FROM DIPAK =======================================================================================

-- -------------------------add content type -------------------------------------------
-- Now add code group "Content type"
insert into com_code(codeID,codeNameEn,codeNameMr,displayOrder,codeGroupID,createdOn,modifiedOn,createdBy,modifiedBy) 
values(107100,"Teaching aids","शैक्षणिक साधने ",1,107,CURRENT_DATE,CURRENT_DATE,1,1);

insert into com_code(codeID,codeNameEn,codeNameMr,displayOrder,codeGroupID,createdOn,modifiedOn,createdBy,modifiedBy) 
values(107101,"Self learning","स्वअध्ययन ",2,107,CURRENT_DATE,CURRENT_DATE,1,1);

insert into com_code(codeID,codeNameEn,codeNameMr,displayOrder,codeGroupID,createdOn,modifiedOn,createdBy,modifiedBy) 
values(107102,"Trainings","प्रशिक्षणे ",3,107,CURRENT_DATE,CURRENT_DATE,1,1);

-- Now add code group "File type"
insert into com_code(codeID,codeNameEn,codeNameMr,displayOrder,codeGroupID,createdOn,modifiedOn,createdBy,modifiedBy) 
values(108100,"Video","व्हिडीओ ",1,108,CURRENT_DATE,CURRENT_DATE,1,1);

insert into com_code(codeID,codeNameEn,codeNameMr,displayOrder,codeGroupID,createdOn,modifiedOn,createdBy,modifiedBy) 
values(108101,"Audio","ऑडिओ ",2,108,CURRENT_DATE,CURRENT_DATE,1,1);

insert into com_code(codeID,codeNameEn,codeNameMr,displayOrder,codeGroupID,createdOn,modifiedOn,createdBy,modifiedBy) 
values(108102,"PPT","पीपीटी ",3,108,CURRENT_DATE,CURRENT_DATE,1,1);

insert into com_code(codeID,codeNameEn,codeNameMr,displayOrder,codeGroupID,createdOn,modifiedOn,createdBy,modifiedBy) 
values(108103,"Worksheet","वर्कशीट",4,108,CURRENT_DATE,CURRENT_DATE,1,1);


-- add messages for the Search Teaching aid API

Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, `comment`)
Select 100107 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn, 'user' as comment union
Select 100108 as codeID, 100 as codeGroupID, 'Content type cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content' as comment union
Select 100109 as codeID, 100 as codeGroupID, 'File type cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content' as comment union
Select 100113 as codeID, 100 as codeGroupID, 'Language cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Language' as comment union
Select 100114 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'content' as comment union
Select 100115 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'content' as comment 
;

-- add sample topic (by Pradnya)
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, `comment`, createdBy, createdOn, modifiedBy, modifiedOn)
-- Language
Select 105100 as codeID, 105 as codeGroupID, 'Topic1' as codeNameEn, '' as codeNameMr, null as `comment`, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 105101 as codeID, 105 as codeGroupID, 'Topic2' as codeNameEn, '' as codeNameMr, null as `comment`, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- Messages related to self learning search
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100116 as codeID, 100 as codeGroupID, 'File type cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Self learning content search' as comment union
Select 100117 as codeID, 100 as codeGroupID, 'Language cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Self learning content search' as comment union
Select 100118 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Self learning content search' as comment union
Select 100119 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Self learning content search' as comment 
;