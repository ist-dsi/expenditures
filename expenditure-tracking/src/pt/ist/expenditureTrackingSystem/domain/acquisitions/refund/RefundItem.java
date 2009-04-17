package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.math.BigDecimal;
import java.util.Set;

import myorg.domain.util.Money;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.dto.RefundItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;

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

    public boolean hasAtLeastOneResponsibleApproval() {
	for (UnitItem unitItem : getUnitItems()) {
	    if (unitItem.getItemAuthorized()) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void createUnitItem(Unit unit, Money shareValue) {
	createUnitItem(getRequest().addPayingUnit(unit), shareValue);
    }

    @Service
    public RefundInvoice createRefundInvoice(String invoiceNumber, LocalDate invoiceDate, Money value, BigDecimal vatValue,
	    Money refundableValue, byte[] invoiceFile, String filename, RefundItem item, Supplier supplier) {
	RefundInvoice invoice = new RefundInvoice(invoiceNumber, invoiceDate, value, vatValue, refundableValue, invoiceFile,
		filename, item, supplier);

	Set<Unit> payingUnits = item.getRequest().getPayingUnits();
	if (payingUnits.size() == 1) {
	    UnitItem unitItemFor = getUnitItemFor(payingUnits.iterator().next());
	    Money share = unitItemFor.getRealShareValue();
	    Money amount = share == null ? refundableValue : share.addAndRound(refundableValue);

	    clearRealShareValues();
	    unitItemFor.setRealShareValue(amount);
	}
	return invoice;
    }

    public Money getValueSpent() {
	if (getInvoices().isEmpty()) {
	    return null;
	}

	Money spent = Money.ZERO;
	for (RefundInvoice invoice : getInvoices()) {
	    spent = spent.addAndRound(invoice.getRefundableValue());
	}
	return spent;
    }

    @Override
    public boolean isFilledWithRealValues() {
	return !getInvoices().isEmpty();
    }

    public void getSuppliers(final Set<Supplier> suppliers) {
	for (final RefundInvoice refundInvoice : getInvoicesSet()) {
	    final Supplier supplier = refundInvoice.getSupplier();
	    if (supplier != null) {
		suppliers.add(supplier);
	    }
	}
    }

    @Override
    public Money getTotalAmountForCPV(final int year) {
	throw new Error("not.implemented");
    }

}
