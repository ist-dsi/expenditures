/*
 * @(#)SupplierContact.java
 *
 * Copyright 2012 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Finance Module.
 *
 *   The Finance Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Finance Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Finance Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.finance.domain;

import myorg.domain.util.Address;

/**
 * 
 * @author Luis Cruz
 * 
 */
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
