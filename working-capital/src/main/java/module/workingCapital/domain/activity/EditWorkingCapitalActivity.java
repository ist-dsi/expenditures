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

import java.io.IOException;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.io.ByteStreams;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.ExceptionalWorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisition;
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.util.Bundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FundAllocationExpirationDate.FundAllocationNotAllowedException;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

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
        return BundleUtil.getString(Bundle.WORKING_CAPITAL, "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess process, final User user) {
        final WorkingCapital workingCapital = process.getWorkingCapital();
        return workingCapital.getMovementResponsible() != null
                && workingCapital.getMovementResponsible().getUser() == user
                && !workingCapital.isCanceledOrRejected()
                && (process.getCurrentOwner() == null || process.isTakenByCurrentUser());
    }

    public boolean isRegularAndApproved(WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction) {
        return !workingCapitalAcquisitionTransaction.isExceptionalAcquisition()
                && workingCapitalAcquisitionTransaction.isApproved();
    }

    public boolean isExceptionalAndManagementApproved(WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction) {
        return workingCapitalAcquisitionTransaction.isExceptionalAcquisition()
                && ((ExceptionalWorkingCapitalAcquisitionTransaction) workingCapitalAcquisitionTransaction)
                        .isManagementApproved();
    }

    @Override
    protected void process(final EditWorkingCapitalActivityInformation activityInformation) {
        final WorkingCapitalAcquisitionTransaction transaction = activityInformation.getWorkingCapitalAcquisitionTransaction();
        if (isRegularAndApproved(transaction) || isExceptionalAndManagementApproved(transaction)) {
            throw new DomainException(Bundle.WORKING_CAPITAL, "expense.already.approved.cant.edit");
        }
        final WorkingCapitalAcquisition workingCapitalAcquisition = transaction.getWorkingCapitalAcquisition();

        
        final Supplier supplier = (Supplier) activityInformation.getSupplier();
        final Money valueWithoutVat = activityInformation.getValueWithoutVat();
        if (!isRapid(workingCapitalAcquisition)
                && (!supplier.equals(workingCapitalAcquisition.getSupplier())
                        || valueWithoutVat.isGreaterThan(workingCapitalAcquisition.getValueWithoutVat()))
                && !supplier.isFundAllocationAllowed(valueWithoutVat)) {
            throw new FundAllocationNotAllowedException();
        }
        
        if (activityInformation.getInputStream() != null) {
            String displayName = activityInformation.getDisplayName();
            if (displayName == null) {
                displayName = activityInformation.getFilename();
            }
            try {
                workingCapitalAcquisition.edit(activityInformation.getDocumentNumber(), activityInformation.getSupplier(),
                        activityInformation.getDescription(), activityInformation.getAcquisitionClassification(),
                        valueWithoutVat, activityInformation.getMoney(),
                        ByteStreams.toByteArray(activityInformation.getInputStream()), displayName,
                        activityInformation.getFilename());
            } catch (IOException e) {
                throw new Error(e);
            }
        } else {
            workingCapitalAcquisition.edit(activityInformation.getDocumentNumber(), activityInformation.getSupplier(),
                    activityInformation.getDescription(), activityInformation.getAcquisitionClassification(),
                    valueWithoutVat, activityInformation.getMoney());
        }

    }

    private boolean isRapid(final WorkingCapitalAcquisition workingCapitalAcquisition) {
        return workingCapitalAcquisition.getRapid() != null && workingCapitalAcquisition.getRapid();
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
        return Bundle.WORKING_CAPITAL;
    }

    @Override
    protected String[] getArgumentsDescription(EditWorkingCapitalActivityInformation activityInformation) {
        String[] args = new String[1];
        Money limit = WorkingCapitalSystem.getInstanceForCurrentHost().getAcquisitionValueLimit();
        Money value = activityInformation.getMoney();
        if ((limit != null) && (value.compareTo(limit) == 1)) {
            args[0] =
                    "(" + BundleUtil.getString(Bundle.WORKING_CAPITAL, "label.exceptional") + ", "
                            + BundleUtil.getString(Bundle.WORKING_CAPITAL, "label.limit") + " = " + limit.getValue().toString()
                            + ")";
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
