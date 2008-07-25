package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequestItem extends AcquisitionRequestItem_Base {
    
    public AcquisitionRequestItem(final AcquisitionRequestInformation acquisitionRequestInformation) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	addAcquisitionRequestInformations(acquisitionRequestInformation);
    }

    public AcquisitionProcess getAcquisitionProcess() {
	for (final AcquisitionRequestInformation acquisitionRequestInformation : getAcquisitionRequestInformationsSet()) {
	    return acquisitionRequestInformation.getAcquisitionProcess();
	}
	return null;
    }

    public BigDecimal getTotalItemValue() {
	final BigDecimal unitValue = getUnitValue();
	final Integer quantity = getQuantity();
	return unitValue.multiply(new BigDecimal(quantity.intValue()));
    }

    @Service
    public void delete() {
	for (final AcquisitionRequestInformation acquisitionRequestInformation : getAcquisitionRequestInformationsSet()) {
	    removeAcquisitionRequestInformations(acquisitionRequestInformation);
	}
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }
    
}
