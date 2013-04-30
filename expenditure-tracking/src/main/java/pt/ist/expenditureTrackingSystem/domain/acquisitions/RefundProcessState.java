/*
 * @(#)RefundProcessState.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class RefundProcessState extends RefundProcessState_Base {

    protected RefundProcessState(final RefundProcess process) {
        super();
        final Person person = getPerson();
        super.checkArguments(process, person);
        super.initFields(process, person);
        process.systemProcessRelease();

    }

    public RefundProcessState(final RefundProcess refundProcess, final RefundProcessStateType refundProcessStateType) {
        this(refundProcess);
        if (refundProcessStateType == null) {
            throw new DomainException("error.wrong.ProcessState.arguments");
        }
        setRefundProcessStateType(refundProcessStateType);
    }

    public String getLocalizedName() {
        return getRefundProcessStateType().getLocalizedName();
    }

    public boolean isInGenesis() {
        return getRefundProcessStateType() == RefundProcessStateType.IN_GENESIS;
    }

    public boolean isPendingApproval() {
        return getRefundProcessStateType() == RefundProcessStateType.SUBMITTED_FOR_APPROVAL;
    }

    public boolean isInApprovedState() {
        return getRefundProcessStateType() == RefundProcessStateType.APPROVED;
    }

    public boolean isInAllocatedToUnitState() {
        return getRefundProcessStateType() == RefundProcessStateType.FUNDS_ALLOCATED;
    }

    public boolean isAuthorized() {
        return getRefundProcessStateType() == RefundProcessStateType.AUTHORIZED;
    }

    public boolean isPendingInvoicesConfirmation() {
        return getRefundProcessStateType() == RefundProcessStateType.SUBMITTED_FOR_INVOICE_CONFIRMATION;
    }

    public boolean isActive() {
        return getRefundProcessStateType().isActive();
    }

    public boolean isInvoiceConfirmed() {
        return getRefundProcessStateType() == RefundProcessStateType.INVOICES_CONFIRMED;
    }

    public boolean isAllocatedPermanently() {
        return getRefundProcessStateType() == RefundProcessStateType.FUNDS_ALLOCATED_PERMANENTLY;
    }

    public boolean isInSubmittedForInvoiceConfirmationState() {
        return getRefundProcessStateType() == RefundProcessStateType.SUBMITTED_FOR_INVOICE_CONFIRMATION;
    }

    public boolean hasFundsAllocatedPermanently() {
        return getRefundProcessStateType() == RefundProcessStateType.FUNDS_ALLOCATED_PERMANENTLY;
    }

    public boolean isCanceled() {
        return getRefundProcessStateType() == RefundProcessStateType.CANCELED;
    }

    @Override
    public boolean isInFinalStage() {
        final RefundProcessStateType refundProcessStateType = getRefundProcessStateType();
        return refundProcessStateType == RefundProcessStateType.INVOICES_CONFIRMED
                || refundProcessStateType == RefundProcessStateType.FUNDS_ALLOCATED_PERMANENTLY
                || refundProcessStateType == RefundProcessStateType.REFUNDED;
    }

    @Deprecated
    public boolean hasRefundProcessStateType() {
        return getRefundProcessStateType() != null;
    }

}
