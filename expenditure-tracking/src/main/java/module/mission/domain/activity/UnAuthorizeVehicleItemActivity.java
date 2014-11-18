/*
 * @(#)UnAuthorizeVehicleItemActivity.java
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
import module.mission.domain.MissionSystem;
import module.mission.domain.VehiclItem;

import org.fenixedu.bennu.core.domain.User;

/**
 * 
 * @author João Neves
 * 
 */
public class UnAuthorizeVehicleItemActivity extends MissionProcessActivity<MissionProcess, ItemActivityInformation> {

    @Override
    public String getUsedBundle() {
        return "resources/MissionResources";
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return missionProcess.canAuthorizeVehicles() && !missionProcess.getMission().hasAnyAllocatedFunds()
                && MissionSystem.getInstance().getVehicleAuthorizers().contains(user);
    }

    @Override
    public ItemActivityInformation getActivityInformation(MissionProcess process) {
        return new ItemActivityInformation(process, this) {
            @Override
            public boolean hasAllneededInfo() {
                return true;
            }
        };
    }

    @Override
    protected void process(final ItemActivityInformation activityInfo) {
        ((VehiclItem) activityInfo.getMissionItem()).unauthorize();
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(MissionProcess process, User user) {
        return false;
    }
}
