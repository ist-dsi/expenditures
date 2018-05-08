/*
 * @(#)CancelAcquisitionRequest.java
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
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class CancelAcquisitionRequest extends
        WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        AcquisitionProcessState acquisitionProcessState = process.getAcquisitionProcessState();
        Person person = user.getExpenditurePerson();
        return isUserProcessOwner(process, user)
                && ((acquisitionProcessState.isAcquisitionProcessed() && ExpenditureTrackingSystem
                        .isAcquisitionCentralGroupMember(user))
                        || (acquisitionProcessState.isInGenesis() && (process.getRequestor() == person
                                || ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)))
                        || (acquisitionProcessState.isInAllocatedToUnitState() && isUserResponsibleForAuthorizingPayment(process,
                                person)));
    }

    private boolean isUserResponsibleForAuthorizingPayment(RegularAcquisitionProcess process, Person person) {

        return person != null
                && process.isResponsibleForUnit(person, process.getAcquisitionRequest()
                        .getTotalItemValueWithAdditionalCostsAndVat())
                && !process.getAcquisitionRequest().hasBeenAuthorizedBy(person);
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
        activityInformation.getProcess().cancel();
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
    public boolean isConfirmationNeeded(RegularAcquisitionProcess process) {
        return true;
    }

    @Override
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process, User user) {
        return false;
    }
}
