package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.SearchUsers;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions.SearchPaymentProcessesAction;

@StrutsFunctionality(app = SearchPaymentProcessesAction.class, path = "expenditure-organization-user",
        titleKey = "search.link.users")
@Mapping(path = "/expenditureManageUsers")
public class ManageUsersAction extends BaseAction {

    @EntryPoint
    public final ActionForward searchUsers(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ExpenditureTrackingSystem.getInstance();
        SearchUsers searchUsers = getRenderedObject("searchUsers");
        if (searchUsers == null) {
            searchUsers = new SearchUsers();
        }
        request.setAttribute("searchUsers", searchUsers);
        return forward("/expenditureTrackingOrganization/searchUsers.jsp");
    }

}
