/*
 * @(#)FunctionDelegationBean.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package module.mission.domain.util;

import java.io.Serializable;

import module.organization.domain.Accountability;
import module.organization.domain.FunctionDelegation;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.joda.time.LocalDate;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
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
