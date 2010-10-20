<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="module.organization.domain.OrganizationalModel"%>
<%@page import="myorg.domain.MyOrg"%>

<div class="infobox">
	<table>
		<tr>
			<td width="20%">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.requester.unit"/>
			</td>
			<td width="35%">
				<logic:present name="process" property="mission.missionResponsible">
					<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="process" paramProperty="mission.missionResponsible.externalId">
						<fr:view name="process" property="mission.missionResponsible.presentationName"/>
					</html:link>
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
				<fr:view name="process" property="mission.location"/> 
			</td>
			<td width="15%">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.duration"/>
			</td>
			<td width="30%">
				<fr:view name="process" property="mission.daparture"/> - <fr:view name="process" property="mission.arrival"/>
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
		<jsp:include page="../viewMissionQueue.jsp"/>
	</table>
</div>

<jsp:include page="../viewPaymentProcesses.jsp"/>
