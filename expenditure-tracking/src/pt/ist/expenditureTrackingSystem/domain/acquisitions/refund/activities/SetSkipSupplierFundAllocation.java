package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class SetSkipSupplierFundAllocation extends WorkflowActivity<RefundProcess, ActivityInformation<RefundProcess>> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	return isUserProcessOwner(process, user)
		&& !process.getSkipSupplierFundAllocation()
		&& (((process.isInGenesis()
			|| process.getProcessState().getRefundProcessStateType() == RefundProcessStateType.AUTHORIZED)
			&& process.getRequestor() == user.getExpenditurePerson())
			|| ExpenditureTrackingSystem.isSupplierFundAllocationManagerGroupMember(user));

    }

    @Override
    protected void process(ActivityInformation<RefundProcess> activityInformation) {
	activityInformation.getProcess().setSkipSupplierFundAllocation(Boolean.TRUE);
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
