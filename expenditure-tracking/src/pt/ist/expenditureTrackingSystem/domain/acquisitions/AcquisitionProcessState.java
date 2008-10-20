package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class AcquisitionProcessState extends AcquisitionProcessState_Base {

    protected AcquisitionProcessState() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public AcquisitionProcessState(final AcquisitionProcess process, final AcquisitionProcessStateType processStateType) {
	this();
	final Person person = getPerson();
	checkArguments(process, processStateType, person);
	super.initFields(process, person);
	setAcquisitionProcessStateType(processStateType);
    }

    public AcquisitionProcessState(final AcquisitionProcess process, final AcquisitionProcessStateType processStateType,
	    final String justification) {
	this(process, processStateType);
	if (!StringUtils.isEmpty(justification)) {
	    setJustification(justification);
	}
    }

    private void checkArguments(AcquisitionProcess acquisitionProcess, AcquisitionProcessStateType acquisitionProcessStateType,
	    Person person) {
	if (acquisitionProcessStateType == null) {
	    throw new DomainException("error.wrong.AcquisitionProcessState.arguments");
	}
	super.checkArguments(acquisitionProcess, person);
    }

    public String getLocalizedName() {
	return getAcquisitionProcessStateType().getLocalizedName();
    }

    public boolean isActive() {
	return getAcquisitionProcessStateType().isActive();
    }

    public boolean isInState(AcquisitionProcessStateType state) {
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

    public boolean isAllocatedToUnit() {
	return getAcquisitionProcessStateType().compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED) >= 0;
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
}
