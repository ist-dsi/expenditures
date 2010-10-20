<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

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

				<fr:edit id="information.participants" name="information" schema="module.mission.domain.activity.ItemActivityInformation.participants">
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
