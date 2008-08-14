package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.dto.AcquisitionRequestItemBean;

public class EditAcquisitionRequestItemRealValues extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return userHasRole(RoleType.ACCOUNTABILITY);
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.INVOICE_CONFIRMED);
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	AcquisitionRequestItemBean acquisitionRequestItemBean = (AcquisitionRequestItemBean) objects[0];
	acquisitionRequestItemBean.getItem().editRealValues(acquisitionRequestItemBean);
    }

}
