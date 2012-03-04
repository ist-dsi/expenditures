/*
 * @(#)CancelReenforceWorkingCapitalInitializationActivity.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalInitializationReenforcement;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class CancelReenforceWorkingCapitalInitializationActivity extends
	WorkflowActivity<WorkingCapitalProcess, ActivityInformation<WorkingCapitalProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
		+ getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	return workingCapital.hasMovementResponsible()
		&& workingCapital.getMovementResponsible().getUser() == user
		&& !workingCapital.isCanceledOrRejected()
		&& workingCapitalInitialization != null
		&& workingCapitalInitialization.isPendingAproval()
		&& !workingCapitalInitialization.isCanceledOrRejected()
		&& workingCapital.getWorkingCapitalInitializationsCount() > 1;
    }

    @Override
    protected void process(final ActivityInformation<WorkingCapitalProcess> activityInformation) {
	final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	workingCapitalInitialization.delete();
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
	return new ActivityInformation<WorkingCapitalProcess>(process, this);
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
        return false;
    }

}
