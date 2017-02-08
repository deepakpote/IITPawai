-- delete wrong entry from con_contentGrade table. (copied extra 14 records of self learning into the con_contentGrade)

delete from con_contentGrade where contentID >= 200 and contentID <= 214;