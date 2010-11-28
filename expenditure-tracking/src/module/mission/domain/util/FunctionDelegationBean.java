package module.mission.domain.util;

import java.io.Serializable;

import module.organization.domain.Accountability;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.joda.time.LocalDate;

public class FunctionDelegationBean implements Serializable {

    private Accountability accountability;
    private Unit unit;
    private Person person;
    private LocalDate beginDate = new LocalDate();
    private LocalDate endDate = beginDate.plusYears(1);

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

}
