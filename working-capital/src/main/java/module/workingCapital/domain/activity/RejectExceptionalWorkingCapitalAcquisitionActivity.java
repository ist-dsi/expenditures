/*
 * @(#)ApproveWorkingCapitalAcquisitionActivity.java
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

import module.organization.domain.Accountability;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.ExceptionalWorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class RejectExceptionalWorkingCapitalAcquisitionActivity extends
        WorkflowActivity<WorkingCapitalProcess, WorkingCapitalTransactionInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
                + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess process, final User user) {
        final Accountability accountability = process.getWorkingCapitalSystem().getManagementAccountability(user);
        final WorkingCapital workingCapital = process.getWorkingCapital();
        if (accountability != null && !workingCapital.isCanceledOrRejected() && workingCapital.hasAcquisitionPendingApproval()) {
            return true;
        }
        return false;
    }

    @Override
    protected void process(final WorkingCapitalTransactionInformation activityInformation) {
        final ExceptionalWorkingCapitalAcquisitionTransaction workingCapitalTransaction =
                (ExceptionalWorkingCapitalAcquisitionTransaction) activityInformation.getWorkingCapitalTransaction();
        workingCapitalTransaction.rejectByManagement(getLoggedPerson());
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
    public boolean isUserAwarenessNeeded(WorkingCapitalProcess process, User user) {
        final WorkingCapital workingCapital = process.getWorkingCapital();
        return !workingCapital.isCanceledOrRejected() && workingCapital.hasAcquisitionPendingDirectApproval(user);
    }

}
