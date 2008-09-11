package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class NIFSupplierAutoCompleteProvider implements AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	final List<Supplier> result = new ArrayList<Supplier>();
	final String[] input = value.split(" ");
	StringNormalizer.normalize(input);

	for (final Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliers()) {
	    if (supplier.getFiscalIdentificationCode().startsWith(value)) {
		result.add(supplier);
	    } else {
		final String name = StringNormalizer.normalize(supplier.getName());
		if (hasMatch(input, name)) {
		    result.add(supplier);
		}
	    }
	}
	return result;
    }

    private boolean hasMatch(final String[] input, final String unitNameParts) {
	for (final String namePart : input) {
	    if (unitNameParts.indexOf(namePart) == -1) {
		return false;
	    }
	}
	return true;
    }

}
