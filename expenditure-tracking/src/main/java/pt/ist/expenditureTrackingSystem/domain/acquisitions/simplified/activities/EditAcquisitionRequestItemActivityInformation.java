/*
 * @(#)EditAcquisitionRequestItemActivityInformation.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class EditAcquisitionRequestItemActivityInformation extends CreateAcquisitionRequestItemActivityInformation {

    private AcquisitionRequestItem item;

    public EditAcquisitionRequestItemActivityInformation(RegularAcquisitionProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public AcquisitionRequestItem getItem() {
        return item;
    }

    public void setItem(AcquisitionRequestItem item) {
        this.item = item;
        setDescription(item.getDescription());
        setQuantity(item.getQuantity());
        setUnitValue(item.getUnitValue());
        setVatValue(item.getVatValue());
        setAdditionalCostValue(item.getAdditionalCostValue());
        setProposalReference(item.getProposalReference());
        setAcquisitionRequest(item.getAcquisitionRequest());
        setClassification(item.getClassification());
        setCPVReference(item.getCPVReference());
        setDeliveryInfo(UserView.getCurrentUser().getExpenditurePerson()
                .getDeliveryInfoByRecipientAndAddress(item.getRecipient(), item.getAddress()));
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && getItem() != null && super.hasAllneededInfo();
    }

}
