/*
 * @(#)ReenforceWorkingCapitalInitializationActivityInformation.java
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
import module.workflow.domain.WorkflowProcess;
import module.workingCapital.domain.WorkingCapitalProcess;
import pt.ist.bennu.core.domain.util.Money;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ReenforceWorkingCapitalInitializationActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

    private Money amount = null;

    public ReenforceWorkingCapitalInitializationActivityInformation(WorkingCapitalProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && isForwardedFromInput() && amount != null && amount.isPositive();
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

}
