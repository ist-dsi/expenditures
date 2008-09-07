package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;

public class AssignPayingUnitToItem extends GenericAcquisitionProcessActivity {

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
	AcquisitionRequestItem item = (AcquisitionRequestItem) objects[0];
	List<UnitItemBean> beans = (List<UnitItemBean>) objects[1];

	for (; !item.getUnitItems().isEmpty(); item.getUnitItems().get(0).delete())
	    ;

	for (UnitItemBean bean : beans) {
	    if (bean.getAssigned()) {
		item.createUnitItem(bean.getUnit(), bean.getShareValue());
	    }
	}
    }

}
