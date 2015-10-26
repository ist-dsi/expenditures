/*
 * @(#)CostCenter.java
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

import module.organization.domain.Party;
import module.organization.domain.PartyType;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
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
public class CostCenter extends CostCenter_Base {

    public static class CostCenterPartyTypeListener extends RelationAdapter<PartyType, Party> {

        @Override
        public void afterAdd(final PartyType partyType, final Party party) {
            if (party.isUnit() && partyType != null
                    && partyType == ExpenditureTrackingSystem.getInstance().getCostCenterPartyType()) {
                new CostCenter((module.organization.domain.Unit) party);
            }
        }

    }

    static {
        Party.getRelationPartyTypeParty().addListener(new CostCenterPartyTypeListener());
    }

    public CostCenter(final module.organization.domain.Unit unit) {
        super();
        setUnit(unit);
    }

    public CostCenter(final Unit parentUnit, final String name, final String costCenter) {
        super();
        createRealUnit(this, parentUnit, ExpenditureTrackingSystem.getInstance().getCostCenterPartyType(), costCenter, name);

        // TODO : After this object is refactored to retrieve the name and
        // parent from the real unit,
        // the following three lines may be deleted.
        setName(name);
        setCostCenter(costCenter);
        setParentUnit(parentUnit);
    }

    public void setCostCenter(final String costCenter) {
        getUnit().setAcronym("CC. " + costCenter);
    }

    public String getCostCenter() {
        return getUnit().getAcronym().substring(4);
    }

    @Override
    public void findAcquisitionProcessesPendingAuthorization(final Set<AcquisitionProcess> result,
            final boolean recurseSubUnits) {
        final String costCenter = getCostCenter();
        if (costCenter != null) {
            GenericProcess.getAllProcessStream(RegularAcquisitionProcess.class)
                    .filter(p -> p.getPayingUnitStream().anyMatch(u -> u == this) && p.isPendingApproval())
                    .forEach(p -> result.add(p));
        }
    }

    @Override
    public String getPresentationName() {
        return "(CC. " + getCostCenter() + ") " + super.getPresentationName();
    }

    @Override
    public String getShortIdentifier() {
        return getCostCenter();
    }

    @Override
    public boolean isAccountingEmployee(final Person person) {
        final AccountingUnit accountingUnit = getAccountingUnit();
        return accountingUnit != null && accountingUnit.getPeopleSet().contains(person);
    }

    @Override
    public Financer finance(final RequestWithPayment acquisitionRequest) {
        return new Financer(acquisitionRequest, this);
    }

    @Override
    public CostCenter getCostCenterUnit() {
        return this;
    }

/*
@Override
public IndexDocument getDocumentToIndex() {
	IndexDocument document = super.getDocumentToIndex();
	document.indexField(UnitIndexFields.NUMBER_INDEX, getCostCenter());
	return document;
}
*/
    @Override
    public String getUnitNumber() {
        return getCostCenter();
    }

}
