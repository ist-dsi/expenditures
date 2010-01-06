<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization"/>
</h3>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<div class="infobox mtop1 mbottom1">
	<p>
		<logic:iterate id="workingCapitalInitialization" name="workingCapital" property="sortedWorkingCapitalInitializations">
			<bean:define id="workingCapitalInitializationOid" type="java.lang.String" name="workingCapitalInitialization" property="externalId"/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.movementResponsible"/>:
			<bean:write name="workingCapital" property="movementResponsible.name"/>

			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.fiscalId"/>:
			<bean:write name="workingCapitalInitialization" property="fiscalId"/>

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.bankAccountId"/>:
			<bean:write name="workingCapitalInitialization" property="bankAccountId"/>

			<br/>
			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.requestedAnualValue.requested"/>:
			<fr:view name="workingCapitalInitialization" property="requestedAnualValue"/>

			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.authorizedAnualValue"/>:
			<logic:present name="workingCapitalInitialization" property="authorizedAnualValue">
				<strong>
					<fr:view name="workingCapitalInitialization" property="authorizedAnualValue"/>
				</strong>
			</logic:present>

			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.maxAuthorizedAnualValue"/>:
			<logic:present name="workingCapitalInitialization" property="maxAuthorizedAnualValue">
				<fr:view name="workingCapitalInitialization" property="maxAuthorizedAnualValue"/>
			</logic:present>

			<br/>
			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.creation"/>:
			<fr:view name="workingCapitalInitialization" property="requestCreation"/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.requester"/>:
			<bean:write name="workingCapitalInitialization" property="requestor.name"/>

			<br/>
			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.aprovalByUnitResponsible"/>:
			<logic:present name="workingCapitalInitialization" property="aprovalByUnitResponsible">
				<fr:view name="workingCapitalInitialization" property="aprovalByUnitResponsible"/>
			</logic:present>

			<logic:present name="workingCapitalInitialization" property="responsibleForUnitApproval">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.UnitAprovalResponsible"/>:
				<bean:write name="workingCapitalInitialization" property="responsibleForUnitApproval.person.name"/>
			</logic:present>
			<wf:activityLink processName="process" activityName="ApproveActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.ApproveActivity"/>
			</wf:activityLink>
			<wf:activityLink processName="process" activityName="UnApproveActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnApproveActivity"/>
			</wf:activityLink>

			<br/>
			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.verificationByAccounting"/>:
			<logic:present name="workingCapitalInitialization" property="verificationByAccounting">
				<fr:view name="workingCapitalInitialization" property="verificationByAccounting"/>
			</logic:present>

			<logic:present name="workingCapitalInitialization" property="responsibleForAccountingVerification">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.AccountingResponsible"/>:
				<bean:write name="workingCapitalInitialization" property="responsibleForAccountingVerification.child.name"/>
			</logic:present>
			<wf:activityLink processName="process" activityName="VerifyActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.VerifyActivity"/>
			</wf:activityLink>
			<wf:activityLink processName="process" activityName="UnVerifyActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnVerifyActivity"/>
			</wf:activityLink>

			<br/>
			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.authorizationByUnitResponsible"/>:
			<logic:present name="workingCapitalInitialization" property="authorizationByUnitResponsible">
				<fr:view name="workingCapitalInitialization" property="authorizationByUnitResponsible"/>
			</logic:present>

			<logic:present name="workingCapitalInitialization" property="responsibleForUnitAuthorization">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.UnitAuthorizationResponsible"/>:
				<bean:write name="workingCapitalInitialization" property="responsibleForUnitAuthorization.child.name"/>
			</logic:present>
			<wf:activityLink processName="process" activityName="AuthorizeActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.AuthorizeActivity"/>
			</wf:activityLink>
			<wf:activityLink processName="process" activityName="UnAuthorizeActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnAuthorizeActivity"/>
			</wf:activityLink>

		</logic:iterate>
	</p>
</div>

