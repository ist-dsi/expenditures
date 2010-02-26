package module.workingCapital.domain.util;

import java.io.Serializable;
import java.util.Calendar;

import module.organization.domain.Person;
import module.workingCapital.domain.WorkingCapitalInitialization;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class WorkingCapitalInitializationBean implements Serializable {

    private Unit unit;
    private Person person;
    private Integer year = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    private Money requestedAnualValue;
    private String fiscalId;
    private String bankAccountId;

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
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Money getRequestedAnualValue() {
        return requestedAnualValue;
    }
    public void setRequestedAnualValue(Money requestedAnualValue) {
        this.requestedAnualValue = requestedAnualValue;
    }
    public String getFiscalId() {
        return fiscalId;
    }
    public void setFiscalId(String fiscalId) {
        this.fiscalId = fiscalId;
    }
    public String getBankAccountId() {
        return bankAccountId;
    }
    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    @Service
    public WorkingCapitalInitialization create() {
	return new WorkingCapitalInitialization(year, unit, person, requestedAnualValue, fiscalId, bankAccountId);
    }

}
