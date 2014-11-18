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
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.util.MissionState;
import module.mission.presentationTier.dto.SearchMissionsDTO;
import module.organization.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = MissionProcessAction.class, path = "searchMissionProcess", titleKey = "link.sideBar.missionSearch")
/**
 * 
 * @author Luis Cruz
 * 
 */
@Mapping(path = "/searchMissions")
public class SearchMissionsAction extends BaseAction {

    private static final int RESULTS_PER_PAGE = 50;

    @EntryPoint
    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        request.setAttribute("searchBean", new SearchMissionsDTO());
        return forward("/mission/searchMissions.jsp");
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        SearchMissionsDTO searchMissions =
                getRenderedObject("searchBean") != null ? (SearchMissionsDTO) getRenderedObject("searchBean") : new SearchMissionsDTO(
                        request);

        return search(searchMissions, request);
    }

    public ActionForward search(final SearchMissionsDTO searchMissions, final HttpServletRequest request) {
        final Collection<Mission> results = doPagination(request, searchMissions.sortedSearch());

        request.setAttribute("searchResults", results);
        request.setAttribute("searchBean", searchMissions);
        return forward("/mission/searchMissions.jsp");
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

    public ActionForward downloadSearchResult(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final SearchMissionsDTO searchMissions = new SearchMissionsDTO(request);
        final List<Mission> searchResult = searchMissions.sortedSearch();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=searchResult.xls");

        final Spreadsheet spreadsheet = new Spreadsheet("SearchResult");
        spreadsheet.setHeader(getExpendituresMessage("label.acquisitionProcessId"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.type"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.destination"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.departure"));
        spreadsheet.setHeader(getMissionsMessage("label.module.mission.front.page.list.duration"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.items"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.value"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.financer"));
        spreadsheet.setHeader(getExpendituresMessage("label.accounting.units"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.requester.person"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.participants"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.inactiveSince"));
        spreadsheet.setHeader(getMissionsMessage("label.mission.canceled"));
        addMissionStateHeaders(spreadsheet);

        for (final Mission mission : searchResult) {
            final MissionProcess missionProcess = mission.getMissionProcess();

            final Row row = spreadsheet.addRow();
            row.setCell(missionProcess.getProcessIdentification());
            row.setCell(getMissionsMessage(mission.getGrantOwnerEquivalence() ? "title.mission.process.type.grantOwnerEquivalence" : "title.mission.process.type.dislocation"));
            row.setCell(mission.getDestinationDescription());
            row.setCell(mission.getDaparture().toString("yyyy-MM-dd HH:mm"));
            row.setCell(mission.getDurationInDays());
            row.setCell(mission.getMissionItemsCount());
            row.setCell(mission.getValue().toFormatString());
            row.setCell(getFinancingUnits(mission));
            row.setCell(getAccountingUnits(mission));
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
            row.setCell(getExpendituresMessage(missionProcess.isCanceled() ? "button.yes" : "button.no"));
            addMissionStateContent(row, missionProcess);
        }

        try {
            ServletOutputStream writer = response.getOutputStream();
            spreadsheet.exportToXLSSheet(writer);
        } catch (final IOException e) {
            throw new Error(e);
        }

        return null;
    }

    private void addMissionStateHeaders(Spreadsheet spreadsheet) {
        for (MissionState state : MissionState.values()) {
            spreadsheet.setHeader(state.getLocalizedName());
        }
    }

    private void addMissionStateContent(Row row, MissionProcess process) {
        for (MissionState state : MissionState.values()) {
            if (!state.isRequired(process)) {
                row.setCell("-");
            } else {
                row.setCell(state.getStateProgress(process).getLocalizedName());
            }
        }
    }

    private String getMissionsMessage(String label) {
        return BundleUtil.getString("resources.MissionResources", label);
    }

    private String getExpendituresMessage(String label) {
        return BundleUtil.getString("resources.ExpenditureResources", label);
    }

    private String getFinancingUnits(Mission mission) {
        final StringBuilder builder = new StringBuilder();
        for (final MissionFinancer financer : mission.getFinancerSet()) {
            final Unit unit = financer.getUnit();
            if (unit != null) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(unit.getUnit().getAcronym());
            }
        }
        return builder.toString();
    }

    private String getAccountingUnits(final Mission mission) {
        final StringBuilder builder = new StringBuilder();
        for (final MissionFinancer financer : mission.getFinancerSet()) {
            final AccountingUnit accountingUnit = financer.getAccountingUnit();
            if (accountingUnit != null) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(accountingUnit.getName());
            }
        }
        return builder.toString();
    }

}
