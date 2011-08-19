<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<div class="infobox">
	<bean:message key="acquisitionRequestItem.message.info.help.prefix" bundle="ACQUISITION_RESOURCES"/>
	<html:link action="/expenditureTrackingOrganization.do?method=listCPVReferences" styleClass="bluelink" target="_blank">
		<bean:message key="acquisitionRequestItem.message.info.help.infix" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
</div>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>
		
<div class="dinline forminline">

<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>

<fr:edit id="activityBean" name="information" visible="false"/>

<div class="form1">
	<fr:edit id="acquisitionRequestItem.block1" name="information" schema="activityInformation.EditRefundItem">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form thwidth150px"/>
			<fr:property name="columnClasses" value=",,tderror"/>
			<fr:property name="requiredMarkShown" value="true"/>
		</fr:layout>
		<fr:destination name="invalid" path="/expenditureProcesses.do?method=itemInvalidInfo"/>
	</fr:edit>
</div>

<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/></html:submit>
</fr:form>
<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:submit>
</fr:form>
</div>