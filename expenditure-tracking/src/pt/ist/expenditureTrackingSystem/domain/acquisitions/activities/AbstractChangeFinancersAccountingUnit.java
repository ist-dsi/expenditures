package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.Collection;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.ChangeFinancerAccountingUnitBean;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public abstract class AbstractChangeFinancersAccountingUnit<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected void process(T process, Object... objects) {
	Collection<ChangeFinancerAccountingUnitBean> financersBean = (Collection<ChangeFinancerAccountingUnitBean>) objects[0];
	for (ChangeFinancerAccountingUnitBean changeFinancerAccountingUnitBean : financersBean) {
	    changeFinancerAccountingUnitBean.getFinancer()
		    .setAccountingUnit(changeFinancerAccountingUnitBean.getAccountingUnit());
	}

    }

}
