package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;

public class AllocateProjectFundsPermanently extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return process.isProjectAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.INVOICE_CONFIRMED)
		&& allItemsAreFilledWithRealValues(process)
		&& process.getAcquisitionRequest().isEveryItemFullyAttributeInRealValues()
		&& !process.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    private boolean allItemsAreFilledWithRealValues(AcquisitionProcess process) {
	for (AcquisitionRequestItem item : process.getAcquisitionRequest().getAcquisitionRequestItems()) {
	    if (!item.isFilledWithRealValues()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	if (!process.isRealValueEqualOrLessThanFundAllocation()) {
	    throw new DomainException("activities.message.exception.valuesCannotGoOverFundAllocation");
	}
	final List<FundAllocationBean> fundAllocationBeans = (List<FundAllocationBean>) objects[0];
	for (FundAllocationBean fundAllocationBean : fundAllocationBeans) {
	    final ProjectFinancer projectFinancer = (ProjectFinancer) fundAllocationBean.getFinancer();
	    projectFinancer.setEffectiveProjectFundAllocationId(fundAllocationBean.getEffectiveFundAllocationId());
	}
    }

}
