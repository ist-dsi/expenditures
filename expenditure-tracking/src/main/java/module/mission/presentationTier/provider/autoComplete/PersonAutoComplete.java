package module.mission.presentationTier.provider.autoComplete;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import module.organization.domain.Person;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

public class PersonAutoComplete implements AutoCompleteProvider {

    @Override
    public Collection getSearchResults(Map argsMap, String value, int maxCount) {
        Set<Person> people = new HashSet<Person>();
        String[] values = StringNormalizer.normalize(value).toLowerCase().split(" ");
        for (Person person : Bennu.getInstance().getPersonsSet()) {
            if (person.getUser() != null && person.getUser().getProfile() != null) {
                if (person.getUser().getProfile().getFullName() != null) { 
                    final String normalizedName = StringNormalizer.normalize(person.getUser().getProfile().getFullName()).toLowerCase();
                    if (hasMatch(values, normalizedName)) {
                        people.add(person);
                    }
                }
                if (person.getUser() != null && person.getUser().getUsername().indexOf(value) >= 0) {
                    people.add(person);
                }
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
