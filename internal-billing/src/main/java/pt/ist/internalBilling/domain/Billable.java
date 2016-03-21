package pt.ist.internalBilling.domain;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;

public class Billable extends Billable_Base {

    Billable(BillableService billableService, Unit financer, Beneficiary beneficiary) {
        setBillableService(billableService);
        if (!(financer instanceof CostCenter)) {
            throw new Error("only.cost.centers.are.allowed.to.be.financers");
        }
        setUnit(financer);
        setBeneficiary(beneficiary);
        setBillableStatus(BillableStatus.PENDING_AUTHORIZATION);
        setServiceStatus(ServiceStatus.PENDING_ACTIVATION);
    }

    void log(final String description) {
        new BillableLog(this, description);
    }

    @Atomic
    public void authorize() {
        if (getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION) {
            setBillableStatus(BillableStatus.AUTHORIZED);
            log("Authorized subscription of service " + getBillableService().getTitle() + " for unit "
                    + getUnit().getPresentationName() + " to user " + getBeneficiary().getPresentationName()
                    + " with configuration " + getConfiguration());
        }
    }

    public void authorize(JsonObject configuration) {
        if (getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION) {
            setBillableStatus(BillableStatus.AUTHORIZED);
            setConfiguration(configuration.toString());
            log("Authorized subscription of service " + getBillableService().getTitle() + " for unit "
                    + getUnit().getPresentationName() + " to user " + getBeneficiary().getPresentationName()
                    + " with configuration " + getConfiguration());
        }
    }

    @Atomic
    public void revoke() {
        if (getBillableStatus() != BillableStatus.REVOKED) {
            setBillableStatus(BillableStatus.REVOKED);
            log("Revoked subscription of service " + getBillableService().getTitle() + " for unit "
                    + getUnit().getPresentationName() + " to user " + getBeneficiary().getPresentationName()
                    + " with configuration " + getConfiguration());
        }
    }

    public static Set<Billable> pendingAuthorization() {
        final User user = Authenticate.getUser();
        return user == null ? Collections.emptySet() : user.getExpenditurePerson().getAuthorizationsSet().stream()
                .filter(a -> a.isValid()).flatMap(a -> units(a)).flatMap(u -> u.getBillableSet().stream())
                .filter(b -> b.getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION).collect(Collectors.toSet());
    }

    private static Stream<Unit> units(final Authorization a) {
        final Unit unit = a.getUnit();
        return Stream.concat(Stream.of(unit), childUnitsWithNoAuthority(unit));
    }

    private static Stream<Unit> childUnitsWithNoAuthority(final Unit unit) {
        Stream<Unit> result = Stream.empty();
        for (final Unit child : unit.getSubUnitsSet()) {
            if (child.getAuthorizationsSet().isEmpty()) {
                result = Stream.concat(result, Stream.of(child));
                result = Stream.concat(result, childUnitsWithNoAuthority(child));
            }
        }
        return result;
    }

    public JsonObject getConfigurationAsJson() {
        final String config = getConfiguration();
        return config == null ? new JsonObject() : new JsonParser().parse(config).getAsJsonObject();
    }

    public boolean isCurrentUserAllowedToView() {
        final Unit unit = getUnit();
        return InternalBillingService.canViewUnitServices(unit) || isCurrentUserBenificiary();
    }

    public boolean isCurrentUserBenificiary() {
        final Beneficiary beneficiary = getBeneficiary();
        return beneficiary instanceof UserBeneficiary && ((UserBeneficiary) beneficiary).getUser() == Authenticate.getUser();
    }

}
