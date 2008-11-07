package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class RemovePayingUnit extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = getUser();
	return user != null && user.getPerson() == process.getRequestor();
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.getAcquisitionProcessState().isInGenesis();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	List<Unit> units = (List<Unit>) objects[0];
	AcquisitionRequest request = process.getAcquisitionRequest();
	for (Unit unit : units) {
	    request.removePayingUnit(unit);
	}
    }

}
