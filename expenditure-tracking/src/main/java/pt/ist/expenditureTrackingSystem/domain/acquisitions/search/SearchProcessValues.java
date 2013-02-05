/*
 * @(#)SearchProcessValues.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.search;

import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

/**
 * 
 * @author Luis Cruz
 * 
 */
public enum SearchProcessValues implements IPresentableEnum {

    RS5000(SimplifiedProcedureProcess.class, ProcessClassification.CCP), CT1000(SimplifiedProcedureProcess.class,
            ProcessClassification.CT10000), CT75000(SimplifiedProcedureProcess.class, ProcessClassification.CT75000),
    ACQUISITIONS(SimplifiedProcedureProcess.class, null), REFUND(RefundProcess.class, null);

    private Class<? extends PaymentProcess> searchClass;
    private ProcessClassification searchClassification;

    private SearchProcessValues(Class<? extends PaymentProcess> searchClass, ProcessClassification searchClassification) {
        this.searchClass = searchClass;
        this.searchClassification = searchClassification;
    }

    @Override
    public String getLocalizedName() {
        return (searchClassification != null) ? searchClassification.getLocalizedName() : BundleUtil.getStringFromResourceBundle(
                "resources/ExpenditureResources", "label.search." + searchClass.getSimpleName() + ".description");
    }

    public Class<? extends PaymentProcess> getSearchClass() {
        return searchClass;
    }

    public ProcessClassification getSearchClassification() {
        return searchClassification;
    }

}
