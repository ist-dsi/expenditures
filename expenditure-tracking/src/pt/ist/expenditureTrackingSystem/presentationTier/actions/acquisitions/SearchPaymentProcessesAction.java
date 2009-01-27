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
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/search")
@Forwards( { @Forward(name = "search", path = "/acquisitions/search/searchProcesses.jsp"),
	@Forward(name = "manage.searches", path = "/acquisitions/search/manageMySearches.jsp") })
public class SearchPaymentProcessesAction extends BaseAction {

    private static final Context CONTEXT = new Context("acquisitions");

    @Override
    protected Context getContextModule(final HttpServletRequest request) {
	return CONTEXT;
    }

    private ActionForward search(final ActionMapping mapping, final HttpServletRequest request, SearchPaymentProcess searchBean) {
	Person loggedPerson = getLoggedPerson();
	request.setAttribute("results", searchBean.search());
	request.setAttribute("searchBean", searchBean);
	request.setAttribute("person", loggedPerson);

	request.setAttribute("savingName", new VariantBean());
	request.setAttribute("mySearches", new UserSearchBean(loggedPerson));
	return mapping.findForward("search");
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	SearchPaymentProcess searchBean = getRenderedObject("searchBean");
	if (searchBean == null) {
	    searchBean = new SearchPaymentProcess();
	}

	return search(mapping, request, searchBean);
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
	return search(mapping, request, searchBean);
    }

    public ActionForward changeSelectedClass(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	SearchPaymentProcess searchBean = getRenderedObject("searchBean");

	RenderUtils.invalidateViewState("searchBean");
	return search(mapping, request, searchBean);
    }

    public ActionForward mySearches(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	UserSearchBean bean = getRenderedObject("mySearches");
	return search(mapping, request, new SearchPaymentProcess(bean.getSelectedSearch()));
    }

    public ActionForward configurateMySearches(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	List<SavedSearch> systemSearches = ExpenditureTrackingSystem.getInstance().getSystemSearches();
	List<SavedSearch> userSearches = getLoggedPerson().getSaveSearches();
	request.setAttribute("systemSearches", systemSearches);
	request.setAttribute("userSearches", userSearches);

	return mapping.findForward("manage.searches");
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
