package module.internalrequest.ui;

import module.internalrequest.domain.InternalRequestProcess;
import module.internalrequest.domain.util.InternalRequestState;
import module.internalrequest.search.Search;
import module.internalrequest.search.filter.InternalRequestProcessSearchFilter;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/internalrequest/home")
@SpringFunctionality(app = InternalRequestController.class, title = "link.home", description = "title.internalRequests.frontPage")
public class FrontPageController {

    @RequestMapping(method = RequestMethod.GET)
    public String frontPage(@RequestParam(required = false, value = "year") Integer year, Model model, HttpServletRequest request) {
        InternalRequestProcessSearchFilter searchFilter = new InternalRequestProcessSearchFilter();
        if (year != null) { searchFilter.setYear(year); }
        model.addAttribute("searchParams", searchFilter);

        model.addAttribute("requestedByMe", searchInternalRequestsByMe(searchFilter));
        model.addAttribute("takenByMe", searchInternalRequestsTakenBy(Authenticate.getUser(), searchFilter));
        model.addAttribute("pendingApproval", searchPendingInternalRequests(InternalRequestState.APPROVAL, searchFilter));
        model.addAttribute("pendingBudget", searchPendingInternalRequests(InternalRequestState.BUDGETING, searchFilter));
        model.addAttribute("pendingAuthorization", searchPendingInternalRequests(InternalRequestState.AUTHORIZATION, searchFilter));
        model.addAttribute("pendingProcessing", searchPendingInternalRequests(InternalRequestState.PROCESSING, searchFilter));
        model.addAttribute("pendingDelivery", searchPendingInternalRequests(InternalRequestState.DELIVERY, searchFilter));
        model.addAttribute("pendingCostImputation", searchPendingInternalRequests(InternalRequestState.COST_IMPUTATION, searchFilter));

        return "internalrequest/frontPage";
    }

    private List<InternalRequestProcess> searchPendingInternalRequests(InternalRequestState pendingState, InternalRequestProcessSearchFilter filter) {
        filter.setPendingState(pendingState);
        final List<InternalRequestProcess> filterResults = Search.filter(filter).collect(Collectors.toList());
        filter.setPendingState(null);
        return filterResults;
    }

    private List<InternalRequestProcess> searchInternalRequestsByMe(InternalRequestProcessSearchFilter filter) {
        filter.setRequestedByMe(true);
        final List<InternalRequestProcess> filterResults = Search.filter(filter).collect(Collectors.toList());
        filter.setRequestedByMe(false);
        return filterResults;
    }

    private List<InternalRequestProcess> searchInternalRequestsTakenBy(User user, InternalRequestProcessSearchFilter filter) {
        filter.setIncludeTaken(true);
        final List<InternalRequestProcess> filterResults = Search.filter(filter)
                .filter(p -> user.equals(p.getCurrentOwner())).collect(Collectors.toList());
        filter.setIncludeTaken(false);
        return filterResults;
    }
}
