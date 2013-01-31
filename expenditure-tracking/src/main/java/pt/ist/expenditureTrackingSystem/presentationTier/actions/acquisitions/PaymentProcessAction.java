/*
 * @(#)PaymentProcessAction.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.servlets.json.JsonObject;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public abstract class PaymentProcessAction extends BaseAction {

	public ActionForward calculateShareValuesViaAjax(final ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) throws IOException {

		Money money = new Money(request.getParameter("money"));
		String requestorsIds = request.getParameter("requestors");
		String[] ids = requestorsIds.split(",");

		Money[] allocate = money.allocate(ids.length);
		List<JsonObject> sharesResult = new ArrayList<JsonObject>();
		for (int i = 0; i < allocate.length; i++) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addAttribute("id", ids[i]);
			jsonObject.addAttribute("share", allocate[i].getValue().toPlainString());
			sharesResult.add(jsonObject);
		}
		writeJsonReply(response, sharesResult);
		return null;
	}

}
