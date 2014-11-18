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

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class TogleMissionNatureActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && RoleType.MANAGER.group().isMember(user)
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
                throw new DomainException(Bundle.EXPENDITURE, "The associated process do not have toggle permissions.");
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
