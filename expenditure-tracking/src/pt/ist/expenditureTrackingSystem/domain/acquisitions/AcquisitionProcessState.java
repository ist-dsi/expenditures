package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

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

    public boolean isApproved() {
	return isInState(AcquisitionProcessStateType.APPROVED);
    }

    public boolean isAllocatedToSupplier() {
	return getAcquisitionProcessStateType().compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER) >= 0;
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

    public boolean isAllocatedPermanently() {
	return isInState(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
    }

    public boolean hasBeenAllocatedPermanently() {
	return getAcquisitionProcessStateType().compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY) >= 0;
    }

}