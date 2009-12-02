package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;
import java.util.List;

import myorg.domain.util.Money;
import myorg.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class UnitItem extends UnitItem_Base {

    public UnitItem(Financer financer, RequestItem item, Money shareValue, Boolean isAuthorized,
	    Boolean isSubmitedForFundsAllocation) {

	checkParameters(financer, item, shareValue, isAuthorized);

	setFinancer(financer);
	setItem(item);
	setShareValue(shareValue);
	setItemAuthorized(isAuthorized);
	setSubmitedForFundsAllocation(isSubmitedForFundsAllocation);
	setInvoiceConfirmed(Boolean.FALSE);
    }

    private void checkParameters(Financer financer, RequestItem item, Money shareValue, Boolean isApproved) {
	if (financer == null || item == null || shareValue == null || isApproved == null) {
	    throw new DomainException("unitItem.message.exception.parametersCannotBeNull");
	}

	if (shareValue.isZero()) {
	    throw new DomainException("error.share.value.cannot.be.zero");
	}

	if (shareValue.isNegative()) {
	    throw new DomainException("error.share.value.cannot.be.negative");
	}

	Money currentAssignedValue = item.getTotalAssigned();
	if (currentAssignedValue.addAndRound(shareValue).isGreaterThan(item.getValue().round())) {
	    throw new DomainException("unitItem.message.exception.assignedValuedBiggerThanTotal");
	}
    }

    public Money getRoundedShareValue() {
	return getShareValue() != null ? new Money(getShareValue().getRoundedValue()) : null;
    }

    public Money getRoundedRealShareValue() {
	return getRealShareValue() != null ? new Money(getRealShareValue().getRoundedValue()) : null;
    }

    public BigDecimal getVatValue() {
	return getItem().getVatValue();
    }

    public Money getShareValueWithVat() {
	return getShareValue().addPercentage(getVatValue());
    }

    public void delete() {
	removeFinancer();
	removeItem();
	deleteDomainObject();
    }

    @Override
    public void setRealShareValue(Money realShareValue) {
	if (realShareValue != null) {
	    Money totalAmount = getItem().getRealValue();
	    Money currentAssignedValue = getItem().getTotalRealAssigned();

	    if (currentAssignedValue.addAndRound(realShareValue).isGreaterThan(totalAmount.round())) {
		throw new DomainException("unitItem.message.exception.cannotASsignMoreThanTotalAmount");
	    }
	}
	super.setRealShareValue(realShareValue);
    }

    public Unit getUnit() {
	return getFinancer().getUnit();
    }

    public AccountingUnit getAccountingUnit() {
	return getFinancer().getAccountingUnit();
    }

    @Override
    @Deprecated
    public Boolean getSubmitedForFundsAllocation() {
	return super.getSubmitedForFundsAllocation();
    }

    public boolean isApproved() {
	return getSubmitedForFundsAllocation();
    }

    public boolean isWithAllInvoicesConfirmed() {
	List<PaymentProcessInvoice> invoicesFiles = getItem().getInvoicesFiles();
	List<PaymentProcessInvoice> confirmedInvoices = getConfirmedInvoices();

	return !invoicesFiles.isEmpty() && confirmedInvoices.containsAll(invoicesFiles);

    }
}
