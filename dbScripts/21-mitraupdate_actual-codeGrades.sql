DELIMITER $$

DROP PROCEDURE IF EXISTS update_codeGrades $$
CREATE PROCEDURE update_codeGrades(bound VARCHAR(255),IN fileName VARCHAR(255),IN contentIDs VARCHAR(255))

  BEGIN

	DECLARE n INT DEFAULT 0;
	DECLARE indexval INT DEFAULT 0;
    DECLARE oldValue INT;

	DECLARE topVal INT;
    DECLARE id VARCHAR(255);
    DECLARE value TEXT;
    DECLARE occurance INT DEFAULT 0;
    DECLARE i INT DEFAULT 0;
    DECLARE splitted_value INT;
    DECLARE done INT DEFAULT 0;
    DECLARE cur1 CURSOR FOR SELECT  fileName,contentIDs;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
  

    DROP TEMPORARY TABLE IF EXISTS table2;
    CREATE TEMPORARY TABLE table2(
    `id` VARCHAR(255) NULL,
    `valuedata` VARCHAR(255) NOT NULL
    ) ENGINE=Memory;

    OPEN cur1;
    
    
      read_loop: LOOP
        FETCH cur1 INTO id, value;
        IF done THEN
          LEAVE read_loop;
        END IF;

        SET occurance = (SELECT LENGTH(value)
                                 - LENGTH(REPLACE(value, bound, ''))
                                 +1);
        SET i=1;
        WHILE i <= occurance DO
          SET splitted_value =
          (SELECT REPLACE(SUBSTRING(SUBSTRING_INDEX(value, bound, i),
          LENGTH(SUBSTRING_INDEX(value, bound, i - 1)) + 1), ',', ''));

          INSERT INTO table2 VALUES (id, splitted_value);
          SET i = i + 1;

        END WHILE;
      END LOOP;

      
    CLOSE cur1;
    
    SET @topVal = 0;
	
	SET  @topVal = ( select `valuedata` from table2 limit 0,1);
    
    
	SELECT COUNT(*) FROM table2 INTO n;
	SET indexval =1;
	WHILE indexval < n DO 
	  set @oldValue = (select valuedata FROM table2 LIMIT indexval,1);
      
      IF EXISTS(Select contentID from usr_userContent where contentid = @oldValue) THEN 
		  BEGIN
				
				UPDATE usr_userContent
				SET contentid = @topVal
				where contentid = @oldValue;
		  END;
	  END IF;
      
	  IF EXISTS(Select contentID from con_contentResponse where contentid = @oldValue) THEN
		  BEGIN
				
				UPDATE con_contentResponse
				SET contentid = @topVal
				where contentid = @oldValue;
		  END;
	  END IF;
      
	  IF EXISTS(Select contentID from con_contentGrade where contentid = @oldValue) THEN
		  BEGIN
				
				UPDATE con_contentGrade
				SET contentid = @topVal
				where contentid = @oldValue;
		  END;
	  END IF;
      
      -- select 'con_content';
      -- delete from actual table.
      DELETE FROM con_content WHERE contentID = @oldValue;
		
	  SET indexval = indexval + 1;
      
	END WHILE;
    
  END; $$