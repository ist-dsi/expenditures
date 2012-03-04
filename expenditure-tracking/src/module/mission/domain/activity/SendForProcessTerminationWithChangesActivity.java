/*
 * @(#)SendForProcessTerminationWithChangesActivity.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.User;
import myorg.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class SendForProcessTerminationWithChangesActivity extends
	MissionProcessActivity<MissionProcess, SendForProcessTerminationWithChangesActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user) && missionProcess.isReadyForMissionTermination(user);
    }

    @Override
    protected void process(final SendForProcessTerminationWithChangesActivityInformation activityInformation) {
	final MissionProcess missionProcess = (MissionProcess) activityInformation.getProcess();
	missionProcess.sendForProcessTermination(activityInformation.getDescriptionOfChangesAfterArrival());
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(MissionProcess process) {
	return new SendForProcessTerminationWithChangesActivityInformation(process, this);
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
	return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
		"label.module.mission.SendForProcessTerminationWithChangesActivity.confirmation")
		+ "<br/>"
		+ "<br/>"
		+ BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
			"label.module.mission.SendForProcessTerminationWithChangesActivity.confirmation.next.page");
    }

}
