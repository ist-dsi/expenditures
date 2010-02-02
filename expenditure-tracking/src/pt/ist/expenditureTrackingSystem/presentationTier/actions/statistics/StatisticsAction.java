package pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
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
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.statistics.AfterTheFactProcessTotalValueStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.ChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessActivityLogStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessStateCountChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessStateTimeAverageChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessStateTimeChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessTotalValueStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcedureProcessActivityTimeChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcedureProcessStateCountChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcedureProcessStateTimeAverageChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcedureProcessStateTimeChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessTotalValueStatistics;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
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

    protected ActionForward generateChart(final HttpServletResponse response, final ChartData chartData, final long t1) {
	OutputStream outputStream = null;
	try {
	    final byte[] image = ChartGenerator.createBarChartImage(chartData);
	    long t2 = System.currentTimeMillis();
	    System.out.println(chartData.getTitle() + ": " + (t2 - t1) + "ms");
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

    public ActionForward simplifiedProcessStatisticsStateChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String year = request.getParameter("year");

	long t1 = System.currentTimeMillis();
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final SimplifiedProcedureProcessStateCountChartData chartData =
	    	new SimplifiedProcedureProcessStateCountChartData(paymentProcessYear);
	chartData.calculateData();
	return generateChart(response, chartData, t1);
    }

    public ActionForward simplifiedProcessStatisticsStateTimeChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String year = request.getParameter("year");

	long t1 = System.currentTimeMillis();
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final SimplifiedProcedureProcessStateTimeChartData chartData =
	    	new SimplifiedProcedureProcessStateTimeChartData(paymentProcessYear);
	chartData.calculateData();
	return generateChart(response, chartData, t1);
    }

    public ActionForward simplifiedProcessStatisticsStateTimeAverageChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String year = request.getParameter("year");

	long t1 = System.currentTimeMillis();
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final SimplifiedProcedureProcessStateTimeAverageChartData chartData =
	    	new SimplifiedProcedureProcessStateTimeAverageChartData(paymentProcessYear);
	chartData.calculateData();
	return generateChart(response, chartData, t1);
    }

    public ActionForward simplifiedProcessStatisticsActivityTimeChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String year = request.getParameter("year");

	long t1 = System.currentTimeMillis();
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final SimplifiedProcedureProcessActivityTimeChartData chartData =
	    	new SimplifiedProcedureProcessActivityTimeChartData(paymentProcessYear);
	chartData.calculateData();
	return generateChart(response, chartData, t1);
    }
    
    public ActionForward simplifiedProcessStatistics(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	final Integer year = Integer.valueOf((String) request.getParameter("year"));
	return streamSpreadsheet(response, "simplifiedProcedure", simplifiedProcessStatistics(year), year);
    }

    private Spreadsheet simplifiedProcessStatistics(final Integer year) {
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final SimplifiedProcedureProcessStateCountChartData countData = new SimplifiedProcedureProcessStateCountChartData(paymentProcessYear);
	countData.calculateData();
	final SimplifiedProcedureProcessStateTimeChartData medianData = new SimplifiedProcedureProcessStateTimeChartData(paymentProcessYear);
	medianData.calculateData();
	final SimplifiedProcedureProcessStateTimeAverageChartData averageData = new SimplifiedProcedureProcessStateTimeAverageChartData(paymentProcessYear);
	averageData.calculateData();
	final SortedMap<Object, BigDecimal> sumMap = (SortedMap) countData.getResults(Operation.SUM);
	final SortedMap<Object, BigDecimal> averageMap = (SortedMap) averageData.getResults(Operation.AVERAGE);
	final SortedMap<Object, BigDecimal> medianMap = (SortedMap) medianData.getResults(Operation.MEDIAN);
	return generateSpreadSheet(sumMap, averageMap, medianMap);
    }

    public ActionForward refundProcessStatistics(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	final Integer year = Integer.valueOf((String) request.getParameter("year"));
	return streamSpreadsheet(response, "refund", refundProcessStatistics(year), year);
    }

    private Spreadsheet refundProcessStatistics(final Integer year) {
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final RefundProcessStateCountChartData countData = new RefundProcessStateCountChartData(paymentProcessYear);
	countData.calculateData();
	final RefundProcessStateTimeChartData medianData = new RefundProcessStateTimeChartData(paymentProcessYear);
	medianData.calculateData();
	final RefundProcessStateTimeAverageChartData averageData = new RefundProcessStateTimeAverageChartData(paymentProcessYear);
	averageData.calculateData();
	final SortedMap<Object, BigDecimal> sumMap = (SortedMap) countData.getResults(Operation.SUM);
	final SortedMap<Object, BigDecimal> averageMap = (SortedMap) averageData.getResults(Operation.AVERAGE);
	final SortedMap<Object, BigDecimal> medianMap = (SortedMap) medianData.getResults(Operation.MEDIAN);
	return generateSpreadSheet(sumMap, averageMap, medianMap);
    }

    private Spreadsheet generateSpreadSheet(final SortedMap<Object, BigDecimal> sumMap,
	    final SortedMap<Object, BigDecimal> medianMap,
	    final SortedMap<Object, BigDecimal> averageMap) {
	final Spreadsheet spreadsheet = new Spreadsheet("Estatísticas");
	spreadsheet.setHeader("Estado");
	spreadsheet.setHeader("Soma");
	spreadsheet.setHeader("Mediana Tempo (em dias)");
	spreadsheet.setHeader("Média Tempo (em dias)");
	final SortedSet<Object> types = new TreeSet<Object>();
	types.addAll(sumMap.keySet());
	types.addAll(medianMap.keySet());
	types.addAll(averageMap.keySet());
	BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);
	for (final Object type : types) {
	    final BigDecimal sum = sumMap.get(type);
	    final BigDecimal median = medianMap.get(type);
	    final BigDecimal average = averageMap.get(type);

	    final Row row = spreadsheet.addRow();
	    row.setCell(((IPresentableEnum) type).getLocalizedName());
	    row.setCell(sum == null ? "0" : sum.toString());
	    row.setCell(median == null ? "" : median.divide(DAYS_CONST, 2, BigDecimal.ROUND_HALF_UP).toString());
	    row.setCell(average == null ? "" : average.divide(DAYS_CONST, 2, BigDecimal.ROUND_HALF_UP).toString());
	}
	return spreadsheet;
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

	long t1 = System.currentTimeMillis();
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final RefundProcessStateCountChartData chartData = new RefundProcessStateCountChartData(paymentProcessYear);
	chartData.calculateData();
	return generateChart(response, chartData, t1);
    }

    public ActionForward refundProcessStatisticsStateTimeChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String year = request.getParameter("year");

	long t1 = System.currentTimeMillis();
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final RefundProcessStateTimeChartData chartData = new RefundProcessStateTimeChartData(paymentProcessYear);
	chartData.calculateData();
	return generateChart(response, chartData, t1);
    }

    public ActionForward refundProcessStatisticsStateTimeAverageChart(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String year = request.getParameter("year");

	long t1 = System.currentTimeMillis();
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(year));
	final RefundProcessStateTimeAverageChartData chartData = new RefundProcessStateTimeAverageChartData(paymentProcessYear);
	chartData.calculateData();
	return generateChart(response, chartData, t1);
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
