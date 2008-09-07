package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class RemovePayingUnit extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	User user = getUser();
	return user != null && user.getPerson() == process.getRequestor();
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.IN_GENESIS);
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	List<Unit> units = (List<Unit>) objects[0];
	AcquisitionRequest request = process.getAcquisitionRequest();
	for (Unit unit : units) {
	    request.removePayingUnit(unit);
	}
    }

}
