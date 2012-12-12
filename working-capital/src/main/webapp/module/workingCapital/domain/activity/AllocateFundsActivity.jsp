<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.authorizedAnualValue"/>:
			<fr:view name="workingCapitalInitialization" property="authorizedAnualValue"/>

			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.maxAuthorizedAnualValue"/>:
			<fr:view name="workingCapitalInitialization" property="maxAuthorizedAnualValue"/>

			<logic:equal name="workingCapitalInitialization" property="class.name" value="module.workingCapital.domain.WorkingCapitalInitializationReenforcement">
				<br/>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.requestedReenforcementValue"/>:
				<fr:view name="workingCapitalInitialization" property="requestedReenforcementValue"/>
			</logic:equal>
	</p>
</div>

<div class="dinline forminline">	

	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
	
		<fr:edit id="activityBean" name="information" visible="false"/>
	
		<fr:edit id="workingCapitalInitialization" name="information">
			<fr:schema type="module.workingCapital.domain.WorkingCapitalInitialization" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="fundAllocationId" key="label.module.workingCapital.fundAllocationId" required="true"/>
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
