package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class UnitNameAutoCompleteProvider implements AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	List<Unit> units = new ArrayList<Unit>();
	for (Unit unit : ExpenditureTrackingSystem.getInstance().getUnits()) {
	    String[] unitNameParts = unit.getName().split(" ");
	    for (String namePart : unitNameParts) {
		if (namePart.startsWith(value)) {
		    units.add(unit);
		    break;
		}
	    }
	}
	    
	return units;
    }

}
