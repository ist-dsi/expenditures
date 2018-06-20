package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class ChangeAcquisitionRequestItemMaterial
    extends WorkflowActivity<SimplifiedProcedureProcess, ChangeAcquisitionRequestItemMaterialInformation> {

    @Override
    public boolean isActive(final SimplifiedProcedureProcess process, final User user) {
        return isUserProcessOwner(process, user)
                && !process.isCanceled()
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user);
    }

    @Override
    protected void process(final ChangeAcquisitionRequestItemMaterialInformation information) {
        final AcquisitionRequestItem item = information.getItem();
        final Material material = information.getMaterial();
        item.setMaterial(material);
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(final SimplifiedProcedureProcess process) {
        return new ChangeAcquisitionRequestItemMaterialInformation(process, this);
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

    @Override
    public boolean isUserAwarenessNeeded(final SimplifiedProcedureProcess process, final User user) {
        return false;
    }

}
