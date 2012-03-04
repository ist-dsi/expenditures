/*
 * @(#)WorkingCapitalAcquisitionSubmission.java
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

import java.util.Set;
import java.util.TreeSet;

import module.organization.domain.Person;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalAcquisitionSubmission extends WorkingCapitalAcquisitionSubmission_Base {

    public WorkingCapitalAcquisitionSubmission() {
	super();
    }

    public WorkingCapitalAcquisitionSubmission(final WorkingCapital workingCapital, final Person person, final Money value,
	    final boolean isPaymentRequired) {
	this();
	setWorkingCapital(workingCapital);
	setPerson(person);
	setValue(value);
	setPaymentRequired(isPaymentRequired);
    }

    @Override
    public boolean isSubmission() {
	return true;
    }

    public Set<WorkingCapitalAcquisitionTransaction> getWorkingCapitalAcquisitionTransactionsSorted() {
	TreeSet<WorkingCapitalAcquisitionTransaction> sortedAcquisitions = new TreeSet<WorkingCapitalAcquisitionTransaction>(
		COMPARATOR_BY_NUMBER);
	sortedAcquisitions.addAll(getWorkingCapitalAcquisitionTransactionsSet());
	return sortedAcquisitions;
    }

    @Override
    public String getDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "label." + getClass().getName());
    }

    @Override
    public Money getAccumulatedValue() {
	return Money.ZERO;
    }

    @Override
    protected void restoreDebtOfFollowingTransactions(final Money debtValue, final Money accumulatedValue) {
	restoreDebt(debtValue, accumulatedValue);
	final WorkingCapitalTransaction workingCapitalTransaction = getNext();
	if (workingCapitalTransaction != null) {
	    workingCapitalTransaction.restoreDebtOfFollowingTransactions(debtValue, Money.ZERO);
	}
    }

    @Override
    public void restoreDebt(Money debtValue, Money accumulatedValue) {
	super.restoreDebt(debtValue, accumulatedValue);
	setValue(getValue().subtract(accumulatedValue));
    }

}
