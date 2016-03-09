/*
 * @(#)PaymentProcess.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.messaging.domain.Message;
import org.fenixedu.messaging.domain.Message.MessageBuilder;
import org.fenixedu.messaging.domain.MessagingSystem;
import org.fenixedu.messaging.domain.Sender;
import org.joda.time.LocalDate;

import module.finance.util.Money;
import module.mission.domain.MissionProcess;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowLog;
import module.workflow.domain.utils.WorkflowCommentCounter;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.HasPresentableProcessState;
import module.workflow.util.PresentableProcessState;
import module.workflow.widgets.UnreadCommentsWidget;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

@ClassNameBundle(bundle = "ExpenditureResources")
/**
 * 
 * @author João Antunes
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author João Alfaiate
 * 
 */
public abstract class PaymentProcess extends PaymentProcess_Base implements HasPresentableProcessState {

    public static Comparator<PaymentProcess> COMPARATOR_BY_YEAR_AND_ACQUISITION_PROCESS_NUMBER =
            new Comparator<PaymentProcess>() {
                @Override
                public int compare(PaymentProcess o1, PaymentProcess o2) {
                    int yearComp = COMPARATOR_BY_YEAR.compare(o1, o2);
                    return (yearComp != 0) ? yearComp : COMPARATOR_BY_ACQUISITION_PROCESS_NUMBER.compare(o1, o2);
                }
            };

    public static Comparator<PaymentProcess> COMPARATOR_BY_YEAR = new Comparator<PaymentProcess>() {
        @Override
        public int compare(PaymentProcess o1, PaymentProcess o2) {
            return o1.getPaymentProcessYear().getYear().compareTo(o2.getPaymentProcessYear().getYear());
        }
    };

    public static Comparator<PaymentProcess> COMPARATOR_BY_ACQUISITION_PROCESS_NUMBER = new Comparator<PaymentProcess>() {
        @Override
        public int compare(PaymentProcess o1, PaymentProcess o2) {
            return o1.getAcquisitionProcessNumber().compareTo(o2.getAcquisitionProcessNumber());
        }
    };

    static {
        UnreadCommentsWidget.register(new WorkflowCommentCounter(PaymentProcess.class));
    }

    public PaymentProcess() {
        super();
        final PaymentProcessYear acquisitionProcessYear = getPaymentProcessYearForConstruction();
        setPaymentProcessYear(acquisitionProcessYear);
        setAcquisitionProcessNumber(acquisitionProcessYear.nextAcquisitionProcessYearNumber());
    }

    private PaymentProcessYear getPaymentProcessYearForConstruction() {
        return PaymentProcessYear.getPaymentProcessYearByYear(getYearForConstruction());
    }

    protected int getYearForConstruction() {
        return new LocalDate().getYear();
    }

    public abstract <T extends RequestWithPayment> T getRequest();

    /**
     * Use getPayingUnitStream instead
     * 
     */
    @Deprecated
    public List<Unit> getPayingUnits() {
        List<Unit> res = new ArrayList<Unit>();
        for (Financer financer : getRequest().getFinancers()) {
            res.add(financer.getUnit());
        }
        return res;
    }

    public Stream<Unit> getPayingUnitStream() {
        return getRequest().getFinancersSet().stream().map(f -> f.getUnit());
    }

    public boolean isRealValueEqualOrLessThanFundAllocation() {
        Money allocatedMoney = this.getRequest().getTotalValue();
        Money realMoney = this.getRequest().getRealTotalValue();
        return realMoney.isLessThanOrEqual(allocatedMoney);
    }

    public boolean isResponsibleForAtLeastOnePayingUnit(Person person) {
        return getPayingUnitStream().anyMatch(u -> u.isResponsible(person));
    }

    public Person getRequestor() {
        return getRequest().getRequester();
    }

    public boolean isResponsibleForUnit(Person person) {
        return getPayingUnitStream().anyMatch(u -> isResponsibleForUnit(person, u));
    }

