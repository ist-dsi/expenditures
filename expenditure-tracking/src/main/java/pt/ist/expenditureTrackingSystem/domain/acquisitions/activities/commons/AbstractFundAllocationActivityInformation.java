/*
 * @(#)AbstractFundAllocationActivityInformation.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public abstract class AbstractFundAllocationActivityInformation<T extends PaymentProcess> extends ActivityInformation<T> {

    protected List<FundAllocationBean> beans;

    public AbstractFundAllocationActivityInformation(final T process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity, final boolean takeProcess) {
        super(process, activity);
        beans = new ArrayList<FundAllocationBean>();
        if (takeProcess && process.getCurrentOwner() == null) {
            process.takeProcess();
        }
        generateBeans();
    }

    public void generateBeans() {
        for (Financer financer : getFinancers()) {
            beans.add(new FundAllocationBean(financer));
        }
    }

    public List<FundAllocationBean> getBeans() {
        return beans;
    }

    public void setBeans(List<FundAllocationBean> beans) {
        this.beans = beans;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput();
    }

    public abstract Set<? extends Financer> getFinancers();
}
