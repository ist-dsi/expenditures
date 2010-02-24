package module.workingCapital.domain;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class WorkingCapitalAcquisition extends WorkingCapitalAcquisition_Base {
    
    public WorkingCapitalAcquisition() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
    }

    public WorkingCapitalAcquisition(final WorkingCapital workingCapital, final String documentNumber, final Supplier supplier,
	    final String description, final AcquisitionClassification acquisitionClassification, final Money valueWithoutVat, final Money money) {
	setWorkingCapital(workingCapital);
	setDocumentNumber(documentNumber);
	setAcquisitionClassification(acquisitionClassification);
	setSupplier(supplier);
	setDescription(description);
	setValueWithoutVat(valueWithoutVat);
	new WorkingCapitalAcquisitionTransaction(this, money);
    }
    
}
