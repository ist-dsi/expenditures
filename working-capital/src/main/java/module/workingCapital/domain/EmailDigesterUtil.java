/*
 * @(#)EmailDigesterUtil.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 *
 *      https://fenix-ashes.ist.utl.pt/
 *
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package module.workingCapital.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.jfree.data.time.Month;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import module.workflow.util.PresentableProcessState;
import module.workingCapital.util.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 *
 * @author Luis Cruz
 *
 */
@DeclareMessageTemplate(id = "expenditures.capital.pending", bundle = Bundle.WORKING_CAPITAL,
        description = "template.capital.pending", subject = "template.capital.pending.subject",
        text = "template.capital.pending.text", parameters = {
                @TemplateParameter(id = "applicationTitle", description = "template.parameter.application.subtitle"),
                @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
                @TemplateParameter(id = "processesByType", description = "template.parameter.processes.by.type"),
                @TemplateParameter(id = "processesTotal", description = "template.parameter.processes.total") })
@DeclareMessageTemplate(id = "expenditures.capital.pending.termination", bundle = Bundle.WORKING_CAPITAL,
        description = "template.capital.pending.termination", subject = "template.capital.pending.termination.subject",
        text = "template.capital.pending.termination.text", parameters = {
                @TemplateParameter(id = "applicationTitle", description = "template.parameter.application.subtitle"),
                @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
                @TemplateParameter(id = "unit", description = "template.parameter.unit"),
                @TemplateParameter(id = "year", description = "template.parameter.year") })
public class EmailDigesterUtil {

    public static final String BUNDLE = "resources.MissionResources", TAKEN = "taken", PENDING_APPROVAL = "approval",
            PENDING_VERIFICATION = "verification", PENDING_AUTHORIZATION = "authorization", PENDING_PAYMENT = "payment",
            PENDING_PROCESSING = "processing";

    public static class WorkingCapitalProcessBean implements Comparable<WorkingCapitalProcessBean> {
        private String unit;
        private Integer year;

        public String getUnit() {
            return unit;
        }

        public Integer getYear() {
            return year;
        }

        public WorkingCapitalProcessBean(WorkingCapital capital) {
            this.unit = capital.getUnit().getPresentationName();
            this.year = capital.getWorkingCapitalYear().getYear();
        }

        @Override
        public int compareTo(WorkingCapitalProcessBean b) {
            return unit.compareTo(b.getUnit());
        }

    }

    private static List<WorkingCapitalProcessBean> getMissionProcessBeans(Set<WorkingCapitalProcess> processes) {
        return processes.stream().map(p -> new WorkingCapitalProcessBean(p.getWorkingCapital())).sorted()
                .collect(Collectors.toList());
    }

