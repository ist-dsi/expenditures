package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class UnitNameAutoCompleteProvider implements AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	List<Unit> units = new ArrayList<Unit>();
	String[] input = value.split(" ");
	StringNormalizer.normalize(input);

	for (Unit unit : ExpenditureTrackingSystem.getInstance().getUnits()) {
	    String unitName = StringNormalizer.normalize(unit.getName());
	    if (hasMatch(input, unitName)) {
		units.add(unit);
	    }
	}
	return units;
    }

    private boolean hasMatch(String[] input, String unitNameParts) {
	for (final String namePart : input) {
	    if (unitNameParts.indexOf(namePart) == -1) {
		return false;
	    }
	}
	return true;
    }

}
