package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixWebFramework.util.DomainReference;

public class SupplierBean implements Serializable {

    private DomainReference<Supplier> supplier;

    public SupplierBean() {
    }

    public SupplierBean(final Supplier supplier) {
	setSupplier(supplier);
    }

    public Supplier getSupplier() {
        return supplier == null ? null : supplier.getObject();
    }

    public void setSupplier(final Supplier supplier) {
        this.supplier = supplier == null ? null : new DomainReference<Supplier>(supplier);
    }

}
