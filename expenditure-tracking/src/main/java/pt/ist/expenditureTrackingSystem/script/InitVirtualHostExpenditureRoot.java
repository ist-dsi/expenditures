/*
 * @(#)InitVirtualHostExpenditureRoot.java
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
package pt.ist.expenditureTrackingSystem.script;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class InitVirtualHostExpenditureRoot extends WriteCustomTask {

    @Override
    protected void doService() {
	final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
	for (final VirtualHost virtualHost : MyOrg.getInstance().getVirtualHostsSet()) {
	    virtualHost.setExpenditureTrackingSystem(instance);
	}
    }

}
