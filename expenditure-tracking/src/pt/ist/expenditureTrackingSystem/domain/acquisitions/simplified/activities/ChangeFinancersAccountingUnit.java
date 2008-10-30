package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.Collection;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.dto.ChangeFinancerAccountingUnitBean;

public class ChangeFinancersAccountingUnit extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return process.isAccountingEmployeeForOnePossibleUnit();
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInAllocatedToSupplierState()
		&& process.hasAllocatedFundsForAllProjectFinancers()
		&& process.getAcquisitionRequest().hasAnyAccountingUnitFinancerWithNoFundsAllocated(getUser().getPerson());
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	Collection<ChangeFinancerAccountingUnitBean> financersBean = (Collection<ChangeFinancerAccountingUnitBean>) objects[0];
	for (ChangeFinancerAccountingUnitBean changeFinancerAccountingUnitBean : financersBean) {
	    changeFinancerAccountingUnitBean.getFinancer()
		    .setAccountingUnit(changeFinancerAccountingUnitBean.getAccountingUnit());
	}
    }

}
