
UPDATE GENERIC_LOG SET STATE="IN_GENESIS", OJB_CONCRETE_CLASS="pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog" WHERE OJB_CONCRETE_CLASS="pt.ist.expenditureTrackingSystem.domain.processes.GenericLog" AND STATE IS NULL AND ( OPERATION ="GenericAddPayingUnit" OR OPERATION="GenericRemovePayingUnit" OR OPERATION="GenericAssignPayingUnitToItem");


UPDATE GENERIC_LOG SET STATE="FUNDS_ALLOCATED", OJB_CONCRETE_CLASS="pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog" WHERE OJB_CONCRETE_CLASS="pt.ist.expenditureTrackingSystem.domain.processes.GenericLog" AND STATE IS NULL AND OPERATION ="AuthorizeAcquisitionProcess" ;
