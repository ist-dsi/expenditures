package module.workingCapital.domain;

import myorg.domain.MyOrg;
import pt.ist.fenixWebFramework.services.Service;

public class WorkingCapitalSystem extends WorkingCapitalSystem_Base {

    public static WorkingCapitalSystem getInstance() {
	final MyOrg myOrg = MyOrg.getInstance();
	if (!myOrg.hasWorkingCapitalSystem()) {
	    initialize();
	}
	return myOrg.getWorkingCapitalSystem();
    }

    @Service
    public synchronized static void initialize() {
	final MyOrg myOrg = MyOrg.getInstance();
	if (!myOrg.hasWorkingCapitalSystem()) {
	    new WorkingCapitalSystem(myOrg);
	}
    }

    private WorkingCapitalSystem(final MyOrg myOrg) {
	super();
	setMyOrg(myOrg);
    }

}
