package module.internalrequest.domain.activity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;

public class RemoveItemActivity extends InternalRequestProcessActivity<InternalRequestProcess, RemoveItemActivityInformation> {


    @Override
    public boolean isActive(final InternalRequestProcess internalRequestProcess, final User user) {
        return super.isActive(internalRequestProcess, user) && internalRequestProcess.getIsUnderConstruction()
                && internalRequestProcess.canRemoveItems(user.getPerson());
    }

    @Override
    protected void process(final RemoveItemActivityInformation itemActivityInformation) {
        itemActivityInformation.getItem().delete();
    }

    @Override
    public ActivityInformation<InternalRequestProcess> getActivityInformation(final InternalRequestProcess process) {
        return new RemoveItemActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isConfirmationNeeded(InternalRequestProcess process) {
        return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
        return BundleUtil.getString("resources/InternalRequestResources", "activity.RemoveItemActivity.info");
    }

}
