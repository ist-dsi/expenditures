package pt.ist.expenditureTrackingSystem.domain.acquisitions.standard;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.StandardAcquisitionProcessStateType;

public class StandardAcquitionProcessState extends StandardAcquitionProcessState_Base {
    
    public  StandardAcquitionProcessState() {
        super();
    }

    @Override
    public String getLocalizedName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isAcquisitionProcessed() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isActive() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isAllocatedPermanently() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isAllocatedToSupplier() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isAllocatedToUnit() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isApproved() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isCanceled() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isInAllocatedToSupplierState() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isInAllocatedToUnitState() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isInGenesis() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isInvoiceConfirmed() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isInvoiceReceived() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isPayed() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isPendingApproval() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isPendingFundAllocation() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isPendingInvoiceConfirmation() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isProcessed() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public Enum<StandardAcquisitionProcessStateType> getCurrentState() {
	return getAcquisitionProcessStateType();
    }

    @Override
    public boolean hasBeenAllocatedPermanently() {
	// TODO Auto-generated method stub
	return false;
    }
    
}
