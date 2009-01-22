package pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessActivityLogStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessActivityLogStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessStatistics;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/statistics")
@Forwards( {
    @Forward(name = "show.statistics", path = "/statistics/showStatistics.jsp"),
    @Forward(name = "show.statistics.simplified.process", path = "/statistics/showStatisticsSimplifiedProcess.jsp"),
    @Forward(name = "show.statistics.refund.process", path = "/statistics/showStatisticsRefundProcess.jsp")
})
public class StatisticsAction extends BaseAction {

    private static final Context CONTEXT = new Context("statistics");

    @Override
    protected Context getContextModule(final HttpServletRequest request) {
	return CONTEXT;
    }

    public ActionForward showStatistics(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return mapping.findForward("show.statistics");
    }

    public ActionForward showSimplifiedProcessStatistics(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	YearBean yearBean = getRenderedObject();
	if (yearBean == null) {
	    yearBean = new YearBean();
	}
	request.setAttribute("yearBean", yearBean);

	final SimplifiedProcessStatistics simplifiedProcessStatistics = SimplifiedProcessStatistics.create(yearBean.getYear());
	request.setAttribute("simplifiedProcessStatistics", simplifiedProcessStatistics);

	return mapping.findForward("show.statistics.simplified.process");
    }

    public ActionForward simplifiedProcessStatisticsChart(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final String year = request.getParameter("year");

	final SimplifiedProcessStatistics simplifiedProcessStatistics = SimplifiedProcessStatistics.create(new Integer(year));

	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator.simplifiedProcessStatisticsImage(simplifiedProcessStatistics);
	    outputStream = response.getOutputStream();
	    response.setContentType("image/jpeg");
	    outputStream.write(image);
	    outputStream.flush();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final RuntimeException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final IOException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (final IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
		try {
		    response.flushBuffer();
		} catch (IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
	    }
	}

	return null;
    }

    public ActionForward simplifiedProcessStatisticsTimeChart(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final String year = request.getParameter("year");

	final SimplifiedProcessActivityLogStatistics simplifiedProcessActivityLogStatistics = SimplifiedProcessActivityLogStatistics.create(new Integer(year));

	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator.simplifiedProcessStatisticsActivityTimeImage(simplifiedProcessActivityLogStatistics);
	    outputStream = response.getOutputStream();
	    response.setContentType("image/jpeg");
	    outputStream.write(image);
	    outputStream.flush();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final RuntimeException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final IOException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (final IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
		try {
		    response.flushBuffer();
		} catch (IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
	    }
	}

	return null;
    }

    public ActionForward simplifiedProcessStatisticsActivityTimeChartForProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final SimplifiedProcedureProcess simplifiedProcedureProcess = getDomainObject(request, "processId");

	final SimplifiedProcessActivityLogStatistics simplifiedProcessActivityLogStatistics = SimplifiedProcessActivityLogStatistics.create(simplifiedProcedureProcess);

	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator.simplifiedProcessStatisticsActivityTimeImage(simplifiedProcessActivityLogStatistics);
	    outputStream = response.getOutputStream();
	    response.setContentType("image/jpeg");
	    outputStream.write(image);
	    outputStream.flush();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final RuntimeException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final IOException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (final IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
		try {
		    response.flushBuffer();
		} catch (IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
	    }
	}

	return null;
    }

    public ActionForward showRefundProcessStatistics(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	YearBean yearBean = getRenderedObject();
	if (yearBean == null) {
	    yearBean = new YearBean();
	}
	request.setAttribute("yearBean", yearBean);

	final RefundProcessStatistics refundProcessStatistics = RefundProcessStatistics.create(yearBean.getYear());
	request.setAttribute("refundProcessStatistics", refundProcessStatistics);

	return mapping.findForward("show.statistics.refund.process");
    }

    public ActionForward refundProcessStatisticsChart(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final String year = request.getParameter("year");

	final RefundProcessStatistics refundProcessStatistics = RefundProcessStatistics.create(new Integer(year));

	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator.refundProcessStatisticsImage(refundProcessStatistics);
	    outputStream = response.getOutputStream();
	    response.setContentType("image/jpeg");
	    outputStream.write(image);
	    outputStream.flush();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final RuntimeException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final IOException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (final IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
		try {
		    response.flushBuffer();
		} catch (IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
	    }
	}

	return null;
    }

    public ActionForward refundProcessStatisticsTimeChart(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final String year = request.getParameter("year");

	final RefundProcessActivityLogStatistics refundProcessActivityLogStatistics = RefundProcessActivityLogStatistics.create(new Integer(year));

	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator.refundProcessStatisticsActivityTimeImage(refundProcessActivityLogStatistics);
	    outputStream = response.getOutputStream();
	    response.setContentType("image/jpeg");
	    outputStream.write(image);
	    outputStream.flush();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final RuntimeException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final IOException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (final IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
		try {
		    response.flushBuffer();
		} catch (IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
	    }
	}

	return null;
    }

    public ActionForward refundProcessStatisticsActivityTimeChartForProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final RefundProcess refundProcess = getDomainObject(request, "processId");

	final RefundProcessActivityLogStatistics refundProcessActivityLogStatistics = RefundProcessActivityLogStatistics.create(refundProcess);

	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator.refundProcessStatisticsActivityTimeImage(refundProcessActivityLogStatistics);
	    outputStream = response.getOutputStream();
	    response.setContentType("image/jpeg");
	    outputStream.write(image);
	    outputStream.flush();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final RuntimeException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} catch (final IOException e) {
	    e.printStackTrace();
	    throw new Error(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (final IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
		try {
		    response.flushBuffer();
		} catch (IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
	    }
	}

	return null;
    }

}
