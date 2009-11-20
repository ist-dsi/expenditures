package pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.util.Money;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.statistics.AfterTheFactProcessTotalValueStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessActivityLogStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessTotalValueStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessActivityLogStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessTotalValueStatistics;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(path = "/statistics")
public class StatisticsAction extends ContextBaseAction {

    public ActionForward showStatistics(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return forward(request, "/statistics/showStatistics.jsp");
    }

    public ActionForward showSimplifiedProcessStatistics(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	YearBean yearBean = getRenderedObject();
	if (yearBean == null) {
	    yearBean = new YearBean();
	}
	request.setAttribute("yearBean", yearBean);

	final SimplifiedProcessStatistics simplifiedProcessStatistics = SimplifiedProcessStatistics.create(yearBean.getYear());
	request.setAttribute("simplifiedProcessStatistics", simplifiedProcessStatistics);

	return forward(request, "/statistics/showStatisticsSimplifiedProcess.jsp");
    }

    public ActionForward simplifiedProcessStatisticsChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
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

    public ActionForward simplifiedProcessStatisticsTimeChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String year = request.getParameter("year");

	final SimplifiedProcessActivityLogStatistics simplifiedProcessActivityLogStatistics = SimplifiedProcessActivityLogStatistics
		.create(new Integer(year));

	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator
		    .simplifiedProcessStatisticsActivityTimeImage(simplifiedProcessActivityLogStatistics);
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

