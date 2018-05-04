package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.SigningState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

/**
 * 
 * @author Ricardo Almeida
 *
 */
public class AddSignedPurchaseOrder extends WorkflowActivity<RegularAcquisitionProcess, AddSignedPurchaseOrderInformation> {

    private static final String SIGNED_SUFFIX = "_signed";

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        return process.isAccessible(user);
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    protected void process(AddSignedPurchaseOrderInformation activityInformation) {
        final PurchaseOrderDocument signedDoc =
                new PurchaseOrderDocument(activityInformation.getProcess(), activityInformation.getFile(),
                        "" + activityInformation.getRequestId() + SIGNED_SUFFIX + ".pdf", activityInformation.getRequestId());
        signedDoc.setSigningState(SigningState.SIGNED);
    }

}
