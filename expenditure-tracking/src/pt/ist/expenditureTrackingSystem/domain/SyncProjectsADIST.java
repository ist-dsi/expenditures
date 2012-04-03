/*
 * @(#)SyncProjectsADIST.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;


/**
 * 
 * @author Luis Cruz
 * 
 */
public class SyncProjectsADIST extends SyncProjectsAux {

    private final static Money AUTHORIZED_VALUE = new Money("5000");

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.adist";
    }

    @Override
    protected String getVirtualHost() {
	return "dot.adist.ist.utl.pt";
    }

    @Override
    protected Money getAuthorizationValue() {
	return AUTHORIZED_VALUE;
    }

    @Override
    protected Unit findCostCenter(final String costCenterString) {
	final ExpenditureTrackingSystem ets = ExpenditureTrackingSystem.getInstance();
	if (ets.hasAnyTopLevelUnits()) {
	    return ets.getTopLevelUnitsIterator().next();
	}

	System.out.println("No top level unit configured for virtual host: " + getVirtualHost());
	return null;
    }

    @Override
    protected Boolean isDefaultRegeimIsCCP(final String type) {
	return Boolean.FALSE;
    }

    @Override
    protected boolean hasExpenditureAuthorizationDelegation(final String responsibleString) {
	return true;
    }

}
