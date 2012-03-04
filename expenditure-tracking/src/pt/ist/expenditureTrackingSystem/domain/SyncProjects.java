/*
 * @(#)SyncProjects.java
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

/**
 * 
 * @author Luis Cruz
 * 
 */
public class SyncProjects extends SyncProjects_Base {

    public SyncProjects() {
        super();
    }

    @Override
    public void executeTask() {
	try {
	    new SyncProjectsIST().syncData();
	    System.out.println("Done with IST");
	    new SyncProjectsISTid().syncData();
	    System.out.println("Done with IST-ID");
	    new SyncProjectsADIST().syncData();
	    System.out.println("Done with ADIST");
	} catch (final IOException e) {
	    throw new Error(e);
	} catch (final SQLException e) {
	    throw new Error(e);
	}
    }

    @Override
    public String getLocalizedName() {
	return getClass().getName();
    }

}
