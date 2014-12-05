/*
 * @(#)DashBoardAction.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.actions.dashboard;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.dashBoard.domain.DashBoardPanel;
import module.dashBoard.domain.DashBoardWidget;
import module.dashBoard.presentationTier.DashBoardManagementAction;
import module.workflow.widgets.QuickViewWidget;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.ActivateEmailNotificationWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MyProcessesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MySearchesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.PendingRefundWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.PendingSimplifiedWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.TakenProcessesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.UnreadCommentsWidget;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(path = "/dashBoard")
/**
 * 
 * @author Pedro Santos
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class DashBoardAction extends BaseAction {

    public static MultiLanguageString dashBoardTitle = new MultiLanguageString().with(new Locale("pt"), "Resumo").with(
            new Locale("en"), "Resume");

    public ActionForward viewDigest(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        Person loggedPerson = Person.getLoggedPerson();
        User user = loggedPerson.getUser();
        DashBoardPanel panelsForUser = user.getUserDashBoard();
        DashBoardPanel panel = panelsForUser == null ? createDashBoardPanel(user) : panelsForUser;
        return DashBoardManagementAction.forwardToDashBoard(panel, request);
    }

    @Atomic
    private DashBoardPanel createDashBoardPanel(User user) {
        DashBoardPanel expenditureUserDashBoardPanel = new DashBoardPanel(user);
        expenditureUserDashBoardPanel.addWidgetToColumn(0, new DashBoardWidget(UnreadCommentsWidget.class));
        expenditureUserDashBoardPanel.addWidgetToColumn(0, new DashBoardWidget(MySearchesWidget.class));
        expenditureUserDashBoardPanel.addWidgetToColumn(0, new DashBoardWidget(MyProcessesWidget.class));
        expenditureUserDashBoardPanel.addWidgetToColumn(1, new DashBoardWidget(PendingRefundWidget.class));
        expenditureUserDashBoardPanel.addWidgetToColumn(1, new DashBoardWidget(PendingSimplifiedWidget.class));
        expenditureUserDashBoardPanel.addWidgetToColumn(2, new DashBoardWidget(ActivateEmailNotificationWidget.class));
        expenditureUserDashBoardPanel.addWidgetToColumn(2, new DashBoardWidget(QuickViewWidget.class));
        expenditureUserDashBoardPanel.addWidgetToColumn(2, new DashBoardWidget(TakenProcessesWidget.class));
        return expenditureUserDashBoardPanel;
    }

}
