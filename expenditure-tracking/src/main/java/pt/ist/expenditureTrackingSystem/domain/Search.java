package pt.ist.expenditureTrackingSystem.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import pt.ist.fenixWebFramework.services.Service;

public abstract class Search<T> implements Serializable {

    protected abstract class SearchResultSet<T> extends HashSet<T> {

	public SearchResultSet(Collection<? extends T> c) {
	    super(c);
	}

	@Override
	public boolean add(final T t) {
	    return matchesSearchCriteria(t) && super.add(t);
	}

	protected abstract boolean matchesSearchCriteria(final T t);

	protected boolean matchCriteria(final String criteria, final String value) {
	    return criteria == null || criteria.length() == 0 || criteria.equals(value);
	}

    }

    public abstract Set<T> search();

    public Set<T> getResult() {
	return search();
    }

    @Service
    public void persistSearch(String name) {
	persist(name);
    }

    protected void persist(String name) {

    }
}
