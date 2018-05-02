package pt.ist.expenditureTrackingSystem.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

@SpringApplication(group = "logged", path = "expenditure-tracking", title = "Nova Aquisicao", hint = "expenditure-tracking")
@SpringFunctionality(app = CreateAcquisitionProcessWizardController.class, title = "Nova Aquisicao")
@RequestMapping("/expenditure/acquisitons/create")
public class CreateAcquisitionProcessWizardController {

    @RequestMapping(method = RequestMethod.GET)
    public String selectSupplier(final Model model) throws Exception {
        return "expenditure-tracking/createAcquisitionProcessWizardSelectSupplier";
    }

    @RequestMapping(value = "/selectType", method = RequestMethod.GET)
    public String selectType(@RequestParam(required = false, value = "supplier") String supplierNif, final Model model)
            throws Exception {
        model.addAttribute("supplier", supplierNif);

        final Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(supplierNif);
        final boolean suggestSimplified = supplier.getTotalAllocated().isLessThan(supplier.getSupplierLimit());
        final boolean suggestRefund = suggestSimplified;
        final boolean suggestConsultation = true;

        model.addAttribute("suggestSimplified", suggestSimplified);
        model.addAttribute("suggestRefund", suggestRefund);
        model.addAttribute("suggestConsultation", suggestConsultation);
        return "expenditure-tracking/createAcquisitionProcessWizardSelectType";
    }

    @RequestMapping(value = "/isRefund", method = RequestMethod.GET)
    public String isRefund(final Model model) throws Exception {
        return "expenditure-tracking/createAcquisitionProcessWizardIsRefund";
    }

    @RequestMapping(value = "/isRefund", method = RequestMethod.POST)
    public String isRefundPost(@RequestParam(required = false, value = "refund") String refund, final Model model)
            throws Exception {
        System.out.println("isRefund " + refund);
        if ("1".equals(refund)) {
            return "redirect:/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderCCP";
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
        final Stream<Supplier> stream = Bennu.getInstance().getSuppliersSet().stream();
        final java.util.function.Supplier<TreeSet<Supplier>> s =
                () -> new TreeSet<Supplier>(Comparator.comparing(u -> u.getName()));
        stream.filter(supplier -> supplierHasMatch(supplier, term, input)).collect(Collectors.toCollection(s))
                .forEach(u -> addToJson(result, u));
    }

    private boolean supplierHasMatch(Supplier supplier, String term, final String[] input) {
        if (supplier.getFiscalIdentificationCode().startsWith(term)) {
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

    private void addToJson(JsonArray result, Supplier s) {
        final JsonObject o = new JsonObject();
        o.addProperty("id", s.getExternalId());
        o.addProperty("nif", s.getFiscalIdentificationCode());
        o.addProperty("name", s.getPresentationName());
        o.addProperty("totalAllocated", s.getTotalAllocated().getRoundedValue());
        o.addProperty("suplierLimit", s.getSupplierLimit().getRoundedValue());
        o.addProperty("totalAllocatedMultipleSupplier", s.getTotalAllocatedForMultipleSupplierConsultation().getRoundedValue());
        //o.addProperty("multipleSuplierLimit", s.getMultipleSupplierLimit().getRoundedValue());
        result.add(o);

    }
}
