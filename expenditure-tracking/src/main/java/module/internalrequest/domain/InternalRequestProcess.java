package module.internalrequest.domain;

import com.google.common.base.Strings;
import module.internalrequest.domain.activity.*;
import module.internalrequest.domain.util.InternalRequestState;
import module.internalrequest.util.Constants;
import module.mission.domain.MissionProcess.CommentBean;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.workflow.activities.*;
import module.workflow.activities.GiveProcess.NotifyUser;
import module.workflow.domain.WorkflowProcess;
import org.apache.commons.lang3.RandomStringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.search.domain.DomainIndexSystem;
import org.fenixedu.bennu.search.domain.YearIndex;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DeclareMessageTemplate(id = "expenditures.internalRequest.passing", bundle = Bundle.INTERNAL_REQUEST, description = "template.internalRequest.passing",
        subject = "template.internalRequest.passing.subject", text = "template.internalRequest.passing.text", parameters = {
        @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
        @TemplateParameter(id = "comments", description = "template.parameter.internalRequest.comments"),
        @TemplateParameter(id = "process", description = "template.parameter.process"),
        @TemplateParameter(id = "responsible", description = "template.parameter.internalRequest.responsible") })
@DeclareMessageTemplate(id = "expenditures.internalRequest.comment", bundle = Bundle.INTERNAL_REQUEST, description = "template.internalRequest.comment",
        subject = "template.internalRequest.comment.subject", text = "template.internalRequest.comment.text", parameters = {
        @TemplateParameter(id = "applicationUrl", description = "template.parameter.application.url"),
        @TemplateParameter(id = "comment", description = "template.parameter.comment"),
        @TemplateParameter(id = "commenter", description = "template.parameter.commenter"),
        @TemplateParameter(id = "process", description = "template.parameter.process") })
public class InternalRequestProcess extends InternalRequestProcess_Base {

    private static final int DELIVERY_CONFIRMATION_CODE_LENGTH = 6;
    private static final long DELIVERY_CONFIRMATION_TIME_LIMIT = 15 * 60 * 1000; // 15 minutes

