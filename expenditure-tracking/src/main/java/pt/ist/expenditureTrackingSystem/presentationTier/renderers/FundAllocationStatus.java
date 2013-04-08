/*
 * @(#)FundAllocationStatus.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class FundAllocationStatus extends OutputRenderer {

    private String onClass;
    private String offClass;
    private String middleClass;

    public String getMiddleClass() {
        return middleClass;
    }

    public void setMiddleClass(String middleClass) {
        this.middleClass = middleClass;
    }

    private String state1;
    private String state2;
    private String state3;
    private String state4;

    public String getState1() {
        return state1;
    }

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public String getState2() {
        return state2;
    }

    public void setState2(String state2) {
        this.state2 = state2;
    }

    public String getState3() {
        return state3;
    }

    public void setState3(String state3) {
        this.state3 = state3;
    }

    public String getState4() {
        return state4;
    }

    public void setState4(String start4) {
        this.state4 = start4;
    }

    public void setOnClass(String onClass) {
        this.onClass = onClass;
    }

    public String getOnClass() {
        return onClass;
    }

    public void setOffClass(String offClass) {
        this.offClass = offClass;
    }

    public String getOffClass() {
        return offClass;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                // AcquisitionRequest request = (AcquisitionRequest) object;
                RequestWithPayment request = (RequestWithPayment) object;

                HtmlBlockContainer container = new HtmlBlockContainer();

                boolean hasProjectFinancers = request.hasAnyProjectFinancers();

                if (hasProjectFinancers) {
                    HtmlInlineContainer inlineStatus1 = new HtmlInlineContainer();
                    inlineStatus1.addChild(new HtmlText(getState1()));
                    inlineStatus1.setClasses(request.hasAllocatedFundsForAllProjectFinancers() ? getOnClass() : getOffClass());
                    container.addChild(inlineStatus1);
                }
                HtmlInlineContainer inlineStatus2 = new HtmlInlineContainer();
                inlineStatus2.addChild(new HtmlText(getState2()));
                inlineStatus2.setClasses(request.hasAllFundAllocationId() ? getOnClass() : getOffClass());
                container.addChild(inlineStatus2);

                if (hasProjectFinancers) {
                    HtmlInlineContainer inlineStatus3 = new HtmlInlineContainer();
                    inlineStatus3.addChild(new HtmlText(getState3()));
                    boolean hasAllocatedFundsPermanentlyForAllProjectFinancers =
                            request.hasAllocatedFundsPermanentlyForAllProjectFinancers();
                    if (hasAllocatedFundsPermanentlyForAllProjectFinancers) {
                        if (request instanceof AcquisitionRequest) {
                            AcquisitionProcessStateType stateType =
                                    ((AcquisitionRequest) request).getProcess().getAcquisitionProcessStateType();

                            if (stateType != AcquisitionProcessStateType.ACQUISITION_PROCESSED) {
                                inlineStatus3.setClasses(getOnClass());
                            } else {
                                inlineStatus3.setClasses(getMiddleClass());
                            }
                        } else {
                            inlineStatus3.setClasses(getOnClass());
                        }
                    } else {
                        inlineStatus3.setClasses(getOffClass());
                    }

                    container.addChild(inlineStatus3);
                }
                HtmlInlineContainer inlineStatus4 = new HtmlInlineContainer();
                inlineStatus4.addChild(new HtmlText(getState4()));
                boolean hasAllEffectiveFundAllocationId = request.hasAllEffectiveFundAllocationId();
                if (hasAllEffectiveFundAllocationId) {
                    if (request instanceof AcquisitionRequest) {
                        AcquisitionProcessStateType stateType =
                                ((AcquisitionRequest) request).getProcess().getAcquisitionProcessStateType();
                        if (stateType != AcquisitionProcessStateType.ACQUISITION_PROCESSED) {
                            inlineStatus4.setClasses(getOnClass());
                        } else {
                            inlineStatus4.setClasses(getMiddleClass());
                        }
                    } else {
                        inlineStatus4.setClasses(getOnClass());
                    }
                } else {
                    inlineStatus4.setClasses(getOffClass());
                }

                container.addChild(inlineStatus4);
                return container;
            }

        };
    }

}
