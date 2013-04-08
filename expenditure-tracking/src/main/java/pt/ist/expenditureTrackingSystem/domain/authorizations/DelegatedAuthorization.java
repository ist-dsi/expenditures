/*
 * @(#)DelegatedAuthorization.java
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
package pt.ist.expenditureTrackingSystem.domain.authorizations;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class DelegatedAuthorization extends DelegatedAuthorization_Base {

    DelegatedAuthorization(Person delegatedPerson, Unit unit, Authorization authorization, Boolean canDelegate,
            LocalDate endDate, Money maxAmount) {
        super();
        checkParameters(authorization, delegatedPerson, unit, canDelegate, endDate, maxAmount);
        setPerson(delegatedPerson);
        setAuthorization(authorization);
        setCanDelegate(canDelegate);
        setUnit(unit);
        setStartDate(new LocalDate());
        setEndDate(endDate);
        setMaxAmount(maxAmount);
    }

    private void checkParameters(Authorization authorization, Person delegatedPerson, Unit unit, Boolean canDelegate,
            LocalDate endDate, Money maxAmount) {
        if (authorization == null || delegatedPerson == null || canDelegate == null || unit == null) {
            throw new DomainException("error.authorization.person.and.delegation.are.required");
        }

        if (authorization.getEndDate() != null
                && ((endDate != null && endDate.isAfter(authorization.getEndDate()) || (authorization.getEndDate() != null && endDate == null)))) {
            throw new DomainException("error.authorization.cannot.be.delegated.after.your.end.date");
        }

        if (!unit.isSubUnit(authorization.getUnit())) {
            throw new DomainException("error.unit.is.not.subunit.of.authorization.unit");
        }

        if (maxAmount == null || maxAmount.isGreaterThan(authorization.getMaxAmount())) {
            throw new DomainException("error.maxAmount.cannot.be.greater.than.authorization.maxAmount");
        }

    }

    public Person getDelegatingPerson() {
        return getAuthorization().getPerson();
    }

    @Service
    public static void delegate(Authorization authorization, Person person, Unit unit, Boolean canDelegate, LocalDate endDate,
            Money maxAmount) {
        new DelegatedAuthorization(person, unit, authorization, canDelegate, endDate, maxAmount);
    }

    @Override
    public boolean isPersonAbleToRevokeDelegatedAuthorization(Person person) {
        return getDelegatingPerson() == person || getAuthorization().isPersonAbleToRevokeDelegatedAuthorization(person);
    }

    @Override
    public void delete() {
        removeAuthorization();
        super.delete();
    }

}
