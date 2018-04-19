package pt.ist.expenditureTrackingSystem.ui;

import java.util.Calendar;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

@SpringApplication(group = "logged", path = "expenseContest", title = "title.expense.contests", hint = "acquisitions")
@SpringFunctionality(app = ContestProcessController.class, title = "title.expense.contests")
@RequestMapping("/expenseContests")
public class ContestProcessController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {
        model.addAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
        return "expenseContest/home";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(final Model model,
            @RequestParam(required = false, value = "processNumber") String processNumber,
            @RequestParam(required = false, value = "year") String year,
            @RequestParam(required = false, value = "material") Material material,
            @RequestParam(required = false, value = "contractType") ContractType contractType,
            @RequestParam(required = false, value = "selectedUser") User selectedUser,
            @RequestParam(required = false, value = "includeContractManager") Boolean includeContractManager,
            @RequestParam(required = false, value = "includeJuryMembers") Boolean includeJuryMembers,
            @RequestParam(required = false, value = "includeRequester") Boolean includeRequester,
            @RequestParam(required = false, value = "selectedUnit") Unit selectedUnit,
            @RequestParam(required = false, value = "selectedSupplier") Supplier selectedSupplier,
            @RequestParam(required = false, value = "includeCandidates") Boolean includeCandidates) {

        model.addAttribute("processNumber", processNumber);
        model.addAttribute("year", year);
        model.addAttribute("material", material);
        model.addAttribute("contractType", contractType);
        model.addAttribute("selectedUser", selectedUser);
        model.addAttribute("includeContractManager", includeContractManager);
        model.addAttribute("includeJuryMembers", includeJuryMembers);
        model.addAttribute("includeRequester", includeRequester);
        model.addAttribute("selectedUnit", selectedUnit);
        model.addAttribute("selectedSupplier", selectedSupplier);
        model.addAttribute("includeCandidates", includeCandidates);

        return "expenseContest/home";
    }

}
