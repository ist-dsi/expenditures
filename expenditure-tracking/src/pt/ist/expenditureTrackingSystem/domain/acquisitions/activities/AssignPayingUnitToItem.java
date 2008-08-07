package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
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

	item.getUnitItems().clear();
	for (UnitItemBean bean : beans) {
	    if (bean.getAssigned()) {
		new UnitItem(bean.getUnit(), item, bean.getShareValue(), Boolean.FALSE);
	    }
	}
    }

}
