/*
 * @(#)PayAcquisition.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.Set;

import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class PayAcquisition<P extends RegularAcquisitionProcess> extends
		WorkflowActivity<P, PayAcquisitionActivityInformation<P>> {

	@Override
	public boolean isActive(P process, User user) {
		Person person = user.getExpenditurePerson();
		return isUserProcessOwner(process, user) && process.getAcquisitionProcessState().isAllocatedPermanently()
				&& (ExpenditureTrackingSystem.isTreasuryMemberGroupMember(user) || process.isTreasuryMember(person));
	}

	@Override
	protected void process(PayAcquisitionActivityInformation<P> activityInformation) {
		P process = activityInformation.getProcess();
		process.getAcquisitionRequest().setPaymentReference(activityInformation.getPaymentReference());
//	for (final PaymentReferenceBean bean : activityInformation.getBeans()) {
//	    final Financer financer = bean.getFinancer();
//	    final String diaryNumber = bean.getDiaryNumber();
//	    financer.addPaymentDiaryNumber(diaryNumber);
//	}
//	if (hasAllDiaryNumbers(process)) {
		process.acquisitionPayed();
//	}
	}

	private boolean hasAllDiaryNumbers(final P process) {
		final Set<Financer> financers = process.getFinancersWithFundsAllocated();
		if (financers.isEmpty()) {
			return false;
		}
		for (final Financer financer : financers) {
			if (financer.getPaymentDiaryNumber() == null || financer.getPaymentDiaryNumber().isEmpty()) {
				return false;
			}
			if (financer.getTransactionNumber() == null || financer.getTransactionNumber().isEmpty()) {
				return false;
			}

		}
		return true;
	}

	@Override
	public PayAcquisitionActivityInformation<P> getActivityInformation(P process) {
		return new PayAcquisitionActivityInformation<P>(process, this);
	}

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
	}

	@Override
	public String getUsedBundle() {
		return "resources/AcquisitionResources";
	}

	@Override
	public boolean isDefaultInputInterfaceUsed() {
		return false;
	}

}
