/*
 * @(#)RejectVerifyWorkingCapitalAcquisitionActivity.java
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
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.util.Bundle;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class RejectVerifyWorkingCapitalAcquisitionActivity extends
        WorkflowActivity<WorkingCapitalProcess, WorkingCapitalTransactionInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.WORKING_CAPITAL, "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
        final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
        return !workingCapital.isCanceledOrRejected() && workingCapital.hasAcquisitionPendingVerification(user);
    }

    @Override
    protected void process(final WorkingCapitalTransactionInformation activityInformation) {
        final WorkingCapitalAcquisitionTransaction workingCapitalTransaction =
                (WorkingCapitalAcquisitionTransaction) activityInformation.getWorkingCapitalTransaction();
        if (!workingCapitalTransaction.isPendingVerification()) {
            throw new DomainException(Bundle.WORKING_CAPITAL, "error.acquisition.already.verified");
        }
        workingCapitalTransaction.rejectVerify(getLoggedPerson());
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new WorkingCapitalTransactionInformation(process, this);
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
