/*
 * @(#)AllocateFundsActivityInformation.java
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

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AllocateFundsActivityInformation extends WorkingCapitalInitializationInformation {

    private String fundAllocationId;

    public AllocateFundsActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	setWorkingCapitalInitialization(workingCapitalInitialization);
    }

    @Override
    public void setWorkingCapitalInitialization(final WorkingCapitalInitialization workingCapitalInitialization) {
        super.setWorkingCapitalInitialization(workingCapitalInitialization);
        if (workingCapitalInitialization != null) {
            fundAllocationId = workingCapitalInitialization.getFundAllocationId();
        }
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput()
		&& super.hasAllneededInfo()
		&& fundAllocationId != null;
    }

    public String getFundAllocationId() {
        return fundAllocationId;
    }

    public void setFundAllocationId(String fundAllocationId) {
        this.fundAllocationId = fundAllocationId;
    }

}
