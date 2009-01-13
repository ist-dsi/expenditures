package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;

public class FundAllocation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process) && process.getAcquisitionProcessState().isInAllocatedToSupplierState()
		&& process.hasAllocatedFundsForAllProjectFinancers()
		&& !process.hasAllFundAllocationId(getUser().getPerson());
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	final List<FundAllocationBean> fundAllocationBeans = (List<FundAllocationBean>) objects[0];
	for (FundAllocationBean fundAllocationBean : fundAllocationBeans) {
	    fundAllocationBean.getFinancer().setFundAllocationId(fundAllocationBean.getFundAllocationId());
	}
	if (process.getAcquisitionRequest().hasAllFundAllocationId()) {
	    process.allocateFundsToUnit();
	}
    }

}
