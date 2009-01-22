package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateRefundProcessBean implements Serializable {

    private DomainReference<Person> requestor;
    private DomainReference<Person> refundee;
    private DomainReference<Unit> requestingUnit;
    private boolean requestUnitPayingUnit;
    private boolean externalPerson;
    private String refundeeName;
    private String refundeeFiscalCode;

    public CreateRefundProcessBean(Person requestor) {
	setRequestor(requestor);
	setRefundee(null);
	setRequestingUnit(null);
	setRequestUnitPayingUnit(true);
	setExternalPerson(false);
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

    public boolean isExternalPerson() {
	return externalPerson;
    }

    public void setExternalPerson(boolean externalPerson) {
	this.externalPerson = externalPerson;
    }

    public String getRefundeeName() {
	return refundeeName;
    }

    public void setRefundeeName(String refundeeName) {
	this.refundeeName = refundeeName;
    }

    public String getRefundeeFiscalCode() {
	return refundeeFiscalCode;
    }

    public void setRefundeeFiscalCode(String refundeeFiscalCode) {
	this.refundeeFiscalCode = refundeeFiscalCode;
    }

}
