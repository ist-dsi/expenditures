/*
 * @(#)UnApprove.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AdvancePaymentDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class UnApprove<P extends PaymentProcess> extends WorkflowActivity<P, ActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
        Person person = user.getExpenditurePerson();
        return process.isResponsibleForUnit(person) && isUserProcessOwner(process, user)
                && ((process.isPendingApproval() && process.getRequest().hasBeenApprovedBy(person))
                        || process.isInApprovedState());
    }

    @Override
    protected void process(ActivityInformation<P> activityInformation) {
        final Person loggedPerson = Person.getLoggedPerson();
        final PaymentProcess process = activityInformation.getProcess();
        process.getRequest().unapprove(loggedPerson);
        if (process instanceof SimplifiedProcedureProcess) {
            AdvancePaymentDocument advancePaymentDocument = ((SimplifiedProcedureProcess) process).getAdvancePaymentDocument();
            if (advancePaymentDocument != null) {
                advancePaymentDocument.delete();
            }
        }
        process.submitForApproval();
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
    public boolean isUserAwarenessNeeded(P process, User user) {
        return false;
    }

}
