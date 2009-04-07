<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<bean:define id="processClass" name="refundProcess" property="class.simpleName"/>
<bean:define id="actionMapping" value='<%= "/acquisition" + processClass%>'/>

<bean:define id="currentState" name="refundProcess" property="processState.refundProcessStateType"/>
<fr:view name="refundProcess"> 
	<fr:layout name="process-state">
		<fr:property name="stateParameterName" value="state"/>
		<fr:property name="url" value="/viewLogs.do?method=viewOperationLog&processOid=${OID}"/>
		<fr:property name="contextRelative" value="true"/>
		<fr:property name="currentStateClass" value=""/>
		<fr:property name="linkable" value="false"/>
	</fr:layout>
</fr:view>

<div class="wrapper">
<h2><bean:message key="refundProcess.title.viewRefundRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<jsp:include page="../../commons/defaultErrorDisplay.jsp"/>

<logic:present name="refundProcess" property="currentOwner">
	<bean:define id="ownerName" name="refundProcess" property="currentOwner.firstAndLastName"/>
	<div class="infoop4">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>


<div class="infoop1">
	<ul>
	<logic:iterate id="activity" name="refundProcess" property="activeActivitiesForRequest">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page='<%= actionMapping + ".do?method=execute" + activityName %>' paramId="refundProcessOid" paramName="refundProcess" paramProperty="OID">
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
	<logic:empty name="refundProcess" property="activeActivitiesForRequest">
		<p>
			<em>
				<bean:message key="messages.info.noOperatesAvailabeATM" bundle="EXPENDITURE_RESOURCES"/>.
			</em>
		</p>
	</logic:empty>
</div>

<bean:define id="urlConfirm"><%=actionMapping %>.do</bean:define>
<bean:define id="processOid" name="refundProcess" property="OID"/>

<logic:present name="confirmTake">
	<div class="warning2">
		<p><span><bean:message key="message.confirm.take.acquisition.process" bundle="ACQUISITION_RESOURCES"/></span></p>
		<div class="forminline">
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="takeProcess"/>
				<html:hidden property="confirmTake" value="yes"/>
				<html:hidden property="processOid" value="<%= processOid.toString() %>"/>
				<html:submit styleClass="inputbutton"><bean:message key="button.yes" bundle="EXPENDITURE_RESOURCES"/></html:submit>
			</form>
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="viewProcess"/>
				<html:hidden property="processOid" value="<%= processOid.toString() %>"/>
				<html:cancel styleClass="inputbutton"><bean:message key="button.no" bundle="EXPENDITURE_RESOURCES"/></html:cancel>
			</form>
		</div>
	</div>
</logic:present>

<logic:present name="confirmCancelProcess">
	<div class="warning2">
		<p><span><bean:message key="message.confirm.cancel.refund.process" bundle="ACQUISITION_RESOURCES"/></span></p>
		<div class="forminline">
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="cancelRefundProcess"/>
				<html:hidden property="processOid" value="<%= processOid.toString() %>"/>
				<html:submit styleClass="inputbutton"><bean:message key="button.yes" bundle="EXPENDITURE_RESOURCES"/></html:submit>
			</form>
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="viewProcess"/>
				<html:hidden property="processOid" value="<%= processOid.toString() %>"/>
				<html:cancel styleClass="inputbutton"><bean:message key="button.no" bundle="EXPENDITURE_RESOURCES"/></html:cancel>
			</form>
		</div>
	</div>
</logic:present>

