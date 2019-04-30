/*
 * @(#)SavedSearch.java
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
package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class SavedSearch extends SavedSearch_Base {

    protected SavedSearch() {
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public SavedSearch(String name, Person person, SearchPaymentProcess searchBean) {
        this();
        if (person == null) {
            throw new DomainException(Bundle.EXPENDITURE, "message.exception.aPersonIsNeededToSaveTheSearch");
        }
        setSearchName(name);
        setPerson(person);
        setProcessId(searchBean.getProcessId());
        setSearchProcessValues(searchBean.getSearchProcess());
        setPendingOperations(searchBean.getHasAvailableAndAccessibleActivityForUser());
        setShowOnlyResponsabilities(searchBean.getResponsibleUnitSetOnly());
        setRequestor(searchBean.getRequestingPerson());
        setAccountingUnit(searchBean.getAccountingUnit());
        setUnit(searchBean.getRequestingUnit());
        setPayingUnit(searchBean.getPayingUnit());
        setRequestDocumentId(searchBean.getRequestDocumentId());
        setAcquisitionProcessStateType(searchBean.getAcquisitionProcessStateType());
        setRefundProcessStateType(searchBean.getRefundProcessStateType());
        setProposalId(searchBean.getProposalId());
        setRefundeeName(searchBean.getRefundeeName());
        setShowOnlyAcquisitionsExcludedFromSupplierLimit(searchBean.getShowOnlyAcquisitionsExcludedFromSupplierLimit());
        setShowOnlyAcquisitionsWithAdditionalCosts(searchBean.getShowOnlyAcquisitionsWithAdditionalCosts());
        setYear(searchBean.getPaymentProcessYear());
        setTakenBy(searchBean.getTaker());
        setAccountManager(searchBean.getAccountManager());
        setShowOnlyWithUnreadComments(searchBean.getShowOnlyWithUnreadComments());
        setShowPriorityOnly(searchBean.getShowPriorityOnly());
        setCpvReference(searchBean.getCpvReference());
        setShowOnlyAdvancePayments(searchBean.getShowOnlyAdvancePayments());
    }

    public SearchPaymentProcess getSearch() {
        return new SearchPaymentProcess(this);
    }

    @Atomic
    public void delete() {
        setTakenBy(null);
        setYear(null);
        setPayingUnit(null);
        setPerson(null);
        setUnit(null);
        setRequestor(null);
        setAccountingUnit(null);
        setAccountManager(null);
        setSupplier(null);
        setCpvReference(null);
        SavedSearch ownProcessesSearch = MyOwnProcessesSearch.getOwnProcessesSearch();
        for (Person person : getPeople()) {
            person.setDefaultSearch(ownProcessesSearch);
        }
        setExpenditureTrackingSystemForSystemSearch(null);
        setExpenditureTrackingSystem(null);
        deleteDomainObject();
    }

    public boolean isSearchDefaultForUser(Person person) {
        return person.getDefaultSearch() == this;
    }

    public boolean isSearchDefaultForCurrentUser() {
        final Person person = Person.getLoggedPerson();
        return isSearchDefaultForUser(person);
    }

    public static SavedSearch getOwnProcessesSearch() {
        for (SavedSearch search : ExpenditureTrackingSystem.getInstance().getSystemSearches()) {
            if (search instanceof MyOwnProcessesSearch) {
                return search;
            }
        }
        return null;
    }

    @Override
    public Boolean getShowPriorityOnly() {
        Boolean value = super.getShowPriorityOnly();
        return value != null ? value : Boolean.FALSE;
    }

    @Override
    public Boolean getShowOnlyAdvancePayments() {
        Boolean value = super.getShowOnlyAdvancePayments();
        return value != null ? value : Boolean.FALSE;
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> getPeople() {
        return getPeopleSet();
    }

    @Deprecated
    public boolean hasAnyPeople() {
        return !getPeopleSet().isEmpty();
    }

    @Deprecated
    public boolean hasSearchName() {
        return getSearchName() != null;
    }

    @Deprecated
    public boolean hasSearchProcessValues() {
        return getSearchProcessValues() != null;
    }

    @Deprecated
    public boolean hasProcessId() {
        return getProcessId() != null;
    }

    @Deprecated
    public boolean hasPendingOperations() {
        return getPendingOperations() != null;
    }

    @Deprecated
    public boolean hasShowOnlyResponsabilities() {
        return getShowOnlyResponsabilities() != null;
    }

    @Deprecated
    public boolean hasRequestDocumentId() {
        return getRequestDocumentId() != null;
    }

    @Deprecated
    public boolean hasAcquisitionProcessStateType() {
        return getAcquisitionProcessStateType() != null;
    }

    @Deprecated
    public boolean hasRefundProcessStateType() {
        return getRefundProcessStateType() != null;
    }

    @Deprecated
    public boolean hasProposalId() {
        return getProposalId() != null;
    }

    @Deprecated
    public boolean hasShowOnlyAcquisitionsExcludedFromSupplierLimit() {
        return getShowOnlyAcquisitionsExcludedFromSupplierLimit() != null;
    }

    @Deprecated
    public boolean hasShowOnlyAcquisitionsWithAdditionalCosts() {
        return getShowOnlyAcquisitionsWithAdditionalCosts() != null;
    }

    @Deprecated
    public boolean hasRefundeeName() {
        return getRefundeeName() != null;
    }

    @Deprecated
    public boolean hasShowOnlyWithUnreadComments() {
        return getShowOnlyWithUnreadComments() != null;
    }

    @Deprecated
    public boolean hasShowPriorityOnly() {
        return getShowPriorityOnly() != null;
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasPayingUnit() {
        return getPayingUnit() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystemForSystemSearch() {
        return getExpenditureTrackingSystemForSystemSearch() != null;
    }

    @Deprecated
    public boolean hasRequestor() {
        return getRequestor() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

    @Deprecated
    public boolean hasSupplier() {
        return getSupplier() != null;
    }

    @Deprecated
    public boolean hasTakenBy() {
        return getTakenBy() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasAccountManager() {
        return getAccountManager() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

    @Deprecated
    public boolean hasCpvReference() {
        return getCpvReference() != null;
    }

    @Deprecated
    public boolean hasAccountingUnit() {
        return getAccountingUnit() != null;
    }

}
