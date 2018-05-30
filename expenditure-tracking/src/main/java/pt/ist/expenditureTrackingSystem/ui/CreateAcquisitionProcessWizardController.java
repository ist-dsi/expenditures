package pt.ist.expenditureTrackingSystem.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

@SpringApplication(group = "logged", path = "expenditure-tracking",
        title = "acquisitionCreationWizard.title.newAcquisitionOrRefund", hint = "expenditure-tracking")
@SpringFunctionality(app = CreateAcquisitionProcessWizardController.class,
        title = "acquisitionCreationWizard.title.newAcquisitionOrRefund")
@RequestMapping("/expenditure/acquisitons/create")
public class CreateAcquisitionProcessWizardController {

    private static final int MAX_AUTOCOMPLETE_SUPPLIER_COUNT = 5;

    @RequestMapping(method = RequestMethod.GET)
    public String selectSupplier(final Model model) throws Exception {
        model.addAttribute("createSupplierUrl", ExpenditureTrackingSystem.getInstance().getCreateSupplierUrl());
        model.addAttribute("createSupplierLabel", ExpenditureTrackingSystem.getInstance().getCreateSupplierLabel());
        return "expenditure-tracking/createAcquisitionProcessWizardSelectSupplier";
    }

    @RequestMapping(value = "/selectType", method = RequestMethod.GET)
    public String selectType(@RequestParam(required = false, value = "supplier") String supplierNif, final Model model)
            throws Exception {
        final Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(supplierNif);
        if (supplier == null) {
            return "redirect:/expenditure/acquisitons/create";
        }

        model.addAttribute("supplier", supplier);

        final boolean suggestSimplified = supplier.getSoftTotalAllocated().isLessThan(supplier.getSupplierLimit());
        final boolean suggestRefund = suggestSimplified;
        final boolean suggestConsultation =
                supplier.getTotalAllocatedForMultipleSupplierConsultation().isLessThan(supplier.getMultipleSupplierLimit());

        model.addAttribute("suggestSimplified", suggestSimplified);
        model.addAttribute("suggestRefund", suggestRefund);
        model.addAttribute("suggestConsultation", suggestConsultation);

        return "expenditure-tracking/createAcquisitionProcessWizardSelectType";
    }

    @RequestMapping(value = "/acquisition", method = RequestMethod.GET)
    public String acquisition(@RequestParam(required = false, value = "supplier") String supplierNif, final Model model)
            throws Exception {
        return redirect("acquisitionSimplifiedProcedureProcess", "prepareCreateAcquisitionProcessFromWizard", "supplier",
                supplierNif);
    }

    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    public String refund(@RequestParam(required = false, value = "supplier") String supplierNif, final Model model)
            throws Exception {
        return redirect("acquisitionRefundProcess", "prepareCreateRefundProcessUnderCCP", "supplier", supplierNif);
    }

    @RequestMapping(value = "/consultation", method = RequestMethod.GET)
    public String consultation(@RequestParam(required = false, value = "supplier") String supplierNif, final Model model)
            throws Exception {
        return "redirect:/consultation/prepareCreateNewMultipleSupplierConsultationProcess?nif=" + supplierNif;
    }

    @RequestMapping(value = "/isRefund", method = RequestMethod.GET)
    public String isRefund(final Model model) throws Exception {
        return "expenditure-tracking/createAcquisitionProcessWizardIsRefund";
    }

    @RequestMapping(value = "/isRefund", method = RequestMethod.POST)
    public String isRefundPost(@RequestParam(required = false, value = "refund") boolean refund, final Model model)
            throws Exception {

        if (refund) {
            return redirect("acquisitionRefundProcess", "prepareCreateRefundProcessUnderCCP");
        }
        return "redirect:/expenditure/acquisitons/create/info";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(final Model model) throws Exception {
        return "expenditure-tracking/createAcquisitionProcessWizardInfo";
    }

    @RequestMapping(value = "/supplier/json", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public @ResponseBody String supplier(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();
        try {
            final String trimmedValue = URLDecoder.decode(term, "UTF-8").trim();
            final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");
            findSuppliers(result, input, term);
            return result.toString();
        } catch (final UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }

    private void findSuppliers(JsonArray result, String[] input, String term) {
        Bennu.getInstance().getSuppliersSet().stream()
            .filter(supplier -> supplierHasMatch(supplier, term, input))
            .filter(supplier -> supplier.getSupplierLimit().isPositive())
            .sorted(Comparator.comparing(u -> u.getName()))
            .limit(MAX_AUTOCOMPLETE_SUPPLIER_COUNT)
            .forEach(u -> addToJson(result, u));
    }

    private boolean supplierHasMatch(Supplier supplier, String term, final String[] input) {
        final String nif = trim(supplier.getFiscalIdentificationCode());
        if (nif == null || nif.isEmpty()) {
            return false;
        }

        if (nif.startsWith(term)) {
            return true;
        } else {
            final String name = StringNormalizer.normalize(supplier.getName());
            for (final String namePart : input) {
                if (name.indexOf(namePart) == -1) {
                    return false;
                }
            }
            return true;
        }
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private void addToJson(JsonArray result, Supplier s) {
        final JsonObject o = new JsonObject();

        //o.addProperty("id", s.getExternalId());
        o.addProperty("nif", s.getFiscalIdentificationCode());
        o.addProperty("name", s.getPresentationName());

        o.addProperty("totalAllocated", s.getSoftTotalAllocated().getRoundedValue());
        o.addProperty("supplierLimit", s.getSupplierLimit().getRoundedValue());
        o.addProperty("multiTotalAllocated", s.getTotalAllocatedForMultipleSupplierConsultation().getRoundedValue());
        o.addProperty("multiSupplierLimit", s.getMultipleSupplierLimit().getRoundedValue());

        JsonObject formatted = new JsonObject();
        formatted.addProperty("totalAllocated", s.getSoftTotalAllocated().toFormatString());
        formatted.addProperty("supplierLimit", s.getSupplierLimit().toFormatString());
        formatted.addProperty("multiTotalAllocated", s.getTotalAllocatedForMultipleSupplierConsultation().toFormatString());
        formatted.addProperty("multiSupplierLimit", s.getMultipleSupplierLimit().toFormatString());

        o.add("formatted", formatted);

        result.add(o);
    }

    private String redirect(final String action, final String method) {
        return redirect(action, method, null, null);
    }

    private String redirect(final String action, final String method, final String param, final String value) {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String contextPath = request.getContextPath();
        String path = "/" + action + ".do?method=" + method;
        if (param != null && value != null) {
            path = path + "&" + param + "=" + value;
        }
        final String safePath = path + "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
                + GenericChecksumRewriter.calculateChecksum(contextPath + path, request.getSession());
        return "redirect:" + safePath;
    }
}
