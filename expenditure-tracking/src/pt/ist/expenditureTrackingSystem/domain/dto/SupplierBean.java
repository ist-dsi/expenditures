package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class SupplierBean implements Serializable {

    private Supplier supplier;

    public SupplierBean() {
    }

    public SupplierBean(final Supplier supplier) {
	setSupplier(supplier);
    }

    public Supplier getSupplier() {
	return supplier;
    }

    public void setSupplier(final Supplier supplier) {
	this.supplier = supplier;
    }

}
