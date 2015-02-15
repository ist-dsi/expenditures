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
package pt.ist.expenditureTrackingSystem.domain;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.portal.domain.PortalConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.domain.Message.MessageBuilder;
import org.fenixedu.messaging.domain.MessagingSystem;
import org.fenixedu.messaging.domain.Sender;
import org.jfree.data.time.Month;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MultiCounter;
import pt.ist.expenditureTrackingSystem.util.ProcessMapGenerator;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class EmailDigesterUtil {

    public static void executeTask() {

        I18N.setLocale(new Locale(CoreConfiguration.getConfiguration().defaultLocale()));
        for (Person person : getPeopleToProcess()) {
            try {
                Authenticate.mock(person.getUser());
                Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> generateAcquisitionMap =
                        ProcessMapGenerator.generateAcquisitionMap(person, true);
                Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> generateRefundMap =
                        ProcessMapGenerator.generateRefundMap(person, true);

                if (!generateAcquisitionMap.isEmpty() || !generateRefundMap.isEmpty()) {
                    try {
                        final String email = person.getEmail();
                        if (email != null) {
                            final Sender sender = MessagingSystem.getInstance().getSystemSender();
                            final Group group = UserGroup.of(person.getUser());
                            final MessageBuilder message = sender.message("Processos Pendentes - Aquisições",
                                    getBody(generateAcquisitionMap, generateRefundMap, CoreConfiguration.getConfiguration()
                                            .applicationUrl()));
                            message.to(group);
                            message.send();
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
        final PaymentProcessYear paymentProcessYear =
                PaymentProcessYear.getPaymentProcessYearByYear(Calendar.getInstance().get(Calendar.YEAR));
        addRequestors(people, paymentProcessYear);
        if (today.getMonthOfYear() == Month.JANUARY) {
            final PaymentProcessYear previousYear =
                    PaymentProcessYear.getPaymentProcessYearByYear(Calendar.getInstance().get(Calendar.YEAR) - 1);
            addRequestors(people, previousYear);
        }
        return people;
    }

    private static void addRequestors(final Set<Person> people, final PaymentProcessYear paymentProcessYear) {
        for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
            if (paymentProcess.getRequest() != null) {
                final Person person = paymentProcess.getRequestor();
                if (person != null && person.getOptions().getReceiveNotificationsByEmail()) {
                    people.add(person);
                }
            }
        }
    }

    private static void addPeopleWithRole(final Set<Person> people, final RoleType roleType) {
        addUsers(people, roleType.group().getMembers());
    }

    private static void addUsers(final Set<Person> people, Collection<User> unverified) {
        for (final User user : unverified) {
            addPerson(people, user.getExpenditurePerson());
        }
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

    private static String getBody(Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> acquisitionMap,
            Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> refundMap, final String url) {

        final StringBuilder builder = new StringBuilder("Caro utilizador, possui processos de aquisições pendentes nas ");
        builder.append(PortalConfiguration.getInstance().getApplicationSubTitle().getContent());
        builder.append(", em ");
        builder.append(url);
        builder.append("/.\n");

        if (!acquisitionMap.isEmpty()) {
            builder.append("Regime simplificado\n");
            for (final MultiCounter<AcquisitionProcessStateType> multiCounter : acquisitionMap.values()) {
                builder.append("\t");
                builder.append(multiCounter.getCountableObject().getLocalizedName());
                builder.append("\t");
                builder.append(ProcessMapGenerator.getDefaultCounter(multiCounter).getValue());
                builder.append("\n");
                final Set<SimplifiedProcedureProcess> processes =
                        (Set) ProcessMapGenerator.getDefaultCounter(multiCounter).getObjects();
                if (processes.size() < 25) {
                    for (final SimplifiedProcedureProcess process : processes) {
                        builder.append("\t\t");
                        builder.append(process.getProcessNumber());
                        builder.append("\n");
                    }
                }
            }
        }
        if (!refundMap.isEmpty()) {
            builder.append("Processos de reembolso\n");
            for (final MultiCounter<RefundProcessStateType> multiCounter : refundMap.values()) {
                builder.append("\t");
                builder.append(multiCounter.getCountableObject().getLocalizedName());
                builder.append("\t");
                builder.append(ProcessMapGenerator.getDefaultCounter(multiCounter).getValue());
                builder.append("\n");
                final Set<RefundProcess> processes = (Set) ProcessMapGenerator.getDefaultCounter(multiCounter).getObjects();
                if (processes.size() < 25) {
                    for (final RefundProcess process : processes) {
                        builder.append("\t\t");
                        builder.append(process.getProcessNumber());
                        builder.append("\n");
                    }
                }
            }
        }
        return builder.toString();
    }

}
