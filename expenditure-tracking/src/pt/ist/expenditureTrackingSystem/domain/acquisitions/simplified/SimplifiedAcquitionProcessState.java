package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.SimplifiedAcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SimplifiedAcquitionProcessState extends SimplifiedAcquitionProcessState_Base {

    public SimplifiedAcquitionProcessState(final AcquisitionProcess process, final SimplifiedAcquisitionProcessStateType state) {
	super();
	final Person person = getPerson();
	checkArguments(process, person, state);
	super.initFields(process, person);
	setAcquisitionProcessStateType(state);
    }

    protected void checkArguments(AcquisitionProcess process, Person person, SimplifiedAcquisitionProcessStateType state) {
	if (state == null) {
	    throw new DomainException("error.wrong.ProcessState.arguments");
	}
	super.checkArguments(process, person);
    }

    @Override
    public String getLocalizedName() {
	return getAcquisitionProcessStateType().getLocalizedName();
    }
    
    @Override
    public boolean isActive() {
	return getAcquisitionProcessStateType().isActive();
    }

    protected boolean isInState(SimplifiedAcquisitionProcessStateType state) {
	return getAcquisitionProcessStateType().equals(state);
    }

    @Override
    public boolean isProcessed() {
	return isInState(SimplifiedAcquisitionProcessStateType.ACQUISITION_PROCESSED);
    }

    @Override
    public boolean isPendingApproval() {
	return isInState(SimplifiedAcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    @Override
    public boolean isPendingFundAllocation() {
	return isInState(SimplifiedAcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION);
    }

    @Override
    public boolean isApproved() {
	return isInState(SimplifiedAcquisitionProcessStateType.APPROVED);
    }

    @Override
    public boolean isAllocatedToSupplier() {
	return getAcquisitionProcessStateType().compareTo(
		SimplifiedAcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER) >= 0;
    }

    @Override
    public boolean isInAllocatedToSupplierState() {
	return isInState(SimplifiedAcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    @Override
    public boolean isAllocatedToUnit() {
	return getAcquisitionProcessStateType().compareTo(SimplifiedAcquisitionProcessStateType.FUNDS_ALLOCATED) >= 0;
    }

    @Override
    public boolean isInAllocatedToUnitState() {
	return isInState(SimplifiedAcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    @Override
    public boolean isPayed() {
	return isInState(SimplifiedAcquisitionProcessStateType.ACQUISITION_PAYED);
    }

    @Override
    public boolean isAcquisitionProcessed() {
	return isInState(SimplifiedAcquisitionProcessStateType.ACQUISITION_PROCESSED);
    }

    @Override
    public boolean isInvoiceReceived() {
	return isInState(SimplifiedAcquisitionProcessStateType.INVOICE_RECEIVED);
    }

    @Override
    public boolean isInvoiceConfirmed() {
	return isInState(SimplifiedAcquisitionProcessStateType.INVOICE_CONFIRMED);
    }

    @Override
    public boolean isPendingInvoiceConfirmation() {
	return isInState(SimplifiedAcquisitionProcessStateType.SUBMITTED_FOR_CONFIRM_INVOICE);
    }

    @Override
    public boolean isInGenesis() {
	return isInState(SimplifiedAcquisitionProcessStateType.IN_GENESIS);
    }

    @Override
    public boolean isCanceled() {
	return isInState(SimplifiedAcquisitionProcessStateType.CANCELED);
    }

    @Override
    public boolean isAllocatedPermanently() {
	return isInState(SimplifiedAcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
    }
    
    @Override
    public boolean hasBeenAllocatedPermanently() {
	return getAcquisitionProcessStateType().compareTo(SimplifiedAcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY) >= 0;
    }

    @Override
    public Enum<SimplifiedAcquisitionProcessStateType> getCurrentState() {
	return getAcquisitionProcessStateType();
    }
}
