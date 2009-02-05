package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.dto.AcquisitionRequestItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CreateAcquisitionRequestItem extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson.equals(process.getRequestor());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.getAcquisitionProcessState().isInGenesis();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
	AcquisitionRequestItemBean bean = (AcquisitionRequestItemBean) objects[0];
	acquisitionRequest.createAcquisitionRequestItem(bean);
    }

}
