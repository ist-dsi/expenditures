<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>
<bean:define id="acquisitionProcessOid"><bean:write name="acquisitionProcess" property="OID"/></bean:define>
<bean:define id="acquisitionProcessClass" name="acquisitionProcess" property="class.simpleName"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + acquisitionProcessClass %>"/>
<bean:define id="urlConfirm"><%=actionMapping %>.do</bean:define>

<logic:equal name="acquisitionProcess" property="processFlowCharAvailable" value="true">
	<bean:define id="currentState" name="acquisitionProcess" property="acquisitionProcessStateType"/>
	<fr:view name="acquisitionProcess"> 
		<fr:layout name="process-state">
			<fr:property name="stateParameterName" value="state"/>
			<fr:property name="url" value="/viewLogs.do?method=viewOperationLog&processOid=${OID}"/>
			<fr:property name="contextRelative" value="true"/>
			<fr:property name="currentStateClass" value=""/>
			<fr:property name="linkable" value="true"/>
		</fr:layout>
	</fr:view>
</logic:equal>
<div class="wrapper">

<h2><bean:message key="acquisitionProcess.title.viewAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<logic:present name="acquisitionProcess" property="currentOwner">
	<bean:define id="ownerName" name="acquisitionProcess" property="currentOwner.firstAndLastName"/>
	<div class="infoop4">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<div class="infoop1">
	<ul>
	<logic:iterate id="activity" name="acquisitionProcess" property="activeActivitiesForRequest">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page='<%= actionMapping + ".do?method=execute" + activityName %>' paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<fr:view name="activity" property="class">
					<fr:layout name="label">
						<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
						<fr:property name="escape" value="false"/>
					</fr:layout>
				</fr:view>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
	<logic:empty name="acquisitionProcess" property="activeActivitiesForRequest">
		<p>
			<em>
				<bean:message key="messages.info.noOperatesAvailabeATM" bundle="EXPENDITURE_RESOURCES"/>.
			</em>
		</p>
	</logic:empty>
</div>

<logic:present name="confirmCancelAcquisitionProcess">
	<div class="warning2">
		<p><span><bean:message key="message.confirm.cancel.acquisition.process" bundle="ACQUISITION_RESOURCES"/></span></p>
		<div class="forminline">
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="cancelAcquisitionRequest"/>
				<html:hidden property="acquisitionProcessOid" value="<%= acquisitionProcessOid %>"/>
				<html:submit styleClass="inputbutton"><bean:message key="button.yes" bundle="EXPENDITURE_RESOURCES"/></html:submit>
			</form>
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="viewAcquisitionProcess"/>
				<html:hidden property="acquisitionProcessOid" value="<%= acquisitionProcessOid %>"/>
				<html:cancel styleClass="inputbutton"><bean:message key="button.no" bundle="EXPENDITURE_RESOURCES"/></html:cancel>
			</form>
		</div>
	</div>
</logic:present>

<logic:present name="confirmTake">
	<div class="warning2">
		<p><span><bean:message key="message.confirm.take.acquisition.process" bundle="ACQUISITION_RESOURCES"/></span></p>
		<div class="forminline">
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="takeProcess"/>
				<html:hidden property="confirmTake" value="yes"/>
				<html:hidden property="processOid" value="<%= acquisitionProcessOid %>"/>
				<html:submit styleClass="inputbutton"><bean:message key="button.yes" bundle="EXPENDITURE_RESOURCES"/></html:submit>
			</form>
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="viewProcess"/>
				<html:hidden property="processOid" value="<%= acquisitionProcessOid %>"/>
				<html:cancel styleClass="inputbutton"><bean:message key="button.no" bundle="EXPENDITURE_RESOURCES"/></html:cancel>
			</form>
		</div>
	</div>