    public ActionForward simplifiedProcessStatisticsActivityTimeChartForProcess(final ActionMapping mapping,
	    final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
	final SimplifiedProcedureProcess simplifiedProcedureProcess = getDomainObject(request, "processId");

	final SimplifiedProcessActivityLogStatistics simplifiedProcessActivityLogStatistics = SimplifiedProcessActivityLogStatistics
		.create(simplifiedProcedureProcess);

	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator
		    .simplifiedProcessStatisticsActivityTimeImage(simplifiedProcessActivityLogStatistics);
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

    public ActionForward showRefundProcessStatistics(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	YearBean yearBean = getRenderedObject();
	if (yearBean == null) {
	    yearBean = new YearBean();
	}
	request.setAttribute("yearBean", yearBean);

	final RefundProcessStatistics refundProcessStatistics = RefundProcessStatistics.create(yearBean.getYear());
	request.setAttribute("refundProcessStatistics", refundProcessStatistics);

	return forward(request, "/statistics/showStatisticsRefundProcess.jsp");
    }

    public ActionForward refundProcessStatisticsChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
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

    public ActionForward refundProcessStatisticsTimeChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String year = request.getParameter("year");

	final RefundProcessActivityLogStatistics refundProcessActivityLogStatistics = RefundProcessActivityLogStatistics
		.create(new Integer(year));

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

    public ActionForward refundProcessStatisticsActivityTimeChartForProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final RefundProcess refundProcess = getDomainObject(request, "processId");

	final RefundProcessActivityLogStatistics refundProcessActivityLogStatistics = RefundProcessActivityLogStatistics
		.create(refundProcess);

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

    public ActionForward showStatisticsReports(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	YearBean yearBean = getRenderedObject();
	if (yearBean == null) {
	    yearBean = new YearBean();
	}
	request.setAttribute("yearBean", yearBean);

	return forward(request, "/statistics/showStatisticsReports.jsp");
    }

    public ActionForward downloadStatisticsByCPV(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {

	final Integer year = Integer.valueOf((String) getAttribute(request, "year"));
	return streamSpreadsheet(response, "cvp", createStatisticsByCPV(year), year);
    }

    private Spreadsheet createStatisticsByCPV(final Integer year) {
	final Spreadsheet spreadsheet = new Spreadsheet("CPV " + year);
	spreadsheet.setHeader("CPV");
	spreadsheet.setHeader("CPV desc.");
	spreadsheet.setHeader("Montante");
	for (final CPVReference reference : getMyOrg().getExpenditureTrackingSystem().getCPVReferencesSet()) {
	    final Money money = reference.getTotalAmountAllocated(year);
	    if (!money.isZero()) {
		final Row row = spreadsheet.addRow();
		row.setCell(reference.getCode());
		row.setCell(reference.getDescription());
		row.setCell(money.toFormatStringWithoutCurrency());
	    }
	}
	return spreadsheet;
    }

    public ActionForward downloadTotalValuesStatistics(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {

	final Integer year = Integer.valueOf((String) getAttribute(request, "year"));
	return streamSpreadsheet(response, "values", createTotalValuesStatistics(year), year);
    }

    private Spreadsheet createTotalValuesStatistics(final Integer year) {
	final Spreadsheet spreadsheet = new Spreadsheet("Valores por Tipo - " + year);
	spreadsheet.setHeader("Tipo");
	spreadsheet.setHeader("Montante");

	final Map<AcquisitionProcessStateType, Money> values = SimplifiedProcessTotalValueStatistics.create(year)
		.getTotalValuesOfProcessesByAcquisitionProcessStateType();
	for (final Entry<AcquisitionProcessStateType, Money> valueEntry : values.entrySet()) {
	    final Row row = spreadsheet.addRow();
	    row.setCell(valueEntry.getKey().getLocalizedName());
	    row.setCell(valueEntry.getValue().toFormatStringWithoutCurrency());
	}

	return spreadsheet;
    }

    public ActionForward downloadRefundTotalValuesStatistics(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {

	final Integer year = Integer.valueOf((String) getAttribute(request, "year"));
	return streamSpreadsheet(response, "refundValues", createRefundTotalValuesStatistics(year), year);
    }

    private Spreadsheet createRefundTotalValuesStatistics(final Integer year) {
	final Spreadsheet spreadsheet = new Spreadsheet("Valores por Tipo - " + year);
	spreadsheet.setHeader("Tipo");
	spreadsheet.setHeader("Montante");

	final Map<RefundProcessStateType, Money> values = RefundProcessTotalValueStatistics.create(year)
		.getTotalValuesOfProcessesByRefundProcessStateType();
	for (final Entry<RefundProcessStateType, Money> valueEntry : values.entrySet()) {
	    final Row row = spreadsheet.addRow();
	    row.setCell(valueEntry.getKey().getLocalizedName());
	    row.setCell(valueEntry.getValue().toFormatStringWithoutCurrency());
	}

	return spreadsheet;
    }

    private ActionForward streamSpreadsheet(final HttpServletResponse response, final String fileName,
	    final Spreadsheet resultSheet, final Integer year) throws IOException {

	response.setContentType("application/xls ");
	response.setHeader("Content-disposition", "attachment; filename=" + fileName + year + ".xls");

	ServletOutputStream outputStream = response.getOutputStream();

	resultSheet.exportToXLSSheet(outputStream);
	outputStream.flush();
	outputStream.close();

	return null;
    }

    public ActionForward downloadAfterTheFactTotalValuesStatistics(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {

	final Integer year = Integer.valueOf((String) getAttribute(request, "year"));
	return streamSpreadsheet(response, "afterTheFact", createAfterTheFactTotalValuesStatistics(year), year);
    }

    private Spreadsheet createAfterTheFactTotalValuesStatistics(final Integer year) {
	final Spreadsheet spreadsheet = new Spreadsheet("Valores por Tipo - " + year);
	spreadsheet.setHeader("Tipo");
	spreadsheet.setHeader("Montante");

	final Map<AfterTheFactAcquisitionType, Money> values = AfterTheFactProcessTotalValueStatistics.create(year)
		.getTotalValuesOfProcessesByAquisitionProcessStateType();
	for (final Entry<AfterTheFactAcquisitionType, Money> valueEntry : values.entrySet()) {
	    final Row row = spreadsheet.addRow();
	    row.setCell(valueEntry.getKey().getLocalizedName());
	    row.setCell(valueEntry.getValue().toFormatStringWithoutCurrency());
	}

	return spreadsheet;
    }

}
