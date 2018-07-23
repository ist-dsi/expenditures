/*
 * @(#)Supplier.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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

import module.finance.util.Address;
import module.finance.util.Money;

/**
 * 
 * @author Luis Cruz
 * @author Susana Fernandes
 * 
 */
public class Supplier extends Supplier_Base {

    public static Money SUPPLIER_LIMIT = new Money("20000");

    public static Money SOFT_SUPPLIER_LIMIT = new Money("18000");

    public static Money MULTIPLE_SUPPLIER_LIMIT = new Money("74999.99");

    public static Money SOFT_MULTIPLE_SUPPLIER_LIMIT = new Money("68000");

    public Supplier() {
        super();
        setFinanceSystem(FinanceSystem.getInstance());
        setSupplierLimit(SOFT_SUPPLIER_LIMIT);
        setMultipleSupplierLimit(SOFT_MULTIPLE_SUPPLIER_LIMIT);
    }

    public void delete() {
        if (checkIfCanBeDeleted()) {
            setFinanceSystem(null);
            deleteDomainObject();
        }
    }

    protected boolean checkIfCanBeDeleted() {
        return getProvisionsSet().isEmpty();
    }

    public String getPresentationName() {
        return getFiscalIdentificationCode() + " - " + getName();
    }

    @Override
    public void setSupplierLimit(final Money supplierLimit) {
        final Money newLimit = supplierLimit.isGreaterThanOrEqual(SUPPLIER_LIMIT) ? SUPPLIER_LIMIT : supplierLimit;
        super.setSupplierLimit(newLimit);
    }

    @Override
    public void setMultipleSupplierLimit(final Money supplierLimit) {
        final Money newLimit = supplierLimit.isGreaterThanOrEqual(MULTIPLE_SUPPLIER_LIMIT) ? MULTIPLE_SUPPLIER_LIMIT : supplierLimit;
        super.setMultipleSupplierLimit(newLimit);
    }    

    public Money getAllocated() {
        Money result = Money.ZERO;
        for (final Provision provision : getProvisionsSet()) {
            if (provision.isInAllocationPeriod()) {
                result = result.add(provision.getValueAllocatedToSupplier());
            }
        }
        return result;
    }

    public Money getAllocated(final String cpvReference) {
        Money result = Money.ZERO;
        for (final Provision provision : getProvisionsSet()) {
            if (provision.isInAllocationPeriod()) {
                result = result.add(provision.getValueAllocatedToSupplier(cpvReference));
            }
        }
        return result;
    }

    public Money getAllocatedForLimit() {
        Money result = Money.ZERO;
        for (final Provision provision : getProvisionsSet()) {
            if (provision.isInAllocationPeriod()) {
                result = result.add(provision.getValueAllocatedToSupplierForLimit());
            }
        }
        return result;
    }

    public boolean isFundAllocationAllowed(final Money value) {
        final Money allocated = getAllocated();
        final Money totalValue = allocated.add(value);
        return totalValue.isLessThanOrEqual(SUPPLIER_LIMIT) && totalValue.isLessThan(getSupplierLimit());
    }

    public boolean isFundAllocationAllowed(final String cpvReference, final Money value) {
        final Money allocated = getAllocated(cpvReference);
        final Money totalValue = allocated.add(value);
        return totalValue.isLessThanOrEqual(SUPPLIER_LIMIT) && totalValue.isLessThan(getSupplierLimit());
    }

    public SupplierContact registerContact(final Address address, final String phone, final String fax, final String email) {
        final SupplierContact contact = getSupplierContact(address, phone, fax, email);
        return contact == null ? new SupplierContact(this, address, phone, fax, email) : contact;
    }

    private SupplierContact getSupplierContact(final Address address, String phone, String fax, String email) {
        for (final SupplierContact contact : getSupplierContactSet()) {
            if (contact.matches(address, phone, fax, email)) {
                return contact;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<module.finance.domain.Provision> getProvisions() {
        return getProvisionsSet();
    }

    @Deprecated
    public java.util.Set<module.finance.domain.SupplierContact> getSupplierContact() {
        return getSupplierContactSet();
    }

}
