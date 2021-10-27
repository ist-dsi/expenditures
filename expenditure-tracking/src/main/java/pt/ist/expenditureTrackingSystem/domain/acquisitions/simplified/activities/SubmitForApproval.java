/*
 * @(#)SubmitForApproval.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class SubmitForApproval extends
        WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        return user.getExpenditurePerson() == process.getRequestor() && isUserProcessOwner(process, user)
                && process.getAcquisitionProcessState().isInGenesis() && process.getAcquisitionRequest().isFilled()
                && process.getAcquisitionRequest().isEveryItemFullyAttributedToPayingUnits()
                && !process.getAcquisitionRequest().getHasDifferentPayingUnitTypology();
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
        final RegularAcquisitionProcess process = activityInformation.getProcess();
        if (process.isSimplifiedProcedureProcess()
                //&& ((SimplifiedProcedureProcess) process).getProcessClassification() != ProcessClassification.CT75000
                //&& !process.hasAcquisitionProposalDocument()
                && ((SimplifiedProcedureProcess) process).hasInvoiceFile()
                && process.getTotalValue().isGreaterThan(ExpenditureTrackingSystem.getInstance().getMaxValueStartedWithInvoive())) {
            throw new DomainException(Bundle.ACQUISITION,
                    "activities.message.exception.exceeded.limit.to.start.process.with.invoice");
        }

        process.submitForApproval();
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return Bundle.ACQUISITION;
    }
}
