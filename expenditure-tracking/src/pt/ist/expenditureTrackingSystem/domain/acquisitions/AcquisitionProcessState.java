package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public abstract class AcquisitionProcessState extends AcquisitionProcessState_Base {

    protected AcquisitionProcessState() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public AcquisitionProcessState(final AcquisitionProcess process) {
	this();
	final Person person = getPerson();
	super.checkArguments(process, person);
	super.initFields(process, person);
    }

    public abstract  Enum getCurrentState();
    
    public abstract String getLocalizedName();

    public abstract boolean isActive();

    public abstract boolean isProcessed();

    public abstract boolean isPendingApproval();

    public abstract boolean isPendingFundAllocation();

    public abstract boolean isApproved();

    public abstract boolean isAllocatedToSupplier();

    public abstract boolean isInAllocatedToSupplierState();

    public abstract boolean isAllocatedToUnit();

    public abstract boolean isInAllocatedToUnitState();

    public abstract boolean isPayed();

    public abstract boolean isAcquisitionProcessed();

    public abstract boolean isInvoiceReceived();

    public abstract boolean isInvoiceConfirmed();

    public abstract boolean isPendingInvoiceConfirmation();

    public abstract boolean isInGenesis();

    public abstract boolean isCanceled();

    public abstract boolean isAllocatedPermanently();
    
    public abstract boolean hasBeenAllocatedPermanently();
}