    public InternalRequestProcess(Person requester, Unit requesting, Unit requested) {
        new InternalRequest(requester, requesting, requested, this);

        int year = DateTime.now().getYear();
        DomainIndexSystem.getInstance().index(year, YearIndex::getInternalRequestProcessSet, this);
        this.setProcessNumber(String.valueOf(InternalRequestSystem.getInstance().getNextProcessNumber(year)));

        this.setIsUnderConstruction(true);
        this.setIsCancelled(false);

        this.setInternalRequestSystem(InternalRequestSystem.getInstance());
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getProcessNumber() {
        final ExpenditureTrackingSystem system = Bennu.getInstance().getExpenditureTrackingSystem();
        if (system.hasProcessPrefix()) {
            return system.getInstitutionalProcessNumberPrefix() + "/" + this.getYearIndex().getYear() + "/IR" + super.getProcessNumber();
        }
        return this.getYearIndex().getYear() + "/IR" + super.getProcessNumber();
    }

    private static boolean isWellFormedProcessNumber(String processNumber) {
        if (Strings.isNullOrEmpty(processNumber)) { return false; }

        final ExpenditureTrackingSystem system = Bennu.getInstance().getExpenditureTrackingSystem();
        if (system.hasProcessPrefix()) {
            return processNumber.matches(system.getInstitutionalProcessNumberPrefix() + "/\\d{4}/IR\\d+");
        }
        return processNumber.matches("/\\d{4}/IR\\d+");
    }

    public static Optional<InternalRequestProcess> getProcessWithNumber(String processNumber) {
        if(!isWellFormedProcessNumber(processNumber)) {
            // this process number is malformed, so we know there's no associated process
            return Optional.empty();
        }

        // we know this process number is well formed, so we can extract its year
        int extractedYear;
        final ExpenditureTrackingSystem system = Bennu.getInstance().getExpenditureTrackingSystem();
        if (system.hasProcessPrefix()) {
            int prefixLength = system.getInstitutionalProcessNumberPrefix().length();
            extractedYear = Integer.parseInt(processNumber.substring(prefixLength + 1, prefixLength + 5));
        } else {
            extractedYear = Integer.parseInt(processNumber.substring(0, 4));
        }

        return InternalRequestProcess.getProcessWithNumber(extractedYear, processNumber);
    }

    public static Optional<InternalRequestProcess> getProcessWithNumber(int year, String processNumber) {
        final Stream<InternalRequestProcess> search = DomainIndexSystem.getInstance().search(year, (i) -> i.getInternalRequestProcessSet().stream());
        return search.filter((p) -> p.getProcessNumber().equals(processNumber)).findAny();
    }

    @Override
    public User getProcessCreator() {
        return getInternalRequest().getRequestingPerson().getUser();
    }

    @Deprecated
    public String getProcessIdentification() {
        return this.getProcessNumber();
    }

    public String getPresentationName() {
        return this.getProcessNumber() + " - "
                + this.getInternalRequest().getRequestedUnit().getPresentationName()
                + " (" + this.getInternalRequest().getCreated().toString(Constants.DATE_FORMAT) + ")";
    }

    /*
     * STATE
     */

    public List<InternalRequestState> getInternalRequestStates() { return Arrays.asList(InternalRequestState.values()); }

    public boolean canAddItems(Person person) { return this.canSubmit(person); }
    public boolean canRemoveItems(Person person) { return this.canSubmit(person); }
    public boolean canSubmit(Person person) {
        if (!this.getIsUnderConstruction()) { return false; }
        return this.getInternalRequest().getRequestingPerson().equals(person);
    }
    public void submit() { this.setIsUnderConstruction(false); }

    public boolean canApprove(Person person) {
        if (!InternalRequestState.APPROVAL.isPending(this)) { return false; }
        return getInternalRequest().getRequestingUnit().isResponsible(person.getUser().getExpenditurePerson());
    }
    public void approve(Person approvedBy) {
        this.setApproved(true);
        this.getInternalRequest().setApprovedBy(approvedBy);
        this.getInternalRequest().setApprovalDate(DateTime.now());
    }

    public boolean canSubmitBudget(Person person) {
        if (!InternalRequestState.BUDGETING.isPending(this)) { return false; }
        final pt.ist.expenditureTrackingSystem.domain.organization.Person expenditurePerson =
                person.getUser().getExpenditurePerson();
        if (expenditurePerson == null) { return false; }
        return getInternalRequest().getRequestingUnit().isProjectAccountingEmployee(expenditurePerson);
    }
    public void submitBudget(Person budgetedBy) {
        this.setHasBudget(true);

        this.getInternalRequest().setBudgetedBy(budgetedBy);
        this.getInternalRequest().setBudgetDate(DateTime.now());
    }

    public boolean canAuthorize(Person person) {
        if (!InternalRequestState.AUTHORIZATION.isPending(this)) { return false; }
        return getInternalRequest().getRequestingUnit().isResponsible(person.getUser().getExpenditurePerson());
    }
    public void authorize(Person authorizedBy) {
        this.setAuthorized(true);
        this.getInternalRequest().setAuthorizedBy(authorizedBy);
        this.getInternalRequest().setAuthorizationDate(DateTime.now());
    }

    public boolean canProcess(Person person) {
        if (!InternalRequestState.PROCESSING.isPending(this)) { return false; }
        final module.organization.domain.Unit requestedUnit = this.getInternalRequest().getRequestedUnit().getUnit();
        final AccountabilityType deliveryAccountabilityType = InternalRequestSystem.getInstance()
                .getOrganizationalInternalRequestDeliveryAccountabilityType();
        return person.ancestorsInclude(requestedUnit, deliveryAccountabilityType, LocalDate.now(), LocalDate.now());
    }
    public void process(Person processedBy) {
        this.setFinishedProcessing(true);
        this.getInternalRequest().setProcessedBy(processedBy);
        this.getInternalRequest().setProcessDate(DateTime.now());
    }

    public void generateDeliveryConfirmationCode() {
        this.setDeliveryConfirmationCode(RandomStringUtils.random(DELIVERY_CONFIRMATION_CODE_LENGTH, false, true));
        this.setDeliveryConfirmationTime(DateTime.now());
    }
    public DateTime getDeliveryConfirmationTimeLimit() {
        return this.getDeliveryConfirmationTime() == null ? null : this.getDeliveryConfirmationTime()
                .plus(DELIVERY_CONFIRMATION_TIME_LIMIT);
    }
    public String getDeliveryConfirmationTimeLimitIsoString() {
        return this.getDeliveryConfirmationTimeLimit().toString();
    }
    public boolean isAcceptingDeliveryConfirmationCode() {
        return !this.getHasBeenDelivered() && this.getDeliveryConfirmationTime() != null
                && this.getDeliveryConfirmationTimeLimit().isAfterNow();
    }
    public boolean acceptsDeliveryConfirmationCode(String deliveryCode) {
        return isAcceptingDeliveryConfirmationCode() && this.getDeliveryConfirmationCode() != null
                && this.getDeliveryConfirmationCode().equals(deliveryCode);
    }
    public boolean isUserAbleToSeeDeliveryConfirmationCode() {
        return this.isAcceptingDeliveryConfirmationCode()
                && Authenticate.getUser().equals(this.getInternalRequest().getRequestingPerson().getUser());
    }

    public boolean canDeliver(Person person) {
        if (!InternalRequestState.DELIVERY.isPending(this)) { return false; }
        final module.organization.domain.Unit requestedUnit = this.getInternalRequest().getRequestedUnit().getUnit();
        final AccountabilityType deliveryAccountabilityType = InternalRequestSystem.getInstance()
                .getOrganizationalInternalRequestDeliveryAccountabilityType();
        return person.ancestorsInclude(requestedUnit, deliveryAccountabilityType, LocalDate.now(), LocalDate.now());
    }
    public void deliver(Person deliveredBy, String deliveryCode) {
        if (!acceptsDeliveryConfirmationCode(deliveryCode)) {
            return;
        }

        this.setHasBeenDelivered(true);
        this.getInternalRequest().setDeliveredBy(deliveredBy);
        this.getInternalRequest().setDeliveryDate(DateTime.now());
    }

    public boolean canImputeCosts(Person person) {
        if (!InternalRequestState.COST_IMPUTATION.isPending(this)) { return false; }
        return getInternalRequest().getRequestedUnit().isAccountingEmployee(person.getUser().getExpenditurePerson());
    }
    public void imputeCosts(Person imputedBy) {
        this.setHasCostImputation(true);
        this.getInternalRequest().setImputedBy(imputedBy);
        this.getInternalRequest().setCostImputationDate(DateTime.now());
    }

    public void revert() {
        this.setIsUnderConstruction(true);

        this.setApproved(false);
        this.getInternalRequest().setApprovedBy(null);
        this.getInternalRequest().setApprovalDate(null);

        this.setHasBudget(false);
        this.getInternalRequest().getItemsSet().forEach(InternalRequestItem::revert);
        this.getInternalRequest().setBudgetedBy(null);
        this.getInternalRequest().setBudgetDate(null);

        this.setAuthorized(false);
        this.getInternalRequest().setAuthorizedBy(null);
        this.getInternalRequest().setAuthorizationDate(null);

        this.setFinishedProcessing(false);
        this.getInternalRequest().setProcessedBy(null);
        this.getInternalRequest().setProcessDate(null);

        this.setDeliveryConfirmationCode(null);
        this.setDeliveryConfirmationTime(null);

        this.setHasBeenDelivered(false);
        this.getInternalRequest().setDeliveredBy(null);
        this.getInternalRequest().setDeliveryDate(null);

        this.setHasCostImputation(false);
        this.getInternalRequest().setImputedBy(null);
        this.getInternalRequest().setCostImputationDate(null);
    }

    public void cancel() {
        this.revert();
        this.setIsUnderConstruction(false);
        this.setIsCancelled(true);
    }

    @Override
    public boolean isAccessible(final User user) {
        final Person person = user.getPerson();
        final InternalRequest internalRequest = getInternalRequest();
        return isTakenByPerson(user)
                || RoleType.MANAGER.group().isMember(user)
                || internalRequest.getRequestingPerson() == person
                || internalRequest.getApprovedBy() == person
                || internalRequest.getBudgetedBy() == person
                || internalRequest.getAuthorizedBy() == person
                || internalRequest.getProcessedBy() == person
                || internalRequest.getDeliveredBy() == person
                || internalRequest.getImputedBy() == person
                || canApprove(person) || canSubmitBudget(person) || canAuthorize(person) || canProcess(person) || canDeliver(person) || canImputeCosts(person)
                || (user.getExpenditurePerson() != null && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user))
                || (user.getExpenditurePerson() != null && ExpenditureTrackingSystem.isAcquisitionsProcessAuditorGroupMember(user))
                || getObserversSet().contains(user);
    }

