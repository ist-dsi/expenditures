package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.ChangeFinancerAccountingUnitBean;

public abstract class AbstractChangeFinancersAccountUnit<P extends PaymentProcess> extends
	WorkflowActivity<P, AbstractChangeFinancersAccountUnitActivityInformation<P>> {

    @Override
    protected void process(AbstractChangeFinancersAccountUnitActivityInformation<P> activityInformation) {
	for (ChangeFinancerAccountingUnitBean changeFinancerAccountingUnitBean : activityInformation.getBeans()) {
	    changeFinancerAccountingUnitBean.getFinancer()
		    .setAccountingUnit(changeFinancerAccountingUnitBean.getAccountingUnit());
	}

    }
}
