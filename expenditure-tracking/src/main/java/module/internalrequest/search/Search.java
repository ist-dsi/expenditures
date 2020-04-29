package module.internalrequest.search;

import module.internalrequest.search.filter.SearchFilter;
import module.internalrequest.search.filter.SearchFilter.SearchOrder;
import org.springframework.beans.support.PagedListHolder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Search {
    public static <T, S extends SearchFilter<T>> Stream<T> filter(S searchFilter) {
        Comparator<T> comparator = searchFilter.getComparator();
        if (SearchOrder.DESC.equals(searchFilter.getOrder())) {
            comparator = comparator.reversed();
        }

        return searchFilter.getItems().filter(searchFilter::includeInSearch).sorted(comparator);
    }

    public static <T, S extends SearchFilter<T>> PagedListHolder<T> pagedFilter(S searchFilter) {
        List<T> filteredResults = Search.filter(searchFilter).collect(Collectors.toList());

        final PagedListHolder<T> pagedResults = new PagedListHolder<T>(filteredResults);
        pagedResults.setPage(searchFilter.getPage());
        pagedResults.setPageSize(searchFilter.getItemsPerPage());
        return pagedResults;
    }
}
