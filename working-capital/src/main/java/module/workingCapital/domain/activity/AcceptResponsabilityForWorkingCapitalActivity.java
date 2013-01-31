/*
 * @(#)AcceptResponsabilityForWorkingCapitalActivity.java
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
import module.workingCapital.domain.WorkingCapitalProcess;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AcceptResponsabilityForWorkingCapitalActivity extends
		WorkflowActivity<WorkingCapitalProcess, WorkingCapitalInitializationInformation> {

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
				+ getClass().getSimpleName());
	}

	@Override
	public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
		final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
		return !workingCapital.isCanceledOrRejected() && workingCapital.isMovementResponsible(user)
				&& workingCapital.isPendingAcceptResponsability();
	}

	@Override
	protected void process(final WorkingCapitalInitializationInformation activityInformation) {
		final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
		workingCapitalInitialization.setAcceptedResponsability(new DateTime());
	}

	@Override
	public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
		final WorkingCapitalInitializationInformation workingCapitalInitializationInformation =
				new WorkingCapitalInitializationInformation(process, this);
		final WorkingCapital workingCapital = process.getWorkingCapital();
		final WorkingCapitalInitialization workingCapitalInitialization =
				workingCapital.getSortedWorkingCapitalInitializations().last();
		workingCapitalInitializationInformation.setWorkingCapitalInitialization(workingCapitalInitialization);
		return workingCapitalInitializationInformation;
	}

	@Override
	public boolean isDefaultInputInterfaceUsed() {
		return true;
	}

	@Override
	public boolean isConfirmationNeeded(final WorkingCapitalProcess process) {
		return true;
	}

	@Override
	public String getUsedBundle() {
		return "resources/WorkingCapitalResources";
	}

	@Override
	public String getLocalizedConfirmationMessage() {
		return BundleUtil.getFormattedStringFromResourceBundle("resources/WorkingCapitalResources",
				"label.module.workingCapital.initialization.accept.confirmation");
	}

}
