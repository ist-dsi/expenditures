<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="module.organization.domain.OrganizationalModel"%>
<%@page import="pt.ist.bennu.core.domain.MyOrg"%>

<div class="infobox">
	<table>
		<tr>
			<td width="20%">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.requester.unit"/>
			</td>
			<td width="35%">
				<logic:present name="process" property="mission.missionResponsible">
					<logic:equal name="process" property="mission.missionResponsible.class.name" value="module.organization.domain.Unit">
						<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="process" paramProperty="mission.missionResponsible.externalId">
							<fr:view name="process" property="mission.missionResponsible.presentationName"/>
						</html:link>
					</logic:equal>
					<logic:equal name="process" property="mission.missionResponsible.class.name" value="module.organization.domain.Person">
						<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="process" paramProperty="mission.missionResponsible.externalId">
							<fr:view name="process" property="mission.missionResponsible.name"/>
						</html:link>
					</logic:equal>
				</logic:present>
				<logic:notPresent name="process" property="mission.missionResponsible">
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.requester.unit.not.defined"/>
				</logic:notPresent>
			</td>
			<td width="15%">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.requester.person"/>
			</td>
			<td width="30%">
				<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="process" paramProperty="mission.requestingPerson.externalId">
					<fr:view name="process" property="mission.requestingPerson.name"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<td width="20%">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.location"/>
			</td>
			<td width="35%">
				<fr:view name="process" property="mission.location"/>, <logic:present name="process" property="mission.country"><fr:view name="process" property="mission.country.name"/></logic:present>
			</td>
			<td width="15%">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.duration"/>
			</td>
			<td width="30%" style="white-space: nowrap;">
				<fr:view name="process" property="mission.daparture"/> - <fr:view name="process" property="mission.arrival"/>  (<fr:view name="process" property="mission.durationInDays" /> <bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.days"/>)
			</td>
		</tr>
		<tr>
			<td width="20%">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.objective"/>
			</td>
			<td colspan="3">
				<fr:view name="process" property="mission.objective"/>
			</td>
		</tr>
		<tr>
			<td width="20%">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.value"/>
			</td>
			<td colspan="3">
				<fr:view name="process" property="mission.value"/>
			</td>
		</tr>
		<jsp:include page="viewMissionQueue.jsp"/>
	</table>
</div>
