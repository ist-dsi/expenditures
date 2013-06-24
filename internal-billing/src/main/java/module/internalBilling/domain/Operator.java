package module.internalBilling.domain;

import pt.ist.bennu.core.domain.exceptions.DomainException;

public class Operator extends Operator_Base {
    
    public Operator(final String name, final String fiscalIdentificationNumber) {
        super();
        setInternalBillingSystem(InternalBillingSystem.getInstance());
        edit(name, fiscalIdentificationNumber);
    }

    public void edit(final String name, final String fiscalIdentificationNumber) {
	setName(name);
	setFiscalIdentificationNumber(fiscalIdentificationNumber);
    }

    @Override
    public void setFiscalIdentificationNumber(String fiscalIdentificationNumber) {
	checkIsUnique(fiscalIdentificationNumber);
	super.setFiscalIdentificationNumber(fiscalIdentificationNumber);
    }

    private void checkIsUnique(final String fiscalIdentificationNumber) {
	for (final Operator operator : InternalBillingSystem.getInstance().getOperatorSet()) {
	    if (operator != this && operator.getFiscalIdentificationNumber().equalsIgnoreCase(fiscalIdentificationNumber)) {
		throw new DomainException("error.duplicate.operator", fiscalIdentificationNumber);
	    }
	}
    }

}
