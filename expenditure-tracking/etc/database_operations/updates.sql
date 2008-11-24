


-- Inserted at 2008-11-24T11:02:17.197Z

alter table ACQUISITION add column DELETED_STATE tinyint(1);





-- Inserted at 2008-11-24T15:03:44.437Z

alter table FILE add column DISPLAY_NAME text;
update FILE set DISPLAY_NAME=FILENAME;

