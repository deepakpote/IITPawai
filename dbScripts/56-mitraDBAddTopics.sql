delete from usr_userTopic;
delete from com_code where codeGroupID = 105;

Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 105100 as codeID, 105 as codeGroupID, 'Techsavvy Level 1' as codeNameEn, 'टेक सेव्ही लेव्हल 1' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union 
Select 105101 as codeID, 105 as codeGroupID, 'Techsavvy Level 2' as codeNameEn, 'टेक सेव्ही लेव्हल 2' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union
Select 105102 as codeID, 105 as codeGroupID, 'Classroom Practice - Mathematics' as codeNameEn, 'वर्ग सराव - गणित' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union
Select 105103 as codeID, 105 as codeGroupID, 'Classroom Practice - Marathi' as codeNameEn, 'वर्ग सराव - मराठी ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union
Select 105104 as codeID, 105 as codeGroupID, 'Classroom Practice - Science' as codeNameEn, 'वर्ग सराव - विज्ञान ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union
Select 105105 as codeID, 105 as codeGroupID, 'Classroom Practice - English' as codeNameEn, 'वर्ग सराव - इंग्रजी ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union
Select 105106 as codeID, 105 as codeGroupID, 'Classroom Practice - Environment Study' as codeNameEn, 'वर्ग सराव - परिसर अभ्यास ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union
Select 105107 as codeID, 105 as codeGroupID, 'Spoken English' as codeNameEn, 'इंग्रजी संवाद ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union
Select 105108 as codeID, 105 as codeGroupID, 'Science Experiments' as codeNameEn, 'विज्ञान प्रयोग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn 
union
Select 105109 as codeID, 105 as codeGroupID, 'Professional Development' as codeNameEn, 'व्यावसायिक विकास' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '56-mitraDBAddTopics' where `key` = 'dbScriptExecutedUntil';

