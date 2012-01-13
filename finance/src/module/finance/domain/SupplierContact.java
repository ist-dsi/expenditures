package module.finance.domain;

import myorg.domain.util.Address;

public class SupplierContact extends SupplierContact_Base {
    
    public SupplierContact(final Supplier supplier, final Address address, final String phone, final String fax, final String email) {
        super();
        setSupplier(supplier);
        setAddress(address);
        setPhone(phone);
        setFax(fax);
        setEmail(email);
    }

    public void delete() {
	removeSupplier();
	deleteDomainObject();
    }

    public boolean matches(final Address address, final String phone, final String fax, final String email) {
	return match(address) && match(getPhone(), phone) && match(getFax(), fax) && match(getEmail(), email);
    }

    private boolean match(final Address address) {
	return (getAddress() == null && address == null) || (getAddress() != null && getAddress().equals(address));
    }

    private boolean match(final String string1, final String string2) {
	return (string1 == null && string2 == null) || (string1 != null && string1.equals(string2));
    }

}
