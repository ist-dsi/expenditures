<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/chart.tld" prefix="chart" %>

<h2>
	<fr:view name="unit" property="presentationName"/>
</h2>

<div class="infobox">
	<table align="center" style="width: 100%; text-align: center;">
		<tr>
			<td align="center">
				<chart:orgChart id="party" name="chart" type="java.lang.Object">
					<div class="orgTBox orgTBoxLight">
						<%
							if (party == request.getAttribute("unit")) {
						%>
								<strong>
									<bean:write name="party" property="partyName"/>
								</strong>
						<%			    
							} else {
						%>
								<html:link page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="party" paramProperty="externalId" styleClass="secondaryLink">
									<bean:write name="party" property="partyName"/>
								</html:link>
						<%			    
							}
						%>
					</div>
				</chart:orgChart>
			</td>
		</tr>
	</table>
</div>

<logic:present role="myorg.domain.RoleType.MANAGER">
	<logic:notPresent name="unit" property="missionSystemFromUnitWithResumedAuthorizations">
		<p>
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitWithResumedAuthorizations.not"/>
			<html:link page="/missionOrganization.do?method=addUnitWithResumedAuthorizations" paramId="unitId" paramName="unit" paramProperty="externalId">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitWithResumedAuthorizations.add.summary"/>
			</html:link>
		</p>
	</logic:notPresent>
	<logic:present name="unit" property="missionSystemFromUnitWithResumedAuthorizations">
		<p style="color: green;">
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitWithResumedAuthorizations"/>
			<html:link page="/missionOrganization.do?method=removeUnitWithResumedAuthorizations" paramId="unitId" paramName="unit" paramProperty="externalId">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitWithResumedAuthorizations.remove.summary"/>
			</html:link>
		</p>
	</logic:present>
</logic:present>

<html:link page="/missionOrganization.do?method=viewPresences" paramId="unitId" paramName="unit" paramProperty="externalId">
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.view.member.presence"/>
</html:link>
<html:messages id="message" message="true">
	<span class="error0"> <bean:write name="message" /> </span>
</html:messages>
<br/>
<bean:define id="urlByMonth" type="java.lang.String">/vaadinContext.do?method=forwardToVaadin#MissionParticipationMap?unit=<bean:write name="unit" property="externalId"/></bean:define>
<html:link page="<%= urlByMonth %>">
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.view.member.presence.by.month"/>
</html:link>

<br/>
<br/>

<h3>
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.mission.responsibilities"/>
</h3>
<br/>
<logic:empty name="authorityAccountabilities">
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.mission.responsibilities.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="authorityAccountabilities">
	<table class="tstyle3">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.person" bundle="ORGANIZATION_RESOURCES"/>
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
		<logic:iterate id="authorityAccountability" name="authorityAccountabilities" type="module.organization.domain.Accountability">
			<tr>
				<td>
					<bean:define id="url" type="java.lang.String">https://fenix.ist.utl.pt/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="authorityAccountability" property="child.user.username"/></bean:define>
					<img src="<%= url %>">
				</td>
				<td>
					<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="authorityAccountability" paramProperty="child.externalId">
						<fr:view name="authorityAccountability" property="child.presentationName"/>
					</html:link>
				</td>
				<td>
					<logic:notPresent name="authorityAccountability" property="functionDelegationDelegator">
						<fr:view name="authorityAccountability" property="accountabilityType.name"/>
					</logic:notPresent>
					<logic:present name="authorityAccountability" property="functionDelegationDelegator">
						<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showDelegationsForAuthorization" paramId="authorizationId" paramName="authorityAccountability" paramProperty="functionDelegationDelegator.accountabilityDelegator.externalId">
							<fr:view name="authorityAccountability" property="accountabilityType.name"/>
							<br/>
							<bean:message key="label.delegation.by" bundle="MISSION_RESOURCES"/>
							<fr:view name="authorityAccountability" property="functionDelegationDelegator.accountabilityDelegator.child.presentationName"/>
						</html:link>
					</logic:present>
				</td>
				<td>
					<fr:view name="authorityAccountability" property="beginDate"/>
				</td>
				<td>
					<logic:present name="authorityAccountability" property="endDate">
						<fr:view name="authorityAccountability" property="endDate"/>
					</logic:present>
				</td>
				<td>
					<html:link page="/missionOrganization.do?method=showDelegationsForAuthorization" paramId="authorizationId" paramName="authorityAccountability" paramProperty="externalId">
						<bean:size id="numberDelegations" name="authorityAccountability" property="functionDelegationDelegated"/>
						<bean:message key="label.delegations" bundle="MISSION_RESOURCES" arg0="<%= numberDelegations.toString() %>"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<br/>

<h3>
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unit.members"/>
</h3>
<br/>
<logic:empty name="workerAccountabilities">
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unit.members.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="workerAccountabilities">
	<table class="tstyle3">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.person" bundle="ORGANIZATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.member.type" bundle="MISSION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.beginDate" bundle="MISSION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.endDate" bundle="MISSION_RESOURCES"/>
			</th>
		</tr>
		<logic:iterate id="authorityAccountability" name="workerAccountabilities" type="module.organization.domain.Accountability">
			<tr>
				<td>
					<bean:define id="url" type="java.lang.String">https://fenix.ist.utl.pt/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="authorityAccountability" property="child.user.username"/></bean:define>
					<img src="<%= url %>">
				</td>
				<td>
					<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="authorityAccountability" paramProperty="child.externalId">
						<fr:view name="authorityAccountability" property="child.presentationName"/>
					</html:link>
				</td>
				<td>
					<fr:view name="authorityAccountability" property="accountabilityType.name"/>
				</td>
				<td>
					<fr:view name="authorityAccountability" property="beginDate"/>
				</td>
				<td>
					<logic:present name="authorityAccountability" property="endDate">
						<fr:view name="authorityAccountability" property="endDate"/>
					</logic:present>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
