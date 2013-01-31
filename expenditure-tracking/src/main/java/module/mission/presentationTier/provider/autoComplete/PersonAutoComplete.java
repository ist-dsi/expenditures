package module.mission.presentationTier.provider.autoComplete;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import module.organization.domain.Person;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class PersonAutoComplete implements AutoCompleteProvider {

	public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
		Set<Person> people = new HashSet<Person>();
		String[] values = StringNormalizer.normalize(value).toLowerCase().split(" ");
		for (Person person : MyOrg.getInstance().getPersons()) {
			final String normalizedName = StringNormalizer.normalize(person.getName()).toLowerCase();
			if (hasMatch(values, normalizedName)) {
				people.add(person);
			}
			if (person.hasUser() && person.getUser().getUsername().indexOf(value) >= 0) {
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
