/*
 * @(#)ConfirmInvoiceActivityInformation.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class ConfirmInvoiceActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private static final long serialVersionUID = 1L;

    public ConfirmInvoiceActivityInformation(RegularAcquisitionProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        final User currentUser = Authenticate.getUser();
        final RegularAcquisitionProcess process = getProcess();
        final Set<AcquisitionInvoice> unconfirmedInvoices = process.getUnconfirmedInvoices(currentUser.getExpenditurePerson());
        for (AcquisitionInvoice unconfirmedInvoice : unconfirmedInvoices) {
            if (!StringUtils.isEmpty(unconfirmedInvoice.getConfirmationReport())) {
                return isForwardedFromInput();
            }
        }
        return true;
    }

}
