package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class FundAllocation<P extends PaymentProcess> extends WorkflowActivity<P, FundAllocationActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	Person person = user.getExpenditurePerson();
	return process.isAccountingEmployee(person) && isUserProcessOwner(process, user) && process.isPendingFundAllocation()
		&& process.hasAllocatedFundsForAllProjectFinancers() && !process.hasAllFundAllocationId(person);
    }

    public FundAllocationActivityInformation<P> getActivityInformation(P process) {
	return new FundAllocationActivityInformation<P>(process, this);
    }

    @Override
    protected void process(FundAllocationActivityInformation<P> activityInformation) {
	P process = activityInformation.getProcess();
	for (FundAllocationBean fundAllocationBean : activityInformation.getBeans()) {
	    fundAllocationBean.getFinancer().setFundAllocationId(fundAllocationBean.getFundAllocationId());
	}
	if (process.getRequest().hasAllFundAllocationId()) {
	    process.allocateFundsToUnit();
	}

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
