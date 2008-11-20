


-- Inserted at 2008-11-10T18:16:37.195Z

alter table ACQUISITION add column DESCRIPTION text;
alter table FINANCER add index (KEY_ACCOUNTING_UNIT);
alter table GENERIC_PROCESS add index (KEY_CURRENT_OWNER);





-- Inserted at 2008-11-20T12:07:19.320Z

alter table FILE add column KEY_PROCESS int(11);
alter table FILE add index (KEY_PROCESS);





-- Inserted at 2008-11-20T16:31:22.200Z

alter table ACQUISITION add column KEY_REFUNDEE int(11);
alter table ACQUISITION add index (KEY_REFUNDEE);


