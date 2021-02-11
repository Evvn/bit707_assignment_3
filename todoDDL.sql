-- Populate To Do Application db

CREATE TABLE ToDo(
	taskNumber    INTEGER PRIMARY KEY,
	taskName      TEXT NOT NULL);  
 
INSERT INTO ToDo VALUES (1,"Task Name One");
INSERT INTO ToDo VALUES (2,"Task Name Two");
INSERT INTO ToDo VALUES (3,"Task Name Three");