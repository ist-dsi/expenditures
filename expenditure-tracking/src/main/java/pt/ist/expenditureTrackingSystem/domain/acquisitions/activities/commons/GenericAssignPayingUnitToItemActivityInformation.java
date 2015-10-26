/*
 * @(#)GenericAssignPayingUnitToItemActivityInformation.java
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

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class GenericAssignPayingUnitToItemActivityInformation<P extends PaymentProcess> extends ActivityInformation<P> {

    protected RequestItem item;
    protected List<UnitItemBean> beans;

    public GenericAssignPayingUnitToItemActivityInformation(P process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
        this.beans = new ArrayList<UnitItemBean>();
    }

    public RequestItem getItem() {
        return item;
    }

    public void setItem(RequestItem item) {
        this.item = item;
        getProcess().getPayingUnitStream().forEach(u -> this.beans.add(new UnitItemBean(u, item)));
    }

    public List<UnitItemBean> getBeans() {
        return beans;
    }

    public void setBeans(List<UnitItemBean> beans) {
        this.beans = beans;
    }

    @Override
    public boolean hasAllneededInfo() {
        return getItem() != null && isForwardedFromInput();
    }
}
