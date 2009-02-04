package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.contents.domain.Page;
import module.contents.domain.Page.PageBean;
import myorg.domain.RoleType;
import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.AnyoneGroup;
import myorg.domain.groups.PersistentGroup;
import myorg.domain.groups.Role;
import myorg.domain.groups.UserGroup;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(path = "/expendituresInterfaceCreationAction")
public class InterfaceCreationAction extends ContextBaseAction {

    @CreateNodeAction(bundle = "EXPENDITURE_RESOURCES", key = "add.node.expenditure-tracking.interface", groupKey = "label.module.expenditure-tracking")
    public final ActionForward createExpenditureNodes(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	final Node homeNode = createNodeForPage(virtualHost, node, "resources.ExpenditureResources", "link.topBar.home",
		AnyoneGroup.getInstance());
	ActionNode.createActionNode(virtualHost, homeNode, "/expendituresHome", "showActiveRequestsForProposal",
		"resources.ExpenditureResources", "link.sideBar.home.publicRequestsForProposal", Role.getRole(RoleType.MANAGER));
	ActionNode.createActionNode(virtualHost, homeNode, "/expendituresHome", "showAcquisitionAnnouncements",
		"resources.ExpenditureResources", "link.sideBar.home.publicAnnouncements", Role.getRole(RoleType.MANAGER));

	final Node requestsForProposalNode = createNodeForPage(virtualHost, node, "resources.ExpenditureResources",
		"link.topBar.requestForProposal", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, requestsForProposalNode, "/requestForProposalProcess",
		"prepareCreateRequestForProposalProcess", "resources.ExpenditureResources",
		"link.sideBar.requestForProposal.create", Role.getRole(RoleType.MANAGER));
	ActionNode.createActionNode(virtualHost, requestsForProposalNode, "/requestForProposalProcess",
		"searchRequestProposalProcess", "resources.ExpenditureResources", "link.sideBar.requestForProposal.search", Role
			.getRole(RoleType.MANAGER));
	ActionNode.createActionNode(virtualHost, requestsForProposalNode, "/requestForProposalProcess", "showPendingRequests",
		"resources.ExpenditureResources", "link.sideBar.requestForProposal.pendingProcesses", Role
			.getRole(RoleType.MANAGER));

	final Node announcementsnNode = createNodeForPage(virtualHost, node, "resources.ExpenditureResources",
		"link.topBar.announcements", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, announcementsnNode, "/announcementProcess", "prepareCreateAnnouncement",
		"resources.ExpenditureResources", "link.sideBar.announcementProcess.createAnnouncement", Role
			.getRole(RoleType.MANAGER));
	ActionNode.createActionNode(virtualHost, announcementsnNode, "/announcementProcess", "searchAnnouncementProcess",
		"resources.ExpenditureResources", "link.sideBar.announcementProcess.searchProcesses", Role
			.getRole(RoleType.MANAGER));
	ActionNode.createActionNode(virtualHost, announcementsnNode, "/announcementProcess", "showMyProcesses",
		"resources.ExpenditureResources", "link.sideBar.announcementProcess.myProcesses", Role.getRole(RoleType.MANAGER));
	ActionNode.createActionNode(virtualHost, announcementsnNode, "/announcementProcess", "showPendingProcesses",
		"resources.ExpenditureResources", "link.sideBar.announcementProcess.pendingProcesses", Role
			.getRole(RoleType.MANAGER));

	final Node aquisitionProcessNode = createNodeForPage(virtualHost, node, "resources.ExpenditureResources",
		"link.topBar.acquisitionProcesses", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, aquisitionProcessNode, "/wizard", "newAcquisitionWizard",
		"resources.ExpenditureResources", "link.sideBar.process.create", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, aquisitionProcessNode, "/search", "search", "resources.ExpenditureResources",
		"link.sideBar.acquisitionProcess.search", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, aquisitionProcessNode, "/wizard", "afterTheFactOperationsWizard",
		"resources.ExpenditureResources", "link.register", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, aquisitionProcessNode, "/acquisitionProcess", "checkFundAllocations",
		"resources.ExpenditureResources", "link.fundAllocations", UserGroup.getInstance());

	final Node organizationNode = createNodeForPage(virtualHost, node, "resources.ExpenditureResources",
		"link.topBar.organization", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, organizationNode, "/organization", "viewOrganization",
		"resources.ExpenditureResources", "link.viewOrganization", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, organizationNode, "/organization", "searchUsers",
		"resources.ExpenditureResources", "search.link.users", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, organizationNode, "/organization", "manageSuppliers",
		"resources.ExpenditureOrganizationResources", "supplier.link.manage", UserGroup.getInstance());

	final Node statisticsNode = createNodeForPage(virtualHost, node, "resources.ExpenditureResources",
		"link.topBar.statistics", UserGroup.getInstance());
	ActionNode.createActionNode(virtualHost, statisticsNode, "/statistics", "showSimplifiedProcessStatistics",
		"resources.StatisticsResources", "label.statistics.process.simplified", Role.getRole(RoleType.MANAGER));
	ActionNode.createActionNode(virtualHost, statisticsNode, "/statistics", "showRefundProcessStatistics",
		"resources.StatisticsResources", "label.statistics.process.refund", Role.getRole(RoleType.MANAGER));

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

    protected Node createNodeForPage(final VirtualHost virtualHost, final Node node, final String bundle, final String key,
	    PersistentGroup userGroup) {
	final PageBean pageBean = new PageBean(virtualHost, node, userGroup);
	final MultiLanguageString statisticsLabel = BundleUtil.getMultilanguageString(bundle, key);
	pageBean.setLink(statisticsLabel);
	pageBean.setTitle(statisticsLabel);
	return (Node) Page.createNewPage(pageBean);
    }

}
