# Migrating Expenditures to version 2.0.0

Expenditures 2.0 is a major release, focused on upgrading its infrastructure to Bennu 3.1, and upgrading the codebase to Java 8.

In order to migrate your existing application to version 2.0, you must run the following steps:

1. Run the following SQL:

	```sql
	ALTER TABLE FINANCE_SYSTEM CHANGE OID_MY_ORG OID_BENNU bigint unsigned;
	```



Deleted stuff that needs to be redone:

class ExternalRequestSyncTask extends .org.fenixedu.bennu.scheduler.custom.CustomTask {
}

	class ExternalRequestSyncTaskMgpIST extends ExternalRequestSyncTask {
	}

	class ExternalRequestSyncTaskMgpADIST extends ExternalRequestSyncTask {
	}

	class ExternalRequestSyncTaskMgpISTID extends ExternalRequestSyncTask {
	}

class pt.ist.expenditureTrackingSystem.domain.task.ExportAuthorizations extends .org.fenixedu.bennu.scheduler.custom.CustomTask {
}

class pt.ist.expenditureTrackingSystem.domain.task.ExportAuthorizationsIST extends pt.ist.expenditureTrackingSystem.domain.task.ExportAuthorizations {
}

class pt.ist.expenditureTrackingSystem.domain.task.ExportAuthorizationsADIST extends pt.ist.expenditureTrackingSystem.domain.task.ExportAuthorizations {
}

class pt.ist.expenditureTrackingSystem.domain.task.ExportAuthorizationsISTID extends pt.ist.expenditureTrackingSystem.domain.task.ExportAuthorizations {
}


class acquisitions.ProjectAcquisitionFundAllocationRequest extends .module.externalAccountingIntegration.domain.FundAllocationRequest {
}

FundAllocationResultService

relation ProjectAcquisitionFundAllocationRequestUnitItem {
	acquisitions.ProjectAcquisitionFundAllocationRequest playsRole projectAcquisitionFundAllocationRequest {
		multiplicity *;
	}
	acquisitions.UnitItem playsRole unitItem {
		multiplicity 0..1;
	}
}

relation AcquisitionFundAllocationDiaryAndTransactionReportRequestFinancer {
	acquisitions.AcquisitionFundAllocationDiaryAndTransactionReportRequest playsRole acquisitionFundAllocationDiaryAndTransactionReportRequest {
		multiplicity *;
	}
	acquisitions.UnitItem playsRole unitItem {
		multiplicity 1..1;
	}
}


class pt.ist.expenditureTrackingSystem.domain.SyncSuppliers extends org.fenixedu.bennu.scheduler.custom.CustomTask {	
}

class pt.ist.expenditureTrackingSystem.domain.SetRoleTypes extends org.fenixedu.bennu.scheduler.custom.CustomTask {	
}

class pt.ist.expenditureTrackingSystem.domain.ConnectPersonToUserTask extends org.fenixedu.bennu.scheduler.custom.CustomTask {	
}

class pt.ist.expenditureTrackingSystem.domain.EmailDigester extends org.fenixedu.bennu.scheduler.custom.CustomTask {
}

class pt.ist.expenditureTrackingSystem.domain.ConnectUnitsToOrganization extends org.fenixedu.bennu.scheduler.custom.CustomTask {
}

class ClearTemporaryMissionItems extends .org.fenixedu.bennu.scheduler.custom.CustomTask {	
}

class EmailDigester extends .org.fenixedu.bennu.scheduler.custom.CustomTask {
}

SyncSuppliers


class acquisitions.AcquisitionFundAllocationDiaryAndTransactionReportRequest extends .module.externalAccountingIntegration.domain.ExternalRequest {



Domain Objects with tables to delete:

ExternalAccountingIntegrationSystem

ExternalRequest

class acquisitions.AcquisitionFundAllocationDiaryAndTransactionReportRequest extends .module.externalAccountingIntegration.domain.ExternalRequest {
	String processId;
	String payingUnitNumber;
	String payingAccountingUnit;
	String diaryNumber;
	String transactionNumber;
}

