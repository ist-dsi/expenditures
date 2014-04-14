/*
 * @(#)ExternalRequestSyncTaskMgpIST.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the External Accounting Integration Module.
 *
 *   The External Accounting Integration Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The External Accounting Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the External Accounting Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.externalAccountingIntegration.domain;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ExternalRequestSyncTaskMgpIST extends ExternalRequestSyncTaskMgpIST_Base {

    public ExternalRequestSyncTaskMgpIST() {
        super();
    }

    @Override
    protected String getDbPropertyPrefix() {
        return "db.mgp.ist";
    }

    @Override
    protected String getVirtualHostName() {
        return "dot.tecnico.ulisboa.pt";
    }

}
