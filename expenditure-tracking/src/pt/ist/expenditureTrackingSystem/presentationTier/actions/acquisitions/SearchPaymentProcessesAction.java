package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.UserSearchBean;
import pt.ist.expenditureTrackingSystem.domain.dto.VariantBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/search")
public class SearchPaymentProcessesAction extends BaseAction {

    private ActionForward search(final ActionMapping mapping, final HttpServletRequest request, SearchPaymentProcess searchBean,
	    boolean advanced) {
	Person loggedPerson = getLoggedPerson();
	request.setAttribute("results", searchBean.search());
	request.setAttribute("searchBean", searchBean);
	request.setAttribute("person", loggedPerson);

	UserSearchBean userSearchBean = new UserSearchBean(loggedPerson);
	if (searchBean.isSearchObjectAvailable()) {
	    userSearchBean.setSelectedSearch(searchBean.getSavedSearch());
	}
	request.setAttribute("savingName", new VariantBean());
	request.setAttribute("mySearches", userSearchBean);
	request.setAttribute("advanced", advanced);
	return forward(request, "/acquisitions/search/searchProcesses.jsp");
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	SearchPaymentProcess searchBean = getRenderedObject("searchBean");
	Person loggedPerson = getLoggedPerson();
	if (searchBean == null) {
	    searchBean = loggedPerson.hasDefaultSearch() ? new SearchPaymentProcess(loggedPerson.getDefaultSearch())
		    : new SearchPaymentProcess();
	    return search(mapping, request, searchBean, false);
	} else {
	    searchBean.setSavedSearch(null);
	    return search(mapping, request, searchBean, true);
	}

    }

    public ActionForward viewSearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	SavedSearch search = getDomainObject(request, "searchOID");
	return search(mapping, request, new SearchPaymentProcess(search), false);
    }

    public ActionForward saveSearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	SearchPaymentProcess searchBean = getRenderedObject("beanToSave");
	String name = getRenderedObject("searchName");
	if (name != null && name.length() > 0) {
	    searchBean.persistSearch(name);
	    RenderUtils.invalidateViewState("searchName");
	} else {
	    request.setAttribute("invalidName", true);
	}
	return search(mapping, request, searchBean, true);
    }

    public ActionForward changeSelectedClass(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	SearchPaymentProcess searchBean = getRenderedObject("searchBean");

	RenderUtils.invalidateViewState("searchBean");
	return search(mapping, request, searchBean, true);
    }

    public ActionForward mySearches(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	UserSearchBean bean = getRenderedObject("mySearches");
	SavedSearch search = bean.getSelectedSearch();
	if (search == null) {
	    search = getLoggedPerson().getDefaultSearch();
	    bean.setSelectedSearch(search);
	    RenderUtils.invalidateViewState("mySearches");
	}
	return search(mapping, request, new SearchPaymentProcess(search), false);
    }

    public ActionForward configurateMySearches(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	List<SavedSearch> systemSearches = ExpenditureTrackingSystem.getInstance().getSystemSearches();
	List<SavedSearch> userSearches = getLoggedPerson().getSaveSearches();
	request.setAttribute("systemSearches", systemSearches);
	request.setAttribute("userSearches", userSearches);

	return forward(request, "/acquisitions/search/manageMySearches.jsp");
    }

    public ActionForward deleteMySearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	SavedSearch search = getDomainObject(request, "savedSearchOID");
	Person person = getLoggedPerson();
	if (person == search.getPerson()) {
	    search.delete();
	}

	return configurateMySearches(mapping, form, request, response);
    }

    public ActionForward setSearchAsDefault(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	SavedSearch search = getDomainObject(request, "savedSearchOID");
	Person person = getLoggedPerson();

	person.setDefaultSearch(search);
	return configurateMySearches(mapping, form, request, response);
    }

}
