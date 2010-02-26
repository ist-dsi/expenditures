<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<jsp:include page="shortBody.jsp"/>

<table class="tstyle3 width100pc">
	<tr>
		<jsp:include page="workingCapitalTransactionLineHeader.jsp"/>
	</tr>
	<tr>
		<jsp:include page="workingCapitalTransactionLine.jsp"/>
	</tr>
</table>

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
					<td><fr:view name="workingCapitalRequest" property="workingCapitalRequester.child.firstAndLastName"/></td>
					<td><fr:view name="workingCapitalRequest" property="requestCreation"/></td>
					<td><fr:view name="workingCapitalRequest" property="requestedValue"/></td>
					<td><fr:view name="workingCapitalRequest" property="paymentMethod"/></td>
					<td><fr:view name="workingCapitalRequest" property="workingCapitalTreasuryProcessor.firstAndLastName"/></td>
					<td><fr:view name="workingCapitalRequest" property="processedByTreasury"/></td>
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
	</div>
	
	<logic:notEmpty name="workingCapitalTransaction" property="workingCapitalAcquisition.approver">
		
		<table class="tstyle3 mtop1 mbottom1 width100pc" >
				<tr><th>Operação</th><th>Data</th><th>Pessoa</th></tr>
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
						<fr:view name="workingCapitalTransaction" property="workingCapitalAcquisition.verifier.child.name"/>
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
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="approved" value="true">
		<logic:notEqual name="workingCapitalTransaction" property="verified" value="true">
			<wf:activityLink processName="process" activityName="UnApproveWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnApproveWorkingCapitalAcquisitionActivity"/>
			</wf:activityLink>
			&nbsp;&nbsp;&nbsp;
		</logic:notEqual>
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="pendingVerification" value="true">
		<wf:activityLink processName="process" activityName="VerifyWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.VerifyWorkingCapitalAcquisitionActivity"/>
		</wf:activityLink>
		&nbsp;&nbsp;&nbsp;
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="verified" value="true">
		<wf:activityLink processName="process" activityName="UnVerifyWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnVerifyWorkingCapitalAcquisitionActivity"/>
		</wf:activityLink>
		&nbsp;&nbsp;&nbsp;
	</logic:equal>
</logic:equal>

