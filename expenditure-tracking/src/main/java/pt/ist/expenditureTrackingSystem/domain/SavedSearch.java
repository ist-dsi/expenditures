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

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;

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
            throw new DomainException("message.exception.aPersonIsNeededToSaveTheSearch");
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
    }

    public SearchPaymentProcess getSearch() {
        return new SearchPaymentProcess(this);
    }

    @Service
    public void delete() {
        removeTakenBy();
        removeYear();
        removePayingUnit();
        removePerson();
        removeUnit();
        removeRequestor();
        removeAccountingUnit();
        removeAccountManager();
        removeSupplier();
        removeCpvReference();
        SavedSearch ownProcessesSearch = MyOwnProcessesSearch.getOwnProcessesSearch();
        for (Person person : getPeople()) {
            person.setDefaultSearch(ownProcessesSearch);
        }
        removeExpenditureTrackingSystemForSystemSearch();
        removeExpenditureTrackingSystem();
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
    public boolean isConnectedToCurrentHost() {
        return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
    }

}
