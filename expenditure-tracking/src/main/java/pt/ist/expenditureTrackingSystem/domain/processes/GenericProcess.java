/*
 * @(#)GenericProcess.java
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
package pt.ist.expenditureTrackingSystem.domain.processes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.domain.WorkflowLog;
import module.workflow.domain.WorkflowProcessComment;
import module.workflow.domain.WorkflowSystem;
import module.workflow.util.ProcessEvaluator;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProcessesThatAreAuthorizedByUserPredicate;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author João Antunes
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public abstract class GenericProcess extends GenericProcess_Base {

    public GenericProcess() {
        super();
        final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
        setExpenditureTrackingSystem(instance);
    }

    public static <T extends GenericProcess> Stream<T> getAllProcesses(Class<T> processClass, PaymentProcessYear year) {
        return year != null ? filter(processClass, null, year.getPaymentProcessSet().stream()) : filter(processClass, null, WorkflowSystem
                .getInstance().getProcessesSet().stream());
    }

    public static void evaluateAllProcess(final Class processClass, final ProcessEvaluator<GenericProcess> processEvaluator,
            final PaymentProcessYear year) {
        if (year == null) {
            evaluate(processClass, (ProcessEvaluator) processEvaluator, WorkflowSystem.getInstance().getProcessesSet());
        } else {
            evaluate(processClass, (ProcessEvaluator) processEvaluator, year.getPaymentProcess());
        }
    }

    public static <T extends GenericProcess> Stream<T> getAllProcess(Class<T> processClass, Predicate predicate, PaymentProcessYear year) {
        return (Stream) (year != null ? filter(processClass, predicate, year.getPaymentProcessSet().stream())
                : filter(processClass, predicate, WorkflowSystem.getInstance().getProcessesSet().stream()));
    }

    public static void evaluateProcessesWithResponsible(final Class processClass, final Person person,
            final PaymentProcessYear year, final ProcessEvaluator<GenericProcess> processEvaluator) {
        if (person == null) {
            return;
        }

        final Class clazz = processClass == null ? PaymentProcess.class : processClass;
        final ProcessesThatAreAuthorizedByUserPredicate predicate = new ProcessesThatAreAuthorizedByUserPredicate(person);

        final ProcessEvaluator<GenericProcess> genericProcessEvaluator = new ProcessEvaluator<GenericProcess>() {
            private final Set<GenericProcess> processed = new HashSet<GenericProcess>();

            {
                next = processEvaluator;
            }

            @Override
            public void evaluate(GenericProcess process) {
                if (!processed.contains(process) && clazz.isAssignableFrom(process.getClass()) && predicate.test(process)) {
                    processed.add(process);
                    super.evaluate(process);
                }
            }
        };

        // final Set<PaymentProcess> processes = new HashSet<PaymentProcess>();
        // final Set<Unit> units = new HashSet<Unit>();
        for (final Authorization authorization : person.getAuthorizations()) {
            final Unit unit = authorization.getUnit();
            unit.evaluateAllProcesses(genericProcessEvaluator, year);
            // units.add(unit);
            // units.addAll(unit.getAllSubUnits());
        }

        // for (final Unit unit : units) {
        // processes.addAll(unit.getProcesses(year));
        // }

        // final Class clazz = processClass == null ? PaymentProcess.class :
        // processClass;
        // final ProcessesThatAreAuthorizedByUserPredicate predicate = new
        // ProcessesThatAreAuthorizedByUserPredicate(person);
        // for (final PaymentProcess paymentProcess : processes) {
        // if (clazz.isAssignableFrom(paymentProcess.getClass()) &&
        // predicate.evaluate(paymentProcess)) {
        // processEvaluator.evaluate(paymentProcess);
        // }
        // }
    }

    public static <T extends PaymentProcess> Stream<T> getProcessesWithResponsible(Class<T> processClass, final Person person,
            PaymentProcessYear year) {
        if (person == null) {
            return Stream.empty();
        }

        Set<PaymentProcess> processes = new HashSet<PaymentProcess>();
        Set<Unit> units = new HashSet<Unit>();
        for (Authorization authorization : person.getAuthorizations()) {
            Unit unit = authorization.getUnit();
            units.add(unit);
            units.addAll(unit.getAllSubUnits());
        }

        for (Unit unit : units) {
            processes.addAll(unit.getProcesses(year));
        }
        final Class classArg = processClass != null ? processClass : PaymentProcess.class;
        return GenericProcess.filter(classArg, null, processes).stream();
    }

    public static void evaluateProcessesForPerson(final Class processClass, final Person person, final PaymentProcessYear year,
            final boolean userAwarness, final ProcessEvaluator<GenericProcess> processEvaluator) {
        if (person.hasAnyAuthorizations()) {
            GenericProcess.evaluateProcessesWithResponsible(processClass, person, year, new ProcessEvaluator<GenericProcess>() {
                {
                    next = processEvaluator;
                }

                @Override
                public void evaluate(final GenericProcess process) {
                    if (process.hasAnyAvailableActivity(userAwarness)) {
                        super.evaluate(process);
                    }
                }

            });
        } else {
            final User user = person.getUser();
            GenericProcess.evaluateAllProcess(processClass, new ProcessEvaluator<GenericProcess>() {
                {
                    next = processEvaluator;
                }

                @Override
                public void evaluate(final GenericProcess process) {
                    if (process.hasAnyAvailableActivity(user, userAwarness)) {
                        super.evaluate(process);
                    }
                }

            }, year);
        }
    }

    public static <T extends PaymentProcess> Stream<T> getProcessesForPerson(Class<T> processClass, final Person person,
            PaymentProcessYear year, final boolean userAwarness) {

        Stream<T> processes = null;
        final User user = person.getUser();
        if (person.hasAnyValidAuthorization()
                && !(ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user) || ExpenditureTrackingSystem
                        .isAcquisitionCentralManagerGroupMember(user))) {
            processes = GenericProcess.getProcessesWithResponsible(processClass, person, year)
                    .filter(process -> process.hasAnyAvailableActivity(user, userAwarness));
        } else {
            processes = GenericProcess.getAllProcess(processClass, new Predicate() {
                @Override
                public boolean test(Object arg0) {
                    GenericProcess process = (GenericProcess) arg0;
                    return process.hasAnyAvailableActivity(user, userAwarness);
                }
            }, year);
        }

        return processes;
    }

    public static boolean isCreateNewProcessAvailable() {
        final User user = Authenticate.getUser();
        return user != null;
    }

    @Override
    public boolean isSystemAbleToNotifyUser(User user) {
        return user.getExpenditurePerson().getEmail() != null;
    }

    protected Person getLoggedPerson() {
        return Person.getLoggedPerson();
    }

    @Override
    public List<WorkflowProcessComment> getUnreadCommentsForCurrentUser() {
        return getUnreadCommentsForPerson(Person.getLoggedPerson());
    }

    public List<WorkflowProcessComment> getUnreadCommentsForPerson(Person person) {
        User user = person.getUser();
        List<WorkflowProcessComment> comments = new ArrayList<WorkflowProcessComment>();
        for (WorkflowProcessComment comment : getComments()) {
            if (comment.isUnreadBy(user)) {
                comments.add(comment);
            }
        }
        return comments;
    }

    public boolean hasActivitiesFromUser(Person person) {
        User user = person.getUser();
        for (WorkflowLog log : getExecutionLogs()) {
            if (log.getActivityExecutor() == user) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.ProcessState> getProcessStates() {
        return getProcessStatesSet();
    }

    @Deprecated
    public boolean hasAnyProcessStates() {
        return !getProcessStatesSet().isEmpty();
    }

    @Deprecated
    public boolean hasCurrentProcessState() {
        return getCurrentProcessState() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

}
