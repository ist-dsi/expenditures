package pt.ist.expenditureTrackingSystem.domain.acquisitions.search;

import java.util.Arrays;
import java.util.Set;

public class SearchProcessValuesArray {

    private final SearchProcessValues[] searchProcessValues;

    public SearchProcessValuesArray(final SearchProcessValues[] searchProcessValues) {
	this.searchProcessValues = getSearchProcessValues(searchProcessValues);
    }

    private SearchProcessValuesArray(final String[] strings) {
	searchProcessValues = new SearchProcessValues[strings.length];
	for (int i = 0; i < strings.length; i++) {
	    final String string = strings[i];
	    searchProcessValues[i] = SearchProcessValues.valueOf(string);
	}
    }

    public String exportAsString() {
	final StringBuilder builder = new StringBuilder();
	for (final SearchProcessValues s : searchProcessValues) {
	    if (builder.length() > 0) {
		builder.append(",");
	    }
	    builder.append(s);
	}
	return builder.toString();
    }

    public static SearchProcessValuesArray importFromString(final String string) {
	return string == null ? null : new SearchProcessValuesArray(string.split(","));
    }

    public static SearchProcessValuesArray importFromString(final Set<SearchProcessValues> set) {
	return set == null ? null : new SearchProcessValuesArray(set.toArray(new SearchProcessValues[0]));
    }

    private static SearchProcessValues[] getSearchProcessValues(final SearchProcessValues[] searchProcessValues) {
	return searchProcessValues == null ? null : Arrays.copyOf(searchProcessValues, searchProcessValues.length);
    }

    public SearchProcessValues[] getSearchProcessValues() {
	return getSearchProcessValues(searchProcessValues);
    }

    public boolean contains(final SearchProcessValues values) {
	for (final SearchProcessValues s : searchProcessValues) {
	    if (s == values) {
		return true;
	    }
	}
	return false;
    }

}
