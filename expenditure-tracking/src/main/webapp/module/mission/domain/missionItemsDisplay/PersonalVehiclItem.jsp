<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<bean:define id="missionItem" name="missionItem" type="module.mission.domain.VehiclItem"/>
<bean:define id="process" name="process"/>

<bean:define id="theme" name="virtualHost" property="theme.name"/>
	
<div class="infobox">

	<table class="tstyle1" style="width: 100%;">
		<tr>
			<th style="width: 185px;">
				<bean:message key="label.mission.item.type" bundle="MISSION_RESOURCES"/>:
			</th>
			<td colspan="3">
				<fr:view name="missionItem" property="localizedName"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.mission.item.vehicle.justification" bundle="MISSION_RESOURCES"/>:
			</th>
			<td colspan="3">
				<fr:view name="missionItem" property="vehiclItemJustification.justification"/>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.mission.item.participants" bundle="MISSION_RESOURCES"/>:</th>
			<td colspan="3">
				<fr:view name="missionItem" property="people">
					<fr:layout name="separator-list">
						<fr:property name="eachLayout" value="values"/>
						<fr:property name="eachSchema" value="person-name"/>
						<fr:property name="separator" value=","/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.mission.item.driver" bundle="MISSION_RESOURCES"/>:</th>
			<td colspan="3">
				<logic:present name="missionItem" property="driver">
					<fr:view name="missionItem" property="driver" schema="person-name" layout="values"/>
				</logic:present>
				<logic:notPresent name="missionItem" property="driver">
					-
				</logic:notPresent>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.mission.item.financers" bundle="MISSION_RESOURCES"/>:</th>
			<td colspan="3">
				<fr:view name="missionItem" property="missionItemFinancers">
					<fr:layout>
						<fr:property name="classes" value="nobullet"/>
						<fr:property name="eachSchema" value="module.mission.domain.MissionItemFinancer"/>
					    <fr:property name="eachLayout" value="nonNullValues-commaSeparated"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.mission.item.transportation.itinerary" bundle="MISSION_RESOURCES"/>:
			</th>
			<td colspan="3">
				<fr:view name="missionItem" property="itinerary"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.mission.item.vehicle.kms" bundle="MISSION_RESOURCES"/>:
			</th>
			<td colspan="3">
				<fr:view name="missionItem" property="kms"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.mission.item.value" bundle="MISSION_RESOURCES"/>:
			</th>
			<td>
				<fr:view name="missionItem" property="value"/>
			</td>
			<td rowspan="2" style="text-align:right;">
				<% if (missionItem.isAuthorized()) { %>
					<wf:activityLink activityName="UnAuthorizeVehicleItemActivity" processName="process" scope="request" paramName0="missionItem" paramValue0="<%= missionItem.getExternalId() %>"/>
				<% } else { %>
					<wf:activityLink activityName="AuthorizeVehicleItemActivity" processName="process" scope="request" paramName0="missionItem" paramValue0="<%= missionItem.getExternalId() %>"/>
				<% } %>
			</td>
			<td rowspan="2" style="text-align:right;">
				<% if (missionItem.isAuthorized()) { %>
					<img src="<%= request.getContextPath() + "/CSS/" + theme + "/images/accept.gif"%>"/>
					<bean:message key="label.authorized" bundle="MISSION_RESOURCES"/>
				<% } else { %>
					<img src="<%= request.getContextPath() + "/CSS/" + theme + "/images/icon_error.png"%>"/>
					<bean:message key="label.authorized.not" bundle="MISSION_RESOURCES"/>
				<% } %>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.mission.item.vehicle.justification.motive" bundle="MISSION_RESOURCES"/>:
			</th>
			<td>
				<fr:view name="missionItem" property="vehiclItemJustification.motive"/>
			</td>
		</tr>
	</table>
	
</div>
