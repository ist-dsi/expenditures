package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ProjectFundAllocation<P extends PaymentProcess> extends
	WorkflowActivity<P, ProjectFundAllocationActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	Person person = user.getExpenditurePerson();
	return process.isProjectAccountingEmployee(person)
		&& isUserProcessOwner(process, user)
		&& process.isPendingFundAllocation()
		&& !process.hasAllocatedFundsForAllProjectFinancers(person)
		&& (!process.hasMissionProcess() || process.getMissionProcess().hasAllAllocatedProjectFunds());
    }

    public ProjectFundAllocationActivityInformation<P> getActivityInformation(P process) {
	return getActivityInformation(process, true);
    }

    protected boolean requiresPriorFundAllocation() {
	final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
	final Boolean b = instance.getRequireFundAllocationPriorToAcquisitionRequest();
	return b != null && b.booleanValue();
    }

    @Override
    protected void process(ProjectFundAllocationActivityInformation<P> activityInformation) {
	for (FundAllocationBean fundAllocationBean : activityInformation.getBeans()) {
	    final ProjectFinancer projectFinancer = (ProjectFinancer) fundAllocationBean.getFinancer();
	    projectFinancer.setProjectFundAllocationId(fundAllocationBean.getFundAllocationId());
	}

	final P process = activityInformation.getProcess();
	if (!requiresPriorFundAllocation() && process.hasAllocatedFundsForAllProjectFinancers()) {
	    process.allocateFundsToUnit();
	}

    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }
    
    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

    public ProjectFundAllocationActivityInformation<P> getActivityInformation(P process, boolean takeProcess) {
	return new ProjectFundAllocationActivityInformation<P>(process, this, takeProcess);
    }

}
