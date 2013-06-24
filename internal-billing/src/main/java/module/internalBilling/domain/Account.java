package module.internalBilling.domain;

import pt.ist.bennu.core.domain.exceptions.DomainException;

public class Account extends Account_Base {
    
    public Account(final Operator operator, final String number) {
        super();
        setOperator(operator);
        setNumber(number);
    }

    @Override
    public void setNumber(final String number) {
	checkIsUnique(number);
        super.setNumber(number);
    }

    private void checkIsUnique(final String number) {
	for (final Account account : getOperator().getAccountSet()) {
	    if (account != this && account.getNumber().equalsIgnoreCase(number)) {
		throw new DomainException("error.duplicate.account", number, getOperator().getFiscalIdentificationNumber());
	    }
	}
    }

}
