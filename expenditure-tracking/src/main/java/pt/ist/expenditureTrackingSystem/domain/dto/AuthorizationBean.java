/*
 * @(#)AuthorizationBean.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AuthorizationBean implements Serializable {

	private Authorization authorization;
	private Unit unit;
	private Person person;
	private LocalDate startDate;
	private LocalDate endDate;
	private Boolean canDelegate;
	private Money maxAmount;
	private boolean returnToUnitInterface = false;
	private String justification;

	public AuthorizationBean(final Person person, final Unit unit, boolean returnToUnitInterface) {
		setPerson(person);
		setUnit(unit);
		setAuthorization(null);
		setStartDate(new LocalDate());
		setCanDelegate(Boolean.FALSE);
		setReturnToUnitInterface(returnToUnitInterface);
	}

	public AuthorizationBean(Authorization authorization) {
		setAuthorization(authorization);
		setPerson(null);
		setCanDelegate(Boolean.FALSE);
		setEndDate(authorization.getEndDate());
	}

	public Authorization getAuthorization() {
		return authorization;
	}

	public void setAuthorization(Authorization authorization) {
		this.authorization = authorization;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Boolean getCanDelegate() {
		return canDelegate;
	}

	public void setCanDelegate(Boolean caDelegate) {
		this.canDelegate = caDelegate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setMaxAmount(Money maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Money getMaxAmount() {
		return maxAmount;
	}

	public boolean isReturnToUnitInterface() {
		return returnToUnitInterface;
	}

	public void setReturnToUnitInterface(boolean returnToUnitInterface) {
		this.returnToUnitInterface = returnToUnitInterface;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

}
