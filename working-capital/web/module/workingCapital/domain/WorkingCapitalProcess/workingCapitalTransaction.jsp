<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<jsp:include page="shortBody.jsp"/>

<table class="tstyle2">
	<tr>
		<jsp:include page="workingCapitalTransactionLineHeader.jsp"/>
	</tr>
	<tr>
		<jsp:include page="workingCapitalTransactionLine.jsp"/>
	</tr>
</table>

<logic:equal name="workingCapitalTransaction" property="payment" value="true">
	<div class="infobox mtop1 mbottom1">
		<p>
			<bean:define id="workingCapitalRequest" name="workingCapitalTransaction" property="workingCapitalRequest"/>

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
		</p>
	</div>
</logic:equal>

<logic:equal name="workingCapitalTransaction" property="acquisition" value="true">
	<br/>
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
			<fr:property name="classes" value="tstyle2"/>
		</fr:layout>
	</fr:view>
	<logic:equal name="workingCapitalTransaction" property="lastTransaction" value="true">
		<bean:define id="workingCapitalTransactionOid" type="java.lang.String" name="workingCapitalTransaction" property="externalId"/>
		<wf:activityLink processName="process" activityName="EditWorkingCapitalActivity" scope="request" paramName0="workingCapitalAcquisitionTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.EditWorkingCapitalActivity"/>
		</wf:activityLink>
		<wf:activityLink processName="process" activityName="ApproveWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalAcquisitionTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.ApproveWorkingCapitalAcquisitionActivity"/>
		</wf:activityLink>
	</logic:equal>
</logic:equal>

