/*
 * @(#)FundAllocationRequest.java
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

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixframework.DomainObject;

/**
 * 
 * @author Luis Cruz
 * 
 */
public abstract class FundAllocationRequest extends FundAllocationRequest_Base {

    public boolean isFinalFundAllocation() {
        return getFinalFundAllocation().booleanValue();
    }

    public void registerFundAllocation(final String fundAllocationNumber, final String operatorUsername) {
        setFundAllocationNumber(fundAllocationNumber);
        setOperatorUsername(operatorUsername);
        setExternalAccountingIntegrationSystemFromPendingResult(null);
    }

    public void cancelFundAllocationRequest() {
        if (!isCanceled()) {
            new CancelFundAllocationRequest(this);
            setExternalAccountingIntegrationSystemFromPendingResult(null);
        }
    }

    public boolean isCanceled() {
        return (getCancelFundAllocationRequest() != null);
    }

    protected String getProcessUrl(final DomainObject process) {
        final StringBuilder result = new StringBuilder();
        result.append("https://");
        result.append(VirtualHost.getVirtualHostForThread().getHostname());
        result.append("/ForwardToProcess/");
        result.append(process.getExternalId());
        return result.toString();
    }

}
