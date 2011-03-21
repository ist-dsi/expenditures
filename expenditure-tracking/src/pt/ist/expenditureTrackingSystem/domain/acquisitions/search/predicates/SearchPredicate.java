package pt.ist.expenditureTrackingSystem.domain.acquisitions.search.predicates;

import java.util.Collection;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.DomainObject;

public abstract class SearchPredicate {

    public abstract boolean evaluate(PaymentProcess process, SearchPaymentProcess searchBean);

    protected boolean matchCriteria(final Unit unit, final Set<Financer> financers) {
	if (unit == null) {
	    return true;
	}
	for (final Financer financer : financers) {
	    if (unit == financer.getUnit()) {
		return true;
	    }
	}
	return false;
    }

    protected boolean matchCriteria(final DomainObject object, final DomainObject otherDomainObject) {
	return object == null || object == otherDomainObject;
    }

    protected boolean matchCriteria(final Person requester, final Person person) {
	return requester == null || requester == person;
    }

    protected boolean matchCriteria(final String string, final String otherString) {
	return string == null || string.length() == 0 || string.equalsIgnoreCase(otherString);
    }

    protected boolean matchContainsCriteria(DomainObject object, Collection collection) {
	return object == null || collection.contains(object);
    }

}
