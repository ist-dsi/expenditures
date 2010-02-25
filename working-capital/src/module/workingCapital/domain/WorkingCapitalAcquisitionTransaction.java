package module.workingCapital.domain;

import myorg.domain.User;
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

    @Override
    public boolean isPendingApproval() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return workingCapitalAcquisition.getApproved() == null;
    }

    @Override
    public boolean isApproved() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return workingCapitalAcquisition.getApproved() != null;
    }

    @Override
    public void approve(final User user) {
        super.approve(user);
        final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
        workingCapitalAcquisition.approve(user);
    }

    @Override
    public boolean isPendingVerification() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return isApproved() && workingCapitalAcquisition.getVerifier() == null;
    }

    @Override
    public boolean isVerified() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return workingCapitalAcquisition.getVerifier() == null;
    }

    @Override
    public void verify(final User user) {
        super.approve(user);
        final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
        workingCapitalAcquisition.verify(user);
    }

}
