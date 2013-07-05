package module.internalBilling.domain;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.domain.exceptions.DomainException;

public class Account extends Account_Base {

    public Account(final Operator operator, final String number, String description) {
        super();
        if (operator == null) {
            throw new DomainException("error.empty.operator");
        }
        if (StringUtils.isBlank(number)) {
            throw new DomainException("error.create.account.empty.number");
        }
        setOperator(operator);
        setNumber(number);
        setDescription(description);
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

    public String getPresentationName() {
        return getOperator().getName() + " - " + getNumber();
    }

}
