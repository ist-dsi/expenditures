package pt.ist.internalBilling.domain;

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;

public class InternalBillingService extends InternalBillingService_Base {
    
    private InternalBillingService() {
        super();
        setBennu(Bennu.getInstance());
    }

    public static InternalBillingService getInstance() {
        final InternalBillingService service = Bennu.getInstance().getInternalBillingService();
        return service == null ? createInternalBillingService() : service; 
    }

    @Atomic
    private static InternalBillingService createInternalBillingService() {
        final InternalBillingService service = Bennu.getInstance().getInternalBillingService();
        return service == null ? new InternalBillingService() : service; 
    }

    public static Stream<BillableService> billableServiceStream() {
        return InternalBillingService.getInstance().getBillableServiceSet().stream();
    }

    public static boolean canViewUnitServices(final User user, final Unit unit) {
        final Person person = user == null ? null : user.getExpenditurePerson();
        return person != null && (unit.isResponsible(person) || unit.isUnitObserver(user) || ExpenditureTrackingSystem.isManager());
    }

    public static boolean canViewUserServices(final User user) {
        final User loggedUser = Authenticate.getUser();
        return user == loggedUser || ExpenditureTrackingSystem.isManager() || isResponsibleForUser(user, loggedUser);
    }

    private static boolean isResponsibleForUser(final User user, final User loggedUser) {
        final Person loggedPerson = loggedUser.getExpenditurePerson();
        return user.getUserBeneficiary().getBillableSet().stream()
            .filter(b -> b.getBillableStatus() == BillableStatus.AUTHORIZED)
            .flatMap(b -> b.getUnit().getAuthorizationsSet().stream())
            .anyMatch(a -> a.getPerson() == loggedPerson && a.isValid());
    }

    public static Stream<Billable> billablesPendingAuthorization(final User user) {
        return user == null ? Stream.empty() : user.getExpenditurePerson().getAuthorizationsSet().stream()
                .filter(a -> a.isValid())
                .flatMap(a -> units(a))
                .flatMap(u -> u.getBillableSet().stream())
                .filter(b -> b.getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION);
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

}
