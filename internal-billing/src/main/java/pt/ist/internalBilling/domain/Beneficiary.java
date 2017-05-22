package pt.ist.internalBilling.domain;

public abstract class Beneficiary extends Beneficiary_Base {
    
    protected Beneficiary() {
        super();
    }

    public abstract String getPresentationName();
    
}
