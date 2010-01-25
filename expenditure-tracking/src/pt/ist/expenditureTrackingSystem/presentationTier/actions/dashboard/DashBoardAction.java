package pt.ist.expenditureTrackingSystem.presentationTier.actions.dashboard;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.dashBoard.domain.DashBoardPanel;
import module.dashBoard.domain.DashBoardWidget;
import module.dashBoard.presentationTier.DashBoardManagementAction;
import myorg.domain.User;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureUserDashBoardPanel;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.ActivateEmailNotificationWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MyProcessesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MySearchesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.PendingRefundWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.PendingSimplifiedWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.QuickViewWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.TakenProcessesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.UnreadCommentsWidget;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(path = "/dashBoard")
public class DashBoardAction extends ContextBaseAction {

    public static MultiLanguageString dashBoardTitle = MultiLanguageString.i18n().add("pt", "Resumo").add("en", "Resume")
	    .finish();

    public ActionForward viewDigest(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	Person loggedPerson = Person.getLoggedPerson();
	User user = loggedPerson.getUser();
	List<ExpenditureUserDashBoardPanel> panelsForUser = DashBoardPanel.getPanelsForUser(user,
		ExpenditureUserDashBoardPanel.class);
	DashBoardPanel panel = panelsForUser.isEmpty() ? createDashBoardPanel(user) : panelsForUser.get(0);
	return DashBoardManagementAction.forwardToDashBoard(panel, request);
    }

    @Service
    private DashBoardPanel createDashBoardPanel(User user) {
	ExpenditureUserDashBoardPanel expenditureUserDashBoardPanel = new ExpenditureUserDashBoardPanel(dashBoardTitle, user);
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
