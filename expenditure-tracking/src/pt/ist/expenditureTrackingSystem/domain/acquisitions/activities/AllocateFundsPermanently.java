package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class AllocateFundsPermanently<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(final T process) {
	return isCurrentUserProcessOwner(process) && process.hasAllocatedFundsPermanentlyForAllProjectFinancers()
		&& !process.hasAllInvoicesAllocated();
    }

    @Override
    protected void process(final T process, final Object... objects) {
	final List<FundAllocationBean> fundAllocationBeans = (List<FundAllocationBean>) objects[0];
	for (FundAllocationBean fundAllocationBean : fundAllocationBeans) {
	    fundAllocationBean.getFinancer().addEffectiveFundAllocationId(fundAllocationBean.getEffectiveFundAllocationId());
	}
	if (process.isInvoiceConfirmed()) {
	    process.allocateFundsPermanently();
	}
    }

}
