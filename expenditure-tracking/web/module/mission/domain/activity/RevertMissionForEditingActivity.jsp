<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<div class="highlightBox">
	<bean:message key="label.RevertMissionForEditingActivity.warning" bundle="MISSION_RESOURCES"/>:
</div>

<div class="dinline forminline">

	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>

		<fr:edit id="activityBean" name="information">
			<fr:schema type="module.mission.domain.activity.RevertMissionForEditingActivityInformation" bundle="MISSION_RESOURCES">
   				<fr:slot name="description" key="label.change.justification.description" layout="longText">
					<fr:property name="columns" value="80"/>
					<fr:property name="rows" value="10"/>
   				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form listInsideClear" />
				<fr:property name="columnClasses" value="width100px,,tderror" />
			</fr:layout>
		</fr:edit>

		<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

</div>