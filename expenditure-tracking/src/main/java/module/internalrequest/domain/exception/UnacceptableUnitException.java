package module.internalrequest.domain.exception;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class UnacceptableUnitException extends InternalRequestDomainException {

    private Unit unit;

    public UnacceptableUnitException(Unit unit) {
        super();
        this.unit = unit;
    }

    public Unit getUnit() { return unit; }
}
