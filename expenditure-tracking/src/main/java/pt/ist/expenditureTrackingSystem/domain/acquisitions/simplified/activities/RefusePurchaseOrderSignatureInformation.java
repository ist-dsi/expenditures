package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

/**
 * 
 * @author Ricardo Almeida
 *
 */
public class RefusePurchaseOrderSignatureInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private String reason;

    public RefusePurchaseOrderSignatureInformation(RegularAcquisitionProcess process,
            WorkflowActivity<? extends RegularAcquisitionProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean hasAllneededInfo() {
        return !Strings.isNullOrEmpty(reason);
    }

}
