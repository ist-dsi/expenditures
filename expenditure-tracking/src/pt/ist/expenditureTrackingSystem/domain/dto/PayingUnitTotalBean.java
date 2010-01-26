package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class PayingUnitTotalBean implements Serializable {

    Financer financer;
    Money amount;

    public PayingUnitTotalBean(Financer financer) {
	setFinancer(financer);
	setAmount(financer.getAmountAllocated());
    }

    public Unit getPayingUnit() {
	return getFinancer().getUnit();
    }

    public Financer getFinancer() {
	return financer;
    }

    public void setFinancer(Financer financer) {
	this.financer = financer;
    }

    public Acquisition getRequest() {
	return getFinancer().getFundedRequest();
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