</logic:present>
<ul class="operations">
	<li>
	<logic:present name="acquisitionProcess" property="currentOwner">
		<logic:equal name="acquisitionProcess" property="userCurrentOwner" value="true">
				<html:link page="<%= actionMapping + ".do?method=releaseProcess" %>" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="acquisitionProcess.link.releaseProcess" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="userCurrentOwner" value="false">
				<html:link page="<%= actionMapping + ".do?method=stealProcess" %>" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="acquisitionProcess.link.stealProcess" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
		</logic:equal>
	</logic:present>
	<logic:notPresent name="acquisitionProcess" property="currentOwner">
		<html:link page="<%= actionMapping + ".do?method=takeProcess" %>" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
				<bean:message key="acquisitionProcess.link.takeProcess" bundle="ACQUISITION_RESOURCES"/>
		</html:link>
	</logic:notPresent>
	</li>
	<li>
		<html:link page="<%= actionMapping + ".do?method=prepareGenericUpload" %>" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
			<bean:message key="acquisitionProcess.link.uploadFile" bundle="ACQUISITION_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link page="/viewLogs.do?method=viewOperationLog&amp;module=acquisitions" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
			<bean:message key="label.log.view" bundle="ACQUISITION_RESOURCES"/>
		</html:link>
	</li>

	<bean:size id="comments"  name="acquisitionProcess" property="comments"/>
	<li> 
		<html:link page="<%= actionMapping + ".do?method=viewComments"%>" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
			<bean:message key="link.comments" bundle="EXPENDITURE_RESOURCES"/> (<%= comments %>)
		</html:link>	
		<logic:greaterThan name="comments" value="0">
			|
			<span class="color888" style="font-size: 0.9em;">
				<bean:message key="label.lastBy" bundle="EXPENDITURE_RESOURCES"/> 
				<bean:define id="mostRecentComment" name="acquisitionProcess" property="mostRecentComment"/>
				<strong><fr:view name="mostRecentComment" property="commenter.name"/></strong>, <fr:view name="mostRecentComment" property="date"/> 
			</span>
		</logic:greaterThan>
	</li>
</ul>

<div class="expenditures">
	<logic:equal name="acquisitionProcess" property="allowedToViewSupplierExpenditures" value="true">
		<logic:notEmpty name="acquisitionProcess" property="acquisitionRequest.suppliers">
			<logic:iterate id="supplier" name="acquisitionProcess" property="acquisitionRequest.suppliers">
			<bean:define id="supplierName" name="supplier" property="name"/>
			<p>
				<bean:message key="supplier.message.info.totalAllocated.withArgument" bundle="EXPENDITURE_ORGANIZATION_RESOURCES" arg0="<%= supplierName.toString() %>"/>:
				<fr:view name="supplier" property="totalAllocated"/>
			</p>
			</logic:iterate>
		</logic:notEmpty>
	</logic:equal>
</div>

<bean:define id="processRequest" name="acquisitionProcess" property="request" toScope="request"/>
<jsp:include page="commons/viewAcquisitionRequest.jsp" flush="true"/>

<logic:notEmpty name="acquisitionProcess" property="acquisitionRequest.totalAmountsForEachPayingUnit">
	<fr:view name="acquisitionProcess" property="acquisitionRequest.totalAmountsForEachPayingUnit"
			schema="viewPayingUnitWithTotalAmount">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 width100pc"/>
			<fr:property name="columnClasses" value=",,nowrap,,"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<div class="documents">
	<p>
		<bean:message key="acquisitionProcess.label.proposalDocument" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<html:link action="<%= actionMapping + ".do?method=downloadAcquisitionProposalDocument"%>" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	<p>
		<bean:message key="acquisitionProcess.label.requestDocument" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.purchaseOrderDocument">
			<html:link action="<%= actionMapping + ".do?method=downloadAcquisitionPurchaseOrderDocument"%>" paramId="purchaseOrderDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.purchaseOrderDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.purchaseOrderDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.purchaseOrderDocument">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	<p>
		<bean:message key="acquisitionProcess.label.invoice" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.invoice">
			<logic:present name="acquisitionProcess" property="acquisitionRequest.invoice.content">
				<html:link action="<%= actionMapping + ".do?method=downloadInvoice"%>" paramId="invoiceOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.invoice.OID">
					<bean:write name="acquisitionProcess" property="acquisitionRequest.invoice.filename"/>
				</html:link>
			</logic:present>	
			<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.invoice">
				<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.invoice">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	<p>
		<bean:message key="acquisitionProcess.label.otherFiles" bundle="ACQUISITION_RESOURCES"/>:
		<logic:notEmpty name="acquisitionProcess" property="files">
			<logic:iterate id="file" name="acquisitionProcess" property="files">
				<html:link action="<%= actionMapping + ".do?method=downloadGenericFile&acquisitionProcess=" + acquisitionProcessOid %>" paramId="fileOID" paramName="file" paramProperty="OID">
					<bean:write name="file" property="displayName"/>
				</html:link>, 
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="acquisitionProcess" property="files"><em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em></logic:empty>
	</p>
