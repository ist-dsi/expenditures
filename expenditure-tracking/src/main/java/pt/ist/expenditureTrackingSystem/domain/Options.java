/*
 * @(#)Options.java
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
package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class Options extends Options_Base {

    public Options(final Person person) {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setPerson(person);
        setDisplayAuthorizationPending(Boolean.FALSE);
        setRecurseAuthorizationPendingUnits(Boolean.FALSE);
        setReceiveNotificationsByEmail(Boolean.FALSE);
    }

    public void delete() {
        setExpenditureTrackingSystem(null);
        setPerson(null);
        deleteDomainObject();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
    }

    @Deprecated
    public boolean hasDisplayAuthorizationPending() {
        return getDisplayAuthorizationPending() != null;
    }

    @Deprecated
    public boolean hasRecurseAuthorizationPendingUnits() {
        return getRecurseAuthorizationPendingUnits() != null;
    }

    @Deprecated
    public boolean hasReceiveNotificationsByEmail() {
        return getReceiveNotificationsByEmail() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
