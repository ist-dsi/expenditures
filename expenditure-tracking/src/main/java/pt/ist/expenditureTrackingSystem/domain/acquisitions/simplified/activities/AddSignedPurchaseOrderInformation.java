package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

/**
 * 
 * @author Ricardo Almeida
 *
 */
public class AddSignedPurchaseOrderInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private byte[] file;
    private String requestId;

    public AddSignedPurchaseOrderInformation(RegularAcquisitionProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return file != null && file.length > 0 && !Strings.isNullOrEmpty(requestId);
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}
