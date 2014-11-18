/*
 * @(#)RemoveRefundInvoice.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class RemoveRefundInvoice extends WorkflowActivity<RefundProcess, RemoveRefundInvoiceActivityInformation> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
        return process.getRequestor() == user.getExpenditurePerson() && isUserProcessOwner(process, user)
                && process.isInAuthorizedState() && process.isAnyRefundInvoiceAvailable();
    }

    /*
     * TODO: This should be only invoice removable when these invoices are as
     * files.
     */
    @Override
    protected void process(RemoveRefundInvoiceActivityInformation activityInformation) {
        activityInformation.getRefundInvoice().delete();
    }

    @Override
    public ActivityInformation<RefundProcess> getActivityInformation(RefundProcess process) {
        return new RemoveRefundInvoiceActivityInformation(process, this);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isConfirmationNeeded(RefundProcess process) {
        return true;
    }

}
