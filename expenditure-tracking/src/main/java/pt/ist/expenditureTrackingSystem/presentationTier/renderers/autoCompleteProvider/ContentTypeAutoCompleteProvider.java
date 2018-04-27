package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;

public class ContentTypeAutoCompleteProvider implements AutoCompleteProvider<Material> {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final List<ContractType> result = new ArrayList<ContractType>();
        final String[] values = value.toLowerCase().split(" ");
        for (final ContractType contractType : ExpenditureTrackingSystem.getInstance().getContractTypeSet()) {
            if (match(contractType.getName().getContent().toLowerCase(), values)) {
                result.add(contractType);
            }
        }
        return result;
    }

    private boolean match(final String description, final String[] inputParts) {
        for (final String namePart : inputParts) {
            if (description.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

}
