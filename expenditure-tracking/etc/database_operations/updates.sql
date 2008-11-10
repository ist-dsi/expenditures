


-- Inserted at 2008-11-10T14:12:29.743Z

alter table ACQUISITION add column DESCRIPTION text;
alter table FINANCER add index (KEY_ACCOUNTING_UNIT);
alter table GENERIC_PROCESS add index (KEY_CURRENT_OWNER);


