package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class FundAllocation<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(T process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(T process) {
	return isCurrentUserProcessOwner(process) && process.isPendingFundAllocation()
		&& process.hasAllocatedFundsForAllProjectFinancers() && !process.hasAllFundAllocationId(getUser().getPerson());
    }

    @Override
    protected void process(T process, Object... objects) {
	final List<FundAllocationBean> fundAllocationBeans = (List<FundAllocationBean>) objects[0];
	for (FundAllocationBean fundAllocationBean : fundAllocationBeans) {
	    fundAllocationBean.getFinancer().setFundAllocationId(fundAllocationBean.getFundAllocationId());
	}
	if (process.getRequest().hasAllFundAllocationId()) {
	    process.allocateFundsToUnit();
	}
    }

}
