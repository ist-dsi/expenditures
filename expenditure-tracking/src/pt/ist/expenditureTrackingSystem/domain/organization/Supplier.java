package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

import myorg.domain.exceptions.DomainException;
import myorg.domain.util.Address;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateSupplierBean;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.plugins.luceneIndexing.IndexableField;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.IndexDocument;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.interfaces.Indexable;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class Supplier extends Supplier_Base implements Indexable {

    public static enum SupplierIndexes implements IndexableField {
	FISCAL_CODE("nif"), NAME("supplierName");

	private String name;

	private SupplierIndexes(String name) {
	    this.name = name;
	}

	@Override
	public String getFieldName() {
	    return this.name;
	}

    }

    public static Money SUPPLIER_LIMIT = new Money("75000");

    public static Money SOFT_SUPPLIER_LIMIT = new Money("60000");

    private Supplier() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setSupplierLimit(SOFT_SUPPLIER_LIMIT);
    }

    public Supplier(String fiscalCode) {
	this();
	if (fiscalCode == null || fiscalCode.length() == 0) {
	    throw new DomainException("error.fiscal.code.cannot.be.empty");
	}
	setFiscalIdentificationCode(fiscalCode);
    }

    public Supplier(String name, String abbreviatedName, String fiscalCode, Address address, String phone, String fax,
	    String email, String nib) {
	this(fiscalCode);
	setName(name);
	setAbbreviatedName(abbreviatedName);
	setAddress(address);
	setPhone(phone);
	setFax(fax);
	setEmail(email);
	setNib(nib);
    }

    @Service
    public void delete() {
	if (checkIfCanBeDeleted()) {
	    removeExpenditureTrackingSystem();
	    deleteDomainObject();
	}
    }

    private boolean checkIfCanBeDeleted() {
	return !hasAnyAcquisitionRequests() && !hasAnyAcquisitionsAfterTheFact() && !hasAnyRefundInvoices()
		&& !hasAnyAnnouncements() && !hasAnySupplierSearches();
    }

    public static Supplier readSupplierByFiscalIdentificationCode(String fiscalIdentificationCode) {
	for (Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliersSet()) {
	    if (supplier.getFiscalIdentificationCode().equals(fiscalIdentificationCode)) {
		return supplier;
	    }
	}
	return null;
    }

    public static Supplier readSupplierByName(final String name) {
	for (Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliersSet()) {
	    if (supplier.getName().equalsIgnoreCase(name)) {
		return supplier;
	    }
	}
	return null;
    }

    public Money getTotalAllocated() {
	Money result = Money.ZERO;
	for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
	    final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
	    if (acquisitionProcess.isActive() && acquisitionProcess.isAllocatedToSupplier()) {
		result = result.add(acquisitionRequest.getValueAllocated());
	    }
	}
	for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
	    if (!acquisitionAfterTheFact.getDeletedState().booleanValue()) {
		result = result.add(acquisitionAfterTheFact.getValue());
	    }
	}
	for (final RefundableInvoiceFile refundInvoice : getRefundInvoicesSet()) {
	    final RefundProcess refundProcess = refundInvoice.getRefundItem().getRequest().getProcess();
	    if (refundProcess.isActive() && !refundProcess.getSkipSupplierFundAllocation()) {
		result = result.add(refundInvoice.getRefundableValue());
	    }
	}
	return result;
    }

    public Money getSoftTotalAllocated() {
	Money result = Money.ZERO;
	result = result.add(getTotalAllocatedByAcquisitionProcesses(true));

	for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
	    if (!acquisitionAfterTheFact.getDeletedState().booleanValue()) {
		result = result.add(acquisitionAfterTheFact.getValue());
	    }
	}
	for (final RefundableInvoiceFile refundInvoice : getRefundInvoicesSet()) {
	    final RefundProcess refundProcess = refundInvoice.getRefundItem().getRequest().getProcess();
	    if (refundProcess.isActive()) {
		result = result.add(refundInvoice.getRefundableValue());
	    }
	}
	return result;
    }

    private Money getTotalAllocatedByAcquisitionProcesses(boolean allProcesses) {
	Money result = Money.ZERO;
	for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
	    if ((allProcesses && !acquisitionRequest.getProcess().getShouldSkipSupplierFundAllocation())
		    || acquisitionRequest.getAcquisitionProcess().isAllocatedToSupplier()) {
		result = result.add(acquisitionRequest.getValueAllocated());
	    }
	}
	return result;
    }

    public Money getTotalAllocatedByAcquisitionProcesses() {
	return getTotalAllocatedByAcquisitionProcesses(false);
    }

    public Money getTotalAllocatedByAfterTheFactAcquisitions(final AfterTheFactAcquisitionType afterTheFactAcquisitionType) {
	Money result = Money.ZERO;
	for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
	    if (!acquisitionAfterTheFact.getDeletedState().booleanValue()) {
		if (acquisitionAfterTheFact.getAfterTheFactAcquisitionType() == afterTheFactAcquisitionType) {
		    result = result.add(acquisitionAfterTheFact.getValue());
		}
	    }
	}
	return result;
    }

    public Money getTotalAllocatedByPurchases() {
	return getTotalAllocatedByAfterTheFactAcquisitions(AfterTheFactAcquisitionType.PURCHASE);
    }

    public Money getTotalAllocatedByWorkingCapitals() {
	return getTotalAllocatedByAfterTheFactAcquisitions(AfterTheFactAcquisitionType.WORKING_CAPITAL);
    }

    public Money getTotalAllocatedByRefunds() {
	return getTotalAllocatedByAfterTheFactAcquisitions(AfterTheFactAcquisitionType.REFUND);
    }

    @Service
    public static Supplier createNewSupplier(CreateSupplierBean createSupplierBean) {
	return new Supplier(createSupplierBean.getName(), createSupplierBean.getAbbreviatedName(), createSupplierBean
		.getFiscalIdentificationCode(), createSupplierBean.getAddress(), createSupplierBean.getPhone(),
		createSupplierBean.getFax(), createSupplierBean.getEmail(), createSupplierBean.getNib());
    }

    public boolean isFundAllocationAllowed(final Money value) {
	final Money totalAllocated = getTotalAllocated();
	final Money totalValue = totalAllocated.add(value);
	return totalValue.isLessThanOrEqual(SUPPLIER_LIMIT) && totalValue.isLessThan(getSupplierLimit());
    }

    public String getPresentationName() {
	return getFiscalIdentificationCode() + " - " + getName();
    }

    @Override
    public void setSupplierLimit(final Money supplierLimit) {
	final Money newLimit = supplierLimit.isGreaterThanOrEqual(SUPPLIER_LIMIT) ? SUPPLIER_LIMIT : supplierLimit;
	super.setSupplierLimit(newLimit);
    }

    @Service
    public void merge(final Supplier supplier) {
	if (supplier != this) {
	    final Set<AcquisitionAfterTheFact> acquisitionAfterTheFacts = supplier.getAcquisitionsAfterTheFactSet();
	    getAcquisitionsAfterTheFactSet().addAll(acquisitionAfterTheFacts);
	    acquisitionAfterTheFacts.clear();

	    final Set<RefundableInvoiceFile> refundInvoices = supplier.getRefundInvoicesSet();
	    getRefundInvoicesSet().addAll(refundInvoices);
	    refundInvoices.clear();

	    final Set<Announcement> announcements = supplier.getAnnouncementsSet();
	    getAnnouncementsSet().addAll(announcements);
	    announcements.clear();

	    final Set<SavedSearch> savedSearches = supplier.getSupplierSearchesSet();
	    getSupplierSearchesSet().addAll(savedSearches);
	    savedSearches.clear();

	    final Set<AcquisitionRequest> acquisitionRequests = supplier.getAcquisitionRequestsSet();
	    getAcquisitionRequestsSet().addAll(acquisitionRequests);
	    acquisitionRequests.clear();

	    final Set<AcquisitionRequest> possibleAcquisitionRequests = supplier.getPossibleAcquisitionRequestsSet();
	    getPossibleAcquisitionRequestsSet().addAll(possibleAcquisitionRequests);
	    possibleAcquisitionRequests.clear();

	    supplier.delete();
	}
    }

    @Override
    public IndexDocument getDocumentToIndex() {
	IndexDocument indexDocument = new IndexDocument(this);
	if (!StringUtils.isEmpty(getFiscalIdentificationCode())) {
	    indexDocument.indexField(SupplierIndexes.FISCAL_CODE, getFiscalIdentificationCode());
	}
	indexDocument.indexField(SupplierIndexes.NAME, StringNormalizer.normalize(getName()));
	return indexDocument;
    }

}
