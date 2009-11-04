package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.dto.AcquisitionRequestItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class EditAcquisitionRequestItem extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && (loggedPerson.equals(process.getRequestor()) || userHasRole(RoleType.ACQUISITION_CENTRAL));
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process)
		&& (getLoggedPerson().equals(process.getRequestor()) && process.getAcquisitionProcessState().isInGenesis() && process
			.getAcquisitionRequest().hasAnyRequestItems())
		|| (process.isSimplifiedAcquisitionProcess()
			&& ((SimplifiedProcedureProcess) process).getProcessClassification() == ProcessClassification.CT75000
			&& userHasRole(RoleType.ACQUISITION_CENTRAL) && process.getAcquisitionProcessState().isAuthorized());
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	AcquisitionRequestItemBean acquisitionRequestItemBean = (AcquisitionRequestItemBean) objects[0];

	acquisitionRequestItemBean.getItem().edit(acquisitionRequestItemBean);

    }
}
