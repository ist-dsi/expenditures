package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.DateTime;
import org.joda.time.base.AbstractInstant;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.dto.AcquisitionRequestItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.ByteArray;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequest extends AcquisitionRequest_Base {

    AcquisitionRequest(final AcquisitionProcess acquisitionProcess, final Person person) {
	super();
	checkParameters(acquisitionProcess, person);
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setAcquisitionProcess(acquisitionProcess);
	setRequester(person);
    }

    private void checkParameters(AcquisitionProcess acquisitionProcess, Person person) {
	if (acquisitionProcess == null) {
	    throw new DomainException("error.acquisition.request.wrong.acquisition.process");
	}
	if (person == null) {
	    throw new DomainException("error.anonymous.creation.of.acquisition.request.information.not.allowed");
	}
    }

    AcquisitionRequest(AcquisitionProcess acquisitionProcess, Supplier supplier, Person person) {
	this(acquisitionProcess, person);
	setSupplier(supplier);
    }

    public void edit(Supplier supplier, Unit requestingUnit, Boolean isRequestingUnitPayingUnit) {
	setSupplier(supplier);
	setRequestingUnit(requestingUnit);
	if (isRequestingUnitPayingUnit) {
	    addPayingUnits(requestingUnit);
	}
    }

    public void addAcquisitionProposalDocument(final String filename, final byte[] bytes) {
	AcquisitionProposalDocument acquisitionProposalDocument = getAcquisitionProposalDocument();
	if (acquisitionProposalDocument == null) {
	    acquisitionProposalDocument = new AcquisitionProposalDocument();
	    setAcquisitionProposalDocument(acquisitionProposalDocument);
	}
	acquisitionProposalDocument.setFilename(filename);
	acquisitionProposalDocument.setContent(new ByteArray(bytes));
    }

    public AcquisitionRequestItem createAcquisitionRequestItem(AcquisitionRequestItemBean requestItemBean) {

	return new AcquisitionRequestItem(requestItemBean);
    }

    public void delete() {
	for (AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
	    acquisitionRequestItem.delete();
	}
	removeAcquisitionProposalDocument();
	removeRequester();
	removeSupplier();
	removeAcquisitionProcess();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public String getFiscalIdentificationCode() {
	return getSupplier() != null ? getSupplier().getFiscalIdentificationCode() : null;
    }

    public void setFiscalIdentificationCode(String fiscalIdentificationCode) {
	Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(fiscalIdentificationCode);
	if (supplier == null) {
	    supplier = new Supplier(fiscalIdentificationCode);
	}
	setSupplier(supplier);
    }

    public Money getTotalItemValue() {
	Money result = Money.ZERO;
	for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
	    result = result.add(acquisitionRequestItem.getTotalItemValue());
	}
	return result;
    }

    public Money getRealTotalValue() {
	Money result = Money.ZERO;
	for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
	    result = result.add(acquisitionRequestItem.getTotalRealValue());
	}
	return result;
    }

    public void receiveInvoice(final String filename, final byte[] bytes, final String invoiceNumber, final DateTime invoiceDate) {
	final Invoice invoice = hasInvoice() ? getInvoice() : new Invoice(this);
	invoice.setFilename(filename);
	invoice.setContent(new ByteArray(bytes));
	invoice.setInvoiceNumber(invoiceNumber);
	invoice.setInvoiceDate(invoiceDate);
    }

    public String getInvoiceNumber() {
	final Invoice invoice = getInvoice();
	return invoice == null ? null : invoice.getInvoiceNumber();
    }

    public DateTime getInvoiceDate() {
	final Invoice invoice = getInvoice();
	return invoice == null ? null : invoice.getInvoiceDate();
    }

    public boolean isFilled() {
	return hasAcquisitionProposalDocument() && getAcquisitionRequestItemsCount() > 0;
    }

    public boolean isInvoiceReceived() {
	final Invoice invoice = getInvoice();
	return invoice != null && invoice.isInvoiceReceived();
    }

    // public String getCostCenter() {
    // return getRequestingUnit().getCostCenter();
    // }

    public boolean isEveryItemFullyAttributedToPayingUnits() {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    if (!item.isValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

    public boolean isEveryItemFullyAttributeInRealValues() {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    if (!item.isRealValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    public void removePayingUnits(Unit payingUnit) {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    if (item.getUnitItemFor(payingUnit) != null) {
		throw new DomainException("error.cannot.remove.paying.unit.that.already.has.items.assigned.remove.them.first");
	    }
	}
	super.removePayingUnits(payingUnit);
    }

    public void approvedBy(Person person) {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    item.approvedBy(person);
	}
    }

    public void unapproveBy(Person person) {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    item.unapprovedBy(person);
	}
    }

    public boolean isApprovedByAllResponsibles() {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    if (!item.isApproved()) {
		return false;
	    }
	}
	return true;
    }

    public boolean hasBeenApprovedBy(Person person) {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    if (item.hasBeenApprovedBy(person)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isInvoiceConfirmedBy(Person person) {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    if (item.isInvoiceConfirmedBy(person)) {
		return true;
	    }
	}
	return false;
    }

    public void confirmInvoiceFor(Person person) {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    item.confirmInvoiceBy(person);
	}
    }

    public void unconfirmInvoiceFor(Person person) {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    item.unconfirmInvoiceBy(person);
	}
    }

    public boolean isInvoiceConfirmedBy() {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    if (!item.isInvoiceConfirmed()) {
		return false;
	    }
	}
	return true;
    }

    public Money getValueAllocated() {
	AcquisitionProcessStateType acquisitionProcessStateType = getAcquisitionProcess().getAcquisitionProcessStateType();

	if (acquisitionProcessStateType.compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY) >= 0) {
	    return getRealTotalValue();
	} else if (acquisitionProcessStateType.compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER) >= 0) {
	    return getTotalItemValue();
	}
	return Money.ZERO;
    }
}
