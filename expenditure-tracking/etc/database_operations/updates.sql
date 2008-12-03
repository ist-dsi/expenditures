delete FROM FILE WHERE OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.ImportFile' AND ID_INTERNAL NOT IN (SELECT KEY_IMPORT_FILE FROM GENERIC_PROCESS WHERE KEY_IMPORT_FILE IS NOT NULL);



-- Inserted at 2008-12-03T16:10:33.053Z

alter table FILE add column ACTIVE tinyint(1) default '1';


