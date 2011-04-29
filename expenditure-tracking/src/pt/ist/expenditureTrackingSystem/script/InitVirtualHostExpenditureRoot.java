package pt.ist.expenditureTrackingSystem.script;

import myorg.domain.MyOrg;
import myorg.domain.VirtualHost;
import myorg.domain.scheduler.WriteCustomTask;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class InitVirtualHostExpenditureRoot extends WriteCustomTask {

    @Override
    protected void doService() {
	final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
	for (final VirtualHost virtualHost : MyOrg.getInstance().getVirtualHostsSet()) {
	    virtualHost.setExpenditureTrackingSystem(instance);
	}
    }

}
