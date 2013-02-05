/*
 * @(#)RoleType.java
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

import java.util.ResourceBundle;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.groups.IRoleEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * This class is now discontinued you should use the persistent groups
 * connected to the expenditure tracking system instead.
 * 
 * @author João Marques
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
@Deprecated
public enum RoleType implements IRoleEnum {

    ACQUISITION_CENTRAL, ACQUISITION_CENTRAL_MANAGER, ACCOUNTING_MANAGER, PROJECT_ACCOUNTING_MANAGER, MANAGER, TREASURY_MANAGER,
    SUPPLIER_MANAGER, SUPPLIER_FUND_ALLOCATION_MANAGER, STATISTICS_VIEWER, AQUISITIONS_UNIT_MANAGER, ACQUISITION_PROCESS_AUDITOR,
    FUND_COMMITMENT_MANAGER;

    @Override
    public String getRoleName() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        return virtualHost == null ? name() : name() + '@' + virtualHost.getHostname();
    }

    @Override
    public String getLocalizedName() {
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language.getLocale());
        return resourceBundle.getString(RoleType.class.getSimpleName() + "." + name());
    }

}
