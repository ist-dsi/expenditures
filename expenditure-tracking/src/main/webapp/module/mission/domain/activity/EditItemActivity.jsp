<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="information" name="information" type="module.mission.domain.activity.ItemActivityInformation"/>
<bean:define id="process" name="information" property="process"/>

<br/>

	<h3>
		<bean:define id="labelKey" type="java.lang.String">label.<bean:write name="information" property="missionItem.class.name"/></bean:define>
		<bean:message bundle="MISSION_RESOURCES" key="<%= labelKey %>"/>
	</h3>

		<logic:equal name="information" property="missionItem.class.name" value="module.mission.domain.RentedVehiclItem">
			<div class="highlightBox">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.VehiclItem.requirements.message"/>
			</div>
		</logic:equal>
		<logic:equal name="information" property="missionItem.class.name" value="module.mission.domain.PersonalVehiclItem">
			<div class="highlightBox">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.VehiclItem.requirements.message"/>
			</div>
		</logic:equal>

	<logic:equal name="information" property="missionItem.class.name" value="module.mission.domain.AccommodationItem">
		<div class="highlightBox">
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.AccommodationItem.requirements.message"/>
		</div>
	</logic:equal>
	<fr:form action="/missionProcess.do">
		<html:hidden  property="method" value="addMissionItem"/>
		<bean:define id="processId" type="java.lang.String" name="process" property="externalId"/>
		<html:hidden property="processId" value="<%= processId %>"/>

		<logic:present name="information" property="missionItem">
			<bean:define id="schemaName" type="java.lang.String" name="information" property="missionItem.class.name"/>
			<fr:edit id="missionItem" name="information" property="missionItem" schema="<%= schemaName %>">
				<fr:layout name="tabular">
					<fr:property name="classes" value="form listInsideClear" />
					<fr:property name="columnClasses" value=",,tderror" />
				</fr:layout>
				<fr:destination name="invalid" path="/missionProcess.do?method=activityInformationPostback"/>
			</fr:edit>

			<bean:size id="numberPeople" name="information" property="process.mission.participantes"/>
			<logic:notEqual name="numberPeople" value="1">
				<h4>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.item.participants"/>
				</h4>

				<fr:edit id="information.participants" name="information">
					<fr:schema type="module.mission.domain.activity.ItemActivityInformation" bundle="MISSION_RESOURCES">
						<fr:slot name="people" layout="option-select" key="label.mission.item.people">
							<fr:property name="eachSchema" value="person-name"/>
							<fr:property name="eachLayout" value="values-dash"/>
					        <fr:property name="providerClass" value="module.mission.presentationTier.provider.MissionItemParticipantProvider" />
					        <fr:property name="classes" value="nobullet noindent"/>
					        <fr:property name="sortBy" value="user.name"/>
					    </fr:slot>
					<% if(information.getMissionItem().isVehicleItem()) { %>
						<fr:slot name="driver" layout="radio-select" key="label.mission.item.driver" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
							<fr:property name="eachSchema" value="person-name"/>
							<fr:property name="eachLayout" value="values-dash"/>
						    <fr:property name="providerClass" value="module.mission.presentationTier.provider.MissionItemParticipantProvider" />
						    <fr:property name="classes" value="nobullet noindent"/>
						    <fr:property name="sortBy" value="user.name"/>
						</fr:slot>
					<% } %>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="form listInsideClear" />
						<fr:property name="columnClasses" value="width100px,,tderror" />
					</fr:layout>
				</fr:edit>
			</logic:notEqual>
		</logic:present>

		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>

		<fr:edit id="information" name="information" schema="module.mission.domain.activity.ItemActivityInformation.empty">
		</fr:edit>
	</fr:form>
