package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;

public class CPVAutoCompleteProvider implements AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	List<CPVReference> result = new ArrayList<CPVReference>();
	for (final CPVReference cpvCode : ExpenditureTrackingSystem.getInstance().getCPVReferences()) {
	    if (cpvCode.getCode().startsWith(value)) {
		result.add(cpvCode);
	    }
	}
	return result;
    }

}
