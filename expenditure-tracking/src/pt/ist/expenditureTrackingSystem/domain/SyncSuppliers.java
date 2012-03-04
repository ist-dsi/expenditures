/*
 * @(#)SyncSuppliers.java
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

import java.io.IOException;
import java.sql.SQLException;

import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Pedro Santos
 * @author Luis Cruz
 * 
 */
public class SyncSuppliers extends SyncSuppliers_Base {

    public SyncSuppliers() {
	super();
    }

    @Override
    public void executeTask() {
	try {
	    syncData();
	} catch (final Exception e) {
	    throw new Error(e);
	}
    }

    @Service
    private void syncData() throws IOException, SQLException {
	SyncSuppliersAux.syncData();
    }

    @Override
    public String getLocalizedName() {
	return getClass().getName();
    }

}
