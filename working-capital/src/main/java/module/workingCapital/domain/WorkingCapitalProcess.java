/*
 * @(#)WorkingCapitalProcess.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 *
 *      https://fenix-ashes.ist.utl.pt/
 *
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package module.workingCapital.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.AddObserver;
import module.workflow.activities.GiveProcess;
import module.workflow.activities.ReleaseProcess;
import module.workflow.activities.StealProcess;
import module.workflow.activities.TakeProcess;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.utils.WorkflowCommentCounter;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.HasPresentableProcessState;
import module.workflow.util.PresentableProcessState;
import module.workflow.widgets.UnreadCommentsWidget;
import module.workingCapital.domain.activity.AcceptResponsabilityForWorkingCapitalActivity;
import module.workingCapital.domain.activity.AllocateFundsActivity;
import module.workingCapital.domain.activity.ApproveActivity;
import module.workingCapital.domain.activity.ApproveExceptionalWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.ApproveWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.AuthorizeActivity;
import module.workingCapital.domain.activity.CancelReenforceWorkingCapitalInitializationActivity;
import module.workingCapital.domain.activity.CancelWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.CancelWorkingCapitalInitializationActivity;
import module.workingCapital.domain.activity.ChangeWorkingCapitalAccountingUnitActivity;
import module.workingCapital.domain.activity.CorrectWorkingCapitalAcquisitionClassificationActivity;
import module.workingCapital.domain.activity.EditInitializationActivity;
import module.workingCapital.domain.activity.EditWorkingCapitalActivity;
import module.workingCapital.domain.activity.ExceptionalCapitalRestitutionActivity;
import module.workingCapital.domain.activity.PayCapitalActivity;
import module.workingCapital.domain.activity.ReenforceWorkingCapitalInitializationActivity;
import module.workingCapital.domain.activity.RegisterCapitalRefundActivity;
import module.workingCapital.domain.activity.RegisterWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.RejectExceptionalWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.RejectVerifyWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.RejectWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.RejectWorkingCapitalInitializationActivity;
import module.workingCapital.domain.activity.RequestCapitalActivity;
import module.workingCapital.domain.activity.RequestCapitalRestitutionActivity;
import module.workingCapital.domain.activity.SubmitForValidationActivity;
import module.workingCapital.domain.activity.TerminateWorkingCapitalActivity;
import module.workingCapital.domain.activity.UnAllocateFundsActivity;
import module.workingCapital.domain.activity.UnApproveActivity;
import module.workingCapital.domain.activity.UnApproveWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.UnAuthorizeActivity;
import module.workingCapital.domain.activity.UnRequestCapitalActivity;
import module.workingCapital.domain.activity.UnRequestCapitalRestitutionActivity;
import module.workingCapital.domain.activity.UnTerminateWorkingCapitalActivity;
import module.workingCapital.domain.activity.UnVerifyActivity;
import module.workingCapital.domain.activity.UnVerifyCentralActivity;
import module.workingCapital.domain.activity.UnVerifyWorkingCapitalAcquisitionActivity;
import module.workingCapital.domain.activity.UndoCancelOrRejectWorkingCapitalInitializationActivity;
import module.workingCapital.domain.activity.VerifyActivity;
import module.workingCapital.domain.activity.VerifyCentralActivity;
import module.workingCapital.domain.activity.VerifyWorkingCapitalAcquisitionActivity;
import module.workingCapital.util.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 *
 * @author João Antunes
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 *
 */
@DeclareMessageTemplate(id = "expenditures.capital.comment", bundle = Bundle.WORKING_CAPITAL,
        description = "template.capital.comment", subject = "template.capital.comment.subject",
        text = "template.capital.comment.text", parameters = {
                @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
                @TemplateParameter(id = "comment", description = "template.parameter.comment"),
                @TemplateParameter(id = "commenter", description = "template.parameter.commenter"),
                @TemplateParameter(id = "unit", description = "template.parameter.unit"),
                @TemplateParameter(id = "year", description = "template.parameter.year") })
@ClassNameBundle(bundle = "WorkingCapitalResources")
public class WorkingCapitalProcess extends WorkingCapitalProcess_Base implements HasPresentableProcessState {

