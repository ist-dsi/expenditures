package pt.ist.internalBilling.domain;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;

public class UnitBeneficiary extends UnitBeneficiary_Base {
    
    public UnitBeneficiary(final Unit unit) {
        super();
        setUnit(unit);
    }

    @Override
    public String getPresentationName() {
        return getUnit().getPresentationName();
    }

    @Atomic
    public static UnitBeneficiary beneficiaryFor(final Unit unit) {
        final UnitBeneficiary beneficiary = unit.getUnitBeneficiary();
        return beneficiary == null ? new UnitBeneficiary(unit) : beneficiary;
    }

}
