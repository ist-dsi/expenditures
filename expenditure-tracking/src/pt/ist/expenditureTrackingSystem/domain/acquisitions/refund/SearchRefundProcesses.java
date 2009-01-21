package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.Collection;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.util.DomainReference;

public class SearchRefundProcesses extends Search<RefundProcess> {

    private String refundeeName;
    private DomainReference<Person> requestingPerson;
    private DomainReference<Unit> requestingUnit;

    public SearchRefundProcesses() {
	setRefundeeName(null);
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
	    String refundeeName = request.getRefundee().getName();
	    Person requestor = request.getRequester();
	    Unit unit = request.getRequestingUnit();

	    return match(getRefundeeName(), refundeeName) && match(getRequestingPerson(), requestor)
		    && match(getRequestingUnit(), unit);
	}

	private boolean match(String searchString, String string) {
	    return searchString == null || searchString.length() == 0 || searchString.equalsIgnoreCase(string);
	}

	private boolean match(Person searchPerson, Person person) {
	    return searchPerson == null || searchPerson == person;
	}

	private boolean match(Unit searchUnit, Unit unit) {
	    return searchUnit == null || searchUnit == unit;
	}
    }

    public String getRefundeeName() {
	return refundeeName;
    }

    public void setRefundeeName(String refundee) {
	this.refundeeName = refundee;
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
