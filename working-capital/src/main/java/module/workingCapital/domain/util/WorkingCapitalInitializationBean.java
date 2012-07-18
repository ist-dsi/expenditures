/*
 * @(#)WorkingCapitalInitializationBean.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain.util;

import java.io.Serializable;
import java.util.Calendar;

import module.organization.domain.Person;
import module.workingCapital.domain.WorkingCapitalInitialization;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalInitializationBean implements Serializable {

    private Unit unit;
    private Person person = initPerson();
    private Integer year = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    private Money requestedMonthlyValue;
    private String fiscalId;
    private String internationalBankAccountNumber;

    private Person initPerson() {
	final User user = UserView.getCurrentUser();
	return user == null ? null : user.getPerson();
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

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public Money getRequestedMonthlyValue() {
	return requestedMonthlyValue;
    }

    public void setRequestedMonthlyValue(Money requestedMonthlyValue) {
	this.requestedMonthlyValue = requestedMonthlyValue;
    }

    public String getFiscalId() {
	return fiscalId;
    }

    public void setFiscalId(String fiscalId) {
	this.fiscalId = fiscalId;
    }

    public String getInternationalBankAccountNumber() {
	return internationalBankAccountNumber;
    }

    public void setInternationalBankAccountNumber(String internationalBankAccountNumber) {
	this.internationalBankAccountNumber = internationalBankAccountNumber;
    }

    @Service
    public WorkingCapitalInitialization create() {
	String iban = internationalBankAccountNumber == null || internationalBankAccountNumber.isEmpty()
		|| !Character.isDigit(internationalBankAccountNumber.charAt(0)) ? internationalBankAccountNumber : "PT50"
		+ internationalBankAccountNumber;
	return new WorkingCapitalInitialization(year, unit, person, requestedMonthlyValue.multiply(6), fiscalId, iban);
    }

}
