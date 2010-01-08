<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="processId" name="process" property="externalId"/>
<bean:define id="name" name="information" property="activityName"/>

<div class="forminline mbottom2">

<fr:form id="form" action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
	<fr:edit  
		id="activityBean"
		name="information" visible="false"/>

	<fr:edit 
		id="financersAccountingUnits" 
		schema="activityInformation.ChangeFinancersAccountingUnit" 
		name="information" property="beans">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>
</fr:form>

<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>

