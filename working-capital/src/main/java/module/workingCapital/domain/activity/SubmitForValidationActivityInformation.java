/*
 * @(#)SubmitForValidationActivityInformation.java
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

import org.joda.time.DateTime;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class SubmitForValidationActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

	private boolean lastSubmission = new DateTime().getMonthOfYear() == 12;
	private boolean paymentRequired = true;

	public SubmitForValidationActivityInformation(WorkingCapitalProcess process,
			WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
		super(process, activity);
	}

	public boolean isLastSubmission() {
		return lastSubmission;
	}

	public void setLastSubmission(boolean lastSubmission) {
		this.lastSubmission = lastSubmission;
	}

	public void setPaymentRequired(boolean paymentRequired) {
		this.paymentRequired = paymentRequired;
	}

	public boolean isPaymentRequired() {
		return paymentRequired;
	}

	@Override
	public boolean hasAllneededInfo() {
		return super.hasAllneededInfo() && isForwardedFromInput();
	}

}
