DELIMITER $$

DROP PROCEDURE IF EXISTS `migrateGrades`$$

CREATE PROCEDURE `migrateGrades`()
BEGIN

DECLARE done INT DEFAULT FALSE;
DECLARE contentIDs varchar(1000);
DECLARE nfilename TEXT;
DECLARE str TEXT;

DECLARE curs1 CURSOR FOR SELECT group_concat(contentID), `fileName` FROM con_content where contentTypeCodeID = 107100  group by fileName;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;


OPEN curs1;

read_loop: LOOP
FETCH curs1 INTO contentIDs,nfilename;

IF done THEN
LEAVE read_loop;
END IF;

CALL update_codeGrades(',',nfilename,contentIDs);
COMMIT;
 
END LOOP;



CLOSE curs1;
END$$
DELIMITER ;