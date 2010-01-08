package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.servlets.json.JsonObject;

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
