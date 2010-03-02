<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>
<bean:define id="workingCapitalInitialization" name="information" property="workingCapitalInitialization"/>
<bean:define id="workingCapital" name="workingCapitalInitialization" property="workingCapital"/>

<div class="infobox mtop1 mbottom1">
	<p>
			<bean:define id="workingCapitalInitializationOid" type="java.lang.String" name="workingCapitalInitialization" property="externalId"/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.movementResponsible"/>:
			<bean:write name="workingCapital" property="movementResponsible.name"/>

			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.fiscalId"/>:
			<bean:write name="workingCapitalInitialization" property="fiscalId"/>

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.internationalBankAccountNumber"/>:
			<bean:write name="workingCapitalInitialization" property="internationalBankAccountNumber"/>

			<br/>
			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.requestedAnualValue.requested"/>:
			<fr:view name="workingCapitalInitialization" property="requestedAnualValue"/>
	</p>
</div>


<div class="dinline forminline">	

	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
	
		<fr:edit id="activityBean" name="information" visible="false"/>
	
		<fr:edit id="workingCapitalInitialization" name="information">
			<fr:schema type="module.workingCapital.domain.WorkingCapitalInitialization" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="authorizedAnualValue" key="label.module.workingCapital.authorizedAnualValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="maxAuthorizedAnualValue" key="label.module.workingCapital.maxAuthorizedAnualValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form listInsideClear" />
				<fr:property name="columnClasses" value="width100px,,tderror" />
			</fr:layout>
		</fr:edit>
	
		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

</div>