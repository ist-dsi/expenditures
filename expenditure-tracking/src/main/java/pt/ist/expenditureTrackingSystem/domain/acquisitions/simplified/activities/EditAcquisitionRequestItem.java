/*
 * @(#)EditAcquisitionRequestItem.java
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

import module.finance.util.Address;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class EditAcquisitionRequestItem extends
        WorkflowActivity<RegularAcquisitionProcess, EditAcquisitionRequestItemActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        Person person = user.getExpenditurePerson();
        return isUserProcessOwner(process, user)
                && ((process.getRequestor() == person && process.getAcquisitionProcessState().isInGenesis() && process
                        .getAcquisitionRequest().hasAnyRequestItems()) || (process.isSimplifiedAcquisitionProcess()
                        && ((SimplifiedProcedureProcess) process).getProcessClassification() == ProcessClassification.CT75000
                        && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user) && process
                        .getAcquisitionProcessState().isAuthorized()));
    }

    @Override
    protected void process(EditAcquisitionRequestItemActivityInformation activityInformation) {
        AcquisitionRequest acquisitionRequest = activityInformation.getAcquisitionRequest();
        DeliveryInfo deliveryInfo = activityInformation.getDeliveryInfo();

        String recipient;
        Address address;
        String phone;
        String email;

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

        activityInformation.getItem().edit(acquisitionRequest, activityInformation.getDescription(),
                activityInformation.getQuantity(), activityInformation.getUnitValue(), activityInformation.getVatValue(),
                activityInformation.getAdditionalCostValue(), activityInformation.getProposalReference(),
                activityInformation.getCPVReference(), recipient, address, phone, email, activityInformation.getClassification());
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
        return new EditAcquisitionRequestItemActivityInformation(process, this);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
