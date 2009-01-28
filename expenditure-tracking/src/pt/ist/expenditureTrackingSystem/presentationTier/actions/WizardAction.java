package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/wizard")
@Forwards( { @Forward(name = "create.acquisition.wizard", path = "/acquisitions/creationWizard.jsp"),
    @Forward(name = "afterTheFact.operations.wizard", path = "/acquisitions/afterTheFactOperationsWizard.jsp")
})
public class WizardAction extends BaseAction {

    private static final Context CONTEXT = new Context("acquisitions");

    @Override
    protected Context getContextModule(final HttpServletRequest request) {
	return CONTEXT;
    }

    public ActionForward newAcquisitionWizard(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return mapping.findForward("create.acquisition.wizard");
    }

    public ActionForward afterTheFactOperationsWizard(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	return mapping.findForward("afterTheFact.operations.wizard");
    }


}
