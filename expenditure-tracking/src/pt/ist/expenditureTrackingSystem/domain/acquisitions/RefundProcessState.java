package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class RefundProcessState extends RefundProcessState_Base {
    
    public  RefundProcessState(RefundProcess process) {
        super();
        setProcess(process);
    }
    
    public boolean isInGenesis() {
	return true;
    }
    
}