    /*
     * ACTIVITIES
     */
    private static final List<WorkflowActivity<? extends InternalRequestProcess, ? extends ActivityInformation>> activities =
            new ArrayList<WorkflowActivity<? extends InternalRequestProcess, ? extends ActivityInformation>>();
    static {
        activities.add(new AddItemActivity());
        activities.add(new RemoveItemActivity());

        activities.add(new SubmitActivity());
        activities.add(new ApproveActivity());
        activities.add(new BudgetItemActivity());
        activities.add(new SubmitBudgetActivity());
        activities.add(new AuthorizeActivity());
        activities.add(new ProcessActivity());
        activities.add(new GenerateDeliveryConfirmationCodeActivity());
        activities.add(new DeliveryConfirmationActivity());
        activities.add(new ImputeCostsActivity());

        activities.add(new RevertActivity());
        activities.add(new CancelActivity());

        activities.add(new GiveProcess<InternalRequestProcess>(new InternalRequestGiveProcessUserNotifier()));
        activities.add(new TakeProcess<InternalRequestProcess>());
        activities.add(new ReleaseProcess<InternalRequestProcess>());
        activities.add(new StealProcess<InternalRequestProcess>());
        activities.add(new AddObserver<InternalRequestProcess>());
        activities.add(new ExceptionalChangeRequestingPerson());
    }

