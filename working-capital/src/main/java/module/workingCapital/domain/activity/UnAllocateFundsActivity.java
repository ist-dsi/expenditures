/*
 * @(#)UnAllocateFundsActivity.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain.activity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.util.Bundle;

/**
 * 
 * @author João Neves
 * 
 */
public class UnAllocateFundsActivity extends WorkflowActivity<WorkingCapitalProcess, WorkingCapitalInitializationInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.WORKING_CAPITAL, "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess process, final User user) {
        final WorkingCapital workingCapital = process.getWorkingCapital();
        return workingCapital.isPendingFundUnAllocation(user)
                && (process.getCurrentOwner() == null || process.isTakenByCurrentUser());
    }

    @Override
    protected void process(final WorkingCapitalInitializationInformation activityInformation) {
        WorkingCapitalProcess process = activityInformation.getProcess();
        final WorkingCapitalInitialization workingCapitalInitialization =
                process.getWorkingCapital().getWorkingCapitalInitialization();
        workingCapitalInitialization.setFundAllocationId(null);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new WorkingCapitalInitializationInformation(process, this) {
            @Override
            public boolean hasAllneededInfo() {
                return true;
            }
        };
    }

    @Override
    public boolean isConfirmationNeeded(WorkingCapitalProcess process) {
        return true;
    }

    @Override
    public String getUsedBundle() {
        return Bundle.WORKING_CAPITAL;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isUserAwarenessNeeded(WorkingCapitalProcess process, User user) {
        return false;
    }

}
