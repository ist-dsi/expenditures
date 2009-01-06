package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateRefundProcessBean implements Serializable {

    DomainReference<Person> requestor;
    DomainReference<Person> refundee;
    DomainReference<Unit> requestingUnit;
    private boolean requestUnitPayingUnit;

    public CreateRefundProcessBean(Person requestor) {
	setRequestor(requestor);
	setRefundee(null);
	setRequestingUnit(null);
	setRequestUnitPayingUnit(false);
    }

    public Person getRequestor() {
	return requestor.getObject();
    }

    public void setRequestor(Person requestor) {
	this.requestor = new DomainReference<Person>(requestor);
    }

    public Person getRefundee() {
	return refundee.getObject();
    }

    public void setRefundee(Person refundee) {
	this.refundee = new DomainReference<Person>(refundee);
    }

    public Unit getRequestingUnit() {
	return requestingUnit.getObject();
    }

    public void setRequestingUnit(Unit requestingUnit) {
	this.requestingUnit = new DomainReference<Unit>(requestingUnit);
    }

    public boolean isRequestUnitPayingUnit() {
	return requestUnitPayingUnit;
    }

    public void setRequestUnitPayingUnit(boolean requestUnitPayingUnit) {
	this.requestUnitPayingUnit = requestUnitPayingUnit;
    }

}
