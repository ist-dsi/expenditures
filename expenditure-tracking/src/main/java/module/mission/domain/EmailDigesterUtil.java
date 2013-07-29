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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import module.organization.domain.Party;

import org.jfree.data.time.Month;
import org.joda.time.LocalDate;

import pt.ist.bennu.core.applicationTier.Authenticate;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.groups.PersistentGroup;
import pt.ist.bennu.core.domain.groups.SingleUserGroup;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.Role;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.messaging.domain.Message;
import pt.ist.messaging.domain.Sender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class EmailDigesterUtil {

    public static void executeTask() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        Language.setLocale(Language.getDefaultLocale());
        for (Person person : getPeopleToProcess()) {

            final User user = person.getUser();
            if (user.getPerson() != null && user.getExpenditurePerson() != null) {
                final UserView userView = Authenticate.authenticate(user);
                pt.ist.fenixWebFramework.security.UserView.setUser(userView);

                try {
                    final MissionYear missionYear = MissionYear.getCurrentYear();
                    final LocalDate today = new LocalDate();
                    final MissionYear previousYear =
                            today.getMonthOfYear() == Month.JANUARY ? MissionYear.findOrCreateMissionYear(today.getYear() - 1) : null;

                    final SortedSet<MissionProcess> takenByUser =
                            previousYear == null ? missionYear.getTaken() : previousYear.getTaken(missionYear.getTaken());
                    final int takenByUserCount = takenByUser.size();

                    final SortedSet<MissionProcess> pendingApproval =
                            previousYear == null ? missionYear.getPendingAproval() : previousYear.getPendingAproval(missionYear
                                    .getPendingAproval());
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

                    final SortedSet<MissionProcess> pendingProcessing =
                            previousYear == null ? missionYear.getPendingProcessingPersonelInformation() : previousYear
                                    .getPendingProcessingPersonelInformation(missionYear
                                            .getPendingProcessingPersonelInformation());
                    final int pendingProcessingCount = pendingProcessing.size();

                    final int totalPending =
                            takenByUserCount + pendingApprovalCount + pendingVehicleAuthorizationCount
                                    + pendingAuthorizationCount + pendingFundAllocationCount + pendingProcessingCount;

                    if (totalPending > 0) {
                        try {
                            final String email = person.getEmail();
                            if (email != null) {
                                final StringBuilder body =
                                        new StringBuilder("Caro utilizador, possui processos de missão pendentes nas ");
                                body.append(virtualHost.getApplicationSubTitle().getContent());
                                body.append(", em https://");
                                body.append(virtualHost.getHostname());
                                body.append("/.\n");

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
                                    body.append("\n\n\n\tPor favor, proceda à libertação dos processos em \"acesso exclusivo\", após concluir as tarefas que nele tem para realizar.\t");
                                    body.append(takenByUserCount);
                                }

                                body.append("\n\nSegue um resumo detalhado dos processos pendentes.\n");
                                if (takenByUserCount > 0) {
                                    report(body, "Pendentes de Libertação", takenByUser);
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
                                if (takenByUserCount > 0) {
                                    report(body, "Processos em \"acesso exclusivo\"", takenByUser);
                                }

                                final Sender sender = virtualHost.getSystemSender();
                                final PersistentGroup group = SingleUserGroup.getOrCreateGroup(person.getUser());
                                new Message(sender, Collections.EMPTY_SET, Collections.singleton(group), Collections.EMPTY_SET,
                                        Collections.EMPTY_SET, null, "Processos Pendentes - Missões", body.toString(), null);
                            }
                        } catch (final Throwable ex) {
                            System.out.println("Unable to lookup email address for: " + person.getUsername());
                            // skip this person... keep going to next.
                        }
                    }
                } finally {
                    pt.ist.fenixWebFramework.security.UserView.setUser(null);
                }
            }
        }
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

    private static Collection<Person> getPeopleToProcess() {
        final Set<Person> people = new HashSet<Person>();
        final LocalDate today = new LocalDate();
        final ExpenditureTrackingSystem expendituresSystem = ExpenditureTrackingSystem.getInstance();
        for (User user : MissionSystem.getInstance().getVehicleAuthorizers()) {
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
        final Role role = Role.getRole(roleType);
        addPeople(people, role.getPersonSet());
    }

    private static void addPeople(final Set<Person> people, Collection<Person> unverified) {
        for (final Person person : unverified) {
            if (person.getOptions().getReceiveNotificationsByEmail()) {
                people.add(person);
            }
        }
    }

}
