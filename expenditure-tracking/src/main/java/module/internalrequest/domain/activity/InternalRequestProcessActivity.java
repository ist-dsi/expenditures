package module.internalrequest.domain.activity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public abstract class InternalRequestProcessActivity<P extends InternalRequestProcess, AI extends ActivityInformation<P>>
        extends
        WorkflowActivity<P, AI> {

    @Override
    public boolean isActive(final P process, final User user) {
        return (!process.getIsCancelled()) && (process.getCurrentOwner() == null || process.isTakenByCurrentUser());
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/InternalRequestResources", "activity." + getClass().getSimpleName());
    }

}
