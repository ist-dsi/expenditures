/*
 * @(#)FindAndCorrectInconsistentWorkingCapitalTransactions.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.workingCapital.domain.util;

import java.util.HashMap;

import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisitionSubmission;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.WorkingCapitalTransaction;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;
import pt.ist.bennu.core.domain.util.Money;

/**
 * 
 * @author João Neves
 * 
 */
public class FindAndCorrectInconsistentWorkingCapitalTransactions extends WriteCustomTask {

    private final boolean PERFORM_CORRECTIONS = false;

    private boolean isNewWorkingCapital = false;

    private HashMap<Integer, WorkingCapitalTransaction> transactionList;

    @Override
    protected void doService() {
        out.println();
        out.println("################################################################################################");
        out.println();
        out.println("Processing " + WorkingCapitalSystem.getInstanceForCurrentHost().getWorkingCapitals().size()
                + " working capitals.");
        for (WorkingCapital workingCapital : WorkingCapitalSystem.getInstanceForCurrentHost().getWorkingCapitals()) {
            isNewWorkingCapital = true;

            if (workingCapital.getWorkingCapitalTransactions().size() == 0) {
                continue;
            }

            transactionList = new HashMap<Integer, WorkingCapitalTransaction>();
            for (WorkingCapitalTransaction transaction : workingCapital.getWorkingCapitalTransactions()) {
                transactionList.put(transaction.getNumber(), transaction);
            }

            WorkingCapitalTransaction transaction = transactionList.get(1);
            checkFirstTransaction(transaction);

            WorkingCapitalTransaction previousTransaction = transaction;
            Integer i = 2;
            transaction = transactionList.get(i);

            while (transaction != null) {
                checkTransactionByType(transaction, previousTransaction);
                checkBalanceEqualsDebt(transaction);
                checkPositiveValues(transaction);

                previousTransaction = transaction;
                i++;
                transaction = transactionList.get(i);
            }
        }
    }

    private void checkFirstTransaction(WorkingCapitalTransaction transaction) {
        if (!transaction.isPayment()) {
            printTransaction(transaction);
            out.println("WARNING - First transaction is not a payment!");
        }

        if (!transaction.getAccumulatedValue().isZero()) {
            printTransaction(transaction);
            out.println("WARNING - First transaction ACCUMULATED VALUE is not zero!");
        }

        if (!transaction.getValue().equals(transaction.getBalance())) {
            printTransaction(transaction);
            out.println("WARNING - First transaction VALUE is DIFFERENT from the BALANCE!");
        }
        checkPositiveValues(transaction);
        checkBalanceEqualsDebt(transaction);
    }

