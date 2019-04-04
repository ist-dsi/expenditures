package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AdvancePaymentDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class EditAdvancePayment extends WorkflowActivity<SimplifiedProcedureProcess, EditAdvancePaymentInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
        Person person = user.getExpenditurePerson();
        AdvancePaymentDocument advancePaymentDocument = process.getAdvancePaymentDocument();
        return isUserProcessOwner(process, user)
                && (process.getAcquisitionProcessState().getAcquisitionProcessStateType()
                        .ordinal() >= AcquisitionProcessStateType.ACQUISITION_PROCESSED.ordinal()
                        && process.getAcquisitionProcessState().getAcquisitionProcessStateType()
                                .ordinal() <= AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY.ordinal())
                && (ExpenditureTrackingSystem.isTreasuryMemberGroupMember(user) || process.isTreasuryMember(person))
                && (advancePaymentDocument != null && advancePaymentDocument.isSigned())
                && !Strings.isNullOrEmpty(process.getFinancialProcessId());
    }

    @Override
    protected void process(EditAdvancePaymentInformation activityInformation) {
        SimplifiedProcedureProcess process = activityInformation.getProcess();
        activityInformation.getPayment().edit(activityInformation.getReference(), activityInformation.getValue(),
                activityInformation.getAdditionalValue(), activityInformation.getDate(), activityInformation.getDescription());
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
        return new EditAdvancePaymentInformation(process, this);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
