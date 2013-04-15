/*
 * @(#)MissionProcess.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import module.mission.domain.util.MissionStageView;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.workflow.activities.GiveProcess.NotifyUser;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcessComment;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.utils.WorkflowCommentCounter;
import module.workflow.widgets.UnreadCommentsWidget;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.emailNotifier.domain.Email;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;

@ClassNameBundle(bundle = "resources/MissionResources")
/**
 * 
 * @author João Antunes
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public abstract class MissionProcess extends MissionProcess_Base {

    static {
        UnreadCommentsWidget.register(new WorkflowCommentCounter(MissionProcess.class));
    }

    public static final Comparator<MissionProcess> COMPARATOR_BY_PROCESS_NUMBER = new Comparator<MissionProcess>() {
        @Override
        public int compare(final MissionProcess o1, final MissionProcess o2) {
            final MissionYear missionYear1 = o1.getMissionYear();
            final MissionYear missionYear2 = o2.getMissionYear();

            final int year = MissionYear.COMPARATOR_BY_YEAR.compare(missionYear1, missionYear2);
            if (year != 0) {
                return year;
            }

            String[] process1IdParts = o1.getProcessNumber().split("/M");
            String[] process2IdParts = o2.getProcessNumber().split("/M");

            final int compareResult = process1IdParts[0].compareTo(process2IdParts[0]);

            if (compareResult == 0) {
                final int process1Num = Integer.parseInt(process1IdParts[process1IdParts.length - 1]);
                final int process2Num = Integer.parseInt(process2IdParts[process2IdParts.length - 1]);
                final int compareNumResult = process2Num - process1Num;

                return compareNumResult == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : compareNumResult;
            } else {
                return compareResult;
            }
        }

    };

    protected static class MissionGiveProcessUserNotifier extends NotifyUser {

        @Override
        public void notifyUser(final User user, final WorkflowProcess process) {
            final pt.ist.expenditureTrackingSystem.domain.organization.Person person = user.getExpenditurePerson();
            if (person != null) {
                final String email = person.getEmail();
                if (email != null && !email.isEmpty()) {
                    final MissionProcess missionProcess = (MissionProcess) process;
                    final User currentUser = UserView.getCurrentUser();
                    final pt.ist.expenditureTrackingSystem.domain.organization.Person currentPerson =
                            currentUser == null ? null : currentUser.getExpenditurePerson();

                    final StringBuilder body = new StringBuilder("Caro utilizador, foi-lhe passado o processo de missão ");
                    body.append(missionProcess.getProcessIdentification());
                    body.append(", que pode ser consultado em http://dot.ist.utl.pt/.");
                    if (currentPerson != null) {
                        body.append(" A passagem do processo foi efectuado por ");
                        body.append(currentPerson.getName());
                        body.append(".\n\n");
                    }

                    final Set<WorkflowProcessComment> commentsSet =
                            new TreeSet<WorkflowProcessComment>(WorkflowProcessComment.REVERSE_COMPARATOR);
                    commentsSet.addAll(process.getCommentsSet());
                    if (!commentsSet.isEmpty()) {
                        body.append("O processo contem os seguintes comentários:\n\n");
                        for (final WorkflowProcessComment workflowProcessComment : commentsSet) {
                            final String comment = workflowProcessComment.getComment();
                            final DateTime date = workflowProcessComment.getDate();
                            final User commenter = workflowProcessComment.getCommenter();

                            body.append(date.toString("yyyy-MM-dd HH:mm"));
                            body.append(" - ");
                            body.append(commenter.getPresentationName());
                            body.append("\n");
                            body.append(comment);
                            body.append("\n\n");
                        }
                    }

                    body.append("\n---\n");
                    body.append("Esta mensagem foi enviada por meio das Aplicações Centrais do IST.\n");

                    final Collection<String> toAddress = Collections.singleton(email);
                    final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
                    new Email(virtualHost.getApplicationSubTitle().getContent(), virtualHost.getSystemEmailAddress(),
                            new String[] {}, toAddress, Collections.EMPTY_LIST, Collections.EMPTY_LIST,
                            "Passagem de Processo Pendentes - Missões", body.toString());
                }
            }
        }

    }

    public MissionProcess() {
        super();
        setMissionSystem(MissionSystem.getInstance());
        final MissionYear missionYear = MissionYear.getCurrentYear();
        setMissionYear(missionYear);
        setProcessNumber(missionYear.nextNumber().toString());
        setIsUnderConstruction(Boolean.TRUE);
        setIsCanceled(Boolean.FALSE);
    }

    @Deprecated
    public String getProcessIdentification() {
        return getProcessNumber();
    }

    @Override
    public String getProcessNumber() {
        final ExpenditureTrackingSystem system = getMissionSystem().getExpenditureTrackingSystem();
        if (system.hasProcessPrefix()) {
            return system.getInstitutionalProcessNumberPrefix() + "/" + getMissionYear().getYear() + "/M"
                    + super.getProcessNumber();
        }
        return getMissionYear().getYear() + "/M" + super.getProcessNumber();
    }

    public WorkflowActivity getActivity(Class<? extends WorkflowActivity> clazz) {
        for (final WorkflowActivity workflowActivity : getActivities()) {
            if (workflowActivity.getClass() == clazz) {
                return workflowActivity;
            }
        }
        return null;
    }

    public boolean isRequestor(final User user) {
        return getMission().getRequestingPerson() == user.getPerson();
    }

    public boolean isMissionResponsible(User user) {
        return getMission().getMissionResponsible() == user.getPerson();
    }

    public boolean isUnderConstruction() {
        return getIsUnderConstruction().booleanValue() && !isProcessCanceled();
    }

    public boolean isProcessCanceled() {
        final Boolean isCanceled = getIsCanceled();
        return isCanceled != null && isCanceled.booleanValue();
    }

    public boolean hasAnyAproval() {
        return getMission().hasAnyAproval();
    }

    public boolean isPendingApproval() {
        return getMission().isPendingApproval();
    }

    public boolean isPendingApprovalBy(final User user) {
        return getMission().isPendingApprovalBy(user);
    }

    public boolean isPendingAuthorizationBy(final User user) {
        return getMission().isPendingAuthorizationBy(user);
    }

    public boolean isPendingDirectAuthorizationBy(final User user) {
        return getMission().isPendingDirectAuthorizationBy(user);
    }

    public boolean canRemoveApproval(final User user) {
        return getMission().canRemoveApproval(user);
    }

    public boolean canRemoveAuthorization(final User user) {
        return getMission().canRemoveAuthorization(user);
    }

    public void approve(final User user) {
        getMission().approve(user);
    }

    public void authorize(final User user) {
        getMission().authorize(user);
    }

    public void unapprove(final User user) {
        getMission().unapprove(user);
    }

    public void unauthorize(final User user) {
        getMission().unauthorize(user);
    }

    public boolean isApprovedByResponsible() {
        return getMission().isApprovedByResponsible();
    }

    public boolean isApproved() {
        return getMission().isApproved();
    }

    public boolean hasAllAllocatedFunds() {
        return getMission().hasAllAllocatedFunds();
    }

    public boolean hasAllAllocatedProjectFunds() {
        return getMission().hasAllAllocatedProjectFunds();
    }

    public boolean hasAnyAllocatedFunds() {
        return getMission().hasAnyAllocatedFunds();
    }

    public void unAllocateFunds(final Person person) {
        getMission().unAllocateFunds(person);
    }

    public boolean hasAnyAuthorization() {
        return getMission().hasAnyAuthorization();
    }

    public boolean canAuthorizeVehicles() {
        return (!hasCurrentOwner() || isTakenByCurrentUser()) && isApprovedByResponsible() && !isCanceled()
                && !areAllParticipantsAuthorized();
    }

    public boolean canAuthoriseParticipantActivity() {
        return getMission().canAuthoriseParticipantActivity();
    }

    public boolean canUnAuthoriseParticipantActivity() {
        return getMission().canUnAuthoriseParticipantActivity();
    }

    public boolean canUnAuthoriseSomeParticipantActivity() {
        return getMission().canUnAuthoriseSomeParticipantActivity();
    }

    public boolean canAllocateFund() {
        return getMission().canAllocateFund();
    }

    public boolean canAllocateProjectFund() {
        return getMission().canAllocateProjectFund();
    }

    public boolean isDirectResponsibleForPendingProjectFundAllocation() {
        return getMission().isDirectResponsibleForPendingProjectFundAllocation();
    }

    public boolean hasAnyAuthorizedParticipants() {
        return getMission().hasAnyAuthorizedParticipants();
    }

    public boolean hasAnyParticipantes() {
        return getMission().hasAnyParticipantes();
    }

    public boolean hasAnyMissionItems() {
        return getMission().hasAnyMissionItems();
    }

    public boolean hasAnyAllocatedProjectFunds() {
        return getMission().hasAnyAllocatedProjectFunds();
    }

    public void unAllocateProjectFunds(Person person) {
        getMission().unAllocateProjectFunds(person);
    }

    public boolean hasAnyProjectFinancer() {
        return getMission().hasAnyProjectFinancer();
    }

    public boolean isConsistent() {
        return getMission().isConsistent();
    }

    @Override
    public User getProcessCreator() {
        return getMission().getRequestingPerson().getUser();
    }

    @Override
    public void notifyUserDueToComment(final User user, final String comment) {
        List<String> toAddress = new ArrayList<String>();
        toAddress.clear();
        final String email = user.getExpenditurePerson().getEmail();
        if (email != null) {
            toAddress.add(email);

            final User loggedUser = UserView.getCurrentUser();
            final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
            new Email(virtualHost.getApplicationSubTitle().getContent(), virtualHost.getSystemEmailAddress(), new String[] {},
                    toAddress, Collections.EMPTY_LIST, Collections.EMPTY_LIST, BundleUtil.getFormattedStringFromResourceBundle(
                            "resources/MissionResources", "label.email.commentCreated.subject", getProcessIdentification()),
                    BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
                            "label.email.commentCreated.body", loggedUser.getPerson().getName(), getProcessIdentification(),
                            comment, virtualHost.getHostname()));
        }
    }

    protected abstract String notificationSubjectHeader();

    public void notifyAllParticipants() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();

        final Mission mission = getMission();
        for (final Person person : mission.getParticipantesSet()) {
            final User user = person.getUser();
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                new Email(virtualHost.getApplicationSubTitle().getContent(), virtualHost.getSystemEmailAddress(),
                        new String[] {}, Collections.singletonList(user.getEmail()), Collections.EMPTY_LIST,
                        Collections.EMPTY_LIST, BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
                                notificationSubjectHeader(), getProcessIdentification(), mission.getLocation(), mission
                                        .getCountry().getName().getContent()), BundleUtil.getFormattedStringFromResourceBundle(
                                "resources/MissionResources", "label.email.mission.participation.authorized.body"));
            }
        }
    }

    public void setProcessParticipantInformationQueue() {
        if (getMission().allParticipantsAreAuthorized()) {
            addToProcessParticipantInformationQueue();
            notifyAllParticipants();
        }
    }

    public void addToProcessParticipantInformationQueue() {
        final Set<AccountabilityType> accountabilityTypes = new HashSet<AccountabilityType>();
        for (final Person person : getMission().getParticipantesSet()) {
            for (final Accountability accountability : person.getParentAccountabilitiesSet()) {
                if (accountability.isValid()) {
                    final AccountabilityType accountabilityType = accountability.getAccountabilityType();
                    accountabilityTypes.add(accountabilityType);
                }
            }
        }
        for (final AccountabilityTypeQueue accountabilityTypeQueue : MissionSystem.getInstance().getAccountabilityTypeQueuesSet()) {
            if (accountabilityTypes.contains(accountabilityTypeQueue.getAccountabilityType())) {
                final WorkflowQueue workflowQueue = accountabilityTypeQueue.getWorkflowQueue();
                addCurrentQueues(workflowQueue);
            }
        }
    }

    public void removeFromParticipantInformationQueues() {
        for (final AccountabilityTypeQueue accountabilityTypeQueue : MissionSystem.getInstance().getAccountabilityTypeQueuesSet()) {
            final WorkflowQueue workflowQueue = accountabilityTypeQueue.getWorkflowQueue();
            removeCurrentQueues(workflowQueue);
        }
    }

    public boolean isOnTime() {
        return getMission().isOnTime();
    }

    public void justifyLateSubmission(final String justification) {
        if (justification == null || justification.isEmpty()) {
            throw new DomainException(BundleUtil.getStringFromResourceBundle("resources/MissionResources",
                    "justification.cannot.be.null"));
        }
        if (!insufficient(justification)) {
            throw new DomainException(BundleUtil.getStringFromResourceBundle("resources/MissionResources",
                    "justification.is.not.sufficient"));
        }
        final MissionProcessLateJustification lastJustification = getLastMissionProcessLateJustification();
        if (lastJustification == null || !lastJustification.getJustification().equals(justification)) {
            new MissionProcessLateJustification(this, justification);
        }
    }

    private boolean insufficient(final String justification) {
        int count = 0;
        for (final char c : justification.toCharArray()) {
            if ((c > 'a' && c < 'z') || (c > 'A' && c < 'Z')) {
                count++;
            }
        }
        return count > 5;
    }

    public MissionProcessLateJustification getLastMissionProcessLateJustification() {
        final Set<MissionProcessLateJustification> justifications = getMissionProcessLateJustificationsSet();
        if (justifications.isEmpty()) {
            return null;
        }
        return Collections.max(justifications, MissionProcessLateJustification.COMPARATOR_BY_JUSTIFICATION_DATETIME);
    }

    public SortedSet<MissionProcessLateJustification> getOrderedMissionProcessLateJustificationsSet() {
        final SortedSet<MissionProcessLateJustification> result =
                new TreeSet<MissionProcessLateJustification>(MissionProcessLateJustification.COMPARATOR_BY_JUSTIFICATION_DATETIME);
        result.addAll(getMissionProcessLateJustificationsSet());
        return result;
    }

    public boolean areAllParticipantsAuthorized() {
        return getMission().areAllParticipantsAuthorized();
    }

    public boolean getAreAllParticipantsAuthorized() {
        return areAllParticipantsAuthorized();
    }

    public boolean areAllParticipantsAuthorizedForPhaseOne() {
        return getMission().areAllParticipantsAuthorizedForPhaseOne();
    }

    public int getPersonAuthorizationChainSize(final Person person) {
        return getMission().getPersonAuthorizationChainSize(person);
    }

    public boolean hasAnyActivePaymentProcess() {
        for (final PaymentProcess paymentProcess : getPaymentProcessSet()) {
            if (paymentProcess.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean isExpenditureAuthorized() {
        return getMission().isExpenditureAuthorized();
    }

    public void cancel() {
        setIsCanceled(Boolean.TRUE);
    }

    public boolean isCanceled() {
        return getIsCanceled().booleanValue();
    }

    public String getPresentationName() {
        return getProcessIdentification() + " - ";
    }

    public boolean isPendingParticipantAuthorisationBy(final User user) {
        return getMission().isPendingParticipantAuthorisationBy(user);
    }

    public boolean isAuthorized() {
        return getMission().isAuthorized();
    }

    public MissionStageView getMissionStageView() {
        return new MissionStageView(this);
    }

    public boolean isAccountingEmployee(final pt.ist.expenditureTrackingSystem.domain.organization.Person expenditurePerson) {
        return getMission().isAccountingEmployee(expenditurePerson);
    }

    public boolean isProjectAccountingEmployee(final pt.ist.expenditureTrackingSystem.domain.organization.Person expenditurePerson) {
        return getMission().isProjectAccountingEmployee(expenditurePerson);
    }

    public boolean isDirectProjectAccountingEmployee(
            final pt.ist.expenditureTrackingSystem.domain.organization.Person expenditurePerson) {
        return getMission().isDirectProjectAccountingEmployee(expenditurePerson);
    }

    public boolean isCurrentUserAbleToAccessQueueHistory() {
        for (final WorkflowQueue workflowQueue : getQueueHistorySet()) {
            if (workflowQueue.isCurrentUserAbleToAccessQueue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAccessible(final User user) {
        final Person person = user.getPerson();
        final Mission mission = getMission();
        return isTakenByPerson(user)
                || getProcessCreator() == user
                || mission.getRequestingPerson() == person
                || user.hasRoleType(RoleType.MANAGER)
                || (user.getExpenditurePerson() != null && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user))
                || (user.getExpenditurePerson() != null && ExpenditureTrackingSystem
                        .isAcquisitionsProcessAuditorGroupMember(user))
                || (person != null && person.getMissionsSet().contains(mission)) || mission.isParticipantResponsible(person)
                || mission.isFinancerResponsible(user.getExpenditurePerson())
                || mission.isFinancerAccountant(user.getExpenditurePerson()) || mission.isPersonelSectionMember(user)
                || ExpenditureTrackingSystem.isFundCommitmentManagerGroupMember(user)
                || mission.getParticipantesSet().contains(person) || mission.isUnitObserver(user);
    }

    public boolean isReadyForMissionTermination() {
        return getMission().isReadyForMissionTermination()
                && (!hasAnyActivePaymentProcess() || allPaymentProcessesAreConcluded());
    }

    private boolean allPaymentProcessesAreConcluded() {
        for (final PaymentProcess paymentProcess : getPaymentProcessSet()) {
            if (paymentProcess.isActive()) {
                final ProcessState currentProcessState = paymentProcess.getCurrentProcessState();
                if (!currentProcessState.isInFinalStage()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isReadyForMissionTermination(final User user) {
        return !isCanceled() && (isRequestor(user) || isMissionResponsible(user)) && isReadyForMissionTermination();
    }

    public void sendForProcessTermination(final String descriptionOfChangesAfterArrival) {
        getMission().sendForProcessTermination(descriptionOfChangesAfterArrival);
    }

    public boolean isTerminated() {
        return getMission().isTerminated();
    }

    public boolean isTerminatedWithChanges() {
        return getMission().isTerminatedWithChanges();
    }

    public boolean isArchived() {
        return getMission().isArchived();
    }

    public boolean canArchiveMission() {
        return getMission().canArchiveMission();
    }

    public boolean canArchiveMissionDirect() {
        return getMission().canArchiveMissionDirect();
    }

    public boolean hasNoItemsAndParticipantesAreAuthorized() {
        return getMission().hasNoItemsAndParticipantesAreAuthorized();
    }

    public void revertProcessTermination() {
        getMission().revertProcessTermination();
    }

    public boolean getAreAccomodationItemsAvailable() {
        return getMission().getAreAccomodationItemsAvailable();
    }

    public boolean getPersonelExpenseItemsAvailable() {
        return getMission().getPersonelExpenseItemsAvailable();
    }

    public boolean canTogleMissionNature() {
        return getMission().canTogleMissionNature();
    }

    public int getNumberOfDays() {
        return getMission().getNumberOfDays();
    }

    public void checkForAnyOverlappingParticipations() {
        getMission().checkForAnyOverlappingParticipations();
    }

    public void checkForSupportDocuments() {
        if (getFiles().size() == 0) {
            throw new DomainException(BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
                    "error.mission.must.have.a.support.file"));
        }
    }

    public boolean hasCommitmentNumber() {
        return getMission().hasCommitmentNumber();
    }

    public boolean hasAllCommitmentNumbers() {
        return getMission().hasAllCommitmentNumbers();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getMissionSystem() == VirtualHost.getVirtualHostForThread().getMissionSystem();
    }

    public Collection<MissionProcess> getAssociatedMissionProcesses() {
        if (!hasMissionProcessAssociation()) {
            return CollectionUtils.EMPTY_COLLECTION;
        }
        List<MissionProcess> associatedProcesses = new ArrayList<MissionProcess>();
        associatedProcesses.addAll(getMissionProcessAssociation().getMissionProcesses());
        associatedProcesses.remove(this);

        return associatedProcesses;
    }

    public void addAssociatedMissionProcess(MissionProcess processToAdd) {
        if (getAssociatedMissionProcesses().contains(processToAdd)) {
            throw new DomainException(BundleUtil.getStringFromResourceBundle("resources/MissionResources",
                    "error.cannot.associate.MissionProcesses.already.associated"));
        }
        if (hasMissionProcessAssociation() && processToAdd.hasMissionProcessAssociation()) {
            throw new DomainException(BundleUtil.getStringFromResourceBundle("resources/MissionResources",
                    "error.cannot.merge.MissionProcessAssociations"));
        }

        if (hasMissionProcessAssociation()) {
            getMissionProcessAssociation().addMissionProcesses(processToAdd);
        } else if (processToAdd.hasMissionProcessAssociation()) {
            processToAdd.getMissionProcessAssociation().addMissionProcesses(this);
        } else {
            new MissionProcessAssociation(this, processToAdd);
        }
    }

    public void removeAssociatedMissionProcess(MissionProcess processToRem) {
        if (processToRem == this) {
            throw new DomainException("error.cannot.remove.MissionProcesses.this");
        }
        if (!hasMissionProcessAssociation()) {
            return;
        }

        MissionProcessAssociation association = getMissionProcessAssociation();
        association.removeMissionProcesses(processToRem);
        if (association.getMissionProcesses().size() < 2) {
            association.delete();
        }
    }

    @Deprecated
    public java.util.Set<module.mission.domain.MissionProcessLateJustification> getMissionProcessLateJustifications() {
        return getMissionProcessLateJustificationsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess> getPaymentProcess() {
        return getPaymentProcessSet();
    }

    @Deprecated
    public boolean hasAnyMissionProcessLateJustifications() {
        return !getMissionProcessLateJustificationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyPaymentProcess() {
        return !getPaymentProcessSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyCurrentQueues() {
        return !getCurrentQueuesSet().isEmpty();
    }

    @Deprecated
    public boolean hasIsUnderConstruction() {
        return getIsUnderConstruction() != null;
    }

    @Deprecated
    public boolean hasIsCanceled() {
        return getIsCanceled() != null;
    }

    @Deprecated
    public boolean hasMissionYear() {
        return getMissionYear() != null;
    }

    @Deprecated
    public boolean hasMissionProcessAssociation() {
        return getMissionProcessAssociation() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

    @Deprecated
    public boolean hasMission() {
        return getMission() != null;
    }

    @Deprecated
    public boolean hasCurrentOwner() {
        return getCurrentOwner() != null;
    }

}
