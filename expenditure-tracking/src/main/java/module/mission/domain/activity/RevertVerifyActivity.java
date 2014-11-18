/*
 * @(#)RevertVerifyActivity.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
import module.mission.domain.MissionSystem;
import module.mission.domain.util.MissionState;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * 
 * @author João Neves
 * 
 */
public class RevertVerifyActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        if (!super.isActive(missionProcess, user)) {
            return false;
        }
        if (!MissionSystem.canUserVerifyProcesses(user)) {
            return false;
        }
        if (MissionState.VEHICLE_AUTHORIZATION.isRequired(missionProcess)) {
            return MissionState.VEHICLE_AUTHORIZATION.isPending(missionProcess);
        } else if (MissionState.FUND_ALLOCATION.isRequired(missionProcess)) {
            return MissionState.FUND_ALLOCATION.isPending(missionProcess) && !missionProcess.isCanceled();
        } else {
            return MissionState.PARTICIPATION_AUTHORIZATION.isPending(missionProcess);
        }
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
        MissionProcess process = activityInformation.getProcess();
        process.getMission().setIsVerified(false);
        process.addToVerificationQueue();
    }
}