<ul class="operations">
	<li>
	<logic:present name="refundProcess" property="currentOwner">
		<logic:equal name="refundProcess" property="userCurrentOwner" value="true">
				<html:link page='<%= actionMapping + ".do?method=releaseProcess" %>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
					<bean:message key="acquisitionProcess.link.releaseProcess" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
		</logic:equal>
		<logic:equal name="refundProcess" property="userCurrentOwner" value="false">
				<html:link page='<%= actionMapping + ".do?method=stealProcess" %>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
					<bean:message key="acquisitionProcess.link.stealProcess" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
		</logic:equal>
	</logic:present>
	<logic:notPresent name="refundProcess" property="currentOwner">
		<html:link page='<%= actionMapping + ".do?method=takeProcess" %>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
				<bean:message key="acquisitionProcess.link.takeProcess" bundle="ACQUISITION_RESOURCES"/>
		</html:link>
	</logic:notPresent>
	</li>
	<li>
		<html:link page='<%= actionMapping + ".do?method=prepareGenericUpload" %>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
			<bean:message key="acquisitionProcess.link.uploadFile" bundle="ACQUISITION_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link page="/viewLogs.do?method=viewOperationLog&amp;module=acquisitions" paramId="processOid" paramName="refundProcess" paramProperty="OID">
			<bean:message key="label.log.view" bundle="ACQUISITION_RESOURCES"/>
		</html:link>
	</li>

	<bean:size id="comments"  name="refundProcess" property="comments"/>
	<li> 
		<html:link page='<%= actionMapping + ".do?method=viewComments"%>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
			<bean:message key="link.comments" bundle="EXPENDITURE_RESOURCES"/> (<%= comments %>)
		</html:link>	
		<logic:greaterThan name="comments" value="0">
			|
			<span class="color888" style="font-size: 0.9em;">
				<bean:message key="label.lastBy" bundle="EXPENDITURE_RESOURCES"/> 
				<bean:define id="mostRecentComment" name="refundProcess" property="mostRecentComment"/>
				<strong><fr:view name="mostRecentComment" property="commenter.name"/></strong>, <fr:view name="mostRecentComment" property="date"/> 
			</span>
		</logic:greaterThan>
	</li>
</ul>

<div class="infoop2">
	<fr:view name="refundProcess" property="request"
			schema="viewRefundRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>



<bean:define id="payingUnits" name="refundProcess" property="acquisitionRequest.totalAmountsForEachPayingUnit"/>
<logic:notEmpty name="payingUnits">

	<table class="tstyle5 mervt1 width100pc">
		<tr>	
			<th class="aleft"><bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/></th>
			<th class="aright"><bean:message key="financer.label.value" bundle="ACQUISITION_RESOURCES"/></th>
		</tr>
	
	<logic:iterate id="payingUnit" name="payingUnits">
		<tr>
			<td class="aleft"><fr:view name="payingUnit" property="payingUnit.presentationName"/></td>
			<td class="aright"><fr:view name="payingUnit" property="amount"/></td>
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>

<div class="documents" style="margin-bottom: 2em;">
<p>
	<bean:message key="acquisitionProcess.label.otherFiles" bundle="ACQUISITION_RESOURCES"/>:
	<logic:notEmpty name="refundProcess" property="files">
		<logic:iterate id="file" name="refundProcess" property="files">
			<html:link action='<%= actionMapping + ".do?method=downloadGenericFile" %>' paramId="fileOID" paramName="file" paramProperty="OID">
				<bean:write name="file" property="displayName"/>
			</html:link>, 
		</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="refundProcess" property="files"><em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em></logic:empty>
</p>
	
</div>

