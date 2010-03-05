package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.List;

import myorg.domain.util.Money;

import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class OpenNIFSupplierAutoCompleteProvider extends NIFSupplierAutoCompleteProvider {

    @Override
    protected void addResult(final List<Supplier> result, final Supplier supplier) {
	final Money limit = supplier.getSupplierLimit();
	if (limit != null && limit.isPositive()) {
	    super.addResult(result, supplier);
	}
    }

}
