/*
 * @(#)ExternalRequestSyncTask.java
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

import java.sql.SQLException;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.dbUtils.ExternalDbQuery;

/**
 * 
 * @author Luis Cruz
 * 
 */
public abstract class ExternalRequestSyncTask extends ExternalRequestSyncTask_Base {

    public ExternalRequestSyncTask() {
        super();
    }

    @Override
    protected void doOperation() throws SQLException {
        boolean hasOtherRequest = false;
        for (final ExternalRequest externalRequest : ExternalAccountingIntegrationSystem.getInstance()
                .getExternalRequestPendingResultSet()) {
            if (externalRequest.canShareTransaction()) {
                if (!externalRequest.isReadOnly()) {
                    hasOtherRequest = true;
                }
                executeQuery(externalRequest);
            } else if (!hasOtherRequest) {
                executeQuery(externalRequest);
                return;
            }
        }
    }

    protected void setVirtualHost() {
        final String virtualHostName = getVirtualHostName();
        VirtualHost.setVirtualHostForThread(virtualHostName);
    }

    protected abstract String getVirtualHostName();

    @Override
    public String getLocalizedName() {
        return BundleUtil.getFormattedStringFromResourceBundle("resources.ExternalAccountingIntegrationResources",
                "label.task.ExternalRequestSyncTask", getDbPropertyPrefix());
    }

    @Override
    public void executeTask() {
        try {
            setVirtualHost();
            super.executeTask();
        } finally {
            VirtualHost.releaseVirtualHostFromThread();
        }
    }

    @Override
    protected void handle(final SQLException e, final ExternalDbQuery externalDbQuery) {
        final ExternalRequest externalRequest = (ExternalRequest) externalDbQuery;
        externalRequest.handle(e);
    }

}
