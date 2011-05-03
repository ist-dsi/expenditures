package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SetSkipSupplierFundAllocation extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& !process.getShouldSkipSupplierFundAllocation().booleanValue()
		&& (process instanceof SimplifiedProcedureProcess && ((SimplifiedProcedureProcess) process)
			.getProcessClassification().isCCP())
		&& ((process.getAcquisitionProcessState().isInGenesis() && person == process.getRequestor()
			|| (ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
				&& (process.getAcquisitionProcessState().isAuthorized()
			|| process.getAcquisitionProcessState().isAcquisitionProcessed() || process.isInvoiceReceived())))
			|| ExpenditureTrackingSystem.isSupplierFundAllocationManagerGroupMember())
		;
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	activityInformation.getProcess().skipSupplierFundAllocation();
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }
    
}
