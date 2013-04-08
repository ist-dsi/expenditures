/*
 * @(#)EditAfterTheFactAcquisition.java
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
public class EditAfterTheFactAcquisition extends
        WorkflowActivity<AfterTheFactAcquisitionProcess, EditAfterTheFactProcessActivityInformation> {

    @Override
    public boolean isActive(AfterTheFactAcquisitionProcess process, User user) {
        final Person loggedPerson = Person.getLoggedPerson();
        final AcquisitionAfterTheFact acquisitionAfterTheFact = process.getAcquisitionAfterTheFact();
        return loggedPerson != null
                && (ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user) || ExpenditureTrackingSystem
                        .isAcquisitionCentralManagerGroupMember(user))
                && !acquisitionAfterTheFact.getDeletedState().booleanValue();
    }

    @Override
    public ActivityInformation<AfterTheFactAcquisitionProcess> getActivityInformation(AfterTheFactAcquisitionProcess process) {
        return new EditAfterTheFactProcessActivityInformation(process, this);
    }

    @Override
    protected void process(EditAfterTheFactProcessActivityInformation activityInformation) {
        activityInformation.getProcess().edit(activityInformation.getAfterTheFactAcquisitionType(),
                activityInformation.getValue(), activityInformation.getVatValue(), activityInformation.getSupplier(),
                activityInformation.getDescription());
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
    public boolean isUserAwarenessNeeded(AfterTheFactAcquisitionProcess process, User user) {
        return false;
    }
}
