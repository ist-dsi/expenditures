package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class ChangeAcquisitionRequestItemMaterialInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private AcquisitionRequestItem item;
    private Material material;

    public ChangeAcquisitionRequestItemMaterialInformation(SimplifiedProcedureProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && item != null && material != null;
    }

    public AcquisitionRequestItem getItem() {
        return item;
    }

    public void setItem(AcquisitionRequestItem item) {
        this.item = item;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

}
