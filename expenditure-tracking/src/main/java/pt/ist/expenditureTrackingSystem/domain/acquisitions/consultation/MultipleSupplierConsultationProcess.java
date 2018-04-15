package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.messaging.core.domain.Message;
import org.joda.time.LocalDate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.GiveProcess;
import module.workflow.activities.ReleaseProcess;
import module.workflow.activities.StealProcess;
import module.workflow.activities.TakeProcess;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.AddFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.AddJuryMember;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.AddMultipleSupplierConsultationPart;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.AddSupplier;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.AddTieBreakCriteria;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.Adjudicate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.AllocateFunds;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.Approve;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.Authorize;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.CloseCandidateDocumentRegistry;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.CommitFunds;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.CompleteDocumentation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.EditConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.EditLowPriceLimitInfo;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.Evaluate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.NotifyCandidates;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.Publish;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.PublishEvaluation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.RemoveFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.RemoveMultipleSupplierConsultationPart;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.ReopenCandidateDocumentRegistry;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.SelectSupplier;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.SubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnAdjudicate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnAllocateFunds;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnApprove;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnAuthorize;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnCommitFunds;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnCompleteDocumentation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnEvaluate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnNotifyCandidates;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnPublish;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnPublishEvaluation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnSelectSupplier;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnSubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.UnVerify;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities.Verify;
import pt.ist.fenixframework.Atomic;

public class MultipleSupplierConsultationProcess extends MultipleSupplierConsultationProcess_Base {

    private static List<WorkflowActivity<? extends MultipleSupplierConsultationProcess, ? extends ActivityInformation<? extends MultipleSupplierConsultationProcess>>> activities =
            new ArrayList<WorkflowActivity<? extends MultipleSupplierConsultationProcess, ? extends ActivityInformation<? extends MultipleSupplierConsultationProcess>>>();
    static {
        activities.add(new EditConsultation());
        activities.add(new AddMultipleSupplierConsultationPart());
        activities.add(new RemoveMultipleSupplierConsultationPart());
        activities.add(new EditLowPriceLimitInfo());
        activities.add(new AddFinancer());
        activities.add(new RemoveFinancer());
        activities.add(new AddJuryMember());
        activities.add(new AddSupplier());
        activities.add(new AddTieBreakCriteria());

        activities.add(new SubmitForApproval());
        activities.add(new UnSubmitForApproval());
        activities.add(new Approve());
        activities.add(new UnApprove());
        activities.add(new Verify());
        activities.add(new UnVerify());
        activities.add(new AllocateFunds());
        activities.add(new UnAllocateFunds());
        activities.add(new CompleteDocumentation());
        activities.add(new UnCompleteDocumentation());
        activities.add(new Authorize());
        activities.add(new UnAuthorize());
        activities.add(new Publish());
        activities.add(new UnPublish());
        activities.add(new CloseCandidateDocumentRegistry());
        activities.add(new ReopenCandidateDocumentRegistry());
        activities.add(new Evaluate());
        activities.add(new UnEvaluate());
        activities.add(new PublishEvaluation());
        activities.add(new UnPublishEvaluation());
        activities.add(new Adjudicate());
        activities.add(new UnAdjudicate());
        activities.add(new CommitFunds());
        activities.add(new UnCommitFunds());
        activities.add(new NotifyCandidates());
        activities.add(new UnNotifyCandidates());
        activities.add(new SelectSupplier());
        activities.add(new UnSelectSupplier());

        activities.add(new TakeProcess<MultipleSupplierConsultationProcess>());
        activities.add(new GiveProcess<MultipleSupplierConsultationProcess>());
        activities.add(new ReleaseProcess<MultipleSupplierConsultationProcess>());
        activities.add(new StealProcess<MultipleSupplierConsultationProcess>());
    }

    public MultipleSupplierConsultationProcess(final String description, final Material material, final String justification, final ContractType contractType) {
        final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(LocalDate.now().getYear());
        setYear(paymentProcessYear);
        setProcessNumber(generateProcessNumber(paymentProcessYear));
        setCreator(Authenticate.getUser());
        setState(MultipleSupplierConsultationProcessState.IN_GENESIS);
        setWorkflowSystem(WorkflowSystem.getInstance());
        new MultipleSupplierConsultation(this, description, material, justification, contractType);
    }

    private String generateProcessNumber(final PaymentProcessYear paymentProcessYear) {
        final Integer number = paymentProcessYear.nextAcquisitionProcessYearNumber();
        final ExpenditureTrackingSystem system = ExpenditureTrackingSystem.getInstance();
        return system.getInstitutionalProcessNumberPrefix() + "/" + paymentProcessYear.getYear() + "/PC" + number;
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
        return (List<T>) activities;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public User getProcessCreator() {
        return getCreator();
    }

    @Override
    public void notifyUserDueToComment(final User user, final String comment) {
        Message.fromSystem().to(Group.users(user)).template("expenditures.consultation.comment")
        .parameter("process", getProcessNumber())
        .parameter("commenter", Authenticate.getUser().getProfile().getFullName())
        .parameter("comment", comment)
        .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl())
        .and()
        .send();
    }

    @Atomic
    public static MultipleSupplierConsultationProcess create(final String description, final Material material, final String justification,
            final ContractType contractType) {
        return new MultipleSupplierConsultationProcess(description, material, justification, contractType);
    }

    @Override
    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
        List<Class<? extends ProcessFile>> availableFileTypes = new ArrayList<Class<? extends ProcessFile>>();
        availableFileTypes.add(SupplierCriteriaSelectionDocument.class);
        availableFileTypes.add(TechnicalSpecificationDocument.class);
        availableFileTypes.addAll(super.getAvailableFileTypes());
        return availableFileTypes;
    }

    public boolean isInAllocationPeriod() {
        final Integer year = getYear().getYear().intValue();
        final int i = Calendar.getInstance().get(Calendar.YEAR);
        return year == i || year == i - 1 || year == i - 2;
    }

    public boolean doesNotExceedSupplierLimits() {
        return getConsultation().getSupplierSet().stream().allMatch(s -> s.isMultipleSupplierLimitAllocationAvailable());
    }

}
