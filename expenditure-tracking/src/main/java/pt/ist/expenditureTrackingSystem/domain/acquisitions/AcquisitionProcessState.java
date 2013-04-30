/*
 * @(#)AcquisitionProcessState.java
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
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AcquisitionProcessState extends AcquisitionProcessState_Base {

    protected AcquisitionProcessState() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public AcquisitionProcessState(final AcquisitionProcess process) {
        this();
        final Person person = getPerson();
        super.checkArguments(process, person);
        super.initFields(process, person);
        process.systemProcessRelease();
    }

    public AcquisitionProcessState(final AcquisitionProcess process, AcquisitionProcessStateType type) {
        this(process);
        if (type == null) {
            throw new DomainException("error.wrong.ProcessState.arguments");
        }
        setAcquisitionProcessStateType(type);
    }

    public String getLocalizedName() {
        return getAcquisitionProcessStateType().getLocalizedName();
    }

    public boolean isActive() {
        return getAcquisitionProcessStateType().isActive();
    }

    protected boolean isInState(AcquisitionProcessStateType state) {
        return getAcquisitionProcessStateType().equals(state);
    }

    public boolean isProcessed() {
        return isInState(AcquisitionProcessStateType.ACQUISITION_PROCESSED);
    }

    public boolean isPendingApproval() {
        return isInState(AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    public boolean isPendingFundAllocation() {
        return isInState(AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION);
    }

    public boolean isAuthorized() {
        return isInState(AcquisitionProcessStateType.AUTHORIZED);
    }

    public boolean isAllocatedToSupplier() {

        return !((AcquisitionProcess) getProcess()).getShouldSkipSupplierFundAllocation()
                && getAcquisitionProcessStateType().compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER) >= 0;
    }

    public boolean isInAllocatedToSupplierState() {
        return isInState(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    public boolean isAllocatedToUnit() {
        return getAcquisitionProcessStateType().compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED) >= 0;
    }

    public boolean isInAllocatedToUnitState() {
        return isInState(AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    public boolean isPayed() {
        return isInState(AcquisitionProcessStateType.ACQUISITION_PAYED);
    }

    public boolean isAcquisitionProcessed() {
        return isInState(AcquisitionProcessStateType.ACQUISITION_PROCESSED);
    }

    public boolean isInvoiceReceived() {
        return isInState(AcquisitionProcessStateType.INVOICE_RECEIVED);
    }

    public boolean isPastInvoiceReceived() {
        return isActive() && getAcquisitionProcessStateType().isInOrPastState(AcquisitionProcessStateType.INVOICE_RECEIVED);
    }

    public boolean isInvoiceConfirmed() {
        return isInState(AcquisitionProcessStateType.INVOICE_CONFIRMED);
    }

    public boolean isPendingInvoiceConfirmation() {
        return isInState(AcquisitionProcessStateType.SUBMITTED_FOR_CONFIRM_INVOICE);
    }

    public boolean isInGenesis() {
        return isInState(AcquisitionProcessStateType.IN_GENESIS);
    }

    public boolean isCanceled() {
        return isInState(AcquisitionProcessStateType.CANCELED);
    }

    public boolean isRejected() {
        return isInState(AcquisitionProcessStateType.REJECTED);
    }

    public boolean isAllocatedPermanently() {
        return isInState(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
    }

    public boolean hasBeenAllocatedPermanently() {
        return getAcquisitionProcessStateType().compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY) >= 0;
    }

    public boolean isInApprovedState() {
        return isPendingFundAllocation();
    }

    @Override
    public boolean isInFinalStage() {
        return isInState(AcquisitionProcessStateType.INVOICE_CONFIRMED)
                || isInState(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY)
                || isInState(AcquisitionProcessStateType.ACQUISITION_PAYED);
    }

    @Deprecated
    public boolean hasAcquisitionProcessStateType() {
        return getAcquisitionProcessStateType() != null;
    }

}
