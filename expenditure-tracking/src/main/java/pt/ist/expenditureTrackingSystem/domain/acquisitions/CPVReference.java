/*
 * @(#)CPVReference.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.plugins.luceneIndexing.IndexableField;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.IndexDocument;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.interfaces.Indexable;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.interfaces.Searchable;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class CPVReference extends CPVReference_Base /* implements Indexable, Searchable */ {

    public static enum CPVIndexes implements IndexableField {
	CODE("code"), DESCRIPTION("desc");

	private String name;

	private CPVIndexes(String name) {
	    this.name = name;
	}

	@Override
	public String getFieldName() {
	    return this.name;
	}
    }

    public static Comparator<CPVReference> COMPARATOR_BY_DESCRIPTION = new Comparator<CPVReference>() {

	@Override
	public int compare(final CPVReference o1, final CPVReference o2) {
	    return o1.getDescription().compareTo(o2.getDescription());
	}

    };

    public CPVReference(String code, String description) {
	checkParameters(code, description);

	setCode(code);
	setDescription(description);
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    private void checkParameters(String code, String description) {
	if (code == null || description == null) {
	    throw new DomainException("error.code.and.description.are.required");
	}
	if (getCPVCode(code) != null) {
	    throw new DomainException("error.cpv.code.already.exists");
	}
    }

    public static CPVReference getCPVCode(String code) {
	for (CPVReference reference : MyOrg.getInstance().getCPVReferences()) {
	    if (reference.getCode().equals(code)) {
		return reference;
	    }
	}
	return null;
    }

    public String getFullDescription() {
	return getCode() + " - " + getDescription();
    }

    public Money getTotalAmountAllocated(final int year) {
	Money money = Money.ZERO;
	for (final RequestItem requestItem : getAcquisitionItemsSet()) {
	    money = money.add(requestItem.getTotalAmountForCPV(year));
	}
	return money;
    }

    public boolean isPriorityCode() {
	return getExpenditureTrackingSystemForPriorities() != null;
    }

    @Service
    public void markAsPriority() {
	setExpenditureTrackingSystemForPriorities(ExpenditureTrackingSystem.getInstance());
    }

    @Service
    public void unmarkAsPriority() {
	setExpenditureTrackingSystemForPriorities(null);
    }

/*
    @Override
    public IndexDocument getDocumentToIndex() {
	IndexDocument indexDocument = new IndexDocument(this);
	indexDocument.indexField(CPVIndexes.CODE, getCode());
	indexDocument.indexField(CPVIndexes.DESCRIPTION, StringNormalizer.normalize(getDescription()));
	return indexDocument;
    }

    @Override
    public Set<Indexable> getObjectsToIndex() {
	Set<Indexable> set = new HashSet<Indexable>();
	set.add(this);
	return set;
    }
*/
}
