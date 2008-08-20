package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class NIFSupplierAutoCompleteProvider implements AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	List<Supplier> result = new ArrayList<Supplier>();
	for (final Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliers()) {
	    if (supplier.getFiscalIdentificationCode().startsWith(value)) {
		result.add(supplier);
	    }
	}
	return result;
    }
}
