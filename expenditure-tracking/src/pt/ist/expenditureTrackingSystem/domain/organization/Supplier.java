package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

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

    public Supplier(String name, String fiscalCode, String address, String phone, String fax, String eMail) {
	this(fiscalCode);
	setName(name);
	setAddress(address);
	setPhone(phone);
	setFax(fax);
	setEMail(eMail);
    }

    public static Supplier readSupplierByFiscalIdentificationCode(String fiscalIdentificationCode) {
	for (Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliersSet()) {
	    if (supplier.getFiscalIdentificationCode().equals(fiscalIdentificationCode)) {
		return supplier;
	    }
	}
	return null;
    }

}