</div>

<bean:define id="itemSet" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/> 
<logic:present name="itemSet">
	
		<logic:equal  name="acquisitionProcess" property="invoiceReceived"  value="true">		
			<logic:equal name="acquisitionProcess" property="acquisitionRequest.realValueLessThanTotalValue" value="false">
				<div class="infoop4">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realValueLessThanTotalValue" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
			<logic:equal name="acquisitionProcess" property="acquisitionRequest.realUnitShareValueLessThanUnitShareValue" value="false">
				<div class="infoop4">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realUnitShareValueLessThanUnitShareValue" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
			<logic:equal name="acquisitionProcess" property="acquisitionRequest.realTotalValueEqualsRealShareValue" value="false">
				<div class="infoop4">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realTotalValueEqualsRealShareValue" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
		</logic:equal>
	
	
		<logic:notEmpty name="itemSet">
			<bean:size id="size" name="itemSet"/>
			<logic:greaterThan value="1" name="size">
			<table class="tstyle5 thright mvert1" style="width: 100%;" id="itemResume">
				<tr>
					<th></th>
					<th><bean:message key="acquisitionRequestItem.label.quantity" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="acquisitionRequestItem.label.unitValue" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="acquisitionRequestItem.label.vat" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="acquisitionRequestItem.label.additionalCostValue" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="acquisitionRequestItem.label.totalValueWithAdditionalCostsAndVat" bundle="ACQUISITION_RESOURCES"/></th>
				</tr>
				<logic:iterate id="itemResume" name="itemSet" indexId="index">
					<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
					<tr>
						<td><%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= "#item" + currentIndex%>">Item<%= currentIndex %></a></td>
						<td class="aright"><fr:view name="itemResume" property="quantity"/></td>
						<td class="aright"><fr:view name="itemResume" property="unitValue"/></td>
						<td class="aright"><fr:view name="itemResume" property="totalVatValue"/></td>
						<td class="aright">
							<fr:view name="itemResume" property="additionalCostValue" type="myorg.domain.util.Money">
								<fr:layout name="null-as-label">
									<fr:property name="subLayout" value="default"/>
								</fr:layout>
							</fr:view>
						</td>
						<td class="aright"><fr:view name="itemResume" property="totalItemValueWithAdditionalCostsAndVat"/></td>
					</tr>
				</logic:iterate>
			</table>
			</logic:greaterThan>
		</logic:notEmpty>
		
		<bean:size id="totalItems" name="itemSet"/>
		<logic:iterate id="acquisitionRequestItem" name="itemSet" indexId="index">
			<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
			<div class="item" id="<%= "item" + currentIndex %>">
				<strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)
				<bean:define id="itemOID" name="acquisitionRequestItem" property="OID"/>
				
				<logic:iterate id="activity" name="acquisitionProcess" property="activeActivitiesForItem" indexId="index">
					<logic:greaterThan name="index" value="0"> | </logic:greaterThan>
					<bean:define id="activityName" name="activity" property="class.simpleName"/> 
						<html:link page='<%= actionMapping + ".do?method=execute" + activityName + "&acquisitionRequestItemOid=" + itemOID%>' paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
							<fr:view name="activity" property="class">
								<fr:layout name="label">
									<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
								</fr:layout>
							</fr:view>
						</html:link>
				</logic:iterate>
	
				<logic:equal name="acquisitionRequestItem" property="valueFullyAttributedToUnits" value="false">
					<div class="infoop4">
						<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
					</div>
				</logic:equal>
				
				<logic:equal  name="acquisitionProcess" property="acquisitionProcessState.invoiceConfirmed"  value="true">		
					<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="false">
						<div class="infoop4">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.info.valuesNotFilled" bundle="ACQUISITION_RESOURCES"/>
						</div>
					</logic:equal>
				</logic:equal>
			
				<bean:define id="item" name="acquisitionRequestItem" toScope="request"/>
				<jsp:include page="commons/viewAcquisitionRequestItem.jsp"/>
				<p class="aright">
					<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#itemResume"><bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/></a>
				</p>
			</div>
		</logic:iterate>
		
	</logic:present>
	
</div>


