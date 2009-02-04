package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.contents.domain.Page;
import module.contents.domain.Page.PageBean;
import myorg.domain.RoleType;
import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.Role;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping( path="/expendituresInterfaceCreationAction" )
public class InterfaceCreationAction extends ContextBaseAction {

    @CreateNodeAction( bundle="EXPENDITURE_RESOURCES", key="add.node.expenditure-tracking.interface", groupKey="label.module.expenditure-tracking" )
    public final ActionForward createExpenditureNodes(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	final PageBean pageBean = new PageBean(virtualHost, node);
	final MultiLanguageString statisticsLabel = BundleUtil.getMultilanguageString("resources.ExpenditureResources", "link.topBar.statistics");
	pageBean.setLink(statisticsLabel);
	pageBean.setTitle(statisticsLabel);
	final Node statisticsNode = (Node) Page.createNewPage(pageBean);

	ActionNode.createActionNode(virtualHost, statisticsNode, "/statistics", "showSimplifiedProcessStatistics", "resources.StatisticsResources", "label.statistics.process.simplified", Role.getRole(RoleType.MANAGER));
	ActionNode.createActionNode(virtualHost, statisticsNode, "/statistics", "showRefundProcessStatistics", "resources.StatisticsResources", "label.statistics.process.refund", Role.getRole(RoleType.MANAGER));

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

}
