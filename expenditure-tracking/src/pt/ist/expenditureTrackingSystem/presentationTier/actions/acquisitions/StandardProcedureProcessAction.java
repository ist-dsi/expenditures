package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.standard.StandardProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/acquisitionStandardProcedureProcess")
public class StandardProcedureProcessAction extends RegularAcquisitionProcessAction {

    @Override
    protected String getSelectUnitToAddForwardUrl() {
	return "/acquisitions/selectPayingUnitToAdd.jsp";
    }

    @Override
    protected String getRemovePayingUnitsForwardUrl() {
	return "/acquisitions/removePayingUnits.jsp";
    }

    @Override
    protected String getAssignUnitItemForwardUrl() {
	return "/acquisitions/assignUnitItem.jsp";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected StandardProcedureProcess getProcess(HttpServletRequest request) {
	StandardProcedureProcess process = getDomainObject(request, "acquisitionProcessOid");
	if (process == null) {
	    process = (StandardProcedureProcess) super.getProcess(request);
	}
	return process;
    }

    public ActionForward prepareCreateAcquisitionStandardProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

////	CreateAcquisitionProcessBean acquisitionProcessBean = new CreateAcquisitionProcessBean();
//	acquisitionProcessBean.setSuppliers(Collections.EMPTY_LIST);
//	request.setAttribute("acquisitionProcessBean", acquisitionProcessBean);
//	return forward(request, "/acquisitions/standardProcess/createStandardAcquisitionProcess.jsp");
	return null;
    }

    public ActionForward addSupplierInCreationPostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateAcquisitionProcessBean acquisitionProcessBean = getRenderedObject("bean");
	Supplier supplierToAdd = acquisitionProcessBean.getSupplierToAdd();
	acquisitionProcessBean.addSupplierToList(supplierToAdd);
	acquisitionProcessBean.setSupplierToAdd(null);

	RenderUtils.invalidateViewState("bean");
	request.setAttribute("acquisitionProcessBean", acquisitionProcessBean);
	return forward(request, "/acquisitions/standardProcess/createStandardAcquisitionProcess.jsp");
    }

    public ActionForward removeSupplierInCreationPostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	String index = request.getParameter("index");
	CreateAcquisitionProcessBean acquisitionProcessBean = getRenderedObject("bean-" + index);
	acquisitionProcessBean.removeSupplierFromList(Integer.valueOf(index).intValue());

	request.setAttribute("acquisitionProcessBean", acquisitionProcessBean);
	return forward(request, "/acquisitions/standardProcess/createStandardAcquisitionProcess.jsp");
    }

    public ActionForward createNewAquisitionStandardProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateAcquisitionProcessBean createAcquisitionProcessBean = getRenderedObject("acquisitionProcessBean");
	createAcquisitionProcessBean.setRequester(getLoggedPerson());
	StandardProcedureProcess acquisitionProcess = StandardProcedureProcess
		.createNewAcquisitionProcess(createAcquisitionProcessBean);

	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    @Override
    protected Class<? extends AcquisitionProcess> getProcessClass() {
	return StandardProcedureProcess.class;
    }
}
