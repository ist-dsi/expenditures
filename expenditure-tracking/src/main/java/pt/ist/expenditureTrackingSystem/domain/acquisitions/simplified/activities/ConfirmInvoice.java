/*
 * @(#)ConfirmInvoice.java
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

import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class ConfirmInvoice extends WorkflowActivity<RegularAcquisitionProcess, ConfirmInvoiceActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        Person person = user.getExpenditurePerson();
        return isUserProcessOwner(process, user) && person != null && process.isActive() && !process.isInvoiceReceived()
                && (process.isAcquisitionProcessed() || process.isPendingInvoiceConfirmation())
                && !process.getUnconfirmedInvoices(person).isEmpty() && process.isResponsibleForUnit(person);
    }

    @Override
    protected void process(ConfirmInvoiceActivityInformation activityInformation) {
        final RegularAcquisitionProcess process = activityInformation.getProcess();
        process.confirmInvoiceBy(Authenticate.getUser().getExpenditurePerson());
        process.createFundAllocationRequest(true);
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
        return new ConfirmInvoiceActivityInformation(process, this);
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
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(final RegularAcquisitionProcess process, final User user) {
        final Person person = user.getExpenditurePerson();
        if (isActive(process, user) && person.hasAnyValidAuthorization()) {
            for (final RequestItem requestItem : process.getRequest().getRequestItemsSet()) {
                for (final UnitItem unitItem : requestItem.getUnitItemsSet()) {
                    final Unit unit = unitItem.getUnit();
                    for (final PaymentProcessInvoice invoice : requestItem.getInvoicesFilesSet()) {
                        if (!unitItem.getConfirmedInvoices().contains(invoice) && unit.isDirectResponsible(person)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
