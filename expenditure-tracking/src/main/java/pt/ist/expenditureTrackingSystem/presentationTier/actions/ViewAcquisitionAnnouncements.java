/*
 * @(#)ViewAcquisitionAnnouncements.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.presentationTier.Context;
import pt.ist.bennu.core.presentationTier.LayoutContext;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.announcements.RCISTAnnouncement;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/viewAcquisitionAnnouncements")
/**
 * 
 * @author Luis Cruz
 * 
 */
public class ViewAcquisitionAnnouncements extends ContextBaseAction {

    private static final int REQUESTS_PER_PAGE = 10;
    private static final String PUBLIC_LAYOUT = "publicAcquisitionAnnouncements";

    public final ActionForward list(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {

	final ProcessClassification processClassification = getProcessClassification(request);

	ArrayList<RCISTAnnouncement> approvedList = new ArrayList<RCISTAnnouncement>();
	approvedList.addAll(Announcement.getAnnouncements(RCISTAnnouncement.class, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		RCISTAnnouncement announcement = (RCISTAnnouncement) arg0;
		if (announcement.getActive() && matchesProcessClassificationCriteria(announcement)) {
		    return true;
		}
		return false;
	    }

	    private boolean matchesProcessClassificationCriteria(final RCISTAnnouncement announcement) {
		return processClassification == null || processClassification == getProcessClassification(announcement);
	    }

	    private ProcessClassification getProcessClassification(final RCISTAnnouncement announcement) {
		final AcquisitionRequest acquisition = announcement.getAcquisition();
		final AcquisitionProcess process = acquisition.getProcess();
		if (process instanceof SimplifiedProcedureProcess) {
		    final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) process;
		    return simplifiedProcedureProcess.getProcessClassification();
		}
		return null;
	    }

	}));

	Collections.sort(approvedList, new ReverseComparator(new BeanComparator("creationDate")));

	final CollectionPager<Announcement> pager = new CollectionPager<Announcement>((Collection) approvedList,
		REQUESTS_PER_PAGE);

	request.setAttribute("collectionPager", pager);
	request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

	final String pageParameter = request.getParameter("pageNumber");
	final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
	request.setAttribute("pageNumber", page);
	request.setAttribute("announcements", pager.getPage(page));

	return forward(request, "/public/viewAcquisitionAnnouncements.jsp");
    }

    @Override
    public Context createContext(final String contextPathString, HttpServletRequest request) {
	LayoutContext layout = new LayoutContext(contextPathString);
	layout.setLayout(PUBLIC_LAYOUT);
	return layout;
    }

    private ProcessClassification getProcessClassification(final HttpServletRequest request) {
	final String parameter = request.getParameter("processClassification");
	if (parameter != null && !parameter.isEmpty()) {
	    return ProcessClassification.valueOf(parameter);
	}
	final Object attribute = request.getAttribute("processClassification");
	if (attribute != null && attribute instanceof String) {
	    final String string = (String) attribute;
	    if (!string.isEmpty()) {
		return ProcessClassification.valueOf(string);
	    }
	}
	return null;
    }

}