    private boolean isResponsibleForUnit(final Person person, final Unit unit) {
        return person.getValidAuthorizationStream().anyMatch(a -> unit.isSubUnit(a.getUnit()));
    }

    public boolean isResponsibleForUnit() {
        final Person loggedPerson = getLoggedPerson();
        return loggedPerson != null && isResponsibleForUnit(loggedPerson);
    }

    public boolean isProjectAccountingEmployee(final Person person) {
        return getRequest().isProjectAccountingEmployee(person);
    }

    public boolean isTreasuryMember(final Person person) {
        return getRequest().isTreasuryMember(person);
    }

    public boolean isProjectAccountingEmployee() {
        final Person loggedPerson = getLoggedPerson();
        return loggedPerson != null && isProjectAccountingEmployee(loggedPerson);
    }

    public boolean hasAllocatedFundsForAllProjectFinancers(final Person person) {
        return getRequest().hasAllocatedFundsForAllProjectFinancers(person);
    }

    public boolean hasAllInvoicesAllocated() {
        return getRequest().hasAllInvoicesAllocated();
    }

    public boolean hasAllInvoicesAllocatedInProject() {
        return getRequest().hasAllInvoicesAllocatedInProject();
    }

    public boolean hasAnyFundAllocationId() {
        return getRequest().hasAnyFundAllocationId();
    }

    public boolean hasAnyEffectiveFundAllocationId() {
        return getRequest().hasAnyEffectiveFundAllocationId();
    }

    public boolean hasAnyNonProjectFundAllocationId() {
        return getRequest().hasAnyNonProjectFundAllocationId();
    }

    public boolean isAccountingEmployee(final Person person) {
        return getRequest().isAccountingEmployee(person);
    }

    public boolean isProjectAccountingEmployeeForOnePossibleUnit() {
        final Person loggedPerson = getLoggedPerson();
        return loggedPerson != null && isProjectAccountingEmployeeForOnePossibleUnit(loggedPerson);
    }

    public boolean isProjectAccountingEmployeeForOnePossibleUnit(final Person person) {
        return getRequest().isProjectAccountingEmployeeForOnePossibleUnit(person);
    }

    public boolean isAccountingEmployee() {
        final Person loggedPerson = getLoggedPerson();
        return loggedPerson != null && isAccountingEmployee(loggedPerson);
    }

    public boolean isAccountingEmployeeForOnePossibleUnit() {
        final Person loggedPerson = getLoggedPerson();
        return loggedPerson != null && isAccountingEmployeeForOnePossibleUnit(loggedPerson);
    }

    public boolean isAccountingEmployeeForOnePossibleUnit(final Person person) {
        return getRequest().isAccountingEmployeeForOnePossibleUnit(person);
    }

    public boolean hasAllocatedFundsForAllProjectFinancers() {
        return getRequest().hasAllocatedFundsForAllProjectFinancers();
    }

    public boolean hasAnyAllocatedFunds() {
        return getRequest().hasAnyAllocatedFunds();
    }

    public boolean hasAllFundAllocationId(Person person) {
        return getRequest().hasAllFundAllocationId(person);
    }

    public Set<Financer> getFinancersWithFundsAllocated() {
        return getRequest().getFinancersWithFundsAllocated();
    }

    public Set<Financer> getFinancersWithFundsInitiallyAllocated() {
        return getRequest().getFinancersWithFundsInitiallyAllocated();
    }

