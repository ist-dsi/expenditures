/**
 * 
 */
package module.mission.presentationTier.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.mission.domain.Mission;
import module.mission.presentationTier.dto.SearchMissionsDTO;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author Shezad Anavarali Date: Aug 20, 2009
 * 
 */
@Mapping(path = "/searchMissions")
public class SearchMissionsAction extends ContextBaseAction {

    private static final int RESULTS_PER_PAGE = 50;

    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	request.setAttribute("searchBean", new SearchMissionsDTO());
	return forward(request, "/mission/searchMissions.jsp");
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	SearchMissionsDTO searchMissions = getRenderedObject("searchBean") != null ? (SearchMissionsDTO) getRenderedObject("searchBean")
		: new SearchMissionsDTO(request);

	return search(searchMissions, request);
    }

    public static ActionForward search(final SearchMissionsDTO searchMissions, final HttpServletRequest request) {
	final Collection<Mission> results = doPagination(request, searchMissions.sortedSearch());

	request.setAttribute("searchResults", results);
	request.setAttribute("searchBean", searchMissions);
	return forward(request, "/mission/searchMissions.jsp");
    }

    private static Collection<Mission> doPagination(final HttpServletRequest request, Collection<Mission> allResults) {
	final CollectionPager<Mission> pager = new CollectionPager<Mission>(allResults, RESULTS_PER_PAGE);
	request.setAttribute("collectionPager", pager);
	request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));
	final String pageParameter = request.getParameter("pageNumber");
	final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
	request.setAttribute("pageNumber", page);
	return pager.getPage(page);
    }

}