    public static void registerActivity(
            WorkflowActivity<? extends InternalRequestProcess, ? extends ActivityInformation<? extends InternalRequestProcess>> activity) {
        activities.add(activity);
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
        return (List) activities;
    }

    public static final Comparator<InternalRequestProcess> COMPARATOR_BY_PROCESS_NUMBER = new Comparator<InternalRequestProcess>() {
        @Override
        public int compare(final InternalRequestProcess o1, final InternalRequestProcess o2) {
            final int year1 = o1.getYearIndex().getYear();
            final int year2 = o2.getYearIndex().getYear();
            if (year1 != year2) { return Integer.compare(year1, year2); }

            final long n1 = extractProcessNumberOfYear(o1.getProcessNumber());
            final long n2 = extractProcessNumberOfYear(o2.getProcessNumber());
            if (n1 != n2) { return Long.compare(n1, n2); }

            // if at this point o1 and o2 are not the same, that is very abnormal...
            return o1.getExternalId().compareTo(o2.getExternalId());
        }

        private long extractProcessNumberOfYear(final String processNumber) {
            final int i = processNumber.indexOf("/IR");
            return Long.parseLong(processNumber.substring(i + 3));
        }
    };

    @Override
    public void notifyUserDueToComment(User user, String comment) {
        Message.fromSystem().to(Group.users(user)).template("expenditures.internalRequest.comment")
                .parameter("process", getProcessNumber())
                .parameter("commenter", Authenticate.getUser().getProfile().getFullName())
                .parameter("comment", comment)
                .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl())
                .and().send();
    }

    public void delete() {
        this.revert();
        this.getInternalRequest().delete();
        this.getYearIndex().removeInternalRequestProcess(this);
        this.setInternalRequestSystem(null);
        super.delete();
    }

    protected static class InternalRequestGiveProcessUserNotifier extends NotifyUser {

        @Override
        public void notifyUser(final User user, final WorkflowProcess process) {
            final InternalRequestProcess internalRequestProcess = (InternalRequestProcess) process;
            Message.fromSystem()
                    .to(Group.users(user))
                    .template("expenditures.internalRequest.passing")
                    .parameter("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl())
                    .parameter("process", internalRequestProcess.getProcessNumber())
                    .parameter("comments",
                            internalRequestProcess.getCommentsSet().stream().map(CommentBean::new).collect(Collectors.toSet()))
                    .parameter("responsible", Authenticate.getUser().getProfile().getFullName())
                    .and().send();
        }
    }

}
