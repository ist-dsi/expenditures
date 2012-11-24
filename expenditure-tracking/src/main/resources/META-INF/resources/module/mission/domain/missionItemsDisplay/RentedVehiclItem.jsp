<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<div class="infobox">

	<table class="tstyle1" style="width: 100%;">
		<tr>
			<th style="width: 185px;">
				<bean:message key="label.mission.item.type" bundle="MISSION_RESOURCES"/>:
			</th>
			<td>
				<fr:view name="missionItem" property="localizedName"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.mission.item.vehicle.justification" bundle="MISSION_RESOURCES"/>:
			</th>
			<td>
				<fr:view name="missionItem" property="vehiclItemJustification.justification"/>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.mission.item.participants" bundle="MISSION_RESOURCES"/>:</th>
			<td>
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
			<td>
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
			<td>
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
			<td>
				<fr:view name="missionItem" property="itinerary"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.mission.item.value" bundle="MISSION_RESOURCES"/>:
			</th>
			<td>
				<fr:view name="missionItem" property="value"/>
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
