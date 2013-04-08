<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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
	<fr:form id="distributeCostsForm" action="/missionProcess.do">
		<html:hidden  property="method" value="distributeMissionItemCosts"/>
		<bean:define id="processId" type="java.lang.String" name="process" property="externalId"/>
		<html:hidden property="processId" value="<%= processId %>"/>

		<logic:present name="information" property="missionItem">
			<bean:size id="numberFinancers" name="information" property="missionItemFinancerBeans"/>
			<logic:notEqual name="numberFinancers" value="0">
			<logic:notEqual name="numberFinancers" value="1">
				<h4>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.item.cost.distribution"/>
				</h4>

				<fr:edit id="information.mission.item.financer.beans" name="information" property="missionItemFinancerBeans" schema="module.mission.domain.activity.DistributeItemCostsActivityInformation.MissionItemFinancerBean">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle3 inputInsideAlignRight"/>
					</fr:layout>
				</fr:edit>

				<p class="mtop05 mbottom2">
					<a href="javascript:document.getElementById('distributeCostsForm').method.value='distributeMissionItemFinancerValues'; document.getElementById('distributeCostsForm').submit();">
						<bean:message key="label.mission.item.cost.distribution.automatically" bundle="MISSION_RESOURCES"/>
					</a>
				</p>
			</logic:notEqual>
			</logic:notEqual>
		</logic:present>

		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>

		<fr:edit id="information" name="information" schema="module.mission.domain.activity.ItemActivityInformation.empty">
		</fr:edit>
	</fr:form>
