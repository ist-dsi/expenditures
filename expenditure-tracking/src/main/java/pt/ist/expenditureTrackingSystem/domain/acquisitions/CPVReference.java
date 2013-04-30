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

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.plugins.luceneIndexing.IndexableField;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class CPVReference extends CPVReference_Base /* implements Indexable, Searchable */{

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

    public static Comparator<CPVReference> COMPARATOR_BY_CODE = new Comparator<CPVReference>() {

        @Override
        public int compare(final CPVReference o1, final CPVReference o2) {
            if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1 == null) {
                return 1;
            }
            return o2 == null ? -1 : o1.getCode().compareTo(o2.getCode());
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
        for (CPVReference reference : MyOrg.getInstance().getCPVReferencesSet()) {
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

    @Atomic
    public void markAsPriority() {
        setExpenditureTrackingSystemForPriorities(ExpenditureTrackingSystem.getInstance());
    }

    @Atomic
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
    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem> getAcquisitionItems() {
        return getAcquisitionItemsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.SavedSearch> getSavedSearch() {
        return getSavedSearchSet();
    }

    @Deprecated
    public boolean hasAnyAcquisitionItems() {
        return !getAcquisitionItemsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnySavedSearch() {
        return !getSavedSearchSet().isEmpty();
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystemForPriorities() {
        return getExpenditureTrackingSystemForPriorities() != null;
    }

    @Deprecated
    public boolean hasMyOrg() {
        return getMyOrg() != null;
    }

}