<bean:size id="totalItems" name="refundProcess" property="request.requestItems"/>
<logic:iterate id="refundItem" name="refundProcess" property="request.requestItems" indexId="index">
	<div class="item">
	<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
	<strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)
		<bean:define id="itemOID" name="refundItem" property="OID"/>
	<logic:iterate id="activity" name="refundProcess" property="activeActivitiesForItem" indexId="index">
		<logic:greaterThan name="index" value="0"> | </logic:greaterThan>
			<bean:define id="activityName" name="activity" property="class.simpleName"/> 
			<html:link page='<%= actionMapping + ".do?method=execute" + activityName + "&refundItemOid=" + itemOID%>' paramId="refundProcessOid" paramName="refundProcess" paramProperty="OID">
			<fr:view name="activity" property="class">
				<fr:layout name="label">
						<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
				</fr:layout>
			</fr:view>
		</html:link>
	</logic:iterate>
	
	<logic:equal name="refundProcess" property="inGenesis" value="true">
		<logic:equal name="refundItem" property="valueFullyAttributedToUnits" value="false">
			<div class="infoop4">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
			</div>
		</logic:equal>
	</logic:equal>		
	
	<logic:equal name="refundProcess" property="anyRefundInvoiceAvailable" value="true">
		<logic:equal name="refundProcess" property="inAuthorizedState" value="true">
			<logic:equal name="refundItem" property="realValueFullyAttributedToUnits" value="false">
				<div class="infoop4">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
		</logic:equal>
		<logic:equal name="refundProcess" property="inSubmittedForInvoiceConfirmationState" value="true">
			<logic:equal name="refundItem" property="realValueFullyAttributedToUnits" value="false">
				<div class="infoop4">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
		</logic:equal>
	</logic:equal>

	<bean:define id="item" name="refundItem" toScope="request"/>
	<jsp:include page="../commons/viewRefundItem.jsp"/>
		<logic:notEmpty name="refundItem" property="invoices">
				<table class="tstyle5 tdmiddle tdnoborder vpadding05" style="width: 100%;">
					<tr>
					<th><bean:message key="acquisitionProcess.label.invoice.number" bundle="ACQUISITION_RESOURCES"/></th>		
					<th><bean:message key="acquisitionProcess.label.invoice.date" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.invoice.value" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.invoice.vatValue" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.invoice.totalValue" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.invoice.refundableValue" bundle="ACQUISITION_RESOURCES"/></th>
					
					</tr>	
					<logic:iterate id="invoice" name="refundItem" property="invoices">
												<tr>
							<td class="nowrap" rowspan="2" style="border-right: 1px solid #eee !important;"><fr:view name="invoice" property="invoiceNumber"/></td>
							<td class="nowrap"><fr:view name="invoice" property="invoiceDate"/></td>
							<td class="nowrap"><fr:view name="invoice" property="value"/></td>
							<td class="nowrap"><fr:view name="invoice" property="vatValue"/></td>
							<td class="nowrap"><fr:view name="invoice" property="valueWithVat"/></td>
							<td class="nowrap"><fr:view name="invoice" property="refundableValue"/></td>
							<%--
							    <td> 
							    	<logic:present name="invoice" property="supplier">
							    	  <fr:view name="invoice" property="supplier.name"/>
							    	</logic:present>
							 		<logic:notPresent name="invoice" property="supplier">
							    	-
							    	</logic:notPresent>
							    </td>
								<td>
									<html:link action='<%= actionMapping + ".do?method=downloadInvoice" %>' paramId="invoiceOID" paramName="invoice" paramProperty="OID">
										<bean:write name="invoice" property="file.filename"/>
									</html:link>
								</td>
							--%>
						</tr>
						<tr style="border-bottom: 4px solid #eee;">
							<td colspan="6" class="aleft">
								<html:link action='<%= actionMapping + ".do?method=downloadInvoice" %>' paramId="invoiceOID" paramName="invoice" paramProperty="OID">
									<bean:write name="invoice" property="file.filename"/>
								</html:link>

					    		<logic:present name="invoice" property="supplier">
						    	  (<fr:view name="invoice" property="supplier.name"/>)
						    	</logic:present>
						    	<logic:notPresent name="invoice" property="supplier">
						    	</logic:notPresent>
							
							</td>
						</tr>
					</logic:iterate>
				</table>
				
		</logic:notEmpty>
		<p class="aright" style="margin-bottom: 0;">
			<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#"><bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/></a>
		</p>
		
		</div>
</logic:iterate>
</div>