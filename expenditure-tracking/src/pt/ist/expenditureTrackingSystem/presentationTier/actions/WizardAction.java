package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/wizard")
public class WizardAction extends BaseAction {

    public ActionForward newAcquisitionWizard(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return forward(request, "/acquisitions/creationWizard.jsp");
    }

    public ActionForward afterTheFactOperationsWizard(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return forward(request, "/acquisitions/afterTheFactOperationsWizard.jsp");
    }

}
