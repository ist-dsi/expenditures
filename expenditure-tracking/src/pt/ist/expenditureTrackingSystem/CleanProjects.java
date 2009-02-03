package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import myorg.domain.util.Money;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CleanProjects {

    private static final Comparator<Authorization> AUTHORIZATION_COMPARATOR = new Comparator<Authorization>() {

	private int compareByMaxAmount(final Authorization authorization1, final Authorization authorization2) {
	    final Money max1 = authorization1.getMaxAmount();
	    final Money max2 = authorization2.getMaxAmount();
	    return max2.compareTo(max1);
	}

	private int compareByStartDate(final Authorization authorization1, final Authorization authorization2) {
	    final LocalDate start1 = authorization1.getStartDate();
	    final LocalDate start2 = authorization2.getStartDate();
	    return start1.compareTo(start2);
	}

	private int compareByStartDateAndId(final Authorization authorization1, final Authorization authorization2) {
	    final int start = compareByStartDate(authorization1, authorization2);
	    return start == 0 ? authorization1.getIdInternal().compareTo(authorization2.getIdInternal()) : start;
	}

	@Override
	public int compare(final Authorization authorization1, final Authorization authorization2) {
	    final int max = compareByMaxAmount(authorization1, authorization2);
	    return max == 0 ? compareByStartDateAndId(authorization1, authorization2) : max;
	}

    };

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	// TODO : reimplmenent as scheduled script
	//FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
	Language.setLocale(new Locale("pt", "PT"));
    }

    public static void main(String[] args) {
	init();
	try {
	    cleanData();
	} catch (final IOException e) {
	    throw new Error(e);
	} catch (final SQLException e) {
	    throw new Error(e);
	}
	System.out.println("Done.");
    }

    @Service
    public static void cleanData() throws IOException, SQLException {
	int maintained = 0;
	int deleted = 0;

	for (final Person person : ExpenditureTrackingSystem.getInstance().getPeopleSet()) {
	    final Map<Unit, Set<Authorization>> authorizationMap = new HashMap<Unit, Set<Authorization>>();
	    for (final Authorization authorization : person.getAuthorizationsSet()) {
		final Unit unit = authorization.getUnit();
		final Set<Authorization> authorizations;
		if (authorizationMap.containsKey(unit)) {
		    authorizations = authorizationMap.get(unit);
		} else {
		    authorizations = new TreeSet<Authorization>(AUTHORIZATION_COMPARATOR);
		    authorizationMap.put(unit, authorizations);
		}
		authorizations.add(authorization);
	    }

	    for (final Entry<Unit, Set<Authorization>> entry : authorizationMap.entrySet()) {
		final Set<Authorization> authorizations = entry.getValue();
		if (authorizations.size() > 1) {
		    int i = 0;
		    for (final Authorization authorization : authorizations) {
			if (i++ > 0) {
			    authorization.delete();
			    deleted++;
			} else {
			    maintained++;
			}
		    }
		}
	    }
	}

	System.out.println("Kept " + maintained + " authorizations.");
	System.out.println("Deleted " + deleted + " authorizations.");
    }

}
