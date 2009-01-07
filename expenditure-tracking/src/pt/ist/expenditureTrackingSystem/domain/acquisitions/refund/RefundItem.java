package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.dto.RefundItemBean;
import pt.ist.expenditureTrackingSystem.domain.util.Money;

public class RefundItem extends RefundItem_Base {

    public RefundItem(RefundRequest request, Money valueEstimation, CPVReference reference, String description) {
	super();
	if (description == null || reference == null || valueEstimation == null || valueEstimation.equals(Money.ZERO)) {
	    throw new DomainException("refundProcess.message.exception.refundItem.invalidArguments");
	}
	setDescription(description);
	setCPVReference(reference);
	setValueEstimation(valueEstimation);
	setRequest(request);
    }

    @Override
    public Money getRealValue() {
	return getValueSpent();
    }

    @Override
    public Money getValue() {
	return getValueEstimation();
    }

    @Override
    public BigDecimal getVatValue() {
	return null;
    }

    public void edit(RefundItemBean bean) {
	setDescription(bean.getDescription());
	setCPVReference(bean.getCPVReference());
	setValueEstimation(bean.getValueEstimation());
    }

    public void delete() {
	removeRequest();
	super.delete();
    }

    public boolean isValueFullyAttributedToUnits() {
	Money totalValue = Money.ZERO;
	for (UnitItem unitItem : getUnitItems()) {
	    totalValue = totalValue.add(unitItem.getShareValue());
	}

	return totalValue.equals(getValueEstimation());
    }

}
