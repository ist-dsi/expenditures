package pt.ist.expenditureTrackingSystem.ui;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

@SpringFunctionality(app = ContestProcessController.class, title = "title.consultation.process")
@RequestMapping("/consultation")
public class MultipleSupplierConsultationProcessController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {
        return "consultation/home";
    }

    @RequestMapping(value = "/prepareCreateNewMultipleSupplierConsultationProcess", method = RequestMethod.GET)
    public String search(@RequestParam(required = false, value = "nif") String nif, final Model model) {
        final Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(nif);
        model.addAttribute("supplier", supplier);

        return "consultation/createNewMultipleSupplierConsultationProcess";
    }

    @SkipCSRF
    @RequestMapping(value = "/materials", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String materials(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();

        final String[] values = term.toLowerCase().split(" ");
        for (final Material material : ExpenditureTrackingSystem.getInstance().getMaterialsSet()) {
            if (material.getMaterialSapId().contains(term) || material.getMaterialCpv().getCode().startsWith(term)
                    || match(material.getDescription().toLowerCase(), values)) {
                final JsonObject o = new JsonObject();
                o.addProperty("id", material.getExternalId());
                o.addProperty("name", material.getFullDescription());
                result.add(o);
            }
        }

        return result.toString();
    }

    @SkipCSRF
    @RequestMapping(value = "/contractTypes", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String contractTypes(@RequestParam(required = false, value = "term") String term, final Model model) {
        final JsonArray result = new JsonArray();

        final String[] values = term.toLowerCase().split(" ");
        for (final ContractType contractType : ExpenditureTrackingSystem.getInstance().getContractTypeSet()) {
            if (match(contractType.getName().getContent(), values)) {
                final JsonObject o = new JsonObject();
                o.addProperty("id", contractType.getExternalId());
                o.addProperty("name", contractType.getName().getContent());
                result.add(o);
            }
        }

        return result.toString();
    }

    @RequestMapping(value = "/units", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String units(@RequestParam(required = false, value = "term") String term, final Model model) {
        final String tValue = term.trim();
        final String[] values = term.toLowerCase().split(" ");
        return ExpenditureTrackingSystem.getInstance().getUnitsSet().stream().filter(u -> match(tValue, values, u)).limit(100l)
                .map(u -> toJson(u.getExternalId(), u.getPresentationName())).collect(toJsonArray()).toString();
    }

    private <T extends JsonElement> Collector<T, JsonArray, JsonArray> toJsonArray() {
        return Collector.of(JsonArray::new, (array, element) -> array.add(element), (one, other) -> {
            one.addAll(other);
            return one;
        }, Characteristics.IDENTITY_FINISH);
    }

    private JsonObject toJson(final String id, final String name) {
        final JsonObject result = new JsonObject();
        result.addProperty("id", id);
        result.addProperty("name", name);
        return result;
    }

    private boolean match(final String value, final String[] values, final Supplier s) {
        if (s.getFiscalIdentificationCode().startsWith(value)) {
            return true;
        }
        final String name = StringNormalizer.normalize(s.getName());
        return hasMatch(values, name);
    }

    private boolean match(final String value, final String[] values, final Unit unit) {
        final String[] input = StringNormalizer.normalize(value).split(" ");

        if (unit instanceof SubProject) {
            final String unitName = StringNormalizer.normalize(unit.getName());
            final String unitAcronym = StringNormalizer.normalize(unit.getUnit().getAcronym());
            if (hasMatch(input, unitName) || hasMatch(input, unitAcronym)) {
                return true;
            }
            final Unit parentUnit = unit.getParentUnit();
            if (parentUnit instanceof CostCenter) {
                final CostCenter costCenter = (CostCenter) parentUnit;
                final String unitCode = costCenter.getCostCenter();
                if (!StringUtils.isEmpty(unitCode) && value.equalsIgnoreCase(unitCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasMatch(final String[] input, final String unitNameParts) {
        for (final String namePart : input) {
            if (unitNameParts.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

    @SkipCSRF
    @RequestMapping(value = "/suppliers", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String suppliers(@RequestParam(required = false, value = "term") String term, final Model model) {
        final String[] values = term.toLowerCase().split(" ");
        return Bennu.getInstance().getSuppliersSet().stream().filter(s -> match(term, values, s)).limit(100l)
                .map(s -> toJson(s.getExternalId(), s.getPresentationName())).collect(toJsonArray()).toString();
    }

    private boolean match(String description, String[] inputParts) {
        for (final String namePart : inputParts) {
            if (description.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "/createNewMultipleSupplierConsultationProcess", method = RequestMethod.POST)
    public String createNewMultipleSupplierConsultationProcess(final Model model, @RequestParam final String description,
            @RequestParam final Material material, @RequestParam final String justification,
            @RequestParam final ContractType contractType, @RequestParam final String suppliers) {
        final String[] supplierNifArray = suppliers.split(",");

        final ArrayList<Supplier> supplierList = new ArrayList<Supplier>(supplierNifArray.length);
        for (final String s : supplierNifArray) {
            if (s == null || s.isEmpty()) {
                continue;
            }
            final Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(s);
            final boolean exceededAllocationLimit = supplier != null && supplier
                    .getTotalAllocatedForMultipleSupplierConsultation().isGreaterThanOrEqual(supplier.getMultipleSupplierLimit());
            if (supplier == null || exceededAllocationLimit) {
                throw new WebApplicationException(400);
            }
            supplierList.add(supplier);
        }

        final MultipleSupplierConsultationProcess process =
                MultipleSupplierConsultationProcess.create(description, material, justification, contractType, supplierList);

        return "redirect:/ForwardToProcess/" + process.getExternalId();
    }
}
