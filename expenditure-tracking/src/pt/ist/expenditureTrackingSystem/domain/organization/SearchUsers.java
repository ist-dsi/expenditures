package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.Search;

public class SearchUsers extends Search<Person> {

    private String username;
    private String name;

    protected  class SearchResult extends SearchResultSet<Person> {

	public SearchResult(final Collection<? extends Person> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final Person person) {
	    return matchCriteria(username, person.getUsername())
	    		&& matchCriteria(name, person.getName());
	}
	
    }

    @Override
    public Set<Person> search() {
	final Set<Person> people = username != null || name != null ?
		ExpenditureTrackingSystem.getInstance().getPeopleSet() : Collections.EMPTY_SET;
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

}
