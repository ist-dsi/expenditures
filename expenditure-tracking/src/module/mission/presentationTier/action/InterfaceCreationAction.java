package module.mission.presentationTier.action;

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

@Mapping(path = "/missionInterfaceCreationAction")
public class InterfaceCreationAction extends ContextBaseAction {

    @CreateNodeAction(bundle = "MISSION_RESOURCES", key = "add.node.mission.interface", groupKey = "label.module.mission")
    public final ActionForward createAnnouncmentNodes(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	ActionNode homeNode = ActionNode.createActionNode(virtualHost, node, "/missionProcess", "frontPage",
		"resources.MissionResources", "link.sideBar.missionProcess", UserGroup.getInstance());

//	ActionNode homeNode = ActionNode.createActionNode(virtualHost, node, "/searchMissions", "prepare",
//		"resources.MissionResources", "link.sideBar.missionProcess", Role.getRole(RoleType.MANAGER));

//	ActionNode homeNode = ActionNode.createActionNode(virtualHost, node, "/missionProcess", "showProcesses",
//		"resources.MissionResources", "link.sideBar.missionProcess", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, homeNode, "/missionProcess", "missionCreationInstructions", "resources.MissionResources",
		"link.sideBar.newMission", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, homeNode, "/searchMissions", "prepare", "resources.MissionResources",
		"link.sideBar.missionSearch", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, homeNode, "/missionOrganization", "showPerson", "resources.MissionResources",
		"link.sideBar.organization", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, homeNode, "/configureMissions", "prepare", "resources.MissionResources",
		"link.sideBar.missionConfiguration", Role.getRole(RoleType.MANAGER));

	ActionNode.createActionNode(virtualHost, homeNode, "/missionProcess", "help", "resources.MissionResources",
		"link.sideBar.help", UserGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

    @CreateNodeAction(bundle = "MISSION_RESOURCES", key = "add.node.mission.interface.help", groupKey = "label.module.mission")
    public final ActionForward createHelpLinkNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	LinkNode.createLinkNode(virtualHost, node, "https://fenix-ashes.ist.utl.pt/fenixWiki/Qualidade/Missoes", 
		"resources.MissionResources", "link.sideBar.help", UserGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

}
