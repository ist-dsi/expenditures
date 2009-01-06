package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.Collection;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.util.DomainReference;

public class SearchRefundProcesses extends Search<RefundProcess> {

    private DomainReference<Person> refundee;
    private DomainReference<Person> requestingPerson;
    private DomainReference<Unit> requestingUnit;

    public SearchRefundProcesses() {
	setRefundee(null);
	setRequestingPerson(null);
	setRequestingUnit(null);
    }

    @Override
    public Set<RefundProcess> search() {
	try {
	    return new SearchResult(GenericProcess.getAllProcesses(RefundProcess.class));
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}
    }

    private class SearchResult extends SearchResultSet<RefundProcess> {

	public SearchResult(Collection<? extends RefundProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(RefundProcess process) {
	    RefundRequest request = process.getRequest();
	    Person refundee = request.getRefundee();
	    Person requestor = request.getRequestor();
	    Unit unit = request.getRequestingUnit();

	    return match(getRefundee(), refundee) && match(getRequestingPerson(), requestor) && match(getRequestingUnit(), unit);
	}

	private boolean match(Person searchPerson, Person person) {
	    return searchPerson == null || searchPerson == person;
	}

	private boolean match(Unit searchUnit, Unit unit) {
	    return searchUnit == null || searchUnit == unit;
	}
    }

    public Person getRefundee() {
	return refundee.getObject();
    }

    public void setRefundee(Person refundee) {
	this.refundee = new DomainReference<Person>(refundee);
    }

    public Person getRequestingPerson() {
	return requestingPerson.getObject();
    }

    public void setRequestingPerson(Person requestingPerson) {
	this.requestingPerson = new DomainReference<Person>(requestingPerson);
    }

    public Unit getRequestingUnit() {
	return requestingUnit.getObject();
    }

    public void setRequestingUnit(Unit requestingUnit) {
	this.requestingUnit = new DomainReference<Unit>(requestingUnit);
    }
}
