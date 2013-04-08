/*
 * @(#)ChangeAccountingUnitActivity.java
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

import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ChangeAccountingUnitActivity extends MissionProcessActivity<MissionProcess, ChangeAccountingUnitActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        final Mission mission = missionProcess.getMission();
        return super.isActive(missionProcess, user)
                && (missionProcess.isUnderConstruction() && missionProcess.isRequestor(user) || user
                        .hasRoleType(RoleType.MANAGER)) && mission.getFinancerCount() > 0;
    }

    @Override
    protected void process(final ChangeAccountingUnitActivityInformation activityInformation) {
        final MissionFinancer financer = activityInformation.getFinancer();
        final AccountingUnit accountingUnit = activityInformation.getAccountingUnit();
        financer.setAccountingUnit(accountingUnit);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new ChangeAccountingUnitActivityInformation(process, this);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
