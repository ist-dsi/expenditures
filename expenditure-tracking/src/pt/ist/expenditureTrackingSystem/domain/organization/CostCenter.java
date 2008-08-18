package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class CostCenter extends CostCenter_Base {
    
    public CostCenter(final Unit parentUnit, final String name, final String costCenter) {
        super();
    	setName(name);
    	setCostCenter(costCenter);
    	setParentUnit(parentUnit);
    }

    @Override
    public void findAcquisitionProcessesPendingAuthorization(final Set<AcquisitionProcess> result, final boolean recurseSubUnits) {
	final String costCenter = getCostCenter();
	if (costCenter != null) {
	    for (final AcquisitionProcess acquisitionProcess : GenericProcess.getAllProcesses(AcquisitionProcess.class)) {
		if (acquisitionProcess.getPayingUnits().contains(this) && acquisitionProcess.isPendingApproval())
		    result.add(acquisitionProcess);
	    }
	}
	super.findAcquisitionProcessesPendingAuthorization(result, recurseSubUnits);
    }

    @Override
    protected Unit findByCostCenter(final String costCenter) {
	if (getCostCenter() != null && getCostCenter().equals(costCenter)) {
	    return this;
	}
	return super.findByCostCenter(costCenter);
    }

}
