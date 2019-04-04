package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SetFinancialProcessIdentification
        extends WorkflowActivity<SimplifiedProcedureProcess, SetFinancialProcessIdentificationInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
        Person person = user.getExpenditurePerson();
        return isUserProcessOwner(process, user) && process.getAcquisitionProcessState().isAllocatedPermanently()
                && process.isAccountingEmployee(person) && process.getSignedAdvancePaymentDocument() != null
                && process.getAcquisitionRequest().getPaymentSet().isEmpty();
    }

    @Override
    protected void process(SetFinancialProcessIdentificationInformation activityInformation) {
        SimplifiedProcedureProcess process = activityInformation.getProcess();
        process.setFinancialProcessId(activityInformation.getFinancialProcessIdentification());
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
        return new SetFinancialProcessIdentificationInformation(process, this);
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
    public boolean isUserAwarenessNeeded(SimplifiedProcedureProcess process) {
        return super.isUserAwarenessNeeded(process) && Strings.isNullOrEmpty(process.getFinancialProcessId());
    }
}
