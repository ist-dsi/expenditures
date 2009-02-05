package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.dto.SetRefundeeBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SetRefundee extends GenericAcquisitionProcessActivity {

    protected boolean isRequester(final RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson.equals(process.getRequestor());
    }

    @Override
    protected boolean isAccessible(final RegularAcquisitionProcess process) {
	final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
	return acquisitionRequest.getRefundee() == null && (isRequester(process) || userHasRole(RoleType.ACQUISITION_CENTRAL));
    }

    @Override
    protected boolean isAvailable(final RegularAcquisitionProcess process) {
	final AcquisitionProcessState acquisitionProcessState = process.getAcquisitionProcessState();
	return (isRequester(process) && acquisitionProcessState.isInGenesis())
		|| (userHasRole(RoleType.ACQUISITION_CENTRAL) && (acquisitionProcessState.isAcquisitionProcessed()
			|| (process.isInvoiceReceived() && process.checkRealValues())));
    }

    @Override
    protected void process(final RegularAcquisitionProcess process, final Object... objects) {
	final SetRefundeeBean setRefundeeBean = (SetRefundeeBean) objects[0];
	final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
	acquisitionRequest.setRefundee(setRefundeeBean.getRefundee());
    }

}
