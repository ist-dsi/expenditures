/*
 * @(#)WorkingCapitalTransaction.java
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

import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;
import module.workingCapital.domain.util.WorkingCapitalConsistencyException;

import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.util.Money;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalTransaction extends WorkingCapitalTransaction_Base {

    public static Comparator<WorkingCapitalTransaction> COMPARATOR_BY_NUMBER = new Comparator<WorkingCapitalTransaction>() {

        @Override
        public int compare(final WorkingCapitalTransaction o1, final WorkingCapitalTransaction o2) {
            int c = o1.getNumber().compareTo(o2.getNumber());
            return c == 0 ? o2.getExternalId().compareTo(o1.getExternalId()) : c;
        }

    };

    public WorkingCapitalTransaction() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstanceForCurrentHost());
        setTransationInstant(new DateTime());
    }

    @ConsistencyPredicate(WorkingCapitalConsistencyException.class)
    public boolean checkBalancePositive() {
        return !getBalance().isNegative();
    }

    @ConsistencyPredicate(WorkingCapitalConsistencyException.class)
    public boolean checkBalanceEqualsDebt() {
        return getBalance().equals(getDebt());
    }

    @Override
    public void setWorkingCapital(final WorkingCapital workingCapital) {
        final WorkingCapitalTransaction workingCapitalTransaction = workingCapital.getLastTransaction();
        int count = workingCapital.getWorkingCapitalTransactions().size();
        super.setWorkingCapital(workingCapital);
        setNumber(Integer.valueOf(count + 1));
        setValue(Money.ZERO);
        if (workingCapitalTransaction == null) {
            setAccumulatedValue(Money.ZERO);
            setBalance(Money.ZERO);
            setDebt(Money.ZERO);
        } else {
            setAccumulatedValue(workingCapitalTransaction.getAccumulatedValue());
            setBalance(workingCapitalTransaction.getBalance());
            setDebt(workingCapitalTransaction.getDebt());
        }
    }

    public void addDebt(final Money value) {
        setBalance(getBalance().add(value));
        setDebt(getDebt().add(value));
    }

    public void restoreDebt(final Money debtValue, final Money accumulatedValue) {
        setAccumulatedValue(getAccumulatedValue().subtract(accumulatedValue));
        setBalance(getBalance().add(debtValue));
        setDebt(getDebt().add(debtValue));
    }

    public void addValue(final Money value) {
        setValue(getValue().add(value));
        setAccumulatedValue(getAccumulatedValue().add(value));
        setBalance(getBalance().subtract(value));
        setDebt(getDebt().subtract(value));
    }

    public void resetValue(final Money value) {
        final Money diffValue = value.subtract(getValue());
        setValue(value);
        restoreDebtOfFollowingTransactions(diffValue.multiply(-1), diffValue.multiply(-1));
    }

    public String getDescription() {
        return "";
    }

    public boolean isPayment() {
        return false;
    }

    public boolean isSubmission() {
        return false;
    }

    public boolean isAcquisition() {
        return false;
    }

    public boolean isRefund() {
        return false;
    }

    public boolean isExceptionalAcquisition() {
        return false;
    }

    public boolean isExceptionalRefund() {
        return false;
    }

    public boolean isLastTransaction() {
        final WorkingCapital workingCapital = getWorkingCapital();
        return workingCapital.getLastTransaction() == this;
    }

    public boolean isPendingApproval() {
        return false;
    }

    public boolean isApproved() {
        return false;
    }

    public void approve(final User user) {
    }

    public WorkingCapitalTransaction getNext() {
        final int current = getNumber().intValue();
        final WorkingCapital workingCapital = getWorkingCapital();
        for (final WorkingCapitalTransaction workingCapitalTransaction : workingCapital.getWorkingCapitalTransactionsSet()) {
            if (workingCapitalTransaction.getNumber().intValue() == current + 1) {
                return workingCapitalTransaction;
            }
        }
        return null;
    }

    protected void restoreDebtOfFollowingTransactions(final Money debtValue, final Money accumulatedValue) {
        restoreDebt(debtValue, accumulatedValue);
        final WorkingCapitalTransaction workingCapitalTransaction = getNext();
        if (workingCapitalTransaction != null) {
            workingCapitalTransaction.restoreDebtOfFollowingTransactions(debtValue, accumulatedValue);
        }
    }

    protected void restoreDebtOfFollowingTransactions() {
        restoreDebtOfFollowingTransactions(getValue(), getValue());
    }

    public void reject(User loggedPerson) {
        restoreDebtOfFollowingTransactions();
    }

    public boolean isPendingVerification() {
        return false;
    }

    public boolean isVerified() {
        return false;
    }

    public void verify(final User user) {
    }

    public void rejectVerify(User loggedPerson) {
        restoreDebtOfFollowingTransactions();
    }

    public void unVerify() {
    }

    public void unApprove() {
    }

    public boolean isPaymentRequested() {
        return false;
    }

    public boolean isCanceledOrRejected() {
        return false;
    }

    public void cancel() {
        throw new NotImplementedException();
    }

    public boolean isPendingSubmission() {
        return false;
    }

    public WorkingCapitalTransaction getPreviousTransaction() {
        final WorkingCapital workingCapital = getWorkingCapital();
        WorkingCapitalTransaction previous = null;
        for (final WorkingCapitalTransaction workingCapitalTransaction : workingCapital.getWorkingCapitalTransactionsSet()) {
            final DateTime transationInstant = workingCapitalTransaction.getTransationInstant();
            if (transationInstant.isBefore(getTransationInstant())
                    && (previous == null || previous.getTransationInstant().isBefore(transationInstant))) {
                previous = workingCapitalTransaction;
            }
        }
        return previous;
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getWorkingCapitalSystem() == WorkingCapitalSystem.getInstanceForCurrentHost();
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasTransationInstant() {
        return getTransationInstant() != null;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasAccumulatedValue() {
        return getAccumulatedValue() != null;
    }

    @Deprecated
    public boolean hasBalance() {
        return getBalance() != null;
    }

    @Deprecated
    public boolean hasDebt() {
        return getDebt() != null;
    }

    @Deprecated
    public boolean hasWorkingCapital() {
        return getWorkingCapital() != null;
    }

    @Deprecated
    public boolean hasWorkingCapitalSystem() {
        return getWorkingCapitalSystem() != null;
    }

    @Deprecated
    public boolean hasInvoice() {
        return getInvoice() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
