package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class ProjectFundAllocation<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(T process) {
	return process.isProjectAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(T process) {
	return isCurrentUserProcessOwner(process) && process.isInApprovedState()
		&& !process.hasAllocatedFundsForAllProjectFinancers(getUser().getPerson());
    }

    @Override
    protected void process(T process, Object... objects) {
	final List<FundAllocationBean> fundAllocationBeans = (List<FundAllocationBean>) objects[0];
	for (FundAllocationBean fundAllocationBean : fundAllocationBeans) {
	    final ProjectFinancer projectFinancer = (ProjectFinancer) fundAllocationBean.getFinancer();
	    projectFinancer.setProjectFundAllocationId(fundAllocationBean.getFundAllocationId());
	}
    }

}
