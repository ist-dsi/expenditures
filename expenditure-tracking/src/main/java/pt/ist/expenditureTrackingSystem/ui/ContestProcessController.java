package pt.ist.expenditureTrackingSystem.ui;

import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
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
            @RequestParam(required = false, value = "processState") String processState,
            @RequestParam(required = false, value = "material") Material material,
            @RequestParam(required = false, value = "contractType") ContractType contractType,
            @RequestParam(required = false, value = "selectedUser") User selectedUser,
            @RequestParam(required = false, value = "includeContractManager") Boolean includeContractManager,
            @RequestParam(required = false, value = "includeJuryMembers") Boolean includeJuryMembers,
            @RequestParam(required = false, value = "includeRequester") Boolean includeRequester,
            @RequestParam(required = false, value = "includeContractSecretary") Boolean includeContractSecretary,
            @RequestParam(required = false, value = "selectedUnit") Unit selectedUnit,
            @RequestParam(required = false, value = "selectedSupplier") Supplier selectedSupplier,
            @RequestParam(required = false, value = "includeCandidates") Boolean includeCandidates,
            @RequestParam(required = false, value = "pendingOpsByUser") Boolean pendingOpsByUser) {

        model.addAttribute("processNumber", processNumber);
        model.addAttribute("year", year);
        model.addAttribute("processState", processState);
        model.addAttribute("material", material);
        model.addAttribute("contractType", contractType);
        model.addAttribute("selectedUser", selectedUser);
        model.addAttribute("includeContractManager", includeContractManager);
        model.addAttribute("includeJuryMembers", includeJuryMembers);
        model.addAttribute("includeRequester", includeRequester);
        model.addAttribute("includeContractSecretary", includeContractSecretary);
        model.addAttribute("selectedUnit", selectedUnit);
        model.addAttribute("selectedSupplier", selectedSupplier);
        model.addAttribute("includeCandidates", includeCandidates);
        model.addAttribute("pendingOpsByUser", pendingOpsByUser);

        final Set<MultipleSupplierConsultationProcess> searchResult = ExpenditureTrackingSystem.getInstance().getPaymentProcessYearsSet().stream()
            .filter(y -> matchYear(y, year))
            .flatMap(y -> y.getConsultationProcessSet().stream())
            .filter(p -> match(p.getProcessNumber(), processNumber))
            .filter(p -> match(p.getState().toString(), processState))
            .filter(p -> matchObject(p.getConsultation().getMaterial(), material))
            .filter(p -> matchObject(p.getConsultation().getContractType(), contractType))
            .filter(p -> match(p, selectedUser, includeContractManager, includeJuryMembers, includeRequester, includeContractSecretary))
            .filter(p -> match(p, selectedUnit))
            .filter(p -> match(p, selectedSupplier, includeCandidates))
            .filter(p -> p.isAccessibleToCurrentUser())
            .filter(p -> pendingOperationsByUser(p, pendingOpsByUser))
            .sorted().collect(Collectors.toSet());
        model.addAttribute("searchResult", searchResult);

        return "expenseContest/home";
    }

    private boolean match(final MultipleSupplierConsultationProcess p, final Supplier selectedSupplier, final Boolean includeCandidates) {
        final MultipleSupplierConsultation consultation = p.getConsultation();
        return selectedSupplier == null || consultation.getPartSet().stream().anyMatch(part -> part.getSupplier() == selectedSupplier)
                || (consider(includeCandidates) && consultation.getSupplierSet().contains(selectedSupplier));
    }

    private boolean match(final MultipleSupplierConsultationProcess p, final Unit selectedUnit) {
        return selectedUnit == null || p.getConsultation().getFinancerSet().stream().anyMatch(f -> f.getUnit() == selectedUnit);
    }

    private boolean match(final MultipleSupplierConsultationProcess p, final User selectedUser, final Boolean includeContractManager,
            final Boolean includeJuryMembers, final Boolean includeRequester, final Boolean includeContractSecretary) {
        final MultipleSupplierConsultation consultation = p.getConsultation();
        return selectedUser == null
                || (consider(includeContractManager) && consultation.getContractManager() == selectedUser)
                || (consider(includeJuryMembers) && consultation.getJuryMemberSet().stream().anyMatch(m -> m.getUser() == selectedUser))
                || (consider(includeRequester) && p.getCreator() == selectedUser)
                || (consider(includeContractSecretary) && consultation.getContractSecretary() == selectedUser);
    }

    private boolean matchObject(final Object object, final Object search) {
        return search == null || object == search;
    }

    private boolean match(final String value, final String search) {
        return isEmpty(search) || value.indexOf(search) >= 0 || value.equalsIgnoreCase(search);
    }

    private boolean matchYear(final PaymentProcessYear ppy, final String year) {
        return isEmpty(year) || ppy.getYear().intValue() == Integer.parseInt(year);
    }

    private boolean isEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    private boolean consider(final Boolean b) {
        return b != null && b.booleanValue();
    }

    private boolean pendingOperationsByUser(final MultipleSupplierConsultationProcess process, final Boolean pendingOpsByUser) {
        return !consider(pendingOpsByUser) || process.hasAnyAvailableActivity(true);
    }

}
