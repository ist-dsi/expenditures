package module.workingCapital;

import jvstm.cps.ConsistencyException;
import myorg.util.BundleUtil;

public class WorkingCapitalConsistencyException extends ConsistencyException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getLocalizedMessage() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "error." + getMethodFullname());
    }
}