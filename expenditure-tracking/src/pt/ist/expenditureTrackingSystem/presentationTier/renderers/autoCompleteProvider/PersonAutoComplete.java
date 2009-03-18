package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import myorg.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class PersonAutoComplete implements AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	Set<Person> people = new HashSet<Person> ();
	String[] values = StringNormalizer.normalize(value).toLowerCase().split(" ");
	for (Person person : ExpenditureTrackingSystem.getInstance().getPeople()) {
	    final String normalizedName = StringNormalizer.normalize(person.getName()).toLowerCase();
	    if (hasMatch(values, normalizedName)) {
		people.add(person);
	    }
	    if (person.getUsername().indexOf(value) >= 0) {
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
