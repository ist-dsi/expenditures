package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.fenixWebFramework.security.UserView;

public class EditAcquisitionRequestItem extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	User user = UserView.getUser();
	return user != null && user.getPerson().equals(process.getRequestor());
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.IN_GENESIS)
		&& process.getAcquisitionRequest().getAcquisitionRequestItemsCount() > 0;
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {

	AcquisitionRequestItem item = (AcquisitionRequestItem) objects[0];
	String description = (String) objects[1];
	Integer quantity = (Integer) objects[2];
	BigDecimal unitValue = (BigDecimal) objects[3];
	BigDecimal vatValue = (BigDecimal) objects[4];
	String proposalReference = (String) objects[5];
	String salesCode = (String) objects[6];
	item.edit(description, quantity, unitValue, vatValue, proposalReference, salesCode);

    }
}