    private void checkTransactionByType(WorkingCapitalTransaction transaction, WorkingCapitalTransaction previousTransaction) {
        if (transaction.isPayment()) {
            if (!transaction.getAccumulatedValue().equals(previousTransaction.getAccumulatedValue())) {
                printTransaction(transaction);
                out.println("WARNING - Payment transaction changes the ACCUMULATED VALUE!");
            }

            if (!transaction.getBalance().equals(transaction.getValue().add(previousTransaction.getBalance()))) {
                printTransaction(transaction);
                out.println("WARNING - Payment transaction does not update the BALANCE correctly!");
            }
            return;
        }

        if (transaction.isAcquisition()) {
            if (transaction.isCanceledOrRejected()) {
                if (!transaction.getAccumulatedValue().equals(previousTransaction.getAccumulatedValue())) {
                    printTransaction(transaction);
                    out.println("WARNING - Canceled acquisition transaction changes the ACCUMULATED VALUE!");
                    if (PERFORM_CORRECTIONS) {
                        out.println("-> correcting the problem...");
                        final Money correctAccumulatedValue = previousTransaction.getAccumulatedValue();
                        final Money diffAccumulatedValue = correctAccumulatedValue.subtract(transaction.getAccumulatedValue());
                        correctAccumulatedValueOfFollowingTransactions(transaction, diffAccumulatedValue);
                    } else {
                        out.println("-> to correct the problem, set PERFORM_CORRECTIONS to TRUE.");
                    }
                }

                if (!transaction.getBalance().equals(previousTransaction.getBalance())) {
                    printTransaction(transaction);
                    out.println("WARNING - Canceled acquisition transaction changes the BALANCE!");
                    if (PERFORM_CORRECTIONS) {
                        out.println("-> correcting the problem...");
                        final Money correctBalance = previousTransaction.getBalance();
                        final Money diffBalance = correctBalance.subtract(transaction.getBalance());
                        correctBalanceOfFollowingTransactions(transaction, diffBalance);
                        correctDebtOfFollowingTransactions(transaction, diffBalance);
                    } else {
                        out.println("-> to correct the problem, set PERFORM_CORRECTIONS to TRUE.");
                    }
                }
            } else {
                if (!transaction.getAccumulatedValue().equals(
                        transaction.getValue().add(previousTransaction.getAccumulatedValue()))) {
                    printTransaction(transaction);
                    out.println("Acquisition transaction does not update the ACCUMULATED VALUE correctly!");
                    if (PERFORM_CORRECTIONS) {
                        out.println("-> correcting the problem...");
                        final Money correctAccumulatedValue =
                                transaction.getValue().add(previousTransaction.getAccumulatedValue());
                        final Money diffAccumulatedValue = correctAccumulatedValue.subtract(transaction.getAccumulatedValue());
                        correctAccumulatedValueOfFollowingTransactions(transaction, diffAccumulatedValue);
                    } else {
                        out.println("-> to correct the problem, set PERFORM_CORRECTIONS to TRUE.");
                    }
                }

                if (!transaction.getBalance().equals(previousTransaction.getBalance().subtract(transaction.getValue()))) {
                    printTransaction(transaction);
                    out.println("Acquisition transaction does not update the BALANCE correctly!");
                    if (PERFORM_CORRECTIONS) {
                        out.println("-> correcting the problem...");
                        final Money correctBalance = previousTransaction.getBalance().subtract(transaction.getValue());
                        final Money diffBalance = correctBalance.subtract(transaction.getBalance());
                        correctBalanceOfFollowingTransactions(transaction, diffBalance);
                        correctDebtOfFollowingTransactions(transaction, diffBalance);
                    } else {
                        out.println("-> to correct the problem, set PERFORM_CORRECTIONS to TRUE.");
                    }
                }
            }
            return;
        }

        if (transaction instanceof WorkingCapitalAcquisitionSubmission) {
            if (!transaction.getValue().equals(previousTransaction.getAccumulatedValue())) {
                printTransaction(transaction);
                out.println("AcquisitionSubmission transaction VALUE is DIFFERENT from the previous ACCUMULATED VALUE!");
                if (PERFORM_CORRECTIONS) {
                    out.println("-> correcting the problem...");
                    final Money correctValue = previousTransaction.getAccumulatedValue();
                    final Money diffValue = correctValue.subtract(transaction.getValue());
                    correctValue(transaction, diffValue);
                } else {
                    out.println("-> to correct the problem, set PERFORM_CORRECTIONS to TRUE.");
                }
            }

            if (!transaction.getAccumulatedValue().isZero()) {
                printTransaction(transaction);
                out.println("WARNING - AcquisitionSubmission transaction ACCUMULATED VALUE is not zero!");
            }

            if (!transaction.getBalance().equals(previousTransaction.getBalance())) {
                printTransaction(transaction);
                out.println("WARNING - AcquisitionSubmission transaction changes the BALANCE!");
            }
            return;
        }

        if (transaction.isRefund()) {
            if (!previousTransaction.getAccumulatedValue().isZero()) {
                printTransaction(transaction);
                out.println("WARNING - Refund transaction is not performed after a acquisition submission!");
            }

            if (!transaction.getAccumulatedValue().equals(previousTransaction.getAccumulatedValue())) {
                printTransaction(transaction);
                out.println("WARNING - Refund transaction changes the ACCUMULATED VALUE!");
            }

            if (!transaction.getValue().equals(previousTransaction.getBalance())) {
                printTransaction(transaction);
                out.println("WARNING - Refund transaction VALUE is DIFFERENT from the previous BALANCE!");
            }

            if (!transaction.getBalance().isZero()) {
                printTransaction(transaction);
                out.println("WARNING - Refund transaction does not set the BALANCE to zero!");
            }
            return;
        }
    }

