package module.internalBilling.domain;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;

public class Operator extends Operator_Base {

    public Operator(final String name, final String fiscalIdentificationNumber) {
        super();
        setInternalBillingSystem(InternalBillingSystem.getInstance());
        edit(name, fiscalIdentificationNumber);
    }

    public void edit(final String name, final String fiscalIdentificationNumber) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(fiscalIdentificationNumber)) {
            throw new DomainException("error.create.operator.empty.arguments");
        }
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

    public String getPresentationName() {
        return getName() + " - " + getFiscalIdentificationNumber();
    }

    @Atomic
    public static Operator getOrCreateOperator(String name, String fiscalIdentificationNumber) {
        for (final Operator operator : InternalBillingSystem.getInstance().getOperatorSet()) {
            if (operator.getFiscalIdentificationNumber().equalsIgnoreCase(fiscalIdentificationNumber)
                    && operator.getName().equalsIgnoreCase(name)) {
                return operator;
            }
        }
        return new Operator(name, fiscalIdentificationNumber);
    }

    public static Operator getOperator(String fiscalIdentificationNumber) {
        if (!StringUtils.isBlank(fiscalIdentificationNumber)) {
            for (final Operator operator : InternalBillingSystem.getInstance().getOperatorSet()) {
                if (operator.getFiscalIdentificationNumber().equalsIgnoreCase(fiscalIdentificationNumber)) {
                    return operator;
                }
            }
        }
        return null;
    }
}
