/*
 * @(#)JumpToProcessStateInformation.java
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
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class JumpToProcessStateInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private AcquisitionProcessStateType acquisitionProcessStateType;

    public JumpToProcessStateInformation(SimplifiedProcedureProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public AcquisitionProcessStateType getAcquisitionProcessStateType() {
        return acquisitionProcessStateType;
    }

    public void setAcquisitionProcessStateType(AcquisitionProcessStateType acquisitionProcessStateType) {
        this.acquisitionProcessStateType = acquisitionProcessStateType;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getAcquisitionProcessStateType() != null;
    }

}
