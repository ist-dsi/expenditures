<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<div class="infobox">

	<table class="tstyle1" style="width: 100%;">
		<tr>
			<th style="width: 185px;"><bean:message key="label.mission.item.type" bundle="MISSION_RESOURCES"/>:</th>
			<td><fr:view name="missionItem" property="localizedName"/></td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.mission.item.accommodation.numberOfNights" bundle="MISSION_RESOURCES"/>:
			</th>
			<td>
				<fr:view name="missionItem" property="numberOfNights"/>
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
	</table>
</div>

