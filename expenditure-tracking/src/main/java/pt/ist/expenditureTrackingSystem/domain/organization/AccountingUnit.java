/*
 * @(#)AccountingUnit.java
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
package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.dto.AccountingUnitBean;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AccountingUnit extends AccountingUnit_Base {

    public AccountingUnit() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    @Atomic
    public static AccountingUnit createNewAccountingUnit(final AccountingUnitBean accountingUnitBean) {
        final AccountingUnit accountingUnit = new AccountingUnit();
        accountingUnit.setName(accountingUnitBean.getName());
        return accountingUnit;
    }

    @Atomic
    @Override
    public void addResponsiblePeople(final Person people) {
        super.addResponsiblePeople(people);
    }

    @Atomic
    @Override
    public void addPeople(final Person people) {
        super.addPeople(people);
    }

    @Atomic
    @Override
    public void addResponsibleProjectAccountants(final Person people) {
        super.addResponsibleProjectAccountants(people);
    }

    @Atomic
    @Override
    public void addProjectAccountants(final Person people) {
        super.addProjectAccountants(people);
    }

    @Atomic
    @Override
    public void addTreasuryMembers(Person treasuryMembers) {
        super.addTreasuryMembers(treasuryMembers);
    }

    @Atomic
    @Override
    public void removeResponsiblePeople(final Person people) {
        super.removeResponsiblePeople(people);
    }

    @Atomic
    @Override
    public void removePeople(final Person people) {
        super.removePeople(people);
    }

    @Atomic
    @Override
    public void removeResponsibleProjectAccountants(final Person people) {
        super.removeResponsibleProjectAccountants(people);
    }

    @Atomic
    @Override
    public void removeProjectAccountants(final Person people) {
        super.removeProjectAccountants(people);
    }

    @Atomic
    @Override
    public void removeTreasuryMembers(Person treasuryMembers) {
        super.removeTreasuryMembers(treasuryMembers);
    }

    @Atomic
    @Override
    public void addUnits(final Unit unit) {
        super.addUnits(unit);
    }

    public static AccountingUnit readAccountingUnitByUnitName(final String name) {
        for (AccountingUnit accountingUnit : ExpenditureTrackingSystem.getInstance().getAccountingUnitsSet()) {
            if (accountingUnit.getName().equals(name)) {
                return accountingUnit;
            }
        }
        return null;
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> getPeople() {
        return getPeopleSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Unit> getUnits() {
        return getUnitsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer> getFinancers() {
        return getFinancersSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> getTreasuryMembers() {
        return getTreasuryMembersSet();
    }

    @Deprecated
    public java.util.Set<module.mission.domain.MissionFinancer> getMissionFinancer() {
        return getMissionFinancerSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> getResponsibleProjectAccountants() {
        return getResponsibleProjectAccountantsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> getProjectAccountants() {
        return getProjectAccountantsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.SavedSearch> getAccountingUnitsSearches() {
        return getAccountingUnitsSearchesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> getResponsiblePeople() {
        return getResponsiblePeopleSet();
    }

    @Deprecated
    public boolean hasAnyPeople() {
        return !getPeopleSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyUnits() {
        return !getUnitsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyFinancers() {
        return !getFinancersSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyTreasuryMembers() {
        return !getTreasuryMembersSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyMissionFinancer() {
        return !getMissionFinancerSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyResponsibleProjectAccountants() {
        return !getResponsibleProjectAccountantsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyProjectAccountants() {
        return !getProjectAccountantsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAccountingUnitsSearches() {
        return !getAccountingUnitsSearchesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyResponsiblePeople() {
        return !getResponsiblePeopleSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

}
