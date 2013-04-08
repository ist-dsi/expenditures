
alter table MY_ORG add column KEY_EXPENDITURE_TRACKING_SYSTEM int(11) default NULL;
alter table EXPENDITURE_TRACKING_SYSTEM add column KEY_MY_ORG int(11) default NULL; 
insert into MY_ORG values(1, 1);
update MY_ORG set KEY_EXPENDITURE_TRACKING_SYSTEM = 1;
update EXPENDITURE_TRACKING_SYSTEM set KEY_MY_ORG = 1; 
