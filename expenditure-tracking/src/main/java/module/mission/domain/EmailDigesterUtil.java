/*
 * @(#)EmailDigesterUtil.java
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
package module.mission.domain;

import java.util.Collection;
import java.util.Date;
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
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.jfree.data.time.Month;
import org.joda.time.LocalDate;

import module.organization.domain.Party;
import pt.ist.expenditureTrackingSystem._development.Bundle;
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
@DeclareMessageTemplate(id = "expenditures.mission.pending", bundle = Bundle.MISSION, description = "template.mission.pending",
        subject = "template.mission.pending.subject", text = "template.mission.pending.text", parameters = {
                @TemplateParameter(id = "applicationTitle", description = "template.parameter.application.subtitle"),
                @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
                @TemplateParameter(id = "processesByType", description = "template.parameter.mission.processes.by.type"),
                @TemplateParameter(id = "processesTotal", description = "template.parameter.mission.processes.total") })
public class EmailDigesterUtil {

    public static final String TAKEN = "taken", PENDING_APPROVAL = "approval", PENDING_VEHICLE = "vehicle",
            PENDING_AUTHORIZATION = "authorization", PENDING_FUND = "fund", PENDING_PROCESSING = "processing";

    public static class MissionProcessBean implements Comparable<MissionProcessBean> {
        private String id;
        private String destination;
        private Date departure, arrival;

        public String getId() {
            return id;
        }

        public String getDestination() {
            return destination;
        }

        public Date getDeparture() {
            return departure;
        }

        public Date getArrival() {
            return arrival;
        }

        public MissionProcessBean(MissionProcess process) {
            this.id = process.getProcessNumber();
            Mission mission = process.getMission();
            this.destination = mission.getDestinationDescription();
            this.departure = mission.getDaparture().toDate();
            this.arrival = mission.getArrival().toDate();
        }

        @Override
        public int compareTo(MissionProcessBean b) {
            return id.compareTo(b.getId());
        }

    }

    private static List<MissionProcessBean> getMissionProcessBeans(Set<MissionProcess> processes) {
        return processes.stream().map(p -> new MissionProcessBean(p)).sorted().collect(Collectors.toList());
    }

    public static void executeTask() {
        I18N.setLocale(new Locale(CoreConfiguration.getConfiguration().defaultLocale()));
        for (Person person : getPeopleToProcess()) {

            final User user = person.getUser();
            if (user.getPerson() != null && user.getExpenditurePerson() != null) {
                Authenticate.mock(user);

                try {
                    final MissionYear missionYear = MissionYear.getCurrentYear();
                    final LocalDate today = new LocalDate();
                    final MissionYear previousYear =
                            today.getMonthOfYear() == Month.JANUARY ? MissionYear.findOrCreateMissionYear(today.getYear() - 1) : null;

                    Map<String, List<MissionProcessBean>> processesTypeMap = new LinkedHashMap<>();
                    processesTypeMap.put(TAKEN,
                            getMissionProcessBeans(getTaken(missionYear, previousYear).collect(Collectors.toSet())));
                    if (previousYear == null) {
                        processesTypeMap.put(PENDING_APPROVAL, getMissionProcessBeans(missionYear.getPendingAproval()));
                        processesTypeMap.put(PENDING_VEHICLE,
                                getMissionProcessBeans(missionYear.getPendingVehicleAuthorization()));
                        processesTypeMap
                                .put(PENDING_AUTHORIZATION, getMissionProcessBeans(missionYear.getPendingAuthorization()));
                        processesTypeMap.put(PENDING_FUND, getMissionProcessBeans(missionYear.getPendingFundAllocation()));
                        processesTypeMap.put(PENDING_PROCESSING,
                                getMissionProcessBeans(missionYear.getPendingProcessingPersonelInformation()));
                    } else {
                        processesTypeMap.put(PENDING_APPROVAL,
                                getMissionProcessBeans(previousYear.getPendingAproval(missionYear.getPendingAproval())));
                        processesTypeMap.put(PENDING_VEHICLE, getMissionProcessBeans(previousYear
                                .getPendingVehicleAuthorization(missionYear.getPendingVehicleAuthorization())));
                        processesTypeMap.put(PENDING_AUTHORIZATION, getMissionProcessBeans(previousYear
                                .getPendingAuthorization(missionYear.getPendingAuthorization())));
                        processesTypeMap.put(PENDING_FUND, getMissionProcessBeans(previousYear
                                .getPendingFundAllocation(missionYear.getPendingFundAllocation())));
                        processesTypeMap.put(PENDING_PROCESSING, getMissionProcessBeans(previousYear
                                .getPendingProcessingPersonelInformation(missionYear.getPendingProcessingPersonelInformation())));
                    }

                    final int totalPending = processesTypeMap.values().stream().map(Collection::size).reduce(0, Integer::sum);

                    if (totalPending > 0) {
                        Message.fromSystem().to(Group.users(person.getUser())).template("expenditures.mission.pending")
                                .parameter("applicationTitle", Bennu.getInstance().getConfiguration().getApplicationSubTitle())
                                .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl())
                                .parameter("processesByType", processesTypeMap).parameter("processesTotal", totalPending).and()
                                .send();
                    }
                } finally {
                    Authenticate.unmock();
                }
            }
        }
    }

    private static Stream<MissionProcess> getTaken(final MissionYear missionYear, final MissionYear previousYear) {
        return previousYear == null ? missionYear.getTakenStream() : Stream.concat(missionYear.getTakenStream(),
                previousYear.getTakenStream());
    }

    private static Collection<Person> getPeopleToProcess() {
        final Set<Person> people = new HashSet<Person>();
        final LocalDate today = new LocalDate();
        final ExpenditureTrackingSystem expendituresSystem = ExpenditureTrackingSystem.getInstance();
        for (User user : MissionSystem.getInstance().getVehicleAuthorizersSet()) {
            people.add(user.getExpenditurePerson());
        }

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
        final MissionYear missionYear = MissionYear.getCurrentYear();
        addRequestorsAndResponsibles(people, missionYear);
        if (today.getMonthOfYear() == Month.JANUARY) {
            final MissionYear previousYear = MissionYear.findOrCreateMissionYear(today.getYear() - 1);
            addRequestorsAndResponsibles(people, previousYear);
        }
        return people;
    }

    private static void addRequestorsAndResponsibles(final Set<Person> people, final MissionYear missionYear) {
        for (final MissionProcess missionProcess : missionYear.getMissionProcessSet()) {
            final Mission mission = missionProcess.getMission();
            final module.organization.domain.Person requestingPerson = mission.getRequestingPerson();
            if (requestingPerson != null && requestingPerson.getUser().getExpenditurePerson() != null) {
                people.add(requestingPerson.getUser().getExpenditurePerson());
            }
            final Party missionResponsible = mission.getMissionResponsible();
            if (missionResponsible != null && missionResponsible.isPerson()) {
                final module.organization.domain.Person missionPerson = (module.organization.domain.Person) missionResponsible;
                if (missionPerson.getUser() != null) {
                    final User user = missionPerson.getUser();
                    if (user != null && user.getExpenditurePerson() != null) {
                        final Person person = user.getExpenditurePerson();
                        if (person.getOptions().getReceiveNotificationsByEmail()) {
                            people.add(person);
                        }
                    }
                }
            }
        }
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