    private void checkBalanceEqualsDebt(WorkingCapitalTransaction transaction) {
        if (!transaction.getBalance().equals(transaction.getDebt())) {
            printTransaction(transaction);
            out.println("DEBT is DIFFERENT from the BALANCE!");
            if (PERFORM_CORRECTIONS) {
                out.println("-> correcting the problem...");
                final Money correctDebt = transaction.getBalance();
                final Money diffDebt = correctDebt.subtract(transaction.getDebt());
                correctDebt(transaction, diffDebt);
            } else {
                out.println("-> to correct the problem, set PERFORM_CORRECTIONS to TRUE.");
            }
        }
    }

    private void checkPositiveValues(WorkingCapitalTransaction transaction) {
        if (transaction.getValue().isNegative() || transaction.getAccumulatedValue().isNegative()
                || transaction.getBalance().isNegative() || transaction.getDebt().isNegative()) {
            printTransaction(transaction);
            out.println("WARNING - Negative values found!");
        }
    }

    private void correctAccumulatedValueOfFollowingTransactions(WorkingCapitalTransaction transaction, Money diffAccumulatedValue) {
        transaction.setAccumulatedValue(transaction.getAccumulatedValue().add(diffAccumulatedValue));
        out.println("AccumulatedValue of transaction " + transaction.getNumber() + " set to: "
                + transaction.getAccumulatedValue().toFormatString());
        if ((transaction.getNext() != null) && !(transaction.getNext() instanceof WorkingCapitalAcquisitionSubmission)) {
            correctAccumulatedValueOfFollowingTransactions(transaction.getNext(), diffAccumulatedValue);
        }
    }

    private void correctBalanceOfFollowingTransactions(WorkingCapitalTransaction transaction, Money diffBalance) {
        transaction.setBalance(transaction.getBalance().add(diffBalance));
        out.println("Balance of transaction " + transaction.getNumber() + " set to: " + transaction.getBalance().toFormatString());
        if (transaction.getNext() != null) {
            correctBalanceOfFollowingTransactions(transaction.getNext(), diffBalance);
        }
    }

    private void correctDebtOfFollowingTransactions(WorkingCapitalTransaction transaction, Money diffDebt) {
        correctDebt(transaction, diffDebt);
        if (transaction.getNext() != null) {
            correctDebtOfFollowingTransactions(transaction.getNext(), diffDebt);
        }
    }

    private void correctDebt(WorkingCapitalTransaction transaction, Money diffDebt) {
        transaction.setDebt(transaction.getDebt().add(diffDebt));
        out.println("Debt of transaction " + transaction.getNumber() + " set to: " + transaction.getDebt().toFormatString());
    }

    private void correctValue(WorkingCapitalTransaction transaction, Money diffValue) {
        transaction.setValue(transaction.getValue().add(diffValue));
        out.println("Value of transaction " + transaction.getNumber() + " set to: " + transaction.getValue().toFormatString());
    }

    private void printTransaction(WorkingCapitalTransaction transaction) {
        printWorkingCapitalIfNew(transaction.getWorkingCapital());
        out.println();
        out.println("Transaction: " + transaction.getNumber() + ". " + transaction.getDescription() + " ["
                + transaction.getExternalId() + "]");
    }

    private void printWorkingCapitalIfNew(WorkingCapital workingCapital) {
        if (!isNewWorkingCapital) {
            return;
        }

        out.println();
        out.println();
        out.println();
        out.println();
        out.println();
        out.println("Problems found in working capital: " + workingCapital.getUnit().getPresentationName() + " ["
                + workingCapital.getExternalId() + "]");
        out.println(workingCapital.getWorkingCapitalTransactions().size() + " transactions found.");

        isNewWorkingCapital = false;
    }
}
