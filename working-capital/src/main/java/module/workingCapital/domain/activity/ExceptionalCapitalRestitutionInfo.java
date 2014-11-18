/*
 * @(#)ExceptionalCapitalRestitutionInfo.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain.activity;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workingCapital.domain.WorkingCapitalProcess;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author João Neves
 * 
 */
public class ExceptionalCapitalRestitutionInfo extends ActivityInformation<WorkingCapitalProcess> {

    private Money value;
    private String caseDescription;

    public ExceptionalCapitalRestitutionInfo(WorkingCapitalProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    @Override
    public boolean hasAllneededInfo() {
        return super.hasAllneededInfo() && isForwardedFromInput() && value.isPositive() && !StringUtils.isEmpty(caseDescription);
    }
}
