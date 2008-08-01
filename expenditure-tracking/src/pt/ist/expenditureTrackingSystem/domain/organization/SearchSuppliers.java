package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.Search;

public class SearchSuppliers extends Search<Supplier> {

    private String fiscalCode;
    private String name;

    protected class SearchResult extends SearchResultSet<Supplier> {

	public SearchResult(final Collection<? extends Supplier> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final Supplier supplier) {
	    return matchCriteria(fiscalCode, supplier.getFiscalIdentificationCode()) && matchCriteria(name, supplier.getName());
	}

    }

    @Override
    public Set<Supplier> search() {
	final Set<Supplier> suppliers = fiscalCode != null || name != null ? ExpenditureTrackingSystem.getInstance()
		.getSuppliersSet() : Collections.EMPTY_SET;
	return new SearchResult(suppliers);
    }

    public String getFiscalCode() {
	return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
	this.fiscalCode = fiscalCode;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
