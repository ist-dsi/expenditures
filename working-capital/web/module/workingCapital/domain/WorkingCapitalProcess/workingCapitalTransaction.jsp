<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<%@ page import="module.workingCapital.domain.WorkingCapitalAcquisitionSubmission" %>

<jsp:include page="shortBody.jsp"/>

<bean:define id="processId" name="process" property="externalId"/>
<html:link page='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'> 
	<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
</html:link>
<br/>
<br/>

<table class="tstyle3 width100pc">
	<tr>
		<jsp:include page="workingCapitalTransactionLineHeader.jsp"/>
	</tr>
	<tr>
		<jsp:include page="workingCapitalTransactionLine.jsp"/>
	</tr>
</table>

<logic:equal name="workingCapitalTransaction" property="submission" value="true">
	<p class="mtop15">
	<h3>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.domain.WorkingCapitalAcquisitionSubmission"/>
	</h3>
	
	<p>
	
	<fr:view name="workingCapitalTransaction">
		<fr:schema bundle="WORKING_CAPITAL_RESOURCES" type="module.workingCapital.domain.WorkingCapitalAcquisitionSubmission">
			<fr:slot name="paymentRequired" key="label.module.workingCapital.paymentRequired" />
		</fr:schema>
	</fr:view>
	
	<p>
	
	<bean:define id="workingCapitalSubmissionTransaction" name="workingCapitalTransaction"/>
	<logic:notEmpty name="workingCapitalTransaction" property="workingCapitalAcquisitionTransactions">
		<h3>
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.domain.WorkingCapitalAcquisitionSubmission.transactionsSubmitted"/>
		</h3>
		
		<p>
		
		<table class="tstyle3 width100pc">
			<tr>
				<th>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.number"/>
				</th>
				<th>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.description"/>
				</th>
				<th>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.approval"/>
				</th>
				<th>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.value"/>
				</th>
				<th>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.acquisition.acquisitionClassification"/>
				</th>
				<th>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.classifications.economicClassification"/>
				</th>
				<th>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.classifications.pocCode"/>
				</th>
			</tr>
			<logic:iterate id="workingCapitalAcquisitionTransaction" name="workingCapitalSubmissionTransaction" property="workingCapitalAcquisitionTransactionsSorted">
				<tr>
					<td>
						<fr:view name="workingCapitalAcquisitionTransaction" property="number"/>
					</td>
					<td>
						<fr:view name="workingCapitalAcquisitionTransaction" property="description"/>
					</td>
					<td>
						<img src="<%= request.getContextPath() + "/workingCapital/image/accept.gif" %>">
					</td>
					<td>
						<fr:view name="workingCapitalAcquisitionTransaction" property="value"/>
					</td>
					<td>
						<fr:view name="workingCapitalAcquisitionTransaction" property="workingCapitalAcquisition.acquisitionClassification.description"/>
					</td>
					<td>
						<fr:view name="workingCapitalAcquisitionTransaction" property="workingCapitalAcquisition.acquisitionClassification.economicClassification"/>
					</td>
					<td>
						<fr:view name="workingCapitalAcquisitionTransaction" property="workingCapitalAcquisition.acquisitionClassification.pocCode"/>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	
	<p>
	
	<logic:present name="workingCapitalSubmissionTransaction" property="document">
		<html:link  action="<%= "workflowProcessManagement.do?method=downloadFile&amp;fileId=" + ((WorkingCapitalAcquisitionSubmission) workingCapitalSubmissionTransaction).getDocument().getOid() + "&amp;processId=" + processId  %>" >
			<bean:message key="label.module.workingCapital.domain.WorkingCapitalAcquisitionSubmission.document" bundle="WORKING_CAPITAL_RESOURCES" />
		</html:link>
	</logic:present>
	
	
	
</logic:equal>

