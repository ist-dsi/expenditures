/*
 * @(#)WorkingCapitalAcquisitionTransaction.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

import org.joda.time.DateTime;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalAcquisitionTransaction extends WorkingCapitalAcquisitionTransaction_Base {

    private static final String WORKING_CAPITAL_RESOURCES = "resources/WorkingCapitalResources";

    public WorkingCapitalAcquisitionTransaction() {
	super();
	setWorkingCapitalSystem(WorkingCapitalSystem.getInstanceForCurrentHost());
    }

    public WorkingCapitalAcquisitionTransaction(final WorkingCapitalAcquisition workingCapitalAcquisition, final Money value) {
	setWorkingCapital(workingCapitalAcquisition.getWorkingCapital());
	setWorkingCapitalAcquisition(workingCapitalAcquisition);
	addValue(value);
    }

    @Override
    public String getDescription() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return BundleUtil.getStringFromResourceBundle(WORKING_CAPITAL_RESOURCES, "label." + getClass().getName()) + ": "
		+ workingCapitalAcquisition.getAcquisitionClassification().getDescription();
    }

    @Override
    public boolean isAcquisition() {
	return true;
    }

    @Override
    public boolean isPendingApproval() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return !isCanceledOrRejected() && workingCapitalAcquisition.getApproved() == null
		&& workingCapitalAcquisition.getRejectedApproval() == null;
    }

    public boolean isPendingApprovalByUser() {
	final User user = UserView.getCurrentUser();
	return !isCanceledOrRejected() && isPendingApproval() && !getWorkingCapital().isCanceledOrRejected()
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
	return !isCanceledOrRejected() && isApproved() && workingCapitalAcquisition.getSubmitedForVerification() != null
		&& workingCapitalAcquisition.getVerifier() == null;
    }

    @Override
    public boolean isPendingSubmission() {
	final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
	return !isCanceledOrRejected() && isApproved() && workingCapitalAcquisition.getSubmitedForVerification() == null;
    }

    public boolean isPendingVerificationByUser() {
	final User user = UserView.getCurrentUser();
	return isPendingVerification() && !getWorkingCapital().isCanceledOrRejected()
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

    @Override
    public void addValue(final Money value) {
	Money limit = getWorkingCapitalSystem().getAcquisitionValueLimit();
	if ((limit != null) && (getValue().add(value).compareTo(limit) == 1)) {
	    throw new DomainException(BundleUtil.getStringFromResourceBundle(WORKING_CAPITAL_RESOURCES,
		    "error.acquisition.limit.exceeded"));
	}
	super.addValue(value);
    }

    @Override
    public void resetValue(final Money value) {
	Money limit = getWorkingCapitalSystem().getAcquisitionValueLimit();
	if ((limit != null) && (value.compareTo(limit) == 1)) {
	    throw new DomainException(BundleUtil.getStringFromResourceBundle(WORKING_CAPITAL_RESOURCES,
		    "error.acquisition.limit.exceeded"));
	}
	super.resetValue(value);
    }

}
