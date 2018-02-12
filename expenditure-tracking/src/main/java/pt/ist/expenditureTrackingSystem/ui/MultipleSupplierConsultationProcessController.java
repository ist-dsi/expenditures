package pt.ist.expenditureTrackingSystem.ui;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

@SpringApplication(group = "logged", path = "consultation", title = "title.consultation.process", hint = "acquisitions")
@SpringFunctionality(app = MultipleSupplierConsultationProcessController.class, title = "title.consultation.process")
@RequestMapping("/consultation")
public class MultipleSupplierConsultationProcessController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {
        return "consultation/home";
    }

    @RequestMapping(value = "/prepareCreateNewMultipleSupplierConsultationProcess", method = RequestMethod.GET)
    public String search(final Model model) {
        return "consultation/createNewMultipleSupplierConsultationProcess";
    }

    @RequestMapping(value = "/naterials", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String naterials(@RequestParam(required = false, value = "term") String term, final Model model) {
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

    private boolean match(String description, String[] inputParts) {
        for (final String namePart : inputParts) {
            if (description.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "/createNewMultipleSupplierConsultationProcess", method = RequestMethod.POST)
    public String createNewMultipleSupplierConsultationProcess(final Model model,
            @RequestParam final String description, @RequestParam final Material material,
            @RequestParam final String justification, @RequestParam final String contractType) {
        final MultipleSupplierConsultationProcess process = MultipleSupplierConsultationProcess.create(description, material, justification, contractType);
        return "redirect:/ForwardToProcess/" + process.getExternalId();
    }

}
