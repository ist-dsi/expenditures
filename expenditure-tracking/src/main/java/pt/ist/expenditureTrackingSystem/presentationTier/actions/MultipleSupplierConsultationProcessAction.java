package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import module.workflow.util.WorkflowProcessViewer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions.SearchPaymentProcessesAction;

@StrutsFunctionality(app = SearchPaymentProcessesAction.class, path = "multipleConsultation", titleKey = "title.consultation.process.action")
@Mapping(path = "/multipleConsultation")
@WorkflowProcessViewer(value = { MultipleSupplierConsultationProcess.class })
public class MultipleSupplierConsultationProcessAction extends BaseAction {

    @EntryPoint
    public ActionForward home(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return forward("/consultation/home.jsp");
    }

}
