package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class UserSearchBean implements Serializable {

    private SavedSearch selectedSearch;
    private Person user;

    public UserSearchBean(Person person) {
	setUser(person);
	setSelectedSearch(null);
    }

    public SavedSearch getSelectedSearch() {
	return selectedSearch;
    }

    public void setSelectedSearch(SavedSearch selectedSearch) {
	this.selectedSearch = selectedSearch;
    }

    public Person getUser() {
	return user;
    }

    public void setUser(Person user) {
	this.user = user;
    }

}
