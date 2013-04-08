/*
 * @(#)TogleMissionNatureActivity.java
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
import module.workflow.activities.ActivityInformation;
import module.workflow.domain.ActivityLog;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class TogleMissionNatureActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && user.hasRoleType(RoleType.MANAGER)
                && missionProcess.canTogleMissionNature();
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
        final MissionProcess missionProcess = activityInformation.getProcess();
        final Mission mission = missionProcess.getMission();
        final boolean booleanValue = mission.getGrantOwnerEquivalence().booleanValue();
        mission.setGrantOwnerEquivalence(Boolean.valueOf(!booleanValue));
        for (MissionProcess associatedProcess : missionProcess.getAssociatedMissionProcesses()) {
            Mission associatedMission = associatedProcess.getMission();
            if (isActive(associatedProcess)) {
                associatedMission.setGrantOwnerEquivalence(Boolean.valueOf(!booleanValue));
            } else {
                throw new DomainException("The associated process do not have toggle permissions.");
            }

        }
    }

    @Override
    protected ActivityLog logExecution(MissionProcess thisProcess, String operationName, User user,
            ActivityInformation<MissionProcess> activityInfo, String... argumentsDescription) {
        for (MissionProcess associatedProcess : thisProcess.getAssociatedMissionProcesses()) {
            super.logExecution(associatedProcess, operationName, user, activityInfo, argumentsDescription);
        }
        return super.logExecution(thisProcess, operationName, user, activityInfo, argumentsDescription);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new ActivityInformation<MissionProcess>(process, this);
    }

}
