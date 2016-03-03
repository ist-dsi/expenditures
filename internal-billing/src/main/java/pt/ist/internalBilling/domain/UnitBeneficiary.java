package pt.ist.internalBilling.domain;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class UnitBeneficiary extends UnitBeneficiary_Base {
    
    public UnitBeneficiary(final Unit unit) {
        super();
        setUnit(unit);
    }

    public static UnitBeneficiary beneficiaryFor(final Unit unit) {
        final UnitBeneficiary beneficiary = unit.getUnitBeneficiary();
        return beneficiary == null ? new UnitBeneficiary(unit) : beneficiary;
    }

    @Override
    public String getPresentationName() {
        return getUnit().getPresentationName();
    }

}
