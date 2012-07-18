/*
 * @(#)PayAcquisitionActivityInformation.java
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

import java.util.ArrayList;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.PaymentReferenceBean;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class PayAcquisitionActivityInformation<P extends PaymentProcess>
	extends ActivityInformation<P> {

    String paymentReference;
    protected List<PaymentReferenceBean> beans;

    public PayAcquisitionActivityInformation(P process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
	beans = new ArrayList<PaymentReferenceBean>();
	for (final Financer financer : process.getFinancersWithFundsAllocated()) {
	    final User currentUser = UserView.getCurrentUser();
	    if (financer.isTreasuryMember(currentUser.getExpenditurePerson())) {
		beans.add(new PaymentReferenceBean(financer));
	    }
	}
    }

    public String getPaymentReference() {
	return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
	this.paymentReference = paymentReference;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getPaymentReference() != null /* && hasPaymentRefences() */ ;
    }

    private boolean hasPaymentRefences() {
	for (final PaymentReferenceBean referenceBean : beans) {
	    if (referenceBean.getDiaryNumber() != null && !referenceBean.getDiaryNumber().isEmpty()
		    && referenceBean.getTransactionNumber() != null && !referenceBean.getTransactionNumber().isEmpty()) {
		return true;
	    }
	}
	return false;
    }

    public List<PaymentReferenceBean> getBeans() {
        return beans;
    }

    public void setBeans(List<PaymentReferenceBean> beans) {
        this.beans = beans;
    }

}
