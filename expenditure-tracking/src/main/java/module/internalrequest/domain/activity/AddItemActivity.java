package module.internalrequest.domain.activity;

import org.fenixedu.bennu.core.domain.User;

import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;

public class AddItemActivity extends InternalRequestProcessActivity<InternalRequestProcess, AddItemActivityInformation> {


    @Override
    public boolean isActive(final InternalRequestProcess internalRequestProcess, final User user) {
        return super.isActive(internalRequestProcess, user) && internalRequestProcess.getIsUnderConstruction()
                && internalRequestProcess.canAddItems(user.getPerson());
    }

    @Override
    protected void process(final AddItemActivityInformation itemActivityInformation) {
        itemActivityInformation.createNewInternalRequestItem();
    }

    @Override
    public ActivityInformation<InternalRequestProcess> getActivityInformation(final InternalRequestProcess process) {
        return new AddItemActivityInformation(process, this);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
