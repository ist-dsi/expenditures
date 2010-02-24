package module.workingCapital.domain;

import myorg.domain.exceptions.DomainException;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

public class WorkingCapitalAcquisitionTransaction extends WorkingCapitalAcquisitionTransaction_Base {
    
    public WorkingCapitalAcquisitionTransaction() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
    }

    public WorkingCapitalAcquisitionTransaction(final WorkingCapitalAcquisition workingCapitalAcquisition, final Money value) {
	setWorkingCapital(workingCapitalAcquisition.getWorkingCapital());
	setWorkingCapitalAcquisition(workingCapitalAcquisition);
	addValue(value);
	if (getBalance().isNegative()) {
	    throw new DomainException("error.insufficient.funds");
	}
    }

    @Override
    public String getDescription() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "label." + getClass().getName())
		+ ": " + workingCapitalAcquisition.getAcquisitionClassification().getDescription();
    }

    @Override
    public boolean isAcquisition() {
	return true;
    }

}
