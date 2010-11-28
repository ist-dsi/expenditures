<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/chart.tld" prefix="chart" %>

<h2>
	<bean:message key="label.delegation.competence" bundle="MISSION_RESOURCES"/>
</h2>

<div class="infobox">
	<table>
		<tr>
			<th style="padding-right: 25px;" rowspan="4">
				<bean:define id="url" type="java.lang.String">https://fenix.ist.utl.pt/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="accountability" property="child.user.username"/></bean:define>
				<img src="<%= url %>">
			</th>
			<th style="text-align: left; padding-right: 10px;">
				<bean:message key="label.person" bundle="ORGANIZATION_RESOURCES"/>
			</th>
			<td style="text-align: left;">
				<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="accountability" paramProperty="child.externalId">
					<fr:view name="accountability" property="child.presentationName"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<th style="text-align: left; padding-right: 10px;">
				<bean:message key="label.unit" bundle="ORGANIZATION_RESOURCES"/>
			</th>
			<td style="text-align: left;">
				<html:link page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="accountability" paramProperty="parent.externalId" styleClass="secondaryLink">
					<fr:view name="accountability" property="parent.presentationName"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<th style="text-align: left; padding-right: 10px;">
				<bean:message key="label.mission.authority.type" bundle="MISSION_RESOURCES"/>
			</th>
			<td style="text-align: left;">
				<fr:view name="accountability" property="accountabilityType.name"/>
			</td>
		</tr>
		<tr>
			<th style="text-align: left; padding-right: 10px;">
				<bean:message key="label.mission.authority.interval" bundle="MISSION_RESOURCES"/>
			</th>
			<td style="text-align: left;">
				<fr:view name="accountability" property="beginDate"/>
				->
				<logic:present name="accountability" property="endDate">
					<fr:view name="accountability" property="endDate"/>
				</logic:present>
			</td>
		</tr>
	</table>
</div>

<br/>

