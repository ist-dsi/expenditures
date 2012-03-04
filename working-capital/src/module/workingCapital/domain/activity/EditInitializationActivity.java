/*
 * @(#)EditInitializationActivity.java
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
import myorg.domain.User;
import myorg.util.BundleUtil;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class EditInitializationActivity extends WorkflowActivity<WorkingCapitalProcess, EditInitializationActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
		+ getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected() && workingCapital.isPendingAproval() && workingCapital.isRequester(user);
    }

    @Override
    protected void process(final EditInitializationActivityInformation activityInformation) {
	final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
	workingCapitalInitialization.getWorkingCapital().setMovementResponsible(activityInformation.getMovementResponsible());
	workingCapitalInitialization.setRequestedAnualValue(activityInformation.getRequestedMonthlyValue().multiply(12));
	workingCapitalInitialization.setFiscalId(activityInformation.getFiscalId());
	workingCapitalInitialization.setAcceptedResponsability(null);

	final String banOrIban = activityInformation.getInternationalBankAccountNumber();
	final String internationalBankAccountNumber = banOrIban == null || banOrIban.isEmpty()
		|| !Character.isDigit(banOrIban.charAt(0)) ? banOrIban : "PT50" + banOrIban;

	workingCapitalInitialization.setInternationalBankAccountNumber(internationalBankAccountNumber);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
	return new EditInitializationActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
	return false;
    }

}
