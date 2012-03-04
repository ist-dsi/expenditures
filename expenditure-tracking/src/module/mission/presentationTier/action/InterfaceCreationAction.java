/*
 * @(#)InterfaceCreationAction.java
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

import pt.ist.bennu.vaadin.domain.contents.VaadinNode;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Luis Cruz
 * 
 */
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

	VaadinNode.createVaadinNode(virtualHost, node, "resources.ContentResources", "add.interface.vaadinWiki",
		"PageView-322122547202", UserGroup.getInstance());

	VaadinNode.createVaadinNode(virtualHost, node, "resources.ContentResources", "add.interface.vaadinWiki",
		"SimplePageView-322122547202", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, node, "/traditionalPageViewer", "viewPage&pageID=322122547202", "resources.ContentResources",
		"add.interface.vaadinWiki", UserGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

}
