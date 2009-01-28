package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.util.DomainReference;

public class UserSearchBean implements Serializable {

    private DomainReference<SavedSearch> selectedSearch;
    private DomainReference<Person> user;

    public UserSearchBean(Person person) {
	setUser(person);
	setSelectedSearch(null);
    }

    public SavedSearch getSelectedSearch() {
	return selectedSearch.getObject();
    }

    public void setSelectedSearch(SavedSearch selectedSearch) {
	this.selectedSearch = new DomainReference<SavedSearch>(selectedSearch);
    }

    public Person getUser() {
	return user.getObject();
    }

    public void setUser(Person user) {
	this.user = new DomainReference<Person>(user);
    }

}
