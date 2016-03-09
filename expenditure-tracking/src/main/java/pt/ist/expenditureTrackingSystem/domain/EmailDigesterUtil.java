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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import module.workflow.domain.WorkflowProcess;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.domain.Message;
import org.fenixedu.messaging.template.DeclareMessageTemplate;
import org.fenixedu.messaging.template.TemplateParameter;
import org.jfree.data.time.Month;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.Counter;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MultiCounter;
import pt.ist.expenditureTrackingSystem.util.ProcessMapGenerator;

/**
 *
 * @author Luis Cruz
 *
 */

@DeclareMessageTemplate(id = "expenditures.payment.pending", bundle = Bundle.ACQUISITION,
        description = "template.payment.pending", subject = "template.payment.pending.subject",
        text = "template.payment.pending.text", parameters = {
                @TemplateParameter(id = "applicationTitle", description = "template.parameter.application.subtitle"),
                @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
                @TemplateParameter(id = "acquisitions", description = "template.parameter.payment.acquisitions"),
                @TemplateParameter(id = "refunds", description = "template.parameter.payment.refunds") })
public class EmailDigesterUtil {

    public static void executeTask() {

        I18N.setLocale(new Locale(CoreConfiguration.getConfiguration().defaultLocale()));
        for (Person person : getPeopleToProcess()) {
            try {
                User user = person.getUser();
                Authenticate.mock(user);
                Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> generateAcquisitionMap =
                        ProcessMapGenerator.generateAcquisitionMap(person, true);
                Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> generateRefundMap =
                        ProcessMapGenerator.generateRefundMap(person, true);

                if (!generateAcquisitionMap.isEmpty() || !generateRefundMap.isEmpty()) {
                    Message.fromSystem()
                            .to(UserGroup.of(user))
                            .template("expenditures.payment.pending")
                            .parameter("applicationTitle", Bennu.getInstance().getConfiguration().getApplicationSubTitle())
                            .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl())
                            .parameter("acquisitions",
                                    getCounterList(AcquisitionProcessStateType.class.getSimpleName(), generateAcquisitionMap))
                            .parameter("refunds", getCounterList(RefundProcessStateType.class.getSimpleName(), generateRefundMap))
                            .and().send();
                }
            } finally {
                Authenticate.unmock();
            }
        }
    }

    public static class MultiCounterBean implements Comparable<MultiCounterBean> {
        private String name;
        private int value;
        private List<String> processes;

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public List<String> getProcesses() {
            return processes;
        }

        //XXX receiving the relevant super type name here to account for subclasses since we have a class dependent localized name in the properties
        public <T extends Enum> MultiCounterBean(String relevantTypeName, MultiCounter<T> multiCounter) {
            Counter<T> counter = ProcessMapGenerator.getDefaultCounter(multiCounter);
            T countable = counter.getCountableObject();
            this.name = relevantTypeName + "." + countable.name();
            this.value = counter.getValue();
            Set<WorkflowProcess> processes = (Set) counter.getObjects();
            this.processes =
                    processes.size() < 25 ? processes.stream().map(p -> p.getProcessNumber()).sorted()
                            .collect(Collectors.toList()) : null;
        }

        @Override
        public int compareTo(MultiCounterBean o) {
            return name.compareTo(o.getName());
        }
    }

    //XXX receiving the relevant super type name here to account for subclasses since we have a class dependent localized name in the properties
    private static <T extends Enum> List<MultiCounterBean> getCounterList(String relevantTypeName, Map<T, MultiCounter<T>> map) {
        return map.entrySet().stream().map(c -> new MultiCounterBean(relevantTypeName, c.getValue())).sorted()
                .collect(Collectors.toList());
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

}
