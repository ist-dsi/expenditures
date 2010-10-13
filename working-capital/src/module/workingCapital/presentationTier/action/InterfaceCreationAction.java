package module.workingCapital.presentationTier.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.RoleType;
import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.LinkNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.Role;
import myorg.domain.groups.UserGroup;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/workingCapitalInterfaceCreationAction")
public class InterfaceCreationAction extends ContextBaseAction {

    @CreateNodeAction(bundle = "WORKING_CAPITAL_RESOURCES", key = "add.node.workingCapital.interface", groupKey = "label.module.workingCapital")
    public final ActionForward createAnnouncmentNodes(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	ActionNode homeNode = ActionNode.createActionNode(virtualHost, node, "/workingCapital", "frontPage",
		"resources.WorkingCapitalResources", "link.sideBar.workingCapital", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, homeNode, "/workingCapital", "prepareCreateWorkingCapitalInitialization",
		"resources.WorkingCapitalResources", "link.sideBar.workingCapital.create", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, homeNode, "/workingCapital", "frontPage", "resources.WorkingCapitalResources",
		"link.sideBar.workingCapital.frontPage", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, homeNode, "/workingCapital", "configuration",
		"resources.WorkingCapitalResources", "link.sideBar.workingCapitalConfiguration", Role.getRole(RoleType.MANAGER));

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

    @CreateNodeAction(bundle = "WORKING_CAPITAL_RESOURCES", key = "add.node.workingCapital.interface.help", groupKey = "label.module.workingCapital")
    public final ActionForward createHelpLinkNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	LinkNode.createLinkNode(virtualHost, node, "https://fenix-ashes.ist.utl.pt/fenixWiki/Qualidade/FundoDeManeio?action=AttachFile&do=get&target=Manual_FM.pdf", 
		"resources.WorkingCapitalResources", "link.sideBar.workingCapital.help", UserGroup.getInstance());

/*	LinkNode.createLinkNode(virtualHost, node, "https://fenix-ashes.ist.utl.pt/fenixWiki/Qualidade/FundoDeManeio", 
		"resources.WorkingCapitalResources", "link.sideBar.workingCapital.help", UserGroup.getInstance());
*/

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

}
