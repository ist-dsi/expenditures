update PROCESS_STATE SET OJB_CONCRETE_CLASS = 'pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedAcquitionProcessState';
update GENERIC_LOG SET OJB_CONCRETE_CLASS = 'pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcessOperationLog' where OJB_CONCRETE_CLASS = 'pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog';


CREATE TABLE `ACQUISITION_REQUEST_SUPPLIER` (
  `KEY_ACQUISITION_REQUEST` int(11) NOT NULL,
  `KEY_SUPPLIER` int(11) NOT NULL,
  PRIMARY KEY  (`KEY_ACQUISITION_REQUEST`,`KEY_SUPPLIER`),
  KEY `KEY_ACQUISITION_REQUEST` (`KEY_ACQUISITION_REQUEST`),
  KEY `KEY_SUPPLIER` (`KEY_SUPPLIER`)
) ENGINE=InnoDB;

insert into ACQUISITION_REQUEST_SUPPLIER(KEY_ACQUISITION_REQUEST,KEY_SUPPLIER)  select ID_INTERNAL, KEY_SUPPLIER FROM ACQUISITION A WHERE A.OJB_CONCRETE_CLASS="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest" AND A.KEY_SUPPLIER IS NOT NULL;

