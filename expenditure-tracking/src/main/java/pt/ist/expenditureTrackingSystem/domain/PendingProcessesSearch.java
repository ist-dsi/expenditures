/*
 * @(#)PendingProcessesSearch.java
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

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class PendingProcessesSearch extends PendingProcessesSearch_Base {

    public PendingProcessesSearch() {
        super();
        setPendingOperations(Boolean.TRUE);
        setShowOnlyAcquisitionsExcludedFromSupplierLimit(Boolean.FALSE);
        setShowOnlyAcquisitionsWithAdditionalCosts(Boolean.FALSE);
        setExpenditureTrackingSystemForSystemSearch(ExpenditureTrackingSystem.getInstance());
    }

    @Override
    public Boolean getShowOnlyResponsabilities() {
        Person person = Person.getLoggedPerson();
        return !person.getAuthorizations().isEmpty();
    }

    @Override
    public void setPerson(Person person) {
        throw new DomainException(Bundle.EXPENDITURE, "message.exceptiong.notAllowedToSetPerson");
    }

    @Override
    public void setSearchName(String searchName) {
        throw new DomainException(Bundle.EXPENDITURE, "message.exceptiong.notAllowedToSetName");
    }

    @Override
    public String getSearchName() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/ExpenditureResources", I18N.getLocale());
        return bundle.getString("label.pendingProcessesSearchName");
    }
}
