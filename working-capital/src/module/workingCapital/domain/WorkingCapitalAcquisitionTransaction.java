package module.workingCapital.domain;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

import org.joda.time.DateTime;

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
	    throw new DomainException(BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "error.insufficient.funds"));
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
	return !isCanceledOrRejected()
		&& workingCapitalAcquisition.getApproved() == null
		&& workingCapitalAcquisition.getRejectedApproval() == null;
    }

    public boolean isPendingApprovalByUser() {
	final User user = UserView.getCurrentUser();
	return !isCanceledOrRejected()
		&& isPendingApproval()
		&& !getWorkingCapital().isCanceledOrRejected()
		&& getWorkingCapital().hasAcquisitionPendingApproval(user);
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
    public void reject(final User user) {
        super.reject(user);
        final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
        workingCapitalAcquisition.reject(user);
    }

    @Override
    public void unApprove() {
	super.unApprove();
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	workingCapitalAcquisition.unApprove();
    }

    @Override
    public boolean isPendingVerification() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return !isCanceledOrRejected() && isApproved() && workingCapitalAcquisition.getSubmitedForVerification() != null && workingCapitalAcquisition.getVerifier() == null;
    }

    public boolean isPendingSubmission() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return !isCanceledOrRejected() && isApproved() && workingCapitalAcquisition.getSubmitedForVerification() == null;
    }


    public boolean isPendingVerificationByUser() {
	final User user = UserView.getCurrentUser();
	return isPendingVerification()
		&& !getWorkingCapital().isCanceledOrRejected()
		&& getWorkingCapital().hasAcquisitionPendingVerification(user);
    }

    @Override
    public boolean isVerified() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return workingCapitalAcquisition.getVerified() != null;
    }

    @Override
    public void verify(final User user) {
        super.approve(user);
        final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
        workingCapitalAcquisition.verify(user);
    }

    @Override
    public void rejectVerify(final User user) {
        super.rejectVerify(user);
        final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
        workingCapitalAcquisition.rejectVerify(user);

    }

    @Override
    public void unVerify() {
	super.unVerify();
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	workingCapitalAcquisition.unVerify();
    }

    @Override
    public boolean isPaymentRequested() {
	if (isVerified()) {
	    final WorkingCapital workingCapital = getWorkingCapital();
	    final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	    final DateTime verified = workingCapitalAcquisition.getVerified();
	    for (final WorkingCapitalRequest workingCapitalRequest : workingCapital.getWorkingCapitalRequestsSet()) {
		if (workingCapitalRequest.getRequestCreation().isAfter(verified)) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public boolean isCanceledOrRejected() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return workingCapitalAcquisition.isCanceledOrRejected();
    }

    @Override
    public void cancel() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	workingCapitalAcquisition.cancel();
	restoreDebtOfFollowingTransactions();
    }

}
