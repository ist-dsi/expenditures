/*
 * @(#)Project.java
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

import java.util.Set;

import org.joda.time.LocalDate;

import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Party;
import module.organization.domain.PartyType;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class Project extends Project_Base {

    public static class ProjectPartyTypeListener extends RelationAdapter<PartyType, Party> {

        @Override
        public void afterAdd(final PartyType partyType, final Party party) {
            if (party.isUnit() && partyType != null
                    && partyType == ExpenditureTrackingSystem.getInstance().getProjectPartyType()) {
                new Project((module.organization.domain.Unit) party);
            }
        }

    }

    static {
        Party.getRelationPartyTypeParty().addListener(new ProjectPartyTypeListener());
    }

    public Project(final module.organization.domain.Unit unit) {
        super();
        setUnit(unit);
    }

    public Project(final Unit parentUnit, final String name, final String projectCode) {
        super();
        createRealUnit(this, parentUnit, ExpenditureTrackingSystem.getInstance().getProjectPartyType(), projectCode, name);

        // TODO : After this object is refactored to retrieve the name and
        // parent from the real unit,
        // the following three lines may be deleted.
        setName(name);
        setParentUnit(parentUnit);
    }

    @Override
    public boolean isProject() {
        return true;
    }

    @Override
    public void findAcquisitionProcessesPendingAuthorization(final Set<AcquisitionProcess> result,
            final boolean recurseSubUnits) {
        GenericProcess.getAllProcessStream(RegularAcquisitionProcess.class)
            .filter(p -> p.getPayingUnitStream().anyMatch(u -> u == this) && p.isPendingApproval())
            .forEach(p -> result.add(p));
    }

    @Override
    public String getPresentationName() {
        return "(" + getUnit().getAcronym() +") " + super.getPresentationName();
    }

    @Override
    public String getShortIdentifier() {
        return getUnit().getAcronym();
    }

    @Override
    public Financer finance(final RequestWithPayment acquisitionRequest) {
        return new ProjectFinancer(acquisitionRequest, this);
    }

    @Override
    public boolean isProjectAccountingEmployee(final Person person) {
        final AccountingUnit accountingUnit = getAccountingUnit();
        return accountingUnit != null && person != null && person.getProjectAccountingUnitsSet().contains(accountingUnit);
    }

    @Override
    public boolean isAccountingEmployee(final Person person) {
        final AccountingUnit accountingUnit = getAccountingUnit();
        return (accountingUnit != null && person != null && person.getAccountingUnitsSet().contains(accountingUnit))
                || (accountingUnit == null && super.isAccountingEmployee(person));
    }

    public static Project findProjectByCode(final String projectCode) {
        for (Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
            if (unit instanceof Project) {
                if (unit.getUnit().getAcronym().equals(projectCode)) {
                    return (Project) unit;
                }
            }
        }
        return null;
    }

    public SubProject findSubProjectByName(final String institution) {
        return getUnit().getChildAccountabilityStream()
                .filter(a -> a.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                        .getOrganizationalAccountabilityType())
                .map(a -> a.getChild()).filter(p -> p.isUnit()).map(p -> (module.organization.domain.Unit) p)
                .map(u -> u.getExpenditureUnit()).filter(u -> u != null && u instanceof SubProject).map(u -> (SubProject) u)
                .filter(u -> u.getName().equals(institution)).findAny().orElse(null);
    }

    @Override
    public boolean isAccountingResponsible(Person person) {
        final AccountingUnit accountingUnit = getAccountingUnit();
        return accountingUnit != null && person != null
                && person.getResponsibleProjectAccountingUnitsSet().contains(accountingUnit);
    }

    @Override
    public String getUnitNumber() {
        return getUnit().getAcronym();
    }

    public boolean isOpen() {
        final AccountabilityType accountabilityType =
                ExpenditureTrackingSystem.getInstance().getOrganizationalAccountabilityType();
        for (final Accountability accountability : getUnit().getAllParentAccountabilities()) {
            if (!accountability.isErased() && accountability.getAccountabilityType() == accountabilityType
                    && accountability.isActiveNow()) {
                return true;
            }
        }
        return false;
    }

    public void close() {
        final AccountabilityType accountabilityType =
                ExpenditureTrackingSystem.getInstance().getOrganizationalAccountabilityType();
        getUnit().getParentAccountabilityStream().filter(a -> a.getAccountabilityType() == accountabilityType && a.isActiveNow())
                .forEach(a -> a.editDates(a.getBeginDate(), new LocalDate(), null));;
    }

    public void open() {
        final Unit parentProject = getParentUnit();
        if (parentProject != null) {
            final module.organization.domain.Unit parentUnit = getParentUnit().getUnit();
            final module.organization.domain.Unit unit = getUnit();
            final AccountabilityType accountabilityType =
                    ExpenditureTrackingSystem.getInstance().getOrganizationalAccountabilityType();
            parentUnit.addChild(unit, accountabilityType, new LocalDate(), null, null);
        }
    }

}
