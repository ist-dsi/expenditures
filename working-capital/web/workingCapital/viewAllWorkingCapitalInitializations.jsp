<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="module.workingCapital.title.showAllInitializations" bundle="WORKING_CAPITAL_RESOURCES"/>
</h2>

<bean:define id="process" name="workingCapital" property="workingCapitalProcess" toScope="request"/>
<bean:define id="processId" name="process" property="externalId"/>

<jsp:include page="../module/workingCapital/domain/WorkingCapitalProcess/header.jsp"/>

  
<html:link page='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'> 
	<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
</html:link>

<logic:iterate id="workingCapitalInitialization" name="workingCapital" property="sortedWorkingCapitalInitializations">

	<div class="infobox mtop1 mbottom1">
		<fr:view name="workingCapitalInitialization">
			<fr:schema bundle="WORKING_CAPITAL_RESOURCES"  type="module.workingCapital.domain.WorkingCapitalInitialization">
				<fr:slot name="requestCreation" key="label.module.workingCapital.requestingDate"/>
				<fr:slot name="requestor.name"  key="label.module.workingCapital.requester"/>
				<fr:slot name="workingCapital.movementResponsible.name" key="label.module.workingCapital.movementResponsible"/>
				<fr:slot name="fiscalId"  key="label.module.workingCapital.fiscalId"/>
				<fr:slot name="internationalBankAccountNumber"  key="label.module.workingCapital.internationalBankAccountNumber"/>
				<fr:slot name="requestedAnualValue"  key="label.module.workingCapital.requestedAnualValue.requested"/>
				<fr:slot name="authorizedAnualValue"  key="label.module.workingCapital.authorizedAnualValue" layout="null-as-label">
					<fr:property name="subLayout" value=""/>
					<fr:property name="classes" value="bold"/>
				</fr:slot>
				<fr:slot name="maxAuthorizedAnualValue"  key="label.module.workingCapital.maxAuthorizedAnualValue" layout="null-as-label">
					<fr:property name="subLayout" value=""/>
				</fr:slot>
				<logic:present name="workingCapital" property="workingCapitalInitialization.lastSubmission">
					<fr:slot name="lastSubmission" key="label.module.workingCapital.initialization.lastSubmission"/>
				</logic:present>
				<logic:present name="workingCapital" property="workingCapitalInitialization.refundRequested">
					<fr:slot name="refundRequested" key="label.module.workingCapital.initialization.refundRequested"/>
				</logic:present>
				<logic:equal name="workingCapital" property="workingCapitalInitialization.class.name" value="module.workingCapital.domain.WorkingCapitalInitializationReenforcement">
					<fr:slot name="requestedReenforcementValue" key="label.module.workingCapital.initialization.requestedReenforcementValue"/>
					<fr:slot name="authorizedReenforcementValue" key="label.module.workingCapital.initialization.authorizedReenforcementValue"/>
				</logic:equal>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="columnClasses" value="aleft width215px,,"/>
			</fr:layout>
		</fr:view>
	</div>

	<table class="tstyle3 mtop1 mbottom1 width100pc" >
		<tr><th><bean:message key="label.module.workingCapital.operations" bundle="WORKING_CAPITAL_RESOURCES"/></th>
		<th><bean:message key="label.module.workingCapital.date" bundle="WORKING_CAPITAL_RESOURCES"/></th>
		<th><bean:message key="label.module.workingCapital.person" bundle="WORKING_CAPITAL_RESOURCES"/></th>
		<tr>
			<td class="aleft">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.aprovalByUnitResponsible"/>
			</td>
			<td>
				<logic:present name="workingCapitalInitialization" property="aprovalByUnitResponsible">
					<fr:view name="workingCapitalInitialization" property="aprovalByUnitResponsible"/>
				</logic:present>
				<logic:notPresent name="workingCapitalInitialization" property="aprovalByUnitResponsible">
					-
				</logic:notPresent>
			</td>
			<td>
			<logic:present name="workingCapitalInitialization" property="responsibleForUnitApproval">
				<fr:view name="workingCapitalInitialization" property="responsibleForUnitApproval.person.firstAndLastName"/>
			</logic:present>
			<logic:notPresent name="workingCapitalInitialization" property="responsibleForUnitApproval">
				-
			</logic:notPresent>
			</td>
		</tr>

		<tr>
			<td class="aleft">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.verificationByAccounting"/>
			</td>
			<td>
				<logic:present name="workingCapitalInitialization" property="verificationByAccounting">
					<fr:view name="workingCapitalInitialization" property="verificationByAccounting"/>
				</logic:present>
				<logic:notPresent name="workingCapitalInitialization" property="verificationByAccounting">
					-
				</logic:notPresent>
			</td>
			<td>
			<logic:present name="workingCapitalInitialization" property="responsibleForAccountingVerification">
				<fr:view name="workingCapitalInitialization" property="responsibleForAccountingVerification.firstAndLastName"/>
			</logic:present>
			<logic:notPresent name="workingCapitalInitialization" property="responsibleForAccountingVerification">
				-
			</logic:notPresent>
			</td>
		</tr>
			
		<tr>
			<td class="aleft">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.authorizationByUnitResponsible"/>
			</td>
			<td>
				<logic:present name="workingCapitalInitialization" property="authorizationByUnitResponsible">
					<fr:view name="workingCapitalInitialization" property="authorizationByUnitResponsible"/>
				</logic:present>
				<logic:notPresent name="workingCapitalInitialization" property="authorizationByUnitResponsible">
					-
				</logic:notPresent>
			</td>
			<td>
			<logic:present name="workingCapitalInitialization" property="responsibleForUnitAuthorization">
				<fr:view name="workingCapitalInitialization" property="responsibleForUnitAuthorization.child.firstAndLastName"/>
			</logic:present>
			<logic:notPresent name="workingCapitalInitialization" property="responsibleForUnitAuthorization">
				-
			</logic:notPresent>
			</td>
		</tr>
	</table>

</logic:iterate>