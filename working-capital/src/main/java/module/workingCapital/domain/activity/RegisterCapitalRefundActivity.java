/*
 * @(#)RegisterCapitalRefundActivity.java
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

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalRefund;
import module.workingCapital.domain.util.PaymentMethod;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class RegisterCapitalRefundActivity extends
		WorkflowActivity<WorkingCapitalProcess, RegisterCapitalRefundActivityInformation> {

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
				+ getClass().getSimpleName());
	}

	@Override
	public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
		final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
		final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
		return workingCapitalInitialization != null && workingCapitalInitialization.getRefundRequested() != null
				&& workingCapital.isTreasuryMember(user) && workingCapital.getBalance().isPositive();
	}

	@Override
	protected void process(final RegisterCapitalRefundActivityInformation activityInformation) {
		if (activityInformation.isConfirmed()) {
			final WorkingCapitalProcess process = activityInformation.getProcess();
			final WorkingCapital workingCapital = process.getWorkingCapital();
			final Person person = getLoggedPerson().getPerson();
			final PaymentMethod paymentMethod = activityInformation.getPaymentMethod();
			new WorkingCapitalRefund(workingCapital, person, workingCapital.getBalance(), paymentMethod);
		}
	}

	@Override
	public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
		return new RegisterCapitalRefundActivityInformation(process, this);
	}

	@Override
	public boolean isDefaultInputInterfaceUsed() {
		return false;
	}

}
