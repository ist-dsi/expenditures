/*
 * @(#)RCISTAnnouncement.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.Set;

import pt.ist.bennu.core.domain.util.Money;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class RCISTAnnouncement extends RCISTAnnouncement_Base {

    public RCISTAnnouncement(AcquisitionRequest request) {
	super();
	setAcquisition(request);
	setDescription(request.getContractSimpleDescription());
    }

    @Override
    public AcquisitionRequest getAcquisition() {
	return (AcquisitionRequest) super.getAcquisition();
    }

    @Override
    public Set<Unit> getBuyingUnits() {
	return getAcquisition().getPayingUnits();
    }

    @Override
    public Unit getRequestingUnit() {
	return getAcquisition().getRequestingUnit();
    }

    @Override
    public Supplier getSupplier() {
	return getAcquisition().getSelectedSupplier();
    }

    @Override
    public Money getTotalPrice() {
	return getAcquisition().getCurrentTotalItemValueWithAdditionalCostsAndVat();
    }

    @Override
    public Boolean getActive() {
	return getAcquisition().getProcess().isActive();
    }

}