    public static final Comparator<WorkingCapitalProcess> COMPARATOR_BY_UNIT_NAME = new Comparator<WorkingCapitalProcess>() {
        @Override
        public int compare(WorkingCapitalProcess o1, WorkingCapitalProcess o2) {
            final int c = Collator.getInstance().compare(o1.getWorkingCapital().getUnit().getName(),
                    o2.getWorkingCapital().getUnit().getName());
            return c == 0 ? o2.hashCode() - o1.hashCode() : c;
        }
    };

    private static final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activities;

    static {
        final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activitiesAux =
                new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
        activitiesAux.add(new AcceptResponsabilityForWorkingCapitalActivity());
        activitiesAux.add(new CancelWorkingCapitalInitializationActivity());
        activitiesAux.add(new EditInitializationActivity());
        activitiesAux.add(new ChangeWorkingCapitalAccountingUnitActivity());
        activitiesAux.add(new ApproveActivity());
        activitiesAux.add(new UnApproveActivity());
        activitiesAux.add(new VerifyActivity());
        activitiesAux.add(new UnVerifyActivity());
        activitiesAux.add(new VerifyCentralActivity());
        activitiesAux.add(new UnVerifyCentralActivity());
        activitiesAux.add(new AllocateFundsActivity());
        activitiesAux.add(new UnAllocateFundsActivity());
        activitiesAux.add(new AuthorizeActivity());
        activitiesAux.add(new UnAuthorizeActivity());
        activitiesAux.add(new RejectWorkingCapitalInitializationActivity());
        activitiesAux.add(new UndoCancelOrRejectWorkingCapitalInitializationActivity());
        activitiesAux.add(new RequestCapitalActivity());
        activitiesAux.add(new UnRequestCapitalActivity());
        activitiesAux.add(new PayCapitalActivity());
        activitiesAux.add(new RegisterWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new CancelWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new EditWorkingCapitalActivity());
        activitiesAux.add(new CorrectWorkingCapitalAcquisitionClassificationActivity());
        activitiesAux.add(new ApproveWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new RejectWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new UnApproveWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new VerifyWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new RejectVerifyWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new UnVerifyWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new SubmitForValidationActivity());
        activitiesAux.add(new RequestCapitalRestitutionActivity());
        activitiesAux.add(new UnRequestCapitalRestitutionActivity());
        activitiesAux.add(new TerminateWorkingCapitalActivity());
        activitiesAux.add(new UnTerminateWorkingCapitalActivity());
        activitiesAux.add(new RegisterCapitalRefundActivity());
        activitiesAux.add(new ReenforceWorkingCapitalInitializationActivity());
        activitiesAux.add(new CancelReenforceWorkingCapitalInitializationActivity());
        activitiesAux.add(new ExceptionalCapitalRestitutionActivity());
        activitiesAux.add(new ApproveExceptionalWorkingCapitalAcquisitionActivity());
        activitiesAux.add(new RejectExceptionalWorkingCapitalAcquisitionActivity());

        activitiesAux.add(new TakeProcess<WorkingCapitalProcess>());
        activitiesAux.add(new GiveProcess<WorkingCapitalProcess>());
        activitiesAux.add(new ReleaseProcess<WorkingCapitalProcess>());
        activitiesAux.add(new StealProcess<WorkingCapitalProcess>());
        activitiesAux.add(new AddObserver<WorkingCapitalProcess>());

        activities = Collections.unmodifiableList(activitiesAux);

        UnreadCommentsWidget.register(new WorkflowCommentCounter(WorkingCapitalProcess.class));
    }

    public WorkingCapitalProcess() {
        super();
    }

