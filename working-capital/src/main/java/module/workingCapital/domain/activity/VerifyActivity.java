/*
 * @(#)VerifyActivity.java
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
import module.workingCapital.domain.WorkingCapitalInitializationReenforcement;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.util.Bundle;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class VerifyActivity extends WorkflowActivity<WorkingCapitalProcess, VerifyActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.WORKING_CAPITAL, "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
        final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
        return !workingCapital.isCanceledOrRejected() && workingCapital.isPendingVerification(user);
    }

    @Override
    protected void process(final VerifyActivityInformation activityInformation) {
        final User user = getLoggedPerson();
        final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
        workingCapitalInitialization.verify(user, activityInformation.getAuthorizedAnualValue(),
                activityInformation.getMaxAuthorizedAnualValue(), activityInformation.getFundAllocationId());
        if (workingCapitalInitialization instanceof WorkingCapitalInitializationReenforcement) {
            final WorkingCapitalInitializationReenforcement workingCapitalInitializationReenforcement =
                    (WorkingCapitalInitializationReenforcement) workingCapitalInitialization;
            workingCapitalInitializationReenforcement.setAuthorizedReenforcementValue(activityInformation
                    .getAuthorizedReenforcementValue());
        }
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new VerifyActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
