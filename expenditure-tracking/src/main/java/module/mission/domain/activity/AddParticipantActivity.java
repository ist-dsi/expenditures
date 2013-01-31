/*
 * @(#)AddParticipantActivity.java
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

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AddParticipantActivity extends MissionProcessActivity<MissionProcess, ParticipantActivityInformation> {

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
	}

	@Override
	public boolean isActive(final MissionProcess missionProcess, final User user) {
		return super.isActive(missionProcess, user) && missionProcess.isUnderConstruction() && missionProcess.isRequestor(user);
	}

	@Override
	protected void process(final ParticipantActivityInformation participantActivityInformation) {
		final MissionProcess missionProcess = participantActivityInformation.getProcess();
		final Mission mission = missionProcess.getMission();
		final Person person = participantActivityInformation.getPerson();
		mission.addParticipantes(person);
	}

	@Override
	public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
		return new ParticipantActivityInformation(process, this);
	}

	@Override
	public boolean isVisible() {
		return false;
	}

}
