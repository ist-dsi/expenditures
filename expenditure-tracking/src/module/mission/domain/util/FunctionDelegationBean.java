package module.mission.domain.util;

import java.io.Serializable;

import module.organization.domain.Accountability;
import module.organization.domain.FunctionDelegation;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.joda.time.LocalDate;

public class FunctionDelegationBean implements Serializable {

    private Accountability accountability;
    private Unit unit;
    private Person person;
    private LocalDate beginDate = new LocalDate();
    private LocalDate endDate = beginDate.plusYears(1);
    private FunctionDelegation functionDelegation;

    public FunctionDelegationBean(final FunctionDelegation functionDelegation) {
	accountability = functionDelegation.getAccountabilityDelegator();
	unit = (Unit) functionDelegation.getAccountabilityDelegatee().getParent();
	person = (Person) functionDelegation.getAccountabilityDelegatee().getChild();
	beginDate = functionDelegation.getAccountabilityDelegatee().getBeginDate();
	endDate = functionDelegation.getAccountabilityDelegatee().getEndDate();
	this.functionDelegation = functionDelegation;
    }

    public FunctionDelegationBean(final Accountability accountability) {
	this.accountability = accountability;
    }

    public Accountability getAccountability() {
	return accountability;
    }

    public void setAccountability(Accountability accountability) {
	this.accountability = accountability;
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public LocalDate getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
	this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public void setFunctionDelegation(FunctionDelegation functionDelegation) {
	this.functionDelegation = functionDelegation;
    }

    public FunctionDelegation getFunctionDelegation() {
	return functionDelegation;
    }

}
