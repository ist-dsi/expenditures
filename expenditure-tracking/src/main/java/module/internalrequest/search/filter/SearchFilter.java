package module.internalrequest.search.filter;

import java.util.Comparator;
import java.util.stream.Stream;

public abstract class SearchFilter<T> {

    public static final int DEFAULT_ITEMS_PER_PAGE = 50;
    public static final int MAX_ITEMS_PER_PAGE = 200;

    public enum SearchOrder { ASC, DESC }

    private String search;
    private String sort;
    private SearchOrder order;

    private int page;
    private int itemsPerPage;

    public SearchFilter() {
        this.search = getDefaultSearch();
        this.sort = getDefaultSort();
        this.order = getDefaultOrder();
        this.page = getDefaultPage();
        this.itemsPerPage = getDefaultItemsPerPage();
    }

    public String getSearch() {
        return search;
    }
    protected String getDefaultSearch() { return ""; }
    public void setSearch(String search) {
        this.search = cleanInput(search, getDefaultSearch());
    }

    public String getSort() { return sort; }
    protected abstract String getDefaultSort();
    public void setSort(String sort) { this.sort = cleanInput(sort, getDefaultSort()); }

    public Comparator<T> getComparator() { return getDefaultComparator(); };
    protected abstract Comparator<T> getDefaultComparator();

    public SearchOrder getOrder() { return order; }
    protected SearchOrder getDefaultOrder()  { return SearchOrder.ASC; }
    public void setOrder(SearchOrder order) {
        this.order = cleanInput(order, getDefaultOrder());
    }

    public int getPage() {
        return page;
    }
    protected int getDefaultPage() { return 0; }
    public void setPage(int page) {
        this.page = ((page < 0) ? getDefaultPage() : page);
    }

    public int getItemsPerPage() { return itemsPerPage; }
    protected int getDefaultItemsPerPage() { return DEFAULT_ITEMS_PER_PAGE; }
    public void setItemsPerPage(int itemsPerPage) {
        if (itemsPerPage < 0 || itemsPerPage > MAX_ITEMS_PER_PAGE) { this.itemsPerPage = getDefaultItemsPerPage(); }
        else { this.itemsPerPage = itemsPerPage; }
    }

    public abstract Stream<T> getItems();
    public abstract boolean includeInSearch(T object);

    protected String cleanInput(String input, String defaultInput) {
        return (input != null) ? input.trim() : defaultInput;
    }

    protected <I> I cleanInput(I input, I defaultInput) {
        return (input != null) ? input : defaultInput;
    }
}
