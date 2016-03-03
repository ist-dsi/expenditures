package pt.ist.internalBilling.domain;

public class Beneficiary extends Beneficiary_Base {
    
    public Beneficiary() {
        super();
    }

    public String getPresentationName() {
        return getExternalId();
    }
    
}
