/*
 * @(#)DeleteAfterTheFactAcquisitionProcess.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class DeleteAfterTheFactAcquisitionProcess extends
        WorkflowActivity<AfterTheFactAcquisitionProcess, ActivityInformation<AfterTheFactAcquisitionProcess>> {

    @Override
    public boolean isActive(AfterTheFactAcquisitionProcess process, User user) {
        final Person loggedPerson = Person.getLoggedPerson();
        final AcquisitionAfterTheFact acquisitionAfterTheFact = process.getAcquisitionAfterTheFact();

        return loggedPerson != null
                && ((ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user) || ExpenditureTrackingSystem
                        .isAcquisitionCentralManagerGroupMember(user)) || process.getProcessCreator().equals(user))
                && !acquisitionAfterTheFact.getDeletedState().booleanValue();
    }

    @Override
    protected void process(ActivityInformation<AfterTheFactAcquisitionProcess> activityInformation) {
        activityInformation.getProcess().delete();
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isConfirmationNeeded(AfterTheFactAcquisitionProcess process) {
        return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "message.confirm.delete.acquisition.process");
    }

    @Override
    public boolean isUserAwarenessNeeded(AfterTheFactAcquisitionProcess process, User user) {
        return false;
    }
}
