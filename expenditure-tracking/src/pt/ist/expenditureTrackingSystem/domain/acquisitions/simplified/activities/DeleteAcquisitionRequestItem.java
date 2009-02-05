package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class DeleteAcquisitionRequestItem extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson.equals(process.getRequestor());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process) && process.getAcquisitionProcessState().isInGenesis()
		&& process.getAcquisitionRequest().hasAnyRequestItems();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	AcquisitionRequestItem item = (AcquisitionRequestItem) objects[0];
	item.delete();
    }

}