<logic:equal name="workingCapitalTransaction" property="payment" value="true">
	<p class="mtop15">
	<h3>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request"/>
	</h3>
	
	
	<bean:define id="workingCapitalRequest" name="workingCapitalTransaction" property="workingCapitalRequest"/>

	<table class="tstyle3 width100pc">
				<tr>
					<th> <bean:message key="label.module.workingCapital.requester" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
					<th> <bean:message key="label.module.workingCapital.request.creation" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
					<th> <bean:message key="label.module.workingCapital.request.requestedValue" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
					<th> <bean:message key="label.module.workingCapital.request.paymentMethod" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
					<th> <bean:message key="label.module.workingCapital.request.processedByTreasury" bundle="WORKING_CAPITAL_RESOURCES"/></th>
					<th> <bean:message key="label.module.workingCapital.request.dateTreasury" bundle="WORKING_CAPITAL_RESOURCES"/></th>				
				</tr>
				<tr>
					<td><fr:view name="workingCapitalRequest" property="workingCapitalRequester.firstAndLastName"/></td>
					<td><fr:view name="workingCapitalRequest" property="requestCreation"/></td>
					<td><fr:view name="workingCapitalRequest" property="requestedValue"/></td>
					<td><fr:view name="workingCapitalRequest" property="paymentMethod"/></td>
					<td><fr:view name="workingCapitalRequest" property="workingCapitalTreasuryProcessor.firstAndLastName"/></td>
					<td><fr:view name="workingCapitalRequest" property="processedByTreasury"/></td>
				</tr>
	</table>
	</p>
</logic:equal>

<logic:equal name="workingCapitalTransaction" property="refund" value="true">
	<p class="mtop15">
		<h3>
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.WorkingCapitalRefund"/>
		</h3>
	
		<table class="tstyle3 width100pc">
			<tr>
				<th> <bean:message key="label.module.workingCapital.refund.origin" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
				<th> <bean:message key="label.module.workingCapital.refund.value" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
				<th> <bean:message key="label.module.workingCapital.request.processedByTreasury" bundle="WORKING_CAPITAL_RESOURCES"/></th>
				<th> <bean:message key="label.module.workingCapital.request.dateTreasury" bundle="WORKING_CAPITAL_RESOURCES"/></th>				
			</tr>
			<tr>
				<td><fr:view name="workingCapitalTransaction" property="origin.firstAndLastName"/></td>
				<td><fr:view name="workingCapitalTransaction" property="refundedValue"/></td>
				<td><fr:view name="workingCapitalTransaction" property="person.firstAndLastName"/></td>
				<td><fr:view name="workingCapitalTransaction" property="transationInstant"/></td>
			</tr>
		</table>
	</p>
</logic:equal>