    public WorkingCapitalProcess(final WorkingCapital workingCapital) {
        this();
        setWorkingCapital(workingCapital);
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
        return (List) activities;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getProcessNumber() {
        return getWorkingCapital().getUnit().getPresentationName() + " - "
                + BundleUtil.getString(Bundle.WORKING_CAPITAL, "label.module.workingCapital.year") + " "
                + getWorkingCapital().getWorkingCapitalYear().getYear();

    }

    @Override
    public boolean isAccessible(final User user) {
        final WorkingCapital workingCapital = getWorkingCapital();
        return user != null && user.getPerson() != null
                && (RoleType.MANAGER.group().isMember(user)
                        || (user.getExpenditurePerson() != null
                                && ExpenditureTrackingSystem.isAcquisitionsProcessAuditorGroupMember(user))
                || (workingCapital.hasMovementResponsible() && user.getPerson() == workingCapital.getMovementResponsible())
                || workingCapital.isRequester(user) || workingCapital.getWorkingCapitalSystem().isManagementMember(user)
                || workingCapital.isAnyAccountingEmployee(user) || workingCapital.isAccountingResponsible(user)
                || workingCapital.isTreasuryMember(user) || workingCapital.isResponsibleFor(user)
                || isObserver(user.getExpenditurePerson()));
    }

    public boolean isObserver(Person person) {
        final WorkingCapital workingCapital = getWorkingCapital();
        return getObserversSet().contains(person.getUser())
                || workingCapital.getUnit().getObserversSet().contains(person);
    }

    public boolean isPendingAproval(final User user) {
        return getWorkingCapital().isPendingAproval(user);
    }

    public boolean isPendingDirectAproval(final User user) {
        return getWorkingCapital().isPendingDirectAproval(user);
    }

    public boolean isPendingVerification(User user) {
        return getWorkingCapital().isPendingVerification(user);
    }

    public boolean isPendingFundAllocation(User user) {
        return getWorkingCapital().isPendingFundAllocation(user);
    }

    public boolean isPendingAuthorization(User user) {
        return getWorkingCapital().isPendingAuthorization(user);
    }

    @Override
    public User getProcessCreator() {
        return getWorkingCapital().getRequester();
    }

    @Override
    public void notifyUserDueToComment(final User user, final String comment) {
        final WorkingCapital workingCapital = getWorkingCapital();
        Message.fromSystem().to(Group.users(user)).template("expenditures.capital.comment")
                .parameter("unit", workingCapital.getUnit().getPresentationName())
                .parameter("year", workingCapital.getWorkingCapitalYear().getYear())
                .parameter("commenter", Authenticate.getUser().getProfile().getFullName()).parameter("comment", comment)
                .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl()).and().send();
    }

    @Override
    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
        List<Class<? extends ProcessFile>> availableFileTypes = super.getAvailableFileTypes();
        availableFileTypes.add(WorkingCapitalInvoiceFile.class);
        return availableFileTypes;
    }

    @Override
    public List<Class<? extends ProcessFile>> getUploadableFileTypes() {
        return super.getAvailableFileTypes();
    }

    public void submitAcquisitionsForValidation() {
        final WorkingCapital workingCapital = getWorkingCapital();
        workingCapital.submitAcquisitionsForValidation();
    }

    public void unsubmitAcquisitionsForValidation() {
        final WorkingCapital workingCapital = getWorkingCapital();
        workingCapital.unsubmitAcquisitionsForValidation();
    }

    @Override
    public List<Class<? extends ProcessFile>> getDisplayableFileTypes() {
        final List<Class<? extends ProcessFile>> fileTypes = new ArrayList<Class<? extends ProcessFile>>();
        fileTypes.addAll(super.getDisplayableFileTypes());
        fileTypes.remove(WorkingCapitalInvoiceFile.class);
        return fileTypes;
    }

    @Override
    public boolean isTicketSupportAvailable() {
        return true;
    }

    @Override
    public List<? extends PresentableProcessState> getAvailablePresentableStates() {
        return Arrays.asList(WorkingCapitalProcessState.values());
    }

    @Override
    public PresentableProcessState getPresentableAcquisitionProcessState() {
        return getWorkingCapital().getPresentableAcquisitionProcessState();
    }

    public WorkingCapitalSystem getWorkingCapitalSystem() {
        return getWorkingCapital().getWorkingCapitalSystem();
    }

    @Deprecated
    public boolean hasWorkingCapital() {
        return getWorkingCapital() != null;
    }

}
