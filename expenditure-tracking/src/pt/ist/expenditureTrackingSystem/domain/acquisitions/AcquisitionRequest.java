package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.dto.AcquisitionRequestItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.ByteArray;
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

    AcquisitionRequest(AcquisitionProcess acquisitionProcess, Supplier supplier, String project, String subproject,
	    String recipient, String receptionAddress, Person person) {
	this(acquisitionProcess, person);
	setSupplier(supplier);
	setProject(project);
	setSubproject(subproject);
	setRecipient(recipient);
	setReceptionAddress(receptionAddress);
    }

    public void edit(Supplier supplier, String project, String subproject, Unit requestingUnit, Boolean isRequestingUnitPayingUnit) {
	setSupplier(supplier);
	setProject(project);
	setSubproject(subproject);
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
	return new AcquisitionRequestItem(this, requestItemBean.getDescription(), requestItemBean.getQuantity(), requestItemBean
		.getUnitValue(), requestItemBean.getVatValue(), requestItemBean.getProposalReference(), requestItemBean.getSalesCode());
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

    public BigDecimal getTotalItemValue() {
	BigDecimal result = BigDecimal.ZERO;
	for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
	    result = result.add(acquisitionRequestItem.getTotalItemValue());
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

    @Override
    public String getRecipient() {
	if (super.getRecipient() == null) {
	    return getRequester().getName();
	}
	return super.getRecipient();
    }

    public String getCostCenter() {
	return getRequestingUnit().getCostCenter();
    }
    
    public boolean isEveryItemFullyAttributedToPayingUnits() {
	for (AcquisitionRequestItem item : getAcquisitionRequestItems()) {
	    if (!item.isValueFullyAttributedToUnits()) {
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
}
