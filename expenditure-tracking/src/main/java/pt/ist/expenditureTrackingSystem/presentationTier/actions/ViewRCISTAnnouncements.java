/*
 * @(#)ViewRCISTAnnouncements.java
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

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.presentationTier.Context;
import pt.ist.bennu.core.presentationTier.LayoutContext;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.announcements.RCISTAnnouncement;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/viewRCISTAnnouncements")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ViewRCISTAnnouncements extends ContextBaseAction {

    private static final int REQUESTS_PER_PAGE = 10;
    private static final String PUBLIC_LAYOUT = "rcistAnnouncements";

    public final ActionForward viewRCIST(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        ArrayList<RCISTAnnouncement> approvedList = new ArrayList<RCISTAnnouncement>();
        approvedList.addAll(Announcement.getAnnouncements(RCISTAnnouncement.class, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                RCISTAnnouncement announcement = (RCISTAnnouncement) arg0;
                return announcement.getActive();
            }

        }));

        Collections.sort(approvedList, new ReverseComparator(new BeanComparator("creationDate")));

        final CollectionPager<Announcement> pager =
                new CollectionPager<Announcement>((Collection) approvedList, REQUESTS_PER_PAGE);

        request.setAttribute("collectionPager", pager);
        request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
        request.setAttribute("pageNumber", page);
        request.setAttribute("announcements", pager.getPage(page));

        return forward(request, "/public/viewRCISTAnnouncements.jsp");
    }

    @Override
    public Context createContext(final String contextPathString, HttpServletRequest request) {
        LayoutContext layout = new LayoutContext(contextPathString);
        layout.setLayout(PUBLIC_LAYOUT);
        return layout;
    }
}
