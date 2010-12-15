package module.workingCapital.domain.util;

import java.util.HashMap;

import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisitionSubmission;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.WorkingCapitalTransaction;
import myorg.domain.scheduler.WriteCustomTask;
import myorg.domain.util.Money;

public class FindInconsistentWorkingCapitalTransactions extends WriteCustomTask {

    private boolean isNewWorkingCapital = false;

    private HashMap<Integer, WorkingCapitalTransaction> transactionList;

    @Override
    protected void doService() {
	out.println();
	out.println("################################################################################################");
	out.println();
	out.println("Processing " + WorkingCapitalSystem.getInstance().getWorkingCapitalsCount() + " working capitals.");
	for (WorkingCapital workingCapital : WorkingCapitalSystem.getInstance().getWorkingCapitals()) {
	    isNewWorkingCapital = true;

	    if (workingCapital.getWorkingCapitalTransactionsCount() == 0) {
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
		}

		if (!transaction.getBalance().equals(previousTransaction.getBalance())) {
		    printTransaction(transaction);
		    out.println("WARNING - Canceled acquisition transaction changes the BALANCE!");
		}
	    } else {
		if (!transaction.getAccumulatedValue().equals(
			transaction.getValue().add(previousTransaction.getAccumulatedValue()))) {
		    printTransaction(transaction);
		    out.println("Acquisition transaction does not update the ACCUMULATED VALUE correctly; correcting the problem...");

		    final Money correctAccumulatedValue = transaction.getValue().add(previousTransaction.getAccumulatedValue());
		    final Money diffAccumulatedValue = correctAccumulatedValue.subtract(transaction.getAccumulatedValue());
		    correctAccumulatedValueOfFollowingTransactions(transaction, diffAccumulatedValue);
		}

		if (!transaction.getBalance().equals(previousTransaction.getBalance().subtract(transaction.getValue()))) {
		    printTransaction(transaction);
		    out.println("Acquisition transaction does not update the BALANCE correctly; correcting the problem...");

		    final Money correctBalance = previousTransaction.getBalance().subtract(transaction.getValue());
		    final Money diffBalance = correctBalance.subtract(transaction.getBalance());
		    correctBalanceOfFollowingTransactions(transaction, diffBalance);
		    correctDebtOfFollowingTransactions(transaction, diffBalance);
		}
	    }
	    return;
	}

	if (transaction instanceof WorkingCapitalAcquisitionSubmission) {
	    if (!transaction.getValue().equals(previousTransaction.getAccumulatedValue())) {
		printTransaction(transaction);
		out.println("AcquisitionSubmission transaction VALUE is DIFFERENT from the previous ACCUMULATED VALUE; correcting the problem...");

		final Money correctValue = previousTransaction.getAccumulatedValue();
		final Money diffValue = correctValue.subtract(transaction.getValue());
		correctValue(transaction, diffValue);
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
	    out.println("DEBT is DIFFERENT from the BALANCE; correcting the problem...");

	    final Money correctDebt = transaction.getBalance();
	    final Money diffDebt = correctDebt.subtract(transaction.getDebt());
	    correctDebt(transaction, diffDebt);
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
	out.println(workingCapital.getWorkingCapitalTransactionsCount() + " transactions found.");

	isNewWorkingCapital = false;
    }
}
