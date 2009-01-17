package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.util.Money;

public abstract class AbstractDistributeRealValuesForPayingUnits <T extends PaymentProcess> extends AbstractActivity<T> {
    
    @Override
    protected void process(T process, Object... objects) {
	List<UnitItemBean> beans = (List<UnitItemBean>) objects[0];
	RequestItem item = (RequestItem) objects[1];
	Money amount = Money.ZERO;

	item.clearRealShareValues();
	
	for (UnitItemBean bean : beans) {
	    Money share = bean.getRealShareValue();
	    amount = amount.add(share);
	    item.getUnitItemFor(bean.getUnit()).setRealShareValue(share);
	}

    }
}
