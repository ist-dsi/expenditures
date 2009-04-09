<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%><bean:define id="acquisitionProcessOid"><bean:write name="acquisitionProcess" property="OID"/></bean:define>
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

<h2>
	<bean:message key="acquisitionProcess.title.viewAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/>
	<span class="acquisitionNumber">(<fr:view name="acquisitionProcess" property="acquisitionRequest.acquisitionProcessId"/>)</span>	
</h2> 


<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<logic:present name="acquisitionProcess" property="currentOwner">
	<bean:define id="ownerName" name="acquisitionProcess" property="currentOwner.firstAndLastName"/>
	<div class="infoop4">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>


<table class="structural">
	<tr>
		<td style="width: 40%; padding-right: 1em; border: 1px dotted #aaa; padding: 10px 15px;">
			<p class="mtop0 mbottom05"><b style="color: #555;"><bean:message key="label.activities" bundle="EXPENDITURE_RESOURCES"/></b></p>
			<ul class="operations mtop0">
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
			
			
			
			<p class="mtop15 mbottom05"><b style="color: #555;"><bean:message key="link.sideBar.other.operations" bundle="EXPENDITURE_RESOURCES"/></b></p>
			
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
					<html:link page="/viewLogs.do?method=viewOperationLog&amp;module=acquisitions" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
						<bean:message key="label.log.view" bundle="ACQUISITION_RESOURCES"/>
					</html:link>
				</li>
			
				<bean:size id="comments"  name="acquisitionProcess" property="comments"/>
				<li> 
					<html:link page="<%= actionMapping + ".do?method=viewComments"%>" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
						<bean:message key="link.comments" bundle="EXPENDITURE_RESOURCES"/> (<%= comments %>)
					</html:link>	
				</li>
			</ul>

		</td>
				
		<td style="width: 2%;"></td>
		<td style="width: 40%; border: 1px dotted #aaa; padding: 10px 15px;">
			<p class="mtop0 mbottom05"><b style="color: #555;"><bean:message key="label.documents" bundle="EXPENDITURE_RESOURCES"/></b></p>
			<div class="documents mtop0" style="overflow: hidden; width: 300px">
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
			
			<p>
					<html:link page="<%= actionMapping + ".do?method=prepareGenericUpload" %>" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
						<bean:message key="acquisitionProcess.link.uploadFile" bundle="ACQUISITION_RESOURCES"/>
					</html:link>
				</p>
		</td>
	</tr>
</table>

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



<bean:define id="unreadComments" name="acquisitionProcess" property="unreadCommentsForCurrentUser"/>
<logic:notEmpty name="unreadComments">
<bean:size id="count" name="unreadComments"/>
	<div class="infoop4 mtop05 mbottom15">
	<p class="mvert025">
		<logic:greaterThan name="count" value="1">
			<bean:message key="label.unreadComments.info.moreThanOne" arg0="<%= count.toString() %>" bundle="EXPENDITURE_RESOURCES"/>
		</logic:greaterThan>
		<logic:equal name="count" value="1">
			<bean:message key="label.unreadComments.info" arg0="<%= count.toString() %>" bundle="EXPENDITURE_RESOURCES"/>
		</logic:equal>
		
		 <html:link page="<%= actionMapping + ".do?method=viewComments"%>" paramId="processOid" paramName="acquisitionProcess" paramProperty="OID">
				<bean:message key="link.view.unreadComments" bundle="EXPENDITURE_RESOURCES"/> »
		</html:link>
	</p>
</div>
	
</logic:notEmpty>



<bean:define id="processRequest" name="acquisitionProcess" property="request" toScope="request"/>
<jsp:include page="commons/viewAcquisitionRequest.jsp" flush="true"/>

<bean:define id="payingUnits" name="acquisitionProcess" property="acquisitionRequest.totalAmountsForEachPayingUnit"/>
<logic:notEmpty name="payingUnits">

	<table class="tstyle5 mervt1 width100pc">
		<tr>	
			<th class="aleft"><bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/></th>
			<th class="aright">
					<bean:message key="acquisitionProcess.label.accountingUnit" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th id="fundAllocationHeader">
					<bean:message key="financer.label.fundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th id="effectiveFundAllocationHeader"> 
					<bean:message key="financer.label.effectiveFundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th class="aright"><bean:message key="financer.label.value" bundle="ACQUISITION_RESOURCES"/>
				<script type="text/javascript">
						$('#fundAllocationHeader').hide();
						$('#effectiveFundAllocationHeader').hide();
				</script>
			</th>
		</tr>
	
	<logic:iterate id="payingUnit" name="payingUnits">
		<tr>
			<td class="aleft"><fr:view name="payingUnit" property="payingUnit.presentationName"/></td>
			<td class="aright"><fr:view name="payingUnit" property="financer.accountingUnit.name"/></td>
		<logic:present name="payingUnit" property="financer.fundAllocationId">
				<td>
					<fr:view name="payingUnit" property="financer.fundAllocationIds"/> 
					<script type="text/javascript">
						$('#fundAllocationHeader').show();
					</script>
				</td>
			
		</logic:present>
		<logic:present name="payingUnit" property="financer.effectiveFundAllocationId">
				
				<td>
					<fr:view name="payingUnit" property="financer.effectiveFundAllocationIds"/> 
					<script type="text/javascript">
						$('#effectiveFundAllocationHeader').show();
					</script>
				</td>
			
		</logic:present>
			<td class="aright"><fr:view name="payingUnit" property="amount"/></td>
		</tr>
		
	</logic:iterate>
	</table>
</logic:notEmpty>


<bean:define id="itemSet" name="acquisitionProcess" property="acquisitionRequest.orderedAcquisitionRequestItemsSet"/> 
<logic:present name="itemSet">
	
		<logic:equal  name="acquisitionProcess" property="pastInvoiceReceived"  value="true">		
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
						<table class="tstyle5 thright mvert1" style="width: 100%;" id="itemResume">
				<tr>
					<th></th>
					<th class="aleft"><bean:message key="acquisitionRequestItem.label.description" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="acquisitionRequestItem.label.quantity" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="acquisitionRequestItem.label.totalValueWithAdditionalCostsAndVat" bundle="ACQUISITION_RESOURCES"/></th>
				</tr>
				<logic:iterate id="itemResume" name="itemSet" indexId="index">
					<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
					<tr>
						<td>
							<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
							<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= "#item" + currentIndex%>">
								Item <%= currentIndex %>
							</a>
							<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
						</td>

						<td class="aleft"><fr:view name="itemResume" property="description"/></td>
						<td class="aright"><fr:view name="itemResume" property="quantity"/></td>
						<td class="aright"><fr:view name="itemResume" property="currentTotalItemValueWithAdditionalCostsAndVat"/></td>
					</tr>
				</logic:iterate>
				<logic:greaterThan value="1" name="size">
					<tr>
						<td colspan="4" class="aright"><span><b></b><fr:view name="acquisitionProcess" property="acquisitionRequest.currentTotalValue"/></span></td>
					</tr>
				</logic:greaterThan>
			</table>
			
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
					<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
					<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#"><bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/></a>
					<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
				</p>
			</div>
		</logic:iterate>
	</logic:present>
</div>


