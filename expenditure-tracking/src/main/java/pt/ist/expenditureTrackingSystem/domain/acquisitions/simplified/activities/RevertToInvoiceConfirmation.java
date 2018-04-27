/*
 * @(#)RevertToInvoiceConfirmation.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class RevertToInvoiceConfirmation extends WorkflowActivity<SimplifiedProcedureProcess, RevertToInvoiceConfirmationInformation> {

    private boolean allItemsAreFilledWithRealValues(final RegularAcquisitionProcess process) {
        for (final RequestItem requestItem : process.getRequest().getRequestItemsSet()) {
            if (!requestItem.isFilledWithRealValues()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isActive(final SimplifiedProcedureProcess process, final User user) {
        return process.isInvoiceConfirmed()
                && isUserProcessOwner(process, user)
                && (process.isProjectAccountingEmployee() || process.isAccountingEmployee())
                && allItemsAreFilledWithRealValues(process) && process.getRequest().isEveryItemFullyAttributeInRealValues()
                && !process.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(final SimplifiedProcedureProcess process) {
        return new RevertToInvoiceConfirmationInformation(process, this);
    }

    @Override
    protected void process(RevertToInvoiceConfirmationInformation activityInformation) {
        activityInformation.getProcess().unconfirmInvoiceForAll(activityInformation.getInvoice());
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
    public boolean isUserAwarenessNeeded(SimplifiedProcedureProcess process, User user) {
        return false;
    }

}
