package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;

public class MaterialAutoCompleteProvider implements AutoCompleteProvider<Material> {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<Material> result = new ArrayList<Material>();

        String[] values = value.toLowerCase().split(" ");
        for (final Material material : ExpenditureTrackingSystem.getInstance().getMaterialsSet()) {
            if (material.getMaterialSapId().contains(value) || material.getMaterialCpv().getCode().startsWith(value)
                    || match(material.getDescription().toLowerCase(), values)) {
                result.add(material);
            }
            if (result.size() >= maxCount) {
                break;
            }
        }
        return result;
    }

    private boolean match(String description, String[] inputParts) {

        for (final String namePart : inputParts) {
            if (description.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

}
