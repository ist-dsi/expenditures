package pt.ist.expenditureTrackingSystem.domain.processes;

import myorg.domain.User;
import myorg.util.BundleUtil;

/*
 * TODO: When Activities are migrated and have their own
 * localization system we can kill this class
 */
public class GenericLog extends GenericLog_Base {

    protected GenericLog() {
	super();
    }

    public GenericLog(GenericProcess process, User person, String operation) {
	super();
	init(process, person);
	super.setOperation(operation);
    }

    @Override
    public String getDescription() {
	return BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources", "label." + getOperation());
    }

}
