package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateProjectFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.FundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.ProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.dto.DateIntervalBean;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/acquisitionProcess")
public class RegularAcquisitionProcessAction extends PaymentProcessAction {

    public ActionForward checkFundAllocations(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	IViewState viewState = RenderUtils.getViewState("dateSelection");

	DateIntervalBean bean = viewState == null ? new DateIntervalBean() : (DateIntervalBean) viewState.getMetaObject()
		.getObject();

	if (viewState == null) {
	    LocalDate today = new LocalDate();
	    bean.setBegin(today);
	    bean.setEnd(today);
	}

	DateTime begin = bean.getBegin().toDateTimeAtStartOfDay();
	DateTime end = bean.getEnd().plusDays(1).toDateTimeAtStartOfDay();

	List<AcquisitionProcess> processes = new ArrayList<AcquisitionProcess>();

	for (AcquisitionProcess process : GenericProcess.getAllProcesses(AcquisitionProcess.class)) {
	    if (!process.getExecutionLogs(begin, end, FundAllocation.class, ProjectFundAllocation.class,
		    AllocateFundsPermanently.class, AllocateProjectFundsPermanently.class).isEmpty()) {
		processes.add(process);
	    }
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("processes", processes);
	request.setAttribute("bean", bean);

	return forward(request, "/acquisitions/viewFundAllocations.jsp");
    }

}
