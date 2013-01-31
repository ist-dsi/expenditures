/*
 * @(#)HomeAction.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.announcements.CCPAnnouncement;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/expendituresHome")
/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author João Alfaiate
 * 
 */
public class HomeAction extends BaseAction {

	private static final int REQUESTS_PER_PAGE = 25;

	public final ActionForward firstPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final Person person = getLoggedPerson();
		if (person != null) {
			final Set<AcquisitionProcess> pendingAuthorizationAcquisitionProcesses =
					person.findAcquisitionProcessesPendingAuthorization();
			request.setAttribute("pendingAuthorizationAcquisitionProcesses", pendingAuthorizationAcquisitionProcesses);
		} else {
			return showAcquisitionAnnouncements(mapping, form, request, response);
		}
		return forward(request, "/hello.jsp");
	}

	public final ActionForward showAcquisitionAnnouncements(final ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ArrayList<Announcement> approvedList = new ArrayList<Announcement>();
		for (CCPAnnouncement announcement : Announcement.getAnnouncements(CCPAnnouncement.class)) {
			if (announcement.getAnnouncementProcess().isProcessInState(AnnouncementProcessStateType.APPROVED)) {
				approvedList.add(announcement);
			}
		}

		final CollectionPager<Announcement> pager =
				new CollectionPager<Announcement>((Collection) approvedList, REQUESTS_PER_PAGE);

		request.setAttribute("collectionPager", pager);
		request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

		final String pageParameter = request.getParameter("pageNumber");
		final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
		request.setAttribute("pageNumber", page);
		request.setAttribute("announcements", pager.getPage(page));

		return forward(request, "/public/viewAnnouncements.jsp");
	}

}
