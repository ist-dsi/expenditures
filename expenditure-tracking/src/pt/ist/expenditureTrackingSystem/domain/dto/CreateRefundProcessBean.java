package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateRefundProcessBean implements Serializable {

    private DomainReference<Person> requestor;
    private DomainReference<Person> refundee;
    private DomainReference<Unit> requestingUnit;
    private boolean requestUnitPayingUnit = true;
    private boolean externalPerson = false;
    private String refundeeName;
    private String refundeeFiscalCode;

    public CreateRefundProcessBean(final Person requestor) {
	setRequestor(requestor);
    }

    public Person getRequestor() {
	return requestor.getObject();
    }

    public void setRequestor(Person requestor) {
	this.requestor = requestor == null ? null : new DomainReference<Person>(requestor);
    }

    public Person getRefundee() {
	return refundee == null ? null : refundee.getObject();
    }

    public void setRefundee(Person refundee) {
	this.refundee = refundee == null ? null : new DomainReference<Person>(refundee);
    }

    public Unit getRequestingUnit() {
	return requestingUnit == null ? null : requestingUnit.getObject();
    }

    public void setRequestingUnit(Unit requestingUnit) {
	this.requestingUnit = requestingUnit == null ? null : new DomainReference<Unit>(requestingUnit);
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
