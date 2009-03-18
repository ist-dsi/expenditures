package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import myorg.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class PersonNameAutoComplete implements AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	List<Person> people = new ArrayList<Person> ();
	String[] values = StringNormalizer.normalize(value).toLowerCase().split(" ");
	for (Person person : ExpenditureTrackingSystem.getInstance().getPeople()) {
	    final String normalizedName = StringNormalizer.normalize(person.getName()).toLowerCase();
	    if (hasMatch(values, normalizedName)) {
		people.add(person);
	    }
	}
	return people;
    }

    private boolean hasMatch(String[] input, String personNameParts) {
	for (final String namePart : input) {
	    if (personNameParts.indexOf(namePart) == -1) {
		return false;
	    }
	}
	return true;
    }
}
