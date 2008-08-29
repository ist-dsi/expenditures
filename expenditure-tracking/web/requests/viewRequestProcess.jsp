<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<!-- requests/viewRequestProcess -->
requests/viewRequestProcess

<bean:define id="currentState" name="requestForProposalProcess" property="requestForProposalProcessStateType"/>

<%--
<fr:view name="requestForProposalProcess"> 
	<fr:layout name="process-state">
		<fr:property name="stateParameterName" value="state"/>
		<fr:property name="url" value="/viewLogs.do?method=viewOperationLog&acquisitionProcessOid=${OID}"/>
		<fr:property name="contextRelative" value="true"/>
		<fr:property name="currentStateClass" value=""/>
	</fr:layout>
</fr:view>
--%>

<div class="wrapper">

<h2><bean:message key="label.view.requestForProposal.process" bundle="EXPENDITURE_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="infoop1">
	<ul>
	<logic:iterate id="activity" name="requestForProposalProcess" property="activeActivitiesForRequest">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page="<%= "/requestForProposalProcess.do?method=execute" + activityName %>" paramId="requestForProposalProcessOid" paramName="requestForProposalProcess" paramProperty="OID">
				<fr:view name="activity" property="class">
					<fr:layout name="label">
						<fr:property name="bundle" value="REQUEST_RESOURCES"/>
					</fr:layout>
				</fr:view>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
	<logic:empty name="requestForProposalProcess" property="activeActivitiesForRequest">
		<em>
			<bean:message key="label.no.operations.available.at.the.moment" bundle="EXPENDITURE_RESOURCES"/>.
		</em>
	</logic:empty>
</div>

<%--
<div class="expenditures">
	<logic:equal name="acquisitionProcess" property="allowedToViewCostCenterExpenditures" value="true">
		<p>
		<bean:message key="label.unit.total.allocated" bundle="ORGANIZATION_RESOURCES"/>:
		<fr:view name="acquisitionProcess" property="unit.totalAllocated"/>
		</p>
	</logic:equal>
	<logic:equal name="acquisitionProcess" property="allowedToViewSupplierExpenditures" value="true">
		<logic:present name="acquisitionProcess" property="acquisitionRequest.supplier">
			<p>
			<bean:message key="label.supplier.total.allocated" bundle="ORGANIZATION_RESOURCES"/>:
			<fr:view name="acquisitionProcess" property="acquisitionRequest.supplier.totalAllocated"/>
			</p>
		</logic:present>
	</logic:equal>
</div>


<div class="infoop2">
	<fr:view name="acquisitionProcess" property="acquisitionRequest"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
			schema="viewAcquisitionRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<div class="documents">
	<p>
		<bean:message key="label.acquisition.proposal.document" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<html:link action="/acquisitionProcess.do?method=downloadAcquisitionProposalDocument" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	<p>
		<bean:message key="label.acquisition.request.document" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument">
			<html:link action="/acquisitionProcess.do?method=downloadAcquisitionRequestDocument" paramId="acquisitionRequestDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionRequestDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument">
			<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	<p>
		<bean:message key="label.invoice" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.invoice">
			<logic:present name="acquisitionProcess" property="acquisitionRequest.invoice.content">
				<html:link action="/acquisitionProcess.do?method=downloadInvoice" paramId="invoiceOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.invoice.OID">
					<bean:write name="acquisitionProcess" property="acquisitionRequest.invoice.filename"/>
				</html:link>
			</logic:present>	
			<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.invoice">
				<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.invoice">
			<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
		</logic:notPresent>
	</p>
</div>


<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet">

	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet" indexId="index">
		<div class="item">
			<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
			<strong><bean:message key="label.view.acquisition.request.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)
			<bean:define id="itemOID" name="acquisitionRequestItem" property="OID"/>
			
			<logic:iterate id="activity" name="acquisitionProcess" property="activeActivitiesForItem" indexId="index">
				<logic:greaterThan name="index" value="0"> | </logic:greaterThan>
				<bean:define id="activityName" name="activity" property="class.simpleName"/> 
					<html:link page="<%= "/acquisitionProcess.do?method=execute" + activityName + "&acquisitionRequestItemOid=" + itemOID%>" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
						<fr:view name="activity" property="class">
							<fr:layout name="label">
								<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
							</fr:layout>
						</fr:view>
					</html:link>
			</logic:iterate>

			<logic:equal name="acquisitionRequestItem" property="valueFullyAttributedToUnits" value="false">
				<div class="infoop4">
					<strong><bean:message key="label.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="label.item.not.fully.attributed" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
			
			<logic:equal  name="acquisitionProcess" property="acquisitionProcessState.acquisitionProcessStateType"  value="INVOICE_CONFIRMED">		
				<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="false">
					<div class="infoop4">
						<strong><bean:message key="label.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="label.item.real.values.not.filled" bundle="ACQUISITION_RESOURCES"/>
					</div>
				</logic:equal>
				<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="true">
					<logic:equal name="acquisitionRequestItem" property="realValueFullyAttributedToUnits" value="false">
						<div class="infoop4">
							<strong><bean:message key="label.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="label.item.real.values.not.assigned" bundle="ACQUISITION_RESOURCES"/>
						</div>
					</logic:equal>
				</logic:equal>
			</logic:equal>
		
			<div class="infoop2">
				<fr:view name="acquisitionRequestItem"
						schema="viewAcquisitionRequestItem">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
					</fr:layout>
				</fr:view>
			</div>
		</div>
	</logic:iterate>
</logic:present>

</div>
--%>