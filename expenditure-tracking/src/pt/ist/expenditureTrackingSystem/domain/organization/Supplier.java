package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateSupplierBean;
import pt.ist.expenditureTrackingSystem.domain.util.Address;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class Supplier extends Supplier_Base {

    private Supplier() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public Supplier(String fiscalCode) {
	this();
	if (fiscalCode == null || fiscalCode.length() == 0) {
	    throw new DomainException("error.fiscal.code.cannot.be.empty");
	}
	setFiscalIdentificationCode(fiscalCode);
    }

    public Supplier(String name, String abbreviatedName, String fiscalCode, Address address, String phone, String fax,
	    String email) {
	this(fiscalCode);
	setName(name);
	setAbbreviatedName(abbreviatedName);
	setAddress(address);
	setPhone(phone);
	setFax(fax);
	setEmail(email);
    }

    @Service
    public void delete() {
	if (checkIfCanBeDeleted()) {
	    removeExpenditureTrackingSystem();
	    Transaction.deleteObject(this);
	}
    }

    private boolean checkIfCanBeDeleted() {
	return !hasAnyAcquisitionRequests();
    }

    public static Supplier readSupplierByFiscalIdentificationCode(String fiscalIdentificationCode) {
	for (Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliersSet()) {
	    if (supplier.getFiscalIdentificationCode().equals(fiscalIdentificationCode)) {
		return supplier;
	    }
	}
	return null;
    }

    public Money getTotalAllocated() {
	Money result = Money.ZERO;
	for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
	    result = result.add(acquisitionRequest.getValueAllocated());
	}
	return result;
    }

    @Service
    public static Supplier createNewSupplier(CreateSupplierBean createSupplierBean) {
	return new Supplier(createSupplierBean.getName(), createSupplierBean.getAbbreviatedName(), createSupplierBean
		.getFiscalIdentificationCode(), createSupplierBean.getAddress(), createSupplierBean.getPhone(),
		createSupplierBean.getFax(), createSupplierBean.getEmail());
    }

}
