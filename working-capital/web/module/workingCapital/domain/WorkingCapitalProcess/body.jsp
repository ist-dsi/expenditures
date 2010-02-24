<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization"/>
</h3>

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


<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request"/>
</h3>

<logic:empty name="workingCapital" property="workingCapitalRequests">
	<p>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.requests.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="workingCapital" property="workingCapitalRequests">
	<div class="infobox mtop1 mbottom1">
		<p>
			<% boolean hasOne = false; %>
			<logic:iterate id="workingCapitalRequest" name="workingCapital" property="workingCapitalRequests">
				<logic:notPresent name="workingCapitalRequest" property="workingCapitalPayment">
					<% hasOne = true; %>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.creation"/>:
					<fr:view name="workingCapitalRequest" property="requestCreation"/>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.requester"/>:
					<bean:write name="workingCapitalRequest" property="workingCapitalRequester.child.name"/>

					<br/>
					<br/>

					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.requestedValue"/>:
					<fr:view name="workingCapitalRequest" property="requestedValue"/>

					&nbsp;&nbsp;

					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.paymentMethod"/>:
					<fr:view name="workingCapitalRequest" property="paymentMethod"/>

					<br/>
					<br/>

					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.processedByTreasury"/>:
					<logic:present name="workingCapitalRequest" property="processedByTreasury">
						<fr:view name="workingCapitalRequest" property="processedByTreasury"/>
						<logic:present name="workingCapitalRequest" property="workingCapitalTreasuryProcessor">
							<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.treasuryProcessor"/>:
							<fr:view name="workingCapitalRequest" property="workingCapitalTreasuryProcessor.name"/>
						</logic:present>
					</logic:present>
					<logic:notPresent name="workingCapitalRequest" property="processedByTreasury">
						<bean:define id="workingCapitalRequestOid" type="java.lang.String" name="workingCapitalRequest" property="externalId"/>
						<wf:activityLink processName="process" activityName="PayCapitalActivity" scope="request" paramName0="workingCapitalRequest" paramValue0="<%= workingCapitalRequestOid %>">
							<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.PayCapitalActivity"/>
						</wf:activityLink>
					</logic:notPresent>
				</logic:notPresent>
			</logic:iterate>
			<% if (!hasOne) { %>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.none.pending"/>:
			<% } %>
		</p>
	</div>
</logic:notEmpty>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transactions"/>
</h3>

<logic:empty name="workingCapital" property="workingCapitalTransactions">
	<p>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transactions.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="workingCapital" property="workingCapitalTransactions">
	<br/>
	<fr:view name="workingCapital" property="sortedWorkingCapitalTransactions">
		<fr:schema type="module.workingCapital.domain.WorkingCapitalTransaction" bundle="WORKING_CAPITAL_RESOURCES">
			<fr:slot name="number" key="label.module.workingCapital.transaction.number"/>
			<fr:slot name="description" key="label.module.workingCapital.transaction.description"/>
			<fr:slot name="value" key="label.module.workingCapital.transaction.value"/>
			<fr:slot name="accumulatedValue" key="label.module.workingCapital.transaction.accumulatedValue"/>
			<fr:slot name="balance" key="label.module.workingCapital.transaction.balance"/>
			<fr:slot name="debt" key="label.module.workingCapital.transaction.debt"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>

			<fr:property name="linkFormat(edit)" value="/workingCapital.do?method=viewWorkingCapitalTransaction&workingCapitalTransactionOid=${externalId}"/>
			<fr:property name="bundle(edit)" value="WORKING_CAPITAL_RESOURCES"/>
			<fr:property name="key(edit)" value="link.view"/>
			<fr:property name="order(edit)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
