package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;
import pt.ist.expenditureTrackingSystem.domain.util.Money;

public class DistributeRealValuesForPayingUnits extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInvoiceReceived() && !process.getAcquisitionRequest().hasAtLeastOneConfirmation();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	List<UnitItemBean> beans = (List<UnitItemBean>) objects[0];
	AcquisitionRequestItem item = (AcquisitionRequestItem) objects[1];
	Money amount = Money.ZERO;

	item.clearRealShareValues();
	
	for (UnitItemBean bean : beans) {
	    Money share = bean.getRealShareValue();
	    amount = amount.add(share);
	    item.getUnitItemFor(bean.getUnit()).setRealShareValue(share);
	}

    }

}
