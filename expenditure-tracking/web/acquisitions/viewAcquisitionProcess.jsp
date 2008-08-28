<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<bean:define id="currentState" name="acquisitionProcess" property="acquisitionProcessStateType"/>
 
<fr:view name="acquisitionProcess"> 
	<fr:layout name="process-state">
		<fr:property name="stateParameterName" value="state"/>
		<fr:property name="url" value="/viewLogs.do?method=viewOperationLog&acquisitionProcessOid=${OID}"/>
		<fr:property name="contextRelative" value="true"/>
		<fr:property name="currentStateClass" value=""/>
	</fr:layout>
</fr:view>

<div class="wrapper">

<h2><bean:message key="label.view.acquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="infoop1">
	<ul>
	<logic:iterate id="activity" name="acquisitionProcess" property="activeActivitiesForRequest">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page="<%= "/acquisitionProcess.do?method=execute" + activityName %>" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<fr:view name="activity" property="class">
					<fr:layout name="label">
						<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
					</fr:layout>
				</fr:view>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
	<logic:empty name="acquisitionProcess" property="activeActivitiesForRequest">
		<em>
			<bean:message key="label.no.operations.available.at.the.moment" bundle="EXPENDITURE_RESOURCES"/>.
		</em>
	</logic:empty>
</div>


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
	
				<style>
						table.asd th {
						white-space: nowrap;
						width: 1%;
						}
			</style>
			
			<div class="infoop2">
				
				<table class="tstyle1 asd">
	
					<tr>
						<th><bean:message key="label.acquisition.request.item.proposalReference" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td><fr:view name="acquisitionRequestItem" property="proposalReference"/></td>
						<th></th>
						<td></td>
					</tr>
					<tr>
						<th><bean:message key="label.acquisition.request.item.quantity" bundle="ACQUISITION_RESOURCES"/>:</th>
				
						<td><fr:view name="acquisitionRequestItem" property="quantity"/></td>
						<th><bean:message key="label.acquisition.request.item.realQuantity" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td><fr:view name="acquisitionRequestItem" property="realQuantity" type="java.lang.Integer">
									<fr:layout name="null-as-label">
										<fr:property name="subLayout" value="default"/>
									</fr:layout>
								</fr:view>
						</td>
					</tr>
					<tr>
						<th><bean:message key="label.acquisition.request.item.unitValue" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td><fr:view name="acquisitionRequestItem" property="unitValue"/></td>

						<th><bean:message key="label.acquisition.request.item.realUnitValue" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td><fr:view name="acquisitionRequestItem" property="realUnitValue" type="pt.ist.expenditureTrackingSystem.domain.util.Money">
									<fr:layout name="null-as-label">
										<fr:property name="subLayout" value="default"/>
									</fr:layout>
								</fr:view></td>
					</tr>
					<tr>
						<th><bean:message key="label.acquisition.request.item.vatValue" bundle="ACQUISITION_RESOURCES"/></th>
						<td><fr:view name="acquisitionRequestItem" property="vatValue"/></td>
						<th><bean:message key="label.acquisition.request.item.realVatValue" bundle="ACQUISITION_RESOURCES"/>:</th>
				
						<td><fr:view name="acquisitionRequestItem" property="realVatValue" type="java.lang.String">
									<fr:layout name="null-as-label">
										<fr:property name="subLayout" value="default"/>
									</fr:layout>
								</fr:view>
						</td>
					</tr>
					<tr>
						<th><bean:message key="label.acquisition.request.item.totalValue" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td colspan="3"><span><fr:view name="acquisitionRequestItem" property="totalItemValue"/></span></td>
					</tr>
					<tr>
				
						<th><bean:message key="label.acquisition.request.item.assignedValue" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td colspan="3"><span><fr:view name="acquisitionRequestItem" property="totalAssignedValue"/></span></td>
					</tr>
					<tr>
						<th><bean:message key="label.acquisition.request.item.ShipmentValue" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td><fr:view name="acquisitionRequestItem" property="shipmentValue" type="pt.ist.expenditureTrackingSystem.domain.util.Money">
									<fr:layout name="null-as-label">
										<fr:property name="subLayout" value="default"/>
									</fr:layout>
								</fr:view>
							</td>
					</tr>
				
					<tr>
						<th><bean:message key="label.payingUnits" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td colspan="3">
								<fr:view name="acquisitionRequestItem" property="unitItems" schema="viewUnitItem">
									<fr:layout name="tabular">
												<fr:property name="classes" value="payingunits"/>
												<fr:property name="columnClasses" value=",nowrap vatop,"/>
												<fr:property name="sortBy" value="unit.presentationName"/>
									</fr:layout>
								</fr:view>
						</td>
					</tr>
					<tr>
						<th><bean:message key="label.acquisition.request.item.salesCode" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td colspan="3">
						<fr:view name="acquisitionRequestItem" property="CPVReference">
							<fr:layout name="format">
								<fr:property name="format" value="${code} - ${description}"/>
							</fr:layout>
						</fr:view>
					</tr>
				
					<tr>
						<th><bean:message key="label.acquisition.request.item.description" bundle="ACQUISITION_RESOURCES"/>:</th>
						<td colspan="3"><fr:view name="acquisitionRequestItem" property="description"/></td>
					</tr>
	</table>
				
			</div>
					
		</div>
	</logic:iterate>
	
	
</logic:present>

</div>


