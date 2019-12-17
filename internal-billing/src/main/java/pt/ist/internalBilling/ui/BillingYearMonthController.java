package pt.ist.internalBilling.ui;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.internalBilling.domain.Billable;
import pt.ist.internalBilling.domain.BillableService;
import pt.ist.internalBilling.domain.BillableTransaction;
import pt.ist.internalBilling.domain.BillingYearMonth;
import pt.ist.internalBilling.domain.InternalBillingService;
import pt.ist.internalBilling.util.Utils;

@SpringFunctionality(app = InternalBillingController.class, title = "title.internalBilling.billingYearMonth")
@RequestMapping("/internalBilling/billingYearMonth")
public class BillingYearMonthController {

    private final MessageSource messageSource;

    @Autowired
    public BillingYearMonthController(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {
        final JsonArray billingYearMonth = InternalBillingService.billingYearMonthStream()
                .map(s -> Utils.toJson(this::billingYearMonth, s)).collect(Utils.toJsonArray());
        model.addAttribute("billingYearMonth", billingYearMonth);
        return "internalBilling/billingYearMonths";
    }

    private void billingYearMonth(final JsonObject jo, final BillingYearMonth s) {
        jo.addProperty("id", s.getExternalId());
        jo.addProperty("year", s.getYear());
        jo.addProperty("month", s.getMonth());
        final String yearMonthStatus = s.getYearMonthStatus().name();
        jo.addProperty("status", s.getYearMonthStatus().name());
        jo.addProperty("billingYearMonthStatusDescription", messageSource
                .getMessage("label.internalBilling.billingYearMonth.status." + yearMonthStatus, null, I18N.getLocale()));
        jo.addProperty("reportLink", "/internalBilling/billingYearMonth/report?year=" + s.getYear() + "&month=" + s.getMonth());

    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String DownLoadReport(final Model model, final HttpServletResponse response, @RequestParam final int year,
            @RequestParam final int month) throws IOException {

        final Set<Unit> units = ExpenditureTrackingSystem.getInstance().getUnitsSet();

        final Spreadsheet spreadsheet = new Spreadsheet("Report");
        for (BillableService service : InternalBillingService.getInstance().getBillableServiceSet()) {
            final Map<Unit, Money> unitReports = new TreeMap<>(Unit.COMPARATOR_BY_PRESENTATION_NAME);
            for (Unit unit : units) {
                Stream<BillableTransaction> transaction =
                        unit.getBillableSet().stream()
                            .filter(b -> b.getBillableService() == service)
                            .flatMap(b -> b.getBillableTransactionSet().stream())
                            .filter(tx -> match(year, month, tx.getTxDate())).collect(Collectors.toSet()).stream();
                process(unit, unitReports, transaction);
            }
            unitReports.forEach((r, m) -> {
                final Row row = spreadsheet.addRow();
                row.setCell("Service", service.getTitle());
                row.setCell("Unit Code", r.getUnit().getAcronym());
                row.setCell("Unit", r.getUnit().getPresentationName());
                row.setCell("Total", m.toFormatStringWithoutCurrency());
            });
        }
        
        final String filename = "Printer_" + year + "_" + month;
        return download(model, response, spreadsheet, filename);

    }

    private void process(Unit unit, Map<Unit, Money> unitReports, Stream<BillableTransaction> transaction) {
        final Unit u = unit;
        transaction.forEach(tx -> {
            unitReports.merge(u, tx.getTxValue(), (v, nv) -> v.add(nv));

        });

    }

    private boolean match(final int year, final int month, final DateTime txDate) {

        return txDate.getYear() == year && txDate.getMonthOfYear() == month;
    }

    @RequestMapping(value = "/closingYearMonth", method = RequestMethod.POST)
    public String closeYearMonth(final Model model, @RequestParam final int year, @RequestParam final int month) {
        if (BillingYearMonth.isAuthorized()) {
            BillingYearMonth.create(year, month);
        }
        return "redirect:/internalBilling/billingYearMonth";
    }

    public String download(final Model model, final HttpServletResponse response, final Spreadsheet spreadsheet,
            final String filename) {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "filename=" + filename + ".xls");
        try {
            spreadsheet.exportToXLSSheet(response.getOutputStream());
            response.flushBuffer();
        } catch (final IOException e) {
            throw new Error(e);
        }
        return null;

    }

    private String hearder(final String key) {
        return messageSource.getMessage(key, null, I18N.getLocale());
    }
}
