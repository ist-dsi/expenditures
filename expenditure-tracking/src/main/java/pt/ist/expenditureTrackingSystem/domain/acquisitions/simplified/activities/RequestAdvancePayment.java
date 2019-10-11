package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AdvancePaymentRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class RequestAdvancePayment
        extends WorkflowActivity<SimplifiedProcedureProcess, RequestAdvancePaymentActivityInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
        return ExpenditureTrackingSystem.isAdvancePaymentsAllowed() && process.isInGenesis()
                && process.getRequestor() == user.getExpenditurePerson();
    }

    @Override
    protected void process(RequestAdvancePaymentActivityInformation activityInformation) {
        AdvancePaymentRequest.createOrEdit(activityInformation.getProcess().getAcquisitionRequest(),
                activityInformation.getTemplate(), activityInformation.getPaymentMethod(), activityInformation.getPercentage(),
                activityInformation.getAcquisitionJustification(), activityInformation.getEntityJustification());
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
        return new RequestAdvancePaymentActivityInformation(process, this);
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
        return super.isUserAwarenessNeeded(process);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }
    
    @Override
    public boolean isVisible(SimplifiedProcedureProcess process, User user) {
     return  process.getAcquisitionRequest().getAdvancePaymentRequest() ==null;
    }

}
