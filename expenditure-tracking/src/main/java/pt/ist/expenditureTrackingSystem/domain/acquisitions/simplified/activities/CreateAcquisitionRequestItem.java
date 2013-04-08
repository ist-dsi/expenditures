/*
 * @(#)CreateAcquisitionRequestItem.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.util.Address;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;

/**
 * 
 * @author Diogo Figueiredo
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class CreateAcquisitionRequestItem extends
        WorkflowActivity<RegularAcquisitionProcess, CreateAcquisitionRequestItemActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        return isUserProcessOwner(process, user) && process.getRequestor() == user.getExpenditurePerson()
                && process.getAcquisitionProcessState().isInGenesis();
    }

    @Override
    protected void process(CreateAcquisitionRequestItemActivityInformation activityInformation) {
        final AcquisitionRequest acquisitionRequest = activityInformation.getProcess().getAcquisitionRequest();

        DeliveryInfo deliveryInfo = activityInformation.getDeliveryInfo();
        String recipient;
        String email;
        String phone;
        Address address;
        if (deliveryInfo != null) {
            recipient = deliveryInfo.getRecipient();
            email = deliveryInfo.getEmail();
            phone = deliveryInfo.getPhone();
            address = deliveryInfo.getAddress();
        } else {
            recipient = activityInformation.getRecipient();
            email = activityInformation.getEmail();
            phone = activityInformation.getPhone();
            address = activityInformation.getAddress();
            acquisitionRequest.getRequester().createNewDeliveryInfo(recipient, address, phone, email);
        }
        acquisitionRequest.createAcquisitionRequestItem(activityInformation.getAcquisitionRequest(),
                activityInformation.getDescription(), activityInformation.getQuantity(), activityInformation.getUnitValue(),
                activityInformation.getVatValue(), activityInformation.getAdditionalCostValue(),
                activityInformation.getProposalReference(), activityInformation.getCPVReference(), recipient, address, phone,
                email, activityInformation.getClassification());
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
        return new CreateAcquisitionRequestItemActivityInformation(process, this);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
