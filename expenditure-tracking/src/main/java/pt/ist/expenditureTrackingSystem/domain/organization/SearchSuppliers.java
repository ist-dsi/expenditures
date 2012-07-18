/*
 * @(#)SearchSuppliers.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import pt.ist.bennu.core.domain.MyOrg;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.Search;

/**
 * 
 * @author Luis Cruz
 * 
 */
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
	final Set<Supplier> suppliers = fiscalCode != null || name != null ? MyOrg.getInstance()
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
