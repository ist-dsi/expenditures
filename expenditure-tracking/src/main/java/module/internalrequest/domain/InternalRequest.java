package module.internalrequest.domain;

import module.finance.util.Money;
import module.internalrequest.domain.exception.UnacceptableUnitException;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InternalRequest extends InternalRequest_Base {
    
    public InternalRequest(Person requestingPerson, Unit requestingUnit, Unit requestedUnit, InternalRequestProcess process) {
        // BUSINESS LOGIC CHECKING
        if ((!isUnitInternal(requestingUnit))) {
            throw new UnacceptableUnitException(requestingUnit);
        }
        if (!isUnitInternal(requestedUnit)) {
            throw new UnacceptableUnitException(requestedUnit);
        }

        // ALL CLEAR
        this.setCreated(DateTime.now());

        this.setRequestingPerson(requestingPerson);
        this.setRequestingUnit(requestingUnit);
        this.setRequestedUnit(requestedUnit);

        this.setInternalRequestProcess(process);

        this.setInternalRequestSystem(InternalRequestSystem.getInstance());
    }

    public Money getTotalPrice() {
        return this.getItemsSet().stream()
                .map((i) -> i.getPrice() == null ? Money.ZERO : i.getPrice())
                .reduce(Money.ZERO, Money::add).round();
    }

    public static boolean isUnitInternal(Unit unit) {
        if (!(unit instanceof CostCenter || unit instanceof Project || unit instanceof SubProject)) {
            // this unit is not of the right type to be accepted
            return false;
        }

        final module.organization.domain.Unit organizationUnit = unit.getUnit();
        if (organizationUnit == null) {
            // unit does not have representation in the organizational model
            return false;
        }

        final AccountabilityType organizationalAccountabilityType =
                ExpenditureTrackingSystem.getInstance().getOrganizationalAccountabilityType();
        final Set<module.organization.domain.Unit> topUnits =
                ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet().stream().map(Unit::getUnit)
                        .collect(Collectors.toSet());
        for (final module.organization.domain.Unit topUnit : topUnits) {
            if (organizationUnit.ancestorsInclude(topUnit, organizationalAccountabilityType, LocalDate.now(), LocalDate.now())) {
                // the unit is an internal unit
                return true;
            }
        }

        return false;
    }

    public static boolean doesPersonBelongToUnit(Person person, Unit unit) {
        return person.getParentAccountabilityStream()
                .filter(a -> a.isActiveNow() && a.getParent().isUnit())
                .map(a -> (module.organization.domain.Unit) a.getParent())
                .anyMatch((u) -> u.equals(unit.getUnit()));
    }

    public void delete() {
        this.setInternalRequestProcess(null);
        this.setRequestingPerson(null);
        this.setRequestingUnit(null);
        this.setRequestedUnit(null);

        for(InternalRequestItem item : this.getItemsSet()) {
            item.delete();
        }

        this.setApprovedBy(null);
        this.setBudgetedBy(null);
        this.setAuthorizedBy(null);
        this.setProcessedBy(null);
        this.setDeliveredBy(null);
        this.setImputedBy(null);

        this.setInternalRequestSystem(null);
        super.deleteDomainObject();
    }

    public SortedSet<InternalRequestItem> getSortedItemsSet() {
        final SortedSet<InternalRequestItem> result = new TreeSet<>();
        result.addAll(getItemsSet());
        return result;
    }

}
