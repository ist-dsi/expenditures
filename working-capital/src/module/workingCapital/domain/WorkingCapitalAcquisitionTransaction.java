package module.workingCapital.domain;

import myorg.domain.util.Money;

public class WorkingCapitalAcquisitionTransaction extends WorkingCapitalAcquisitionTransaction_Base {
    
    public WorkingCapitalAcquisitionTransaction() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
    }

    public WorkingCapitalAcquisitionTransaction(final WorkingCapitalAcquisition workingCapitalAcquisition, final Money value) {
	setWorkingCapital(workingCapitalAcquisition.getWorkingCapital());
	setWorkingCapitalAcquisition(workingCapitalAcquisition);
	addValue(value);
    }

}
