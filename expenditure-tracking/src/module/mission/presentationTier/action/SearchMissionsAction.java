/*
 * @(#)SearchMissionsAction.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
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
 * 
 * @author Luis Cruz
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
