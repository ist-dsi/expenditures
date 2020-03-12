package module.internalrequest.ui;

import module.internalrequest.domain.InternalRequestProcess;
import module.internalrequest.domain.util.InternalRequestState;
import module.internalrequest.search.Search;
import module.internalrequest.search.filter.InternalRequestProcessSearchFilter;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.expenditureTrackingSystem.util.RedirectToStrutsAction;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RequestMapping("/internalrequest/search")
@SpringFunctionality(app = InternalRequestController.class, title = "link.search", description = "title.internalRequests.search")
public class SearchInternalRequestProcessController {

    @RequestMapping(method = RequestMethod.GET)
    public String list(InternalRequestProcessSearchFilter searchFilter, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("searchParams", searchFilter);
        model.addAttribute("internalRequestStates", InternalRequestState.values());

        PagedListHolder<InternalRequestProcess> filterResults = Search.pagedFilter(searchFilter);

        model.addAttribute("processes", filterResults);
        return "internalrequest/search";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable String id, final Model model) throws Exception {
        InternalRequestProcess internalRequestProcess;
        try {
            internalRequestProcess = FenixFramework.getDomainObject(id);
            if(!internalRequestProcess.isAccessibleToCurrentUser()) {
                return "redirect:/internalrequest/search";
            }
        } catch (RuntimeException e) {
            return "redirect:/internalrequest/search";
        }

        return RedirectToStrutsAction.redirect("workflowProcessManagement", "viewProcess", "processId", id);
    }
}
