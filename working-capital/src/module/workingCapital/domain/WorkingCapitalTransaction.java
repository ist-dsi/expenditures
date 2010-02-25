package module.workingCapital.domain;

import java.util.Comparator;

import myorg.domain.User;
import myorg.domain.util.Money;

import org.joda.time.DateTime;

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
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
        setTransationInstant(new DateTime());
    }

    @Override
    public void setWorkingCapital(final WorkingCapital workingCapital) {
	final WorkingCapitalTransaction workingCapitalTransaction = workingCapital.getLastTransaction();
	int count = workingCapital.getWorkingCapitalTransactionsCount();
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

    public void addValue(final Money value) {
	setValue(getValue().add(value));
	setAccumulatedValue(getAccumulatedValue().add(value));
	setBalance(getBalance().subtract(value));
    }

    public void resetValue(final Money value) {
	final Money diffValue = value.subtract(getValue());
	addValue(diffValue);
    }

    public String getDescription() {
	return "";
    }

    public boolean isPayment() {
	return false;
    }

    public boolean isAcquisition() {
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

    public void approve(final User loggedPerson) {
    }

}
