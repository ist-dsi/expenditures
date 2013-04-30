/*
 * @(#)EmailDigester.java
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

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class EmailDigester extends EmailDigester_Base {

    public EmailDigester() {
        super();
    }

    @Override
    @Atomic
    public void executeTask() {
        for (final VirtualHost virtualHost : MyOrg.getInstance().getVirtualHostsSet()) {
            if (virtualHost.getSystemSender() == null || !virtualHost.getHostname().startsWith("dot")) {
                continue;
            }
            try {
                VirtualHost.setVirtualHostForThread(virtualHost);
                EmailDigesterUtil.executeTask();
            } finally {
                VirtualHost.releaseVirtualHostFromThread();
            }
        }
    }

    @Override
    public String getLocalizedName() {
        return getClass().getName();
    }
}
