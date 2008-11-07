package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;

public class AllocateFundsPermanently extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return process.isAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.getAcquisitionProcessState().isInvoiceConfirmed() && allItemsAreFilledWithRealValues(process)
		&& process.getAcquisitionRequest().isEveryItemFullyAttributeInRealValues()
		&& process.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    private boolean allItemsAreFilledWithRealValues(RegularAcquisitionProcess process) {
	for (AcquisitionRequestItem item : process.getAcquisitionRequest().getAcquisitionRequestItems()) {
	    if (!item.isFilledWithRealValues()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	if (!process.isRealValueEqualOrLessThanFundAllocation()) {
	    throw new DomainException("activities.message.exception.valuesCannotGoOverFundAllocation");
	}
	final List<FundAllocationBean> fundAllocationBeans = (List<FundAllocationBean>) objects[0];
	for (FundAllocationBean fundAllocationBean : fundAllocationBeans) {
	    fundAllocationBean.getFinancer().addEffectiveFundAllocationId(fundAllocationBean.getEffectiveFundAllocationId());
	}
	process.allocateFundsPermanently();
    }

}
