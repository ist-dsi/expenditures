/*
 * @(#)CleanProjects.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author Pedro Santos
 * @author Luis Cruz
 * 
 */
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
            return start == 0 ? authorization1.getExternalId().compareTo(authorization2.getExternalId()) : start;
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
        // FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
        // ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
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

    @Atomic
    public static void cleanData() throws IOException, SQLException {
        int maintained = 0;
        int deleted = 0;

        for (final Person person : MyOrg.getInstance().getPeopleFromExpenditureTackingSystemSet()) {
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
