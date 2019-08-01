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

import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
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
        text = "template.payment.pending.text",
        parameters = { @TemplateParameter(id = "applicationTitle", description = "template.parameter.application.subtitle"),
                @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
                @TemplateParameter(id = "acquisitions", description = "template.parameter.payment.acquisitions"),
                @TemplateParameter(id = "consultation", description = "template.parameter.payment.consultation"),
                @TemplateParameter(id = "refunds", description = "template.parameter.payment.refunds") })
public class EmailDigesterUtil {

    public static void executeTask() {
        I18N.setLocale(new Locale(CoreConfiguration.getConfiguration().defaultLocale()));
        getPeopleToProcess().forEach(EmailDigesterUtil::createMessageDigest);
    }

    private static void createMessageDigest(final Person person) {
        try {
            User user = person.getUser();
            Authenticate.mock(user, "System Automation");

            Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> generateAcquisitionMap =
                    ProcessMapGenerator.generateAcquisitionMap(person, true);
            Map<MultipleSupplierConsultationProcessState, MultiCounter<MultipleSupplierConsultationProcessState>> generateConsultationMap =
                    ProcessMapGenerator.generateConsultationMap(person, true);
            Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> generateRefundMap =
                    ProcessMapGenerator.generateRefundMap(person, true);

            if (!generateAcquisitionMap.isEmpty() || !generateRefundMap.isEmpty()) {
                Message.fromSystem().to(Group.users(person.getUser())).template("expenditures.payment.pending")
                        .parameter("applicationTitle", Bennu.getInstance().getConfiguration().getApplicationSubTitle().getContent())
                        .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl())
                        .parameter("acquisitions",
                                getCounterList(AcquisitionProcessStateType.class.getSimpleName(), generateAcquisitionMap))
                        .parameter("consultation",
                                getCounterList(MultipleSupplierConsultationProcessState.class.getSimpleName(),
                                        generateConsultationMap))
                        .parameter("refunds", getCounterList(RefundProcessStateType.class.getSimpleName(), generateRefundMap))

                        .and().send();
            }
        } finally {
            Authenticate.unmock();
        }
    }

    public static class MultiCounterBean implements Comparable<MultiCounterBean> {
        private final String name;
        private final int value;
        private final List<String> processes;

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
            this.processes = processes.size() < 25 ? processes.stream().map(p -> p.getProcessNumber()).sorted()
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

    private static Stream<Person> getPeopleToProcess() {
        Stream<Person> people;

        final LocalDate today = new LocalDate();
        final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();

        people = instance.getAuthorizationsSet().stream().filter(a -> a.isValidFor(today)).map(a -> a.getPerson());

        for (final RoleType roleType : RoleType.values()) {
            people = Stream.concat(people, peopleWithRole(roleType));
        }

        for (final AccountingUnit accountingUnit : instance.getAccountingUnitsSet()) {
            people = Stream.concat(people, accountingUnit.getPeopleSet().stream());
            people = Stream.concat(people, accountingUnit.getProjectAccountantsSet().stream());
            people = Stream.concat(people, accountingUnit.getResponsiblePeopleSet().stream());
            people = Stream.concat(people, accountingUnit.getResponsibleProjectAccountantsSet().stream());
            people = Stream.concat(people, accountingUnit.getTreasuryMembersSet().stream());
        }

        final PaymentProcessYear paymentProcessYear =
                PaymentProcessYear.getPaymentProcessYearByYear(Calendar.getInstance().get(Calendar.YEAR));
        people = Stream.concat(people, requestors(paymentProcessYear));
        if (today.getMonthOfYear() == Month.JANUARY) {
            final PaymentProcessYear previousYear =
                    PaymentProcessYear.getPaymentProcessYearByYear(Calendar.getInstance().get(Calendar.YEAR) - 1);
            people = Stream.concat(people, requestors(previousYear));
        }

        return people.filter(p -> p.getOptions().getReceiveNotificationsByEmail()).distinct();
    }

    private static Stream<Person> requestors(final PaymentProcessYear paymentProcessYear) {
        return paymentProcessYear.getPaymentProcessSet().stream().filter(pp -> pp.getRequest() != null)
                .map(pp -> pp.getRequestor()).filter(p -> p != null);
    }

    private static Stream<Person> peopleWithRole(final RoleType roleType) {
        return roleType.group().getMembers().map(u -> u.getExpenditurePerson());
    }

}
