package module.finance.domain;

import myorg.domain.MyOrg;
import pt.ist.fenixWebFramework.services.Service;

public class FinanceSystem extends FinanceSystem_Base {
    
    public static FinanceSystem getInstance() {
	final MyOrg myOrg = MyOrg.getInstance();
	if (!myOrg.hasFinanceSystem()) {
	    initialize();
	}
	return myOrg.getFinanceSystem();
    }

    @Service
    public synchronized static void initialize() {
	final MyOrg myOrg = MyOrg.getInstance();
	if (!myOrg.hasFinanceSystem()) {
	    new FinanceSystem(myOrg);
	}
    }

    private FinanceSystem(final MyOrg myOrg) {
	super();
	setMyOrg(myOrg);
    }

}
