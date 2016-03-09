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
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.domain.Message;
import org.fenixedu.messaging.domain.MessagingSystem;
import org.fenixedu.messaging.domain.Sender;
import org.jfree.data.time.Month;
import org.joda.time.LocalDate;

import module.organization.domain.Party;
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
public class EmailDigesterUtil {

    public static void executeTask() {
        I18N.setLocale(new Locale(CoreConfiguration.getConfiguration().defaultLocale()));
        for (Person person : getPeopleToProcess()) {

            final User user = person.getUser();
            if (user.getPerson() != null && user.getExpenditurePerson() != null) {
                Authenticate.mock(user);

                try {
                    final MissionYear missionYear = MissionYear.getCurrentYear();
                    final LocalDate today = new LocalDate();
                    final MissionYear previousYear = today.getMonthOfYear() == Month.JANUARY ? MissionYear
                            .findOrCreateMissionYear(today.getYear() - 1) : null;

                    final int takenByUserCount = (int) getTaken(missionYear, previousYear).count();

                    final SortedSet<MissionProcess> pendingApproval = previousYear == null ? missionYear
                            .getPendingAproval() : previousYear.getPendingAproval(missionYear.getPendingAproval());
                    final int pendingApprovalCount = pendingApproval.size();

                    final SortedSet<MissionProcess> pendingVehicleAuthorization =
                            previousYear == null ? missionYear.getPendingVehicleAuthorization() : previousYear
                                    .getPendingVehicleAuthorization(missionYear.getPendingVehicleAuthorization());
                    final int pendingVehicleAuthorizationCount = pendingVehicleAuthorization.size();

                    final SortedSet<MissionProcess> pendingAuthorization =
                            previousYear == null ? missionYear.getPendingAuthorization() : previousYear
                                    .getPendingAuthorization(missionYear.getPendingAuthorization());
                    final int pendingAuthorizationCount = pendingAuthorization.size();

                    final SortedSet<MissionProcess> pendingFundAllocation =
                            previousYear == null ? missionYear.getPendingFundAllocation() : previousYear
                                    .getPendingFundAllocation(missionYear.getPendingFundAllocation());
                    final int pendingFundAllocationCount = pendingFundAllocation.size();

                    final SortedSet<MissionProcess> pendingProcessing = previousYear == null ? missionYear
                            .getPendingProcessingPersonelInformation() : previousYear.getPendingProcessingPersonelInformation(
                                    missionYear.getPendingProcessingPersonelInformation());
                    final int pendingProcessingCount = pendingProcessing.size();

                    final int totalPending = takenByUserCount + pendingApprovalCount + pendingVehicleAuthorizationCount
                            + pendingAuthorizationCount + pendingFundAllocationCount + pendingProcessingCount;

                    if (totalPending > 0) {
                        try {
                            final String email = person.getEmail();
                            if (email != null) {
                                final StringBuilder body =
                                        new StringBuilder("Caro utilizador, possui processos de missão pendentes nas ");
                                body.append(Bennu.getInstance().getConfiguration().getApplicationSubTitle().getContent());
                                body.append(", em ");
                                body.append(CoreConfiguration.getConfiguration().applicationUrl());
                                body.append(".\n");

                                if (takenByUserCount > 0) {
                                    body.append("\n\tPendentes de Libertação\t");
                                    body.append(takenByUserCount);
                                }
                                if (pendingApprovalCount > 0) {
                                    body.append("\n\tPendentes de Aprovação / Verificação\t");
                                    body.append(pendingApprovalCount);
                                }
                                if (pendingVehicleAuthorizationCount > 0) {
                                    body.append("\n\tPendentes de Autorização de Viatura(s)\t");
                                    body.append(pendingVehicleAuthorizationCount);
                                }
                                if (pendingAuthorizationCount > 0) {
                                    body.append("\n\tPendentes de Autorização\t");
                                    body.append(pendingAuthorizationCount);
                                }
                                if (pendingFundAllocationCount > 0) {
                                    body.append("\n\tPendentes de Cabimentação\t");
                                    body.append(pendingFundAllocationCount);
                                }
                                if (pendingProcessingCount > 0) {
                                    body.append("\n\tPendentes de Processamento por Mim\t");
                                    body.append(pendingProcessingCount);
                                }
                                body.append("\n\n\tTotal de Processos de Missão Pendentes\t");
                                body.append(totalPending);

                                if (takenByUserCount > 0) {
                                    body.append(
                                            "\n\n\n\tPor favor, proceda à libertação dos processos em \"acesso exclusivo\", após concluir as tarefas que nele tem para realizar.\t");
                                    body.append(takenByUserCount);
                                }

                                body.append("\n\nSegue um resumo detalhado dos processos pendentes.\n");
                                if (takenByUserCount > 0) {
                                    report(body, "Pendentes de Libertação", getTaken(missionYear, previousYear));
                                }
                                if (pendingApprovalCount > 0) {
                                    report(body, "Pendentes de Aprovação", pendingApproval);
                                }
                                if (pendingVehicleAuthorizationCount > 0) {
                                    report(body, "Pendentes de Autorização de Viatura(s)", pendingVehicleAuthorization);
                                }
                                if (pendingAuthorizationCount > 0) {
                                    report(body, "Pendentes de Autorização", pendingAuthorization);
                                }
                                if (pendingFundAllocationCount > 0) {
                                    report(body, "Pendentes de Cabimentação", pendingFundAllocation);
                                }
                                if (pendingProcessingCount > 0) {
                                    report(body, "Pendentes de Processamento por Mim", pendingProcessing);
                                }

                                final Sender sender = MessagingSystem.systemSender();
                                Message.from(sender).subject("Processos Pendentes - Missões").textBody(body.toString())
                                        .to(Group.users(person.getUser())).send();
                            }
                        } catch (final Throwable ex) {
                            System.out.println("Unable to lookup email address for: " + person.getUsername());
                            // skip this person... keep going to next.
                        }
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

    private static void report(final StringBuilder body, final String title, final SortedSet<MissionProcess> processes) {
        body.append("\n\t");
        body.append(title);
        body.append(":");
        for (final MissionProcess missionProcess : processes) {
            final Mission mission = missionProcess.getMission();
            body.append("\n\t\t");
            body.append(missionProcess.getProcessIdentification());
            body.append(" - ");
            body.append(mission.getDestinationDescription());
            body.append(" (");
            body.append(mission.getDaparture().toString("yyyy-MM-dd"));
            body.append(" - ");
            body.append(mission.getArrival().toString("yyyy-MM-dd"));
            body.append(")");
        }
    }

    private static void report(final StringBuilder body, final String title, final Stream<MissionProcess> processes) {
        body.append("\n\t");
        body.append(title);
        body.append(":");
        processes.forEach(new Consumer<MissionProcess>() {
            @Override
            public void accept(MissionProcess missionProcess) {
                final Mission mission = missionProcess.getMission();
                body.append("\n\t\t");
                body.append(missionProcess.getProcessIdentification());
                body.append(" - ");
                body.append(mission.getDestinationDescription());
                body.append(" (");
                body.append(mission.getDaparture().toString("yyyy-MM-dd"));
                body.append(" - ");
                body.append(mission.getArrival().toString("yyyy-MM-dd"));
                body.append(")");
            }
        });
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
