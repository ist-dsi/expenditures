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

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.mission.presentationTier.dto.SearchMissionsDTO;
import module.organization.domain.Person;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

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

    public ActionForward downloadSearchResult(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final SearchMissionsDTO searchMissions = new SearchMissionsDTO(request);
	final List<Mission> searchResult = searchMissions.sortedSearch();

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=searchResult.xls");

	final Spreadsheet spreadsheet = new Spreadsheet("SearchResult");
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.ExpenditureResources", "label.acquisitionProcessId"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.MissionResources", "label.mission.destination"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.MissionResources", "label.mission.departure"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.MissionResources", "label.module.mission.front.page.list.duration"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.MissionResources", "label.mission.items"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.MissionResources", "label.mission.value"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.MissionResources", "label.mission.requester.person"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.MissionResources", "label.mission.participants"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.MissionResources", "label.mission.inactiveSince"));

	for (final Mission mission : searchResult) {
	    final MissionProcess missionProcess = mission.getMissionProcess();

	    final Row row = spreadsheet.addRow();
	    row.setCell(missionProcess.getProcessIdentification());
	    row.setCell(mission.getDestinationDescription());
	    row.setCell(mission.getDaparture().toString("yyyy-MM-dd HH:mm"));
	    row.setCell(mission.getDurationInDays());
	    row.setCell(mission.getMissionItemsCount());
	    row.setCell(mission.getValue().toFormatString());
	    row.setCell(mission.getRequestingPerson().getFirstAndLastName());
	    final StringBuilder builder = new StringBuilder();
	    for (final Person person : mission.getParticipantesSet()) {
		if (builder.length() > 0) {
		    builder.append(", ");
		}
		builder.append(person.getName());
	    }
	    row.setCell(builder.toString());
	    final DateTime lastActivity = missionProcess.getDateFromLastActivity();
	    row.setCell(lastActivity == null ? "" : lastActivity.toString("yyyy-MM-dd HH:mm"));
	}

	try {
	    ServletOutputStream writer = response.getOutputStream();
	    spreadsheet.exportToXLSSheet(writer);
	} catch (final IOException e) {
	    throw new Error(e);
	}

	return null;
    }

}
