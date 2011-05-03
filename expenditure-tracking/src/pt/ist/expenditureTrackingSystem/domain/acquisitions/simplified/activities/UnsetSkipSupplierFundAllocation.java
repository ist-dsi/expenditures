package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class UnsetSkipSupplierFundAllocation extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.getSkipSupplierFundAllocation().booleanValue()
		&& (((process.getAcquisitionProcessState().isInGenesis() && person == process.getRequestor())
			|| (ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user) && (process.getAcquisitionProcessState().isAuthorized()
										|| process.getAcquisitionProcessState().isAcquisitionProcessed()
										|| process.isInvoiceReceived())))
			|| ExpenditureTrackingSystem.isSupplierFundAllocationManagerGroupMember(user))
		;
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	activityInformation.getProcess().unSkipSupplierFundAllocation();
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
