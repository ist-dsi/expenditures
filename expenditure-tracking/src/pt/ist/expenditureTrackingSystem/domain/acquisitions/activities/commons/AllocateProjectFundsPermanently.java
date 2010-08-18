package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;

public class AllocateProjectFundsPermanently<P extends PaymentProcess> extends
	WorkflowActivity<P, AllocateProjectFundsPermanentlyActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	return process.isProjectAccountingEmployee(user.getExpenditurePerson()) && isUserProcessOwner(process, user)
		&& !process.hasAllocatedFundsPermanentlyForAllProjectFinancers() && !process.hasAllInvoicesAllocatedInProject();
    }

    @Override
    protected void process(AllocateProjectFundsPermanentlyActivityInformation<P> activityInformation) {
	for (FundAllocationBean fundAllocationBean : activityInformation.getBeans()) {
	    final ProjectFinancer projectFinancer = (ProjectFinancer) fundAllocationBean.getFinancer();
	    projectFinancer.addEffectiveProjectFundAllocationId(fundAllocationBean.getEffectiveFundAllocationId());
	}
    }

    public AllocateProjectFundsPermanentlyActivityInformation<P> getActivityInformation(P process) {
	return new AllocateProjectFundsPermanentlyActivityInformation<P>(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
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
