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
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.joda.time.DateTime;

import module.mission.domain.util.MissionState;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.workflow.activities.GiveProcess.NotifyUser;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcessComment;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.utils.WorkflowCommentCounter;
import module.workflow.util.ClassNameBundle;
import module.workflow.widgets.UnreadCommentsWidget;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

@ClassNameBundle(bundle = "MissionResources")
/**
 *
 * @author João Antunes
 * @author João Neves
 * @author Luis Cruz
 *
 */
@DeclareMessageTemplate(id = "expenditures.mission.passing", bundle = Bundle.MISSION, description = "template.mission.passing",
        subject = "template.mission.passing.subject", text = "template.mission.passing.text", parameters = {
                @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
                @TemplateParameter(id = "comments", description = "template.parameter.mission.comments"),
                @TemplateParameter(id = "process", description = "template.parameter.process"),
                @TemplateParameter(id = "responsible", description = "template.parameter.mission.responsible") })
@DeclareMessageTemplate(id = "expenditures.mission.comment", bundle = Bundle.MISSION, description = "template.mission.comment",
        subject = "template.mission.comment.subject", text = "template.mission.comment.text", parameters = {
                @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
                @TemplateParameter(id = "comment", description = "template.parameter.comment"),
                @TemplateParameter(id = "commenter", description = "template.parameter.commenter"),
                @TemplateParameter(id = "process", description = "template.parameter.process") })