<logic:equal name="workingCapitalTransaction" property="acquisition" value="true">

	<p class="mtop15 mbottom15">
	<h3>
		<bean:message key="label.module.workingCapital.acquisition.details" bundle="WORKING_CAPITAL_RESOURCES"/>
	</h3>
	<div class="infobox mtop1 mbottom1">
		<fr:view name="workingCapitalTransaction" property="workingCapitalAcquisition">
			<fr:schema type="module.workingCapital.domain.WorkingCapitalTransaction" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="supplier.presentationName" key="label.module.workingCapital.acquisition.supplier"/>
				<fr:slot name="documentNumber" key="label.module.workingCapital.acquisition.documentNumber"/>
				<fr:slot name="description" key="label.module.workingCapital.acquisition.description"/>
				<fr:slot name="acquisitionClassification.description" key="label.module.workingCapital.acquisition.acquisitionClassification"/>
				<fr:slot name="acquisitionClassification.economicClassification" key="label.module.workingCapital.configuration.acquisition.classifications.economicClassification"/>
				<fr:slot name="acquisitionClassification.pocCode" key="label.module.workingCapital.configuration.acquisition.classifications.pocCode"/>
				<fr:slot name="valueWithoutVat" key="label.module.workingCapital.acquisition.valueWithoutVat"/>
				<fr:slot name="workingCapitalAcquisitionTransaction.value" key="label.module.workingCapital.acquisition.money"/>
			</fr:schema>
			<fr:layout name="tabular">
			</fr:layout>
		</fr:view>
		<logic:present name="workingCapitalTransaction" property="invoice">
			<bean:define id="url">/workflowProcessManagement.do?method=downloadFile&amp;processId=<bean:write name="workingCapitalTransaction" property="workingCapital.workingCapitalProcess.externalId"/></bean:define>
			<blockquote>
				<html:link action="<%= url %>" paramId="fileId" paramName="workingCapitalTransaction" paramProperty="invoice.externalId">
					<bean:write name="workingCapitalTransaction" property="invoice.filename"/>
				</html:link>
			</blockquote>
		</logic:present>
	</div>
	
	<logic:notEmpty name="workingCapitalTransaction" property="workingCapitalAcquisition.approver">
		
		<table class="tstyle3 mtop1 mbottom1 width100pc" >
				<tr>
					<th>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.operation.description"/>
					</th>
					<th>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.operation.date"/>
					</th>
					<th>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.operation.executor"/>
					</th>
				</tr>
				<tr>
					<td class="aleft">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.aprovalByUnitResponsible"/>
					</td>
					<td>
					<logic:equal name="workingCapitalTransaction" property="approved" value="true">
							<fr:view name="workingCapitalTransaction" property="workingCapitalAcquisition.approved"/>
					</logic:equal>
					<logic:equal name="workingCapitalTransaction" property="approved" value="false">
						-
					</logic:equal>
					</td>
					<td>
					<logic:equal name="workingCapitalTransaction" property="approved" value="true">
						<fr:view name="workingCapitalTransaction" property="workingCapitalAcquisition.approver.person.name"/>
					</logic:equal>
					<logic:equal name="workingCapitalTransaction" property="approved" value="false">
						-
					</logic:equal>
					</td>
				</tr>
				<tr>
					<td class="aleft">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.acquisition.verifier"/>
					</td>
					<td>
					<logic:equal name="workingCapitalTransaction" property="verified" value="true">
						<fr:view name="workingCapitalTransaction" property="workingCapitalAcquisition.verified"/>
					</logic:equal>
					<logic:equal name="workingCapitalTransaction" property="verified" value="false">
						-
					</logic:equal>
					</td>
					<td>
					<logic:equal name="workingCapitalTransaction" property="verified" value="true">
						<fr:view name="workingCapitalTransaction" property="workingCapitalAcquisition.verifier.name"/>
					</logic:equal>
					<logic:equal name="workingCapitalTransaction" property="verified" value="false">
						-
					</logic:equal>
					</td>
				</tr>
			</table>
		
	</logic:notEmpty>
	</p>
	<bean:define id="workingCapitalTransactionOid" type="java.lang.String" name="workingCapitalTransaction" property="externalId"/>
	<logic:equal name="workingCapitalTransaction" property="acquisition" value="true">
		<logic:equal name="workingCapitalTransaction" property="pendingApproval" value="true">
			 <wf:activityLink processName="process" activityName="CancelWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.CancelWorkingCapitalAcquisitionActivity"/>
			</wf:activityLink>
			&nbsp;&nbsp;&nbsp;
			 <wf:activityLink processName="process" activityName="EditWorkingCapitalActivity" scope="request" paramName0="workingCapitalAcquisitionTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.EditWorkingCapitalActivity"/>
			</wf:activityLink>
			&nbsp;&nbsp;&nbsp;
		</logic:equal>
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="pendingApproval" value="true">
		<wf:activityLink processName="process" activityName="ApproveWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.ApproveWorkingCapitalAcquisitionActivity"/>
		</wf:activityLink>
		&nbsp;&nbsp;&nbsp;
		<wf:activityLink processName="process" activityName="RejectWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.RejectWorkingCapitalAcquisitionActivity"/>
		</wf:activityLink>
		&nbsp;&nbsp;&nbsp;
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="approved" value="true">
		<logic:notPresent name="workingCapitalTransaction" property="workingCapitalAcquisition.submitedForVerification">
			<wf:activityLink processName="process" activityName="UnApproveWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnApproveWorkingCapitalAcquisitionActivity"/>
			</wf:activityLink>
			&nbsp;&nbsp;&nbsp;
		</logic:notPresent>
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="pendingVerification" value="true">
		<wf:activityLink processName="process" activityName="VerifyWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.VerifyWorkingCapitalAcquisitionActivity"/>
		</wf:activityLink>
		&nbsp;&nbsp;&nbsp;
		<wf:activityLink processName="process" activityName="RejectVerifyWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.RejectVerifyWorkingCapitalAcquisitionActivity"/>
		</wf:activityLink>
		&nbsp;&nbsp;&nbsp;
		<wf:activityLink processName="process" activityName="CorrectWorkingCapitalAcquisitionClassificationActivity" scope="request" paramName0="workingCapitalAcquisitionTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.CorrectWorkingCapitalAcquisitionClassificationActivity"/>
		</wf:activityLink>
		&nbsp;&nbsp;&nbsp;
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="paymentRequested" value="false">
		<logic:equal name="workingCapitalTransaction" property="verified" value="true">
			<wf:activityLink processName="process" activityName="UnVerifyWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnVerifyWorkingCapitalAcquisitionActivity"/>
			</wf:activityLink>
			&nbsp;&nbsp;&nbsp;
		</logic:equal>
	</logic:equal>
</logic:equal>

