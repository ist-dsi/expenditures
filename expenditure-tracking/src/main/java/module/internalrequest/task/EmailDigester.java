/*
 * @(#)EmailDigester.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package module.internalrequest.task;

import module.internalrequest.domain.InternalRequestProcess;
import module.internalrequest.domain.util.InternalRequestState;
import module.internalrequest.search.Search;
import module.internalrequest.search.filter.InternalRequestProcessSearchFilter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.bennu.search.domain.DomainIndexSystem;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Luis Cruz
 * 
 */
@DeclareMessageTemplate(id = "expenditures.internalRequest.pending", bundle = Bundle.INTERNAL_REQUEST, description = "template.internalRequest.pending",
        subject = "template.internalRequest.pending.subject", text = "template.internalRequest.pending.text", parameters = {
        @TemplateParameter(id = "applicationTitle", description = "template.parameter.application.subtitle"),
        @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
        @TemplateParameter(id = "processesByType", description = "template.parameter.internalRequest.processes.by.type"),
        @TemplateParameter(id = "processesTotal", description = "template.parameter.internalRequest.processes.total") })
@Task(englishTitle = "Send e-mail alerts regarding pending internal request ops", readOnly = false)
public class EmailDigester extends CronTask {

    @Override
    public void runTask() throws Exception {
        I18N.setLocale(new Locale(CoreConfiguration.getConfiguration().defaultLocale()));

        DateTime today = DateTime.now();
        int currYear = today.getYear();
        boolean includePrevYear = today.getMonthOfYear() == 1;

        for (Person person : getPeopleToProcess(currYear, includePrevYear)) {
            final User user = person.getUser();
            if (user.getPerson() != null && user.getExpenditurePerson() != null) {
                Authenticate.mock(user, "System Automation");
                try {
                    Map<String, List<InternalRequestProcess>> processesTypeMap = new LinkedHashMap<>();
                    processesTypeMap.put(TAKEN, getTaken(currYear, includePrevYear).collect(Collectors.toList()));
                    for (final InternalRequestState state : InternalRequestState.values()) {
                        processesTypeMap.put(state.toString(),
                                getPendingState(state, currYear, includePrevYear).collect(Collectors.toList()));
                    }

                    int totalPending = processesTypeMap.values().stream().map(Collection::size).reduce(0, Integer::sum);
                    if (totalPending > 0) {
                        Message.fromSystem().to(Group.users(person.getUser())).template("expenditures.internalRequest.pending")
                                .parameter("applicationTitle",
                                        Bennu.getInstance().getConfiguration().getApplicationSubTitle().getContent())
                                .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl())
                                .parameter("processesByType", processesTypeMap)
                                .parameter("processesTotal", totalPending)
                                .and().send();
                    }
                } finally {
                    Authenticate.unmock();
                }
            }
        }
    }

    public static final String TAKEN = "taken";

    private static Stream<InternalRequestProcess> getTaken(int year, boolean includePrevYear) {
        InternalRequestProcessSearchFilter filter = new InternalRequestProcessSearchFilter();
        filter.setYear(year);
        filter.setIncludeTaken(true);
        Stream<InternalRequestProcess> results = Search.filter(filter).filter(p -> p.getCurrentOwner() != null);

        if (includePrevYear) {
            results = Stream.concat(getTaken(year - 1, false), results);
        }
        return results;
    }

    private static Stream<InternalRequestProcess> getPendingState(InternalRequestState pendingState, int year, boolean includePrevYear) {
        InternalRequestProcessSearchFilter filter = new InternalRequestProcessSearchFilter();
        filter.setYear(year);
        filter.setPendingState(pendingState);
        Stream<InternalRequestProcess> results = Search.filter(filter);

        if (includePrevYear) {
            results = Stream.concat(getPendingState(pendingState, year - 1, false), results);
        }
        return results;
    }

    private static Collection<Person> getPeopleToProcess(int year, boolean includePrevYear) {
        final Set<Person> people = new HashSet<Person>();
        final LocalDate today = new LocalDate();
        final ExpenditureTrackingSystem expendituresSystem = ExpenditureTrackingSystem.getInstance();

        for (final Authorization authorization : expendituresSystem.getAuthorizationsSet()) {
            if (authorization.isValidFor(today)) {
                final Person person = authorization.getPerson();
                if (person.getOptions().getReceiveNotificationsByEmail()) {
                    people.add(person);
                }
            }
        }
        for (final RoleType roleType : RoleType.values()) {
            addPeopleWithRole(people, roleType);
        }
        for (final AccountingUnit accountingUnit : expendituresSystem.getAccountingUnitsSet()) {
            addPeople(people, accountingUnit.getPeopleSet());
            addPeople(people, accountingUnit.getProjectAccountantsSet());
            addPeople(people, accountingUnit.getResponsiblePeopleSet());
            addPeople(people, accountingUnit.getResponsibleProjectAccountantsSet());
            addPeople(people, accountingUnit.getTreasuryMembersSet());
        }

        addRequestingPeople(people, year);
        if (includePrevYear) {
            addRequestingPeople(people, year - 1);
        }
        return people;
    }

    private static void addRequestingPeople(final Set<Person> people, int year) {
        Stream<InternalRequestProcess> yearProcesses =
                DomainIndexSystem.getInstance().search(year, i -> i.getInternalRequestProcessSet().stream());

        Set<Person> requestingPeople = yearProcesses
                .filter( p -> {
                            return (!p.getIsCancelled()) && (p.getInternalRequest().getRequestingPerson() != null) && (
                                    p.getInternalRequest().getRequestingPerson().getUser() != null) && (
                                    p.getInternalRequest().getRequestingPerson().getUser().getExpenditurePerson() != null);
                        }
                ).map(p -> p.getInternalRequest().getRequestingPerson().getUser().getExpenditurePerson())
                .collect(Collectors.toSet());

        addPeople(people, requestingPeople);
    }

    private static void addPeopleWithRole(final Set<Person> people, final RoleType roleType) {
        addUsers(people, roleType.group().getMembers());
    }

    private static void addPeople(final Set<Person> people, Collection<Person> unverified) {
        unverified.forEach(p -> addPerson(people, p));
    }

    private static void addPerson(final Set<Person> people, final Person person) {
        if (person.getOptions().getReceiveNotificationsByEmail()) {
            people.add(person);
        }
    }

    private static void addUsers(final Set<Person> people, Stream<User> unverified) {
        unverified.forEach(u -> addPerson(people, u.getExpenditurePerson()));
    }

}