    public static void executeTask() {
        final DateTime now = new DateTime();

        I18N.setLocale(new Locale(CoreConfiguration.getConfiguration().defaultLocale()));
        for (Person person : getPeopleToProcess()) {

            final User user = person.getUser();
            if (user.getPerson() != null && user.getExpenditurePerson() != null) {
                Authenticate.mock(user);

                try {
                    final LocalizedString applicationTitle = Bennu.getInstance().getConfiguration().getApplicationSubTitle();
                    final String applicationUrl = CoreConfiguration.getConfiguration().applicationUrl();
                    final WorkingCapitalYear workingCapitalYear = WorkingCapitalYear.getCurrentYear();
                    final LocalDate today = new LocalDate();
                    final WorkingCapitalYear previousYear =
                            today.getMonthOfYear() == Month.JANUARY ? WorkingCapitalYear.findOrCreate(today.getYear() - 1) : null;

                    Map<String, List<WorkingCapitalProcessBean>> processesTypeMap = new LinkedHashMap<>();
                    if (previousYear == null) {
                        processesTypeMap.put(TAKEN, getMissionProcessBeans(workingCapitalYear.getTaken()));
                        processesTypeMap.put(PENDING_APPROVAL, getMissionProcessBeans(workingCapitalYear.getPendingAproval()));
                        processesTypeMap.put(PENDING_VERIFICATION,
                                getMissionProcessBeans(workingCapitalYear.getPendingVerification()));
                        processesTypeMap.put(PENDING_PROCESSING,
                                getMissionProcessBeans(workingCapitalYear.getPendingProcessing()));
                        processesTypeMap.put(PENDING_AUTHORIZATION,
                                getMissionProcessBeans(workingCapitalYear.getPendingAuthorization()));
                        processesTypeMap.put(PENDING_PAYMENT, getMissionProcessBeans(workingCapitalYear.getPendingPayment()));
                    } else {
                        processesTypeMap.put(TAKEN, getMissionProcessBeans(previousYear.getTaken(workingCapitalYear.getTaken())));
                        processesTypeMap.put(PENDING_APPROVAL,
                                getMissionProcessBeans(previousYear.getPendingAproval(workingCapitalYear.getPendingAproval())));
                        processesTypeMap.put(PENDING_VERIFICATION, getMissionProcessBeans(previousYear
                                .getPendingVerification(workingCapitalYear.getPendingVerification())));
                        processesTypeMap.put(PENDING_PROCESSING, getMissionProcessBeans(previousYear
                                .getPendingProcessing(workingCapitalYear.getPendingProcessing())));
                        processesTypeMap.put(PENDING_AUTHORIZATION, getMissionProcessBeans(previousYear
                                .getPendingAuthorization(workingCapitalYear.getPendingAuthorization())));
                        processesTypeMap.put(PENDING_PAYMENT,
                                getMissionProcessBeans(previousYear.getPendingPayment(workingCapitalYear.getPendingPayment())));
                    }

                    final int totalPending = processesTypeMap.values().stream().map(Collection::size).reduce(0, Integer::sum);

                    if (totalPending > 0) {
                        Message.fromSystem().to(Group.users(user)).template("expenditures.capital.pending")
                                .parameter("applicationTitle", applicationTitle).parameter("applicationUrl", applicationUrl)
                                .parameter("processesByType", processesTypeMap).parameter("processesTotal", totalPending).and()
                                .send();
                    }

                    for (final WorkingCapital workingCapital : user.getPerson().getMovementResponsibleWorkingCapitalsSet()) {
                        final Integer year = workingCapital.getWorkingCapitalYear().getYear();
                        if (year.intValue() < now.getYear() || (year.intValue() == now.getYear() && now.getMonthOfYear() == 12)
                                && now.getDayOfMonth() > 15) {
                            final PresentableProcessState state = workingCapital.getPresentableAcquisitionProcessState();
                            if (state == WorkingCapitalProcessState.WORKING_CAPITAL_AVAILABLE) {
                                Message.fromSystem().to(Group.users(user)).template("expenditures.capital.pending.termination")
                                        .parameter("applicationTitle", applicationTitle)
                                        .parameter("applicationUrl", applicationUrl)
                                        .parameter("unit", workingCapital.getUnit().getPresentationName())
                                        .parameter("year", workingCapital.getWorkingCapitalYear().getYear()).and().send();
                            }
                        }
                    }
                } finally {
                    Authenticate.unmock();
                }
            }
        }
    }

    protected String getBundle() {
        return "resources.ContentResources";
    }

    protected String getMessage(final String key) {
        return BundleUtil.getString(getBundle(), key);
    }

    private static Collection<Person> getPeopleToProcess() {
        final Set<Person> people = new HashSet<Person>();
        final LocalDate today = new LocalDate();
        final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
        for (final Authorization authorization : instance.getAuthorizationsSet()) {
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
        for (final AccountingUnit accountingUnit : instance.getAccountingUnitsSet()) {
            addPeople(people, accountingUnit.getPeopleSet());
            addPeople(people, accountingUnit.getProjectAccountantsSet());
            addPeople(people, accountingUnit.getResponsiblePeopleSet());
            addPeople(people, accountingUnit.getResponsibleProjectAccountantsSet());
            addPeople(people, accountingUnit.getTreasuryMembersSet());
        }
        final WorkingCapitalYear workingCapitalYear = WorkingCapitalYear.getCurrentYear();
        addMovementResponsibles(people, workingCapitalYear);
        if (today.getMonthOfYear() == Month.JANUARY) {
            final WorkingCapitalYear previousYear = WorkingCapitalYear.findOrCreate(today.getYear() - 1);
            addMovementResponsibles(people, previousYear);
        }

        return people;
    }

    private static void addMovementResponsibles(final Set<Person> people, final WorkingCapitalYear workingCapitalYear) {
        for (final WorkingCapital workingCapital : workingCapitalYear.getWorkingCapitalsSet()) {
            final module.organization.domain.Person movementResponsible = workingCapital.getMovementResponsible();
            if (movementResponsible.getUser() != null) {
                final User user = movementResponsible.getUser();
                if (user.getExpenditurePerson() != null) {
                    final Person person = user.getExpenditurePerson();
                    if (person.getOptions().getReceiveNotificationsByEmail()) {
                        people.add(person);
                    }
                }
            }
        }
    }

    private static void addPeopleWithRole(final Set<Person> people, final RoleType roleType) {
        addUsers(people, roleType.group().getMembers());
    }

    private static void addUsers(Set<Person> people, Stream<User> members) {
        members.forEach(u -> addPerson(people, u.getExpenditurePerson()));
    }

    private static void addPerson(Set<Person> people, Person person) {
        if (person.getOptions().getReceiveNotificationsByEmail()) {
            people.add(person);
        }
    }

    private static void addPeople(final Set<Person> people, Collection<Person> unverified) {
        for (final Person person : unverified) {
            addPerson(people, person);
        }
    }

}
