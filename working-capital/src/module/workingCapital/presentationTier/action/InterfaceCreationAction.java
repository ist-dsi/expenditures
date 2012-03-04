/*
 * @(#)InterfaceCreationAction.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
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
/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
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

	/*	LinkNode.createLinkNode(virtualHost, node, "https://fenix-ashes.ist.utl.pt/fenixWiki/Qualidade/FundoDeManeio?action=AttachFile&do=get&target=Manual_FM.pdf", 
			"resources.WorkingCapitalResources", "link.sideBar.workingCapital.help", UserGroup.getInstance());
	*/
	LinkNode.createLinkNode(virtualHost, node, "https://fenix-ashes.ist.utl.pt/fenixWiki/Qualidade/FundoDeManeio",
		"resources.WorkingCapitalResources", "link.sideBar.workingCapital.help", UserGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

    @CreateNodeAction(bundle = "WORKING_CAPITAL_RESOURCES", key = "add.node.workingCapital.interface.configuration", groupKey = "label.module.workingCapital")
    public final ActionForward createConfigurationNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	ActionNode.createActionNode(virtualHost, node, "/workingCapital", "configuration", "resources.WorkingCapitalResources",
		"link.sideBar.workingCapitalConfiguration", Role.getRole(RoleType.MANAGER));

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

}
