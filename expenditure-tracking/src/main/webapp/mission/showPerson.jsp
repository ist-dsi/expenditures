<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="module.mission.domain.MissionSystem"%>

<div class="infobox">
	<table style="width: 100%;">
		<tr>
			<td style="vertical-align: top;">
				<table class="tstyle1">
					<tr>
						<td><bean:message bundle="MISSION_RESOURCES" key="label.organization.name"/></td>
						<td><bean:write name="person" property="user.displayName" /></td>
					</tr>
					<tr>
						<td><bean:message bundle="MISSION_RESOURCES" key="label.organization.usernames"/></td>
						<td><%= MissionSystem.getUserAliasProvider().getUserAliases((Person) request.getAttribute("person")) %></td>
					</tr>
					<tr>
						<td><bean:message bundle="MISSION_RESOURCES" key="label.organization.email"/></td>
						<td><bean:write name="person" property="user.expenditurePerson.email" /></td>
					</tr>
				</table>
			</td>
			<td style="text-align: right;">
				<bean:define id="username" type="java.lang.String" name="person" property="user.username"/>
				<% if (User.findByUsername(username).getProfile() != null) { %>
					<html:img src="<%= User.findByUsername(username).getProfile().getAvatarUrl() %>"
						align="middle" styleClass="float: right; border: 1px solid #aaa; padding: 3px;" />
				<% } %>
			</td>
		</tr>
	</table>
</div>

<jsp:include page="showPersonWorkingPlaceInformation.jsp"/>

<jsp:include page="showPersonMissionResponsibilities.jsp"/>
