package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.util.DomainReference;

public class PayingUnitTotalBean implements Serializable {

    DomainReference<Unit> payingUnit;
    DomainReference<AcquisitionRequest> request;
    Money amount;

    public PayingUnitTotalBean(Unit payingUnit, AcquisitionRequest request) {
	setPayingUnit(payingUnit);
	setRequest(request);
	Money amount = Money.ZERO;
	for (AcquisitionRequestItem item : request.getAcquisitionRequestItems()) {
	    UnitItem unitItem = item.getUnitItemFor(payingUnit);
	    if (unitItem != null && unitItem.getShareValue() != null) {
		amount = amount.add(unitItem.getShareValue());
	    }
	}
	setAmount(amount);
    }

    public Unit getPayingUnit() {
	return payingUnit.getObject();
    }

    public void setPayingUnit(Unit payingUnit) {
	this.payingUnit = new DomainReference<Unit>(payingUnit);
    }

    public AcquisitionRequest getRequest() {
	return request.getObject();
    }

    public void setRequest(AcquisitionRequest request) {
	this.request = new DomainReference<AcquisitionRequest>(request);
    }

    public Money getAmount() {
	return amount;
    }

    public void setAmount(Money amount) {
	this.amount = amount;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof PayingUnitTotalBean && ((PayingUnitTotalBean) obj).getPayingUnit() == getPayingUnit()
		&& ((PayingUnitTotalBean) obj).getRequest() == getRequest()
		&& ((PayingUnitTotalBean) obj).getAmount().equals(getAmount());
    }
    
    @Override
    public int hashCode() {
	return getPayingUnit().hashCode() + getRequest().hashCode();
    }
}
