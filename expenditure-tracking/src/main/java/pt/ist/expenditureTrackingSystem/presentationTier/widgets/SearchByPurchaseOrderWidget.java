/*
 * @(#)SearchByPurchaseOrderWidget.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.DashboardWidget;
import module.dashBoard.widgets.WidgetController;
import module.workflow.presentationTier.actions.ProcessManagement;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.SearchByPurchaseOrderBean;
import pt.ist.expenditureTrackingSystem.domain.util.ExpenditureTrackingPanelPredicate;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * 
 * @author Jorge Goulart
 * 
 */
@DashboardWidget(nameBundle = "resources.ExpenditureResources", nameKey = "title.widget.searchByPurchaseOrder",
        aditionPredicate = ExpenditureTrackingPanelPredicate.class)
public class SearchByPurchaseOrderWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
        request.setAttribute("searchBean", new SearchByPurchaseOrderBean());
    }

    @Override
    public ActionForward doSubmit(WidgetRequest request) {
        SearchByPurchaseOrderBean searchBean = getRenderedObject("searchByPurchaseOrderBean");
        PaymentProcess process = searchBean.search();

        try {
            JsonArray jsonProcesses = new JsonArray();
            if (process != null) {
                jsonProcesses.add(this.convertProcessToJson(process, request));
            }
            String write = jsonProcesses.toString();

            HttpServletResponse response = request.getResponse();
            response.setContentType("text");
            ServletOutputStream stream = response.getOutputStream();

            byte[] writeBytes = write.getBytes();
            response.setContentLength(writeBytes.length);
            stream.write(writeBytes);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected JsonObject convertProcessToJson(PaymentProcess process, WidgetRequest request) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", process.getAcquisitionProcessId());
        jsonObj.addProperty("name", process.getSuppliersDescription());
        jsonObj.addProperty("url", request.getContextPath()
                + request.injectChecksumIn(ProcessManagement.workflowManagementURL + process.getExternalId()));
        return jsonObj;
    }

    protected <T extends Object> T getRenderedObject(final String id) {
        final IViewState viewState = RenderUtils.getViewState(id);
        return (T) getRenderedObject(viewState);
    }

    protected <T extends Object> T getRenderedObject(final IViewState viewState) {
        if (viewState != null) {
            MetaObject metaObject = viewState.getMetaObject();
            if (metaObject != null) {
                return (T) metaObject.getObject();
            }
        }
        return null;
    }

    @Override
    public String getWidgetDescription() {
        return BundleUtil.getString("resources/ExpenditureResources", "widget.description.SearchByPurchaseOrderWidget");
    }
}
