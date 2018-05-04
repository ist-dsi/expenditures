package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.SigningState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

/**
 * 
 * @author Ricardo Almeida
 *
 */
public class RefusePurchaseOrderSignature
        extends WorkflowActivity<RegularAcquisitionProcess, RefusePurchaseOrderSignatureInformation> {

    @Override
    protected void process(RefusePurchaseOrderSignatureInformation activityInformation) {
        final RegularAcquisitionProcess process = activityInformation.getProcess();
        process.getPurchaseOrderDocument().setSigningState(SigningState.REFUSED);
        process.getPurchaseOrderDocument().delete();
    }

    @Override
    public RefusePurchaseOrderSignatureInformation getActivityInformation(RegularAcquisitionProcess process) {
        return new RefusePurchaseOrderSignatureInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        return process.isAccessible(user);
    }

    @Override
    protected String[] getArgumentsDescription(RefusePurchaseOrderSignatureInformation activityInformation) {
        final String[] args = { activityInformation.getReason() };
        return args;
    }
}
