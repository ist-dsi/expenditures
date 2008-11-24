package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.fenixWebFramework.security.UserView;

public class EditAfterTheFactAcquisition extends AbstractActivity<AfterTheFactAcquisitionProcess> {

    @Override
    protected boolean isAccessible(final AfterTheFactAcquisitionProcess process) {
	final User user = UserView.getUser();
	final Person person = user == null ? null : user.getPerson();
	return person != null && (person.hasRoleType(RoleType.ACQUISITION_CENTRAL)
		|| person.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER));
    }

    @Override
    protected boolean isAvailable(final AfterTheFactAcquisitionProcess process) {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = process.getAcquisitionAfterTheFact();
	return !acquisitionAfterTheFact.getDeleted().booleanValue();
    }

    @Override
    protected void process(final AfterTheFactAcquisitionProcess process, final Object... objects) {
	final AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean = (AfterTheFactAcquisitionProcessBean) objects[0];
	process.edit(afterTheFactAcquisitionProcessBean);
    }

}
