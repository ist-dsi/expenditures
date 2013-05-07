/*
 * @(#)EditWorkingCapitalActivity.java
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
import module.workingCapital.domain.ExceptionalWorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisition;
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.InputStreamUtil;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class EditWorkingCapitalActivity extends WorkflowActivity<WorkingCapitalProcess, EditWorkingCapitalActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
                + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess workingCapitalProcess, final User user) {
        final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
        return workingCapital.hasMovementResponsible() && workingCapital.getMovementResponsible().getUser() == user
                && !workingCapital.isCanceledOrRejected();
    }

    @Override
    protected void process(final EditWorkingCapitalActivityInformation activityInformation) {
        final WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction =
                activityInformation.getWorkingCapitalAcquisitionTransaction();
        boolean isExceptional = workingCapitalAcquisitionTransaction instanceof ExceptionalWorkingCapitalAcquisitionTransaction;
        if (!((!isExceptional && workingCapitalAcquisitionTransaction.isPendingApproval()) || (isExceptional && ((ExceptionalWorkingCapitalAcquisitionTransaction) workingCapitalAcquisitionTransaction)
                .isPendingManagementApproval()))) {
            throw new DomainException("expense.already.approved.cant.edit");
        }
        final WorkingCapitalAcquisition workingCapitalAcquisition =
                workingCapitalAcquisitionTransaction.getWorkingCapitalAcquisition();

        if (activityInformation.getInputStream() != null) {
            String displayName = activityInformation.getDisplayName();
            if (displayName == null) {
                displayName = activityInformation.getFilename();
            }
            workingCapitalAcquisition.edit(activityInformation.getDocumentNumber(), activityInformation.getSupplier(),
                    activityInformation.getDescription(), activityInformation.getAcquisitionClassification(),
                    activityInformation.getValueWithoutVat(), activityInformation.getMoney(),
                    InputStreamUtil.consumeInputStream(activityInformation.getInputStream()), displayName,
                    activityInformation.getFilename());
        } else {
            workingCapitalAcquisition.edit(activityInformation.getDocumentNumber(), activityInformation.getSupplier(),
                    activityInformation.getDescription(), activityInformation.getAcquisitionClassification(),
                    activityInformation.getValueWithoutVat(), activityInformation.getMoney());
        }

    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new EditWorkingCapitalActivityInformation(process, this);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getUsedBundle() {
        return "resources/WorkingCapitalResources";
    }

    @Override
    protected String[] getArgumentsDescription(EditWorkingCapitalActivityInformation activityInformation) {
        String[] args = new String[1];
        Money limit = WorkingCapitalSystem.getInstanceForCurrentHost().getAcquisitionValueLimit();
        Money value = activityInformation.getMoney();
        if ((limit != null) && (value.compareTo(limit) == 1)) {
            args[0] =
                    "(" + BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "label.exceptional") + ", "
                            + BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "label.limit") + " = "
                            + limit.getValue().toString() + ")";
        } else {
            args[0] = "";
        }
        return args;
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
        return false;
    }

}
