package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.Search;

public class SearchUsers extends Search<Person> {

    private String username;
    private String name;
    private Person person;
    private RoleType roleType;

    protected class SearchResult extends SearchResultSet<Person> {

	public SearchResult(final Collection<? extends Person> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final Person person) {
	    return matchCriteria(username, person.getUsername()) && matchCriteria(name, person.getName())
		    && matchCriteria(roleType, person);
	}

	private boolean matchCriteria(final RoleType roleType, final Person person) {
	    return roleType == null || person.hasRoleType(roleType);
	}

    }

    @Override
    public Set<Person> search() {
	final Person person = getPerson();
	if (person != null) {
	    final Set<Person> people = new HashSet<Person>();
	    people.add(person);
	    return people;
	}
	final Set<Person> people = username != null || name != null || roleType != null ? ExpenditureTrackingSystem.getInstance()
		.getPeopleSet() : Collections.EMPTY_SET;
	return new SearchResult(people);
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public RoleType getRoleType() {
	return roleType;
    }

    public void setRoleType(RoleType roleType) {
	this.roleType = roleType;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(final Person person) {
	this.person = person;
    }

}
