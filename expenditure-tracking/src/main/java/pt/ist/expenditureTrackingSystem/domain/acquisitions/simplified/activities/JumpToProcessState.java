/*
 * @(#)JumpToProcessState.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class JumpToProcessState extends WorkflowActivity<SimplifiedProcedureProcess, JumpToProcessStateInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
	return user.hasRoleType(RoleType.MANAGER);
    }

    @Override
    protected void process(JumpToProcessStateInformation activityInformation) {
	final SimplifiedProcedureProcess simplifiedProcedureProcess = activityInformation.getProcess();
	final AcquisitionProcessStateType acquisitionProcessStateType = activityInformation.getAcquisitionProcessStateType();
	new AcquisitionProcessState(simplifiedProcedureProcess, acquisitionProcessStateType);
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
        return new JumpToProcessStateInformation(process, this);
    }

    @Override
    protected String[] getArgumentsDescription(JumpToProcessStateInformation activityInformation) {
        return new String[] { activityInformation.getAcquisitionProcessStateType().getLocalizedName() };
    }

    @Override
    public boolean isUserAwarenessNeeded(SimplifiedProcedureProcess process, User user) {
	return false;
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
