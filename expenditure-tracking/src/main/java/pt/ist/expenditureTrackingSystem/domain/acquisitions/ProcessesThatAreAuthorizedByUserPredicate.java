/*
 * @(#)ProcessesThatAreAuthorizedByUserPredicate.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.function.Predicate;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ProcessesThatAreAuthorizedByUserPredicate implements Predicate {

    private final Person person;

    public ProcessesThatAreAuthorizedByUserPredicate(Person person) {
        this.person = person;
    }

    @Override
    public boolean test(Object arg0) {
        PaymentProcess process = (PaymentProcess) arg0;
        if (process.getRequest() == null) {
            return false;
        }
        final Collection<Financer> financers = process.getRequest().getFinancers();

        for (final Financer financer : financers) {
            final Unit unit = financer.getUnit();
            if (evaluate(unit, process)) {
                return true;
            }
        }
        return false;
    }

    private boolean evaluate(Unit unit, PaymentProcess process) {
        if (unit.hasAuthorizationsFor(person)) {
            return true;
        } else {
            for (Authorization authorization : unit.getAuthorizations()) {
                Person person = authorization.getPerson();
                if (process.hasAnyAvailableActivity(person.getUser(), true)) {
                    return false;
                }
            }
            return unit.hasParentUnit() && evaluate(unit.getParentUnit(), process);
        }
    }

}