    public Set<Financer> getFinancersWithFundsAllocated(Person person) {
        return getRequest().getFinancersWithFundsAllocated(person);
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated() {
        return getRequest().getProjectFinancersWithFundsAllocated();
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated(final Person person) {
        return getRequest().getProjectFinancersWithFundsAllocated(person);
    }

    public abstract boolean isCanceled();

    public abstract boolean isInGenesis();

    public abstract boolean isPendingApproval();

    public abstract boolean isInApprovedState();

    public abstract boolean isPendingFundAllocation();

    public abstract void allocateFundsToUnit();

    public abstract void submitForApproval();

    public abstract void submitForFundAllocation();

    public abstract boolean isInAllocatedToUnitState();

    public abstract boolean isInAuthorizedState();

    protected abstract void authorize();

    public boolean isResponsibleForUnit(final Person person, final Money amount) {
        return getPayingUnitStream().anyMatch(u -> isResponsibleForUnit(person, amount, u));
    }

    public boolean isResponsibleForUnit(final Person person, final Money amount, final Unit unit) {
        return person.getValidAuthorizationStream().anyMatch(a -> isResponsibleForUnit(amount, unit, a));
    }

    private boolean isResponsibleForUnit(final Money amount, final Unit unit, final Authorization authorization) {
        if (authorization.getMaxAmount().isGreaterThanOrEqual(amount) && unit.isSubUnit(authorization.getUnit())) {
            final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
            if (!authorization.getUnit().hasParentUnit() || !isProcessessStartedWithInvoive()
                    || (isProcessessStartedWithInvoive() && (instance.getValueRequireingTopLevelAuthorization() == null
                            || instance.getValueRequireingTopLevelAuthorization().isGreaterThan(amount)))) {
                return true;
            }
        }
        return false;
    }

    protected boolean isProcessessStartedWithInvoive() {
        return false;
    }

    public void skitToAuthorizedState() {
        authorize();
    }

    public void authorizeBy(final Person person) {
        getRequest().authorizeBy(person);
        if (getRequest().isAuthorizedByAllResponsibles()) {
            authorize();
        }
    }

    public boolean hasProjectsAsPayingUnits() {
        return getPayingUnitStream().anyMatch(u -> u instanceof Project || u instanceof SubProject);
    }

    public boolean isRealValueFullyAttributedToUnits() {
        return getRequest().isRealValueFullyAttributedToUnits();
    }

    public boolean isSimplifiedProcedureProcess() {
        return false;
    }

    public boolean isInvoiceConfirmed() {
        return false;
    }

    public boolean hasAllocatedFundsPermanentlyForAllProjectFinancers() {
        final RequestWithPayment requestWithPayment = getRequest();
        return requestWithPayment.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    public boolean hasAllocatedFundsPermanentlyForAnyProjectFinancers() {
        final RequestWithPayment requestWithPayment = getRequest();
        return requestWithPayment.hasAllocatedFundsPermanentlyForAnyProjectFinancer();
    }

    public boolean areAllFundsPermanentlyAllocated() {
        final RequestWithPayment requestWithPayment = getRequest();
        return requestWithPayment.areAllFundsPermanentlyAllocated();
    }

    public void allocateFundsPermanently() {
    }

    public boolean isAllocatedPermanently() {
        return false;
    }

    public void resetEffectiveFundAllocationId() {
    }

    public <T extends WorkflowLog> List<T> getExecutionLogsForState(String stateName) {
        return (List<T>) getExecutionLogs();
    }

    public boolean isPayed() {
        return false;
    }

    public boolean isAuthorized() {
        return isInAuthorizedState();
    }

    public boolean isRefundProcess() {
        return false;
    }

    public abstract String getAcquisitionProcessId();

    private static final String EMPTY_STRING = new String();

    public String getProcessStateDescription() {
        return EMPTY_STRING;
    }

    public String getProcessStateName() {
        return EMPTY_STRING;
    }

    public int getProcessStateOrder() {
        return 0;
    }

    public abstract Collection<Supplier> getSuppliers();

    public String getSuppliersDescription() {
        Iterator<Supplier> iterator = getSuppliers().iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(iterator.next().getName());
            if (iterator.hasNext()) {
                builder.append(" ,");
            }
        }
        return builder.toString();
    }

    public String getTypeDescription() {
        return BundleUtil.getString("resources/ExpenditureResources", "label." + getClass().getSimpleName() + ".description");
    }

    public String getTypeShortDescription() {
        return BundleUtil.getString("resources/ExpenditureResources",
                "label." + getClass().getSimpleName() + ".shortDescription");
    }

    public abstract boolean isAppiableForYear(final int year);

    public boolean isPriorityProcess() {
        for (RequestItem item : getRequest().getRequestItems()) {
            if (item.getCPVReference().isPriorityCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void notifyUserDueToComment(User user, String comment) {
        final String email = user.getExpenditurePerson().getEmail();
        if (email != null) {
            final Sender sender = MessagingSystem.systemSender();
            final Group group = Group.users(user);
            final MessageBuilder message = Message.from(sender);
            message.subject(BundleUtil.getString("resources/AcquisitionResources", "label.email.commentCreated.subject",
                    getAcquisitionProcessId()));
            message.textBody(BundleUtil.getString("resources/AcquisitionResources", "label.email.commentCreated.body",
                    Authenticate.getUser().getName(), getAcquisitionProcessId(), comment,
                    CoreConfiguration.getConfiguration().applicationUrl()));
            message.to(group);
            message.send();
        }
    }

    public boolean isCurrentUserObserver() {
        return isObserver(Person.getLoggedPerson());
    }

    public boolean isObserver(Person person) {
        if (getRequest().getRequestingUnit().getObserversSet().contains(person)) {
            return true;
        }
        return getPayingUnitStream().anyMatch(u -> u.getObserversSet().contains(person));
    }

    @Override
    public User getProcessCreator() {
        return getRequest().getRequester().getUser();
    }

    public List<ProcessFile> getGenericFiles() {
        return getFiles(ProcessFile.class);
    }

    public abstract void revertToState(ProcessState processState);

    public abstract String getLocalizedName();

    @Override
    public void setMissionProcess(final MissionProcess missionProcess) {
        if (missionProcess == null
//		|| (missionProcess.isExpenditureAuthorized() && missionProcess.areAllParticipantsAuthorized())
                || (!missionProcess.isUnderConstruction() && !missionProcess.getIsCanceled() && !missionProcess.isTerminated()
        /*&& missionProcess.getMission().isPendingApproval()*/)) {
            super.setMissionProcess(missionProcess);
        } else {
//	    throw new DomainException(Bundle.EXPENDITURE, "error.cannot.connect.acquisition.to.unauthorized.mission",
            throw new DomainException(Bundle.ACQUISITION, "error.cannot.connect.acquisition.to.unsubmitted.for.approval.mission");
        }
    }

    @Override
    public PresentableProcessState getPresentableAcquisitionProcessState() {
        return null;
    }

    @Override
    public List<? extends PresentableProcessState> getAvailablePresentableStates() {
        return Collections.emptyList();
    }

    public abstract Money getTotalValue();

    public boolean isDirectResponsibleForUnit(final User user, final Money amount) {
        final Person person = user.getExpenditurePerson();
        return getPayingUnitStream().anyMatch(u -> u.isDirectResponsible(person, amount));
    }

    public abstract Set<CPVReference> getCPVReferences();

    public String getAccountingUnitsAsString() {
        final StringBuilder builder = new StringBuilder();
        for (final Financer financer : getFinancersWithFundsAllocated()) {
            final AccountingUnit accountingUnit = financer.getAccountingUnit();
            if (accountingUnit != null) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(accountingUnit.getName());
            }
        }
        return builder.toString();
    }

    public abstract AcquisitionItemClassification getGoodsOrServiceClassification();

    @Deprecated
    public boolean hasAcquisitionProcessNumber() {
        return getAcquisitionProcessNumber() != null;
    }

    @Deprecated
    public boolean hasSkipSupplierFundAllocation() {
        return getSkipSupplierFundAllocation() != null;
    }

    @Deprecated
    public boolean hasPaymentProcessYear() {
        return getPaymentProcessYear() != null;
    }

    @Deprecated
    public boolean hasMissionProcess() {
        return getMissionProcess() != null;
    }

}
