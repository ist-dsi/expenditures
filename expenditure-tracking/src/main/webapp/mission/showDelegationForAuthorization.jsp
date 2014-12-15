<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart" %>

<jsp:include page="delegationForAuthorizationHeader.jsp"/>

<h3>
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.mission.responsibilities.delegated"/>
</h3>
<% if (ExpenditureTrackingSystem.isManager()) { %>
	<html:link page="/missionOrganization.do?method=prepareAddDelegationsForAuthorization" paramId="authorizationId" paramName="accountability" paramProperty="externalId">
		<bean:message key="label.delegations.add" bundle="MISSION_RESOURCES"/>
	</html:link>
<% } %>
<br/>
<br/>
<logic:empty name="functionDelegationDelegated">
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.mission.responsibilities.delegated.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="functionDelegationDelegated">
	<bean:define id="accountability" name="accountability" type="module.organization.domain.Accountability"/>
	<bean:define id="sortBy" name="sortBy" type="java.lang.String"/>
	<bean:define id="order" name="order" type="java.lang.String"/>
	<table class="tstyle3">
		<tr>
			<th>
			</th>
			<th>
				<html:link page='<%= "/missionOrganization.do?method=showDelegationsForAuthorization&authorizationId=" + accountability.getExternalId() + "&sortBy=childPartyName&order=" + ((sortBy.equals("childPartyName") && order.equals("asc")) ? "desc" : "asc") %>'>
					<bean:message key="label.person" bundle="ORGANIZATION_RESOURCES"/>
				</html:link>
			</th>
			<th>
				<html:link page='<%= "/missionOrganization.do?method=showDelegationsForAuthorization&authorizationId=" + accountability.getExternalId() + "&sortBy=parentUnitName&order=" + ((sortBy.equals("parentUnitName") && order.equals("asc")) ? "desc" : "asc") %>'>
					<bean:message key="label.unit" bundle="ORGANIZATION_RESOURCES"/>
				</html:link>
			</th>
			<th>
				<bean:message key="label.mission.authority.type" bundle="MISSION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.beginDate" bundle="MISSION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.endDate" bundle="MISSION_RESOURCES"/>
			</th>
			<th></th>
			<th></th>
		</tr>
		<logic:iterate id="functionDelegation" name="functionDelegationDelegated">
			<bean:define id="childAccountability" name="functionDelegation" property="accountabilityDelegatee" type="module.organization.domain.Accountability"/>
			<tr>
				<td>
					<bean:define id="username" type="java.lang.String" name="childAccountability" property="child.user.username"/>
					<% if (User.findByUsername(username).getProfile() != null) { %>
						<img src="<%= User.findByUsername(username).getProfile().getAvatarUrl() %>">
					<% } %>
				</td>
				<td>
					<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="childAccountability" paramProperty="child.externalId">
						<fr:view name="childAccountability" property="child.presentationName"/>
					</html:link>
				</td>
				<td>
					<html:link page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="childAccountability" paramProperty="parent.externalId" styleClass="secondaryLink">
						<fr:view name="childAccountability" property="parent.presentationName"/>
					</html:link>
				</td>
				<td>
					<fr:view name="childAccountability" property="accountabilityType.name"/>
				</td>
				<td>
					<fr:view name="childAccountability" property="beginDate"/>
				</td>
				<td>
					<logic:present name="childAccountability" property="endDate">
						<fr:view name="childAccountability" property="endDate"/>
					</logic:present>
				</td>

				<% if (ExpenditureTrackingSystem.isManager()) { %>
					<td>
						<html:link page="/missionOrganization.do?method=prepareEditDelegation" paramId="functionDelegationId" paramName="functionDelegation" paramProperty="externalId">
							<bean:message key="label.delegation.edit" bundle="MISSION_RESOURCES"/>
						</html:link>
					</td>
	
					<td>
						<html:link page="/missionOrganization.do?method=removeDelegation" paramId="functionDelegationId" paramName="functionDelegation" paramProperty="externalId">
							<bean:message key="label.delegation.remove" bundle="MISSION_RESOURCES"/>
						</html:link>
					</td>
				<% } %>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
