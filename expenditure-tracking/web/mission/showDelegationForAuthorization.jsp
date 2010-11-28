<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/chart.tld" prefix="chart" %>

<jsp:include page="delegationForAuthorizationHeader.jsp"/>

<h3>
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.mission.responsibilities.delegated"/>
</h3>
<logic:present role="myorg.domain.RoleType.MANAGER">
	<html:link page="/missionOrganization.do?method=prepareAddDelegationsForAuthorization" paramId="authorizationId" paramName="accountability" paramProperty="externalId">
		<bean:message key="label.delegations.add" bundle="MISSION_RESOURCES"/>
	</html:link>
</logic:present>
<br/>
<br/>
<logic:empty name="accountability" property="functionDelegationDelegated">
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.mission.responsibilities.delegated.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="accountability" property="functionDelegationDelegated">
	<table class="tstyle3">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.person" bundle="ORGANIZATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.unit" bundle="ORGANIZATION_RESOURCES"/>
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
			<th>
			</th>
		</tr>
		<logic:iterate id="functionDelegation" name="accountability" property="functionDelegationDelegated">
			<bean:define id="childAccountability" name="functionDelegation" property="accountabilityDelegatee" type="module.organization.domain.Accountability"/>
			<tr>
				<td>
					<bean:define id="url" type="java.lang.String">https://fenix.ist.utl.pt/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="childAccountability" property="child.user.username"/></bean:define>
					<img src="<%= url %>">
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
<!--
				<td>
					<html:link page="/missionOrganization.do?method=showDelegationsForAuthorization" paramId="authorizationId" paramName="childAccountability" paramProperty="externalId">
						<bean:message key="label.delegations" bundle="MISSION_RESOURCES" arg0="0"/>
					</html:link>
				</td>
 -->
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