@DeclareMessageTemplate(id = "expenditures.mission.participation", bundle = Bundle.MISSION,
        description = "template.mission.participation", subject = "template.mission.participation.subject",
        text = "template.mission.participation.text", parameters = {
                @TemplateParameter(id = "foreignCountry", description = "template.parameter.mission.foreign.country"),
                @TemplateParameter(id = "location", description = "template.parameter.mission.location"),
                @TemplateParameter(id = "process", description = "template.parameter.process") })
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

            final String n1 = nString(o1.getProcessNumber());
            final String n2 = nString(o2.getProcessNumber());
            final int c = n1.compareTo(n2);
            return c == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : c;
        }

        private String nString(final String s) {
            final int i = s.indexOf("/M");
            final int l = s.length();
            return i < 0 || l < i + 2 ? s : pad(s.substring(i + 2, l));
        }

        private String pad(final String s) {
            return StringUtils.leftPad(s, 8, '0');
        }

    };

    public static class CommentBean implements Comparable<CommentBean> {
        private DateTime time;
        private String commenter;
        private String text;

        public DateTime getTime() {
            return time;
        }

        public String getPrintedTime() {
            return time.toString("yyyy-MM-dd HH:mm");
        }

        public String getCommenter() {
            return commenter;
        }

        public String getText() {
            return text;
        }

        public String getIndentedText() {
            return "\t" + text.replace("\n", "\n\t");
        }

        public CommentBean(WorkflowProcessComment c) {
            time = c.getDate();
            final User user = c.getCommenter();
            commenter = user.getDisplayName() + "(" + user.getUsername() + ")";
            text = c.getComment();
        }

        @Override
        public int compareTo(CommentBean b) {
            return b.getTime().compareTo(time);
        }

    }

    protected static class MissionGiveProcessUserNotifier extends NotifyUser {

        @Override
        public void notifyUser(final User user, final WorkflowProcess process) {
            final MissionProcess missionProcess = (MissionProcess) process;
            Message.fromSystem()
                    .to(Group.users(user))
                    .template("expenditures.mission.passing")
                    .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl())
                    .parameter("process", missionProcess.getProcessNumber())
                    .parameter("comments",
                            missionProcess.getComments().stream().map(CommentBean::new).collect(Collectors.toSet()))
                    .parameter("responsible", Authenticate.getUser().getProfile().getFullName()).and().send();
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

    public boolean hasBeenCheckedByUnderlings() {
        return getMission().hasBeenCheckedByUnderlings();
    }

    public boolean isPendingDirectAuthorizationBy(final User user) {
        return getMission().isPendingDirectAuthorizationBy(user);
    }

    public boolean isPendingCheckByUnderlings(final User user) {
        return getMission().isPendingCheckByUnderlings(user);
    }

    public boolean isPersonalInformationProcessed() {
        return getMission().isPersonalInformationProcessed();
    }

    public boolean isPersonalInformationProcessingNeeded() {
        return !getProcessParticipantInformationQueues().isEmpty();
    }

    public boolean canRemoveApproval(final User user) {
        return getMission().canRemoveApproval(user);
    }

    public boolean canRemoveAuthorization(final User user) {
        return getMission().canRemoveAuthorization(user);
    }

    public boolean canRemovePreAuthorization(final User user) {
        return getMission().canRemovePreAuthorization(user);
    }

    public void approve(final User user) {
        getMission().approve(user);
    }

    public void authorize(final User user) {
        getMission().authorize(user);
    }

    public void preAuthorize(final User user) {
        getMission().preAuthorize(user);
    }

    public void unapprove(final User user) {
        getMission().unapprove(user);
    }

    public void unauthorize(final User user) {
        getMission().unauthorize(user);
    }

    public void unPreAuthorize(final User user) {
        getMission().unPreAuthorize(user);
    }

    public boolean isApprovedByResponsible() {
        return getMission().isApprovedByResponsible();
    }

    public boolean isApproved() {
        return getMission().isApproved();
    }

    public boolean isVerified() {
        return getMission().isVerified();
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
        return !getMission().getParticipantesSet().isEmpty();
    }

    public boolean hasAnyMissionItems() {
        return getMission().hasAnyMissionItems();
    }

    public boolean hasAnyVehicleItems() {
        return getMission().hasAnyVehicleItems();
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
        Message.fromSystem().to(Group.users(user)).template("expenditures.mission.comment")
                .parameter("process", getProcessNumber())
                .parameter("commenter", Authenticate.getUser().getProfile().getFullName()).parameter("comment", comment)
                .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl()).and().send();
    }

    public void notifyAllParticipants() {
        final Mission mission = getMission();
        final String location = mission.getLocation(), process = getProcessNumber();
        final LocalizedString foreignCountry =
                mission.getCountry() == MissionSystem.getInstance().getCountry() ? mission.getCountry().getName() : null;
        mission.getParticipantesSet()
                .stream()
                .map(Person::getUser)
                .forEach(
                        user -> {
                            Message.fromSystem().to(Group.users(user)).template("expenditures.mission.participation")
                                    .parameter("process", process).parameter("location", location)
                                    .parameter("foreignCountry", foreignCountry == null ? "" : foreignCountry.getContent()).and().send();
                        });
    }

    public void addToVerificationQueue() {
        WorkflowQueue verificationQueue = MissionSystem.getInstance().getVerificationQueue();
        if (verificationQueue == null) {
            throw new DomainException(Bundle.EXPENDITURE, "MissionSystem: "
                    + CoreConfiguration.getConfiguration().applicationUrl() + " has no verification queue configured!");
        }
        addCurrentQueues(verificationQueue);
    }

    public void removeFromVerificationQueue() {
        WorkflowQueue verificationQueue = MissionSystem.getInstance().getVerificationQueue();
        if (verificationQueue == null) {
            throw new DomainException(Bundle.EXPENDITURE, "MissionSystem: "
                    + CoreConfiguration.getConfiguration().applicationUrl() + " has no verification queue configured!");
        }
        removeCurrentQueues(verificationQueue);
    }

    public void addToProcessParticipantInformationQueues() {
        for (WorkflowQueue workflowQueue : getProcessParticipantInformationQueues()) {
            addCurrentQueues(workflowQueue);
            getMission().setIsPersonalInformationProcessed(false);
        }
    }

    public Collection<WorkflowQueue> getProcessParticipantInformationQueues() {
        final Supplier<Stream<AccountabilityType>> types =
                () -> getMission().getParticipantesSet().stream().flatMap(p -> p.getParentAccountabilityStream())
                        .filter(a -> a.isValid()).map(a -> a.getAccountabilityType());
        return MissionSystem.getInstance().getAccountabilityTypeQueuesSet().stream()
                .filter(q -> types.get().anyMatch(t -> t == q.getAccountabilityType())).map(q -> q.getWorkflowQueue())
                .collect(Collectors.toSet());
    }

    public void removeFromParticipantInformationQueues() {
        for (final AccountabilityTypeQueue accountabilityTypeQueue : MissionSystem.getInstance().getAccountabilityTypeQueuesSet()) {
            removeCurrentQueues(accountabilityTypeQueue.getWorkflowQueue());
        }
    }

    public boolean isOnTime() {
        return getMission().isOnTime();
    }

    public void justifyLateSubmission(final String justification) {
        if (justification == null || justification.isEmpty()) {
            throw new DomainException(Bundle.MISSION, "justification.cannot.be.null");
        }
        if (!insufficient(justification)) {
            throw new DomainException(Bundle.MISSION, "justification.is.not.sufficient");
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
        return getIsCanceled() == null || getIsCanceled().booleanValue();
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

    public List<MissionState> getMissionStates() {
        List<MissionState> validStates = new ArrayList<MissionState>();
        for (MissionState state : MissionState.values()) {
            if (state.isRequired(this)) {
                validStates.add(state);
            }
        }
        return validStates;
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
                || RoleType.MANAGER.group().isMember(user)
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
            throw new DomainException(Bundle.MISSION, "error.mission.must.have.a.support.file");
        }
    }

    public boolean hasCommitmentNumber() {
        return getMission().hasCommitmentNumber();
    }

    public boolean hasAllCommitmentNumbers() {
        return getMission().hasAllCommitmentNumbers();
    }

    public Collection<MissionProcess> getAssociatedMissionProcesses() {
        if (!hasMissionProcessAssociation()) {
            return Collections.EMPTY_SET;
        }
        List<MissionProcess> associatedProcesses = new ArrayList<MissionProcess>();
        associatedProcesses.addAll(getMissionProcessAssociation().getMissionProcesses());
        associatedProcesses.remove(this);

        return associatedProcesses;
    }

    public void addAssociatedMissionProcess(final RemoteMissionSystem remoteMissionSystem, final String processNumber,
            final String externalId, boolean connect) {
        for (final RemoteMissionProcess remoteProcess : getRemoteMissionProcessSet()) {
            if (remoteProcess.getRemoteMissionSystem() == remoteMissionSystem) {
                if (remoteProcess.getProcessNumber().equalsIgnoreCase(processNumber)) {
                    // if this process is already associated to another with the specified number then
                    // nothing else needs to be done.
                    return;
                } else {
                    throw new DomainException(Bundle.MISSION, "error.cannot.merge.MissionProcessAssociations");
                }
            }
        }

        final RemoteMissionProcess remoteMissionProcess =
                new RemoteMissionProcess(this, remoteMissionSystem, processNumber, externalId);
        if (connect) {
            remoteMissionProcess.connect();
        }
    }

    public void removeAssociatedMissionProcess(final RemoteMissionProcess remoteMissionProcess, final boolean disconnect) {
        if (disconnect) {
            remoteMissionProcess.disconnect();
        }
        remoteMissionProcess.delete();
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

    public boolean participantsBelongToInstitution() {
        return getMission().participantsBelongToInstitution();
    }

}
