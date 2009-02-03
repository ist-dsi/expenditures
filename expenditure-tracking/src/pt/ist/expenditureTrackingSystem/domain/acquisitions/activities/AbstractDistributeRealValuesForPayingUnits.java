package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import myorg.domain.util.Money;
import pt.ist.fenixWebFramework.security.UserView;

public abstract class AbstractDistributeRealValuesForPayingUnits<T extends PaymentProcess> extends AbstractActivity<T> {

    public boolean isCurrentUserRequestor(T process) {
	User user = UserView.getUser();
	return user != null && process.getRequestor() == user.getPerson();
    }

    @Override
    protected void process(T process, Object... objects) {
	List<UnitItemBean> beans = (List<UnitItemBean>) objects[0];
	RequestItem item = (RequestItem) objects[1];
	Money amount = Money.ZERO;

	item.clearRealShareValues();

	for (UnitItemBean bean : beans) {
	    Money share = bean.getRealShareValue();
	    if (share == null) {
		throw new DomainException("activities.message.exception.monetaryValueMustBeFilled", getName());
	    }
	    amount = amount.add(share);
	    item.getUnitItemFor(bean.getUnit()).setRealShareValue(share);
	}

    }
}
