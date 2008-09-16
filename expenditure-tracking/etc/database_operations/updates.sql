
-- Inserted at 2008-09-15T19:15:12.621+01:00

alter table AUTHORIZATION add column MAX_AMOUNT text;


	



-- Inserted at 2008-09-16T16:47:11.457+01:00

alter table FINANCER add column EFFECTIVE_FUND_ALLOCATION_ID text;
alter table GENERIC_PROCESS add column ACQUISITION_PROCESS_NUMBER int(11);
alter table GENERIC_PROCESS add column KEY_ACQUISITION_PROCESS_YEAR int(11);
alter table GENERIC_PROCESS add index (KEY_ACQUISITION_PROCESS_YEAR);


create table ACQUISITION_PROCESS_YEAR (
  `COUNTER` int(11),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_EXPENDITURE_TRACKING_SYSTEM` int(11),
  `YEAR` int(11),
  primary key (ID_INTERNAL),
  index (KEY_EXPENDITURE_TRACKING_SYSTEM)
) type=InnoDB ;

