<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<script type="text/javascript">
	$("#processControl").addClass("wrapper");
</script>

<div class="wrapper">
<div class="infobox">
	<fr:view name="process" property="request"
			schema="viewRefundRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<bean:define id="payingUnits" name="process" property="request.totalAmountsForEachPayingUnit"/>
<logic:notEmpty name="payingUnits">

	<table class="tstyle4 mvert1 width100pc tdmiddle thnoborder" style="float: left;">
		<tr>	
			<th class="aleft"><bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/></th>
			<th class="acenter" style="width: 70px;">
					<bean:message key="acquisitionProcess.label.accountingUnit" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th id="fundAllocationHeader">
					<bean:message key="financer.label.fundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th id="effectiveFundAllocationHeader"> 
					<bean:message key="financer.label.effectiveFundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th class="aright">
				<bean:message key="acquisitionRequestItem.label.totalValueWithVAT" bundle="ACQUISITION_RESOURCES"/>
				<script type="text/javascript">
						$('#fundAllocationHeader').hide();
						$('#effectiveFundAllocationHeader').hide();
				</script>
			</th>
			<th></th>
		</tr>
	
	<logic:iterate id="payingUnit" name="payingUnits">
		<tr>
			<td class="aleft">
				<bean:define id="unitOID" name="payingUnit" property="payingUnit.externalId" type="java.lang.String"/>
				<html:link styleClass="secondaryLink" page="<%= "/expenditureTrackingOrganization.do?method=viewOrganization&unitOid=" + unitOID%>" target="_blank">
					<fr:view name="payingUnit" property="payingUnit.presentationName"/>
				</html:link>
			</td>
			<bean:define id="financer" name="payingUnit" property="financer"/>
			<td class="acenter" style="width: 80px;"><fr:view name="payingUnit" property="financer.accountingUnit.name"/></td>
			<td class="allocationCell" style="display: none;">
				<logic:equal name="payingUnit" property="financer.fundAllocationPresent" value="true">
					<fr:view name="payingUnit" property="financer.fundAllocationIds"/> 
					<script type="text/javascript">
						$('#fundAllocationHeader').show();
						$('.allocationCell').show();
					</script>
				</logic:equal>
			</td>
			<td class="allocationCell" style="display: none;">
				<logic:equal name="payingUnit" property="financer.effectiveFundAllocationPresent" value="true">
					<fr:view name="payingUnit" property="financer.effectiveFundAllocationIds"/> 
					<script type="text/javascript">
						$('#effectiveFundAllocationHeader').show();
						$('.allocationCell').show();
					</script>
				</logic:equal>
			</td>
		
			<td class="aright nowrap" style="width: 80px;"><fr:view name="payingUnit" property="amount"/></td>
			<td>
				<wf:activityLink id="<%= "removePayingUnit-" + unitOID %>" processName="process" activityName="GenericRemovePayingUnit" scope="request" paramName0="payingUnit" paramValue0="<%= unitOID %>">
					<bean:message bundle="MYORG_RESOURCES" key="link.remove"/>
				</wf:activityLink>
			</td>
		</tr>
	</logic:iterate>
	</table>
	<div style="clear: left;"></div>
</logic:notEmpty>

<bean:size id="totalItems" name="process" property="request.requestItems"/>
<logic:iterate id="refundItem" name="process" property="request.requestItems" indexId="index">
	<div class="item">
	<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
	<strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)
	<bean:define id="itemOID" name="refundItem" property="externalId" type="java.lang.String"/>
	
	<logic:equal name="process" property="inGenesis" value="true">
		<logic:equal name="refundItem" property="valueFullyAttributedToUnits" value="false">
			<div class="infobox_warning">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
			</div>
		</logic:equal>
	</logic:equal>		
	
	<logic:equal name="process" property="inAuthorizedState" value="true">
		<logic:equal name="refundItem" property="anyRefundInvoiceAvailable" value="true">
			<logic:equal name="refundItem" property="realValueFullyAttributedToUnits" value="false">
				<div class="infobox_warning">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
		</logic:equal>
		<logic:equal name="process" property="inSubmittedForInvoiceConfirmationState" value="true">
			<logic:equal name="refundItem" property="realValueFullyAttributedToUnits" value="false">
				<div class="infobox_warning">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
		</logic:equal>
	</logic:equal>

	<bean:define id="item" name="refundItem" toScope="request"/>
	<logic:equal name="item" property="refundValueBiggerThanEstimateValue" value="true">
	<div class="infobox_warning mtop15">
 		<p class="mvert025">
         <bean:message key="label.warning.refundValueBiggerThanEstimateValue" bundle="ACQUISITION_RESOURCES"/>
		 </p>
	</div>
	</logic:equal>
	<bean:define id="itemID" name="item" property="externalId" type="java.lang.String"/>

	<bean:define id="needsSeparator" value="false" toScope="request"/>	
	<wf:activityLink id="<%= "edit-" + itemID %>" processName="process" activityName="EditRefundItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
		<bean:define id="needsSeparator" value="true" toScope="request"/>
		<wf:activityName processName="process" activityName="EditRefundItem" scope="request"/>
	</wf:activityLink>					
		 	
	<wf:isActive processName="process" activityName="GenericAssignPayingUnitToItem" scope="request">	 					
		<logic:equal name="needsSeparator" value="true">
			<bean:define id="needsSeparator" value="false" toScope="request"/>
			| 
		</logic:equal>
	</wf:isActive>
	
	<wf:activityLink id="<%= "assignPayingUnits-" + itemID %>" processName="process" activityName="GenericAssignPayingUnitToItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
		<bean:define id="needsSeparator" value="true" toScope="request"/>
		<wf:activityName processName="process" activityName="GenericAssignPayingUnitToItem" scope="request"/>
	</wf:activityLink>

	<wf:isActive processName="process" activityName="CreateRefundInvoice" scope="request" scope="request">	 						
		<logic:equal name="needsSeparator" value="true">
			<bean:define id="needsSeparator" value="false" toScope="request"/>
			| 
		</logic:equal>
	</wf:isActive>
	
	<wf:activityLink id="<%= "createRefundInvoice-" + itemID %>" processName="process" activityName="CreateRefundInvoice" scope="request" paramName0="item" paramValue0="<%= itemID %>">
		<bean:define id="needsSeparator" value="true" toScope="request"/>
		<wf:activityName processName="process" activityName="CreateRefundInvoice" scope="request"/>
	</wf:activityLink>
	
	<wf:isActive processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request">	 						
	<logic:equal name="needsSeparator" value="true">
		<bean:define id="needsSeparator" value="false" toScope="request"/>
		| 
	</logic:equal>
	</wf:isActive>
	
	<wf:activityLink id="<%= "distributeRealValuesForPayingUnits-" + itemID %>" processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request" paramName0="item" paramValue0="<%= itemID %>">
			<bean:define id="needsSeparator" value="true" toScope="request"/>
			<wf:activityName processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request"/>
	</wf:activityLink>
	
	<wf:isActive processName="process" activityName="DeleteRefundItem" scope="request">	 						
		<logic:equal name="needsSeparator" value="true">
			<bean:define id="needsSeparator" value="false" toScope="request"/>
			| 
		</logic:equal>
	</wf:isActive>
	
	<wf:activityLink id="<%= "deleteRefundItem-" + itemID %>" processName="process" activityName="DeleteRefundItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
		<bean:define id="needsSeparator" value="true" toScope="request"/>
		<wf:activityName processName="process" activityName="DeleteRefundItem" scope="request"/>
	</wf:activityLink>
	
	<jsp:include page="/acquisitions/commons/viewRefundItem.jsp"/>
		<logic:notEmpty name="refundItem" property="refundableInvoices">
				<table class="tstyle3 tdmiddle tdnoborder vpadding05" style="width: 100%;">
					<tr>
					<th><bean:message key="acquisitionProcess.label.invoice.number" bundle="ACQUISITION_RESOURCES"/></th>		
					<th><bean:message key="acquisitionProcess.label.invoice.date" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.invoice.value" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.invoice.vatValue" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.invoice.totalValue" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.invoice.refundableValue" bundle="ACQUISITION_RESOURCES"/></th>
					
					</tr>	
					<logic:iterate id="invoice" name="refundItem" property="refundableInvoices">
						<tr>
							<td class="nowrap" rowspan="2" style="border-right: 1px solid #eee !important;"><fr:view name="invoice" property="invoiceNumber"/></td>
							<td class="nowrap"><fr:view name="invoice" property="invoiceDate" type="org.joda.time.LocalDate"/></td>
							<td class="nowrap"><fr:view name="invoice" property="value"/></td>
							<td class="nowrap"><fr:view name="invoice" property="vatValue"/></td>
							<td class="nowrap"><fr:view name="invoice" property="valueWithVat"/></td>
							<td class="nowrap"><fr:view name="invoice" property="refundableValue"/></td>
							
						</tr>
						<tr style="border-bottom: 4px solid #eee;">
							<td colspan="6" class="aleft">
								<bean:define id="invoiceId" name="invoice" property="externalId" type="java.lang.String"/>
								<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>

								<bean:define id="invoiceDownloadUrl" type="java.lang.String">/workflowProcessManagement.do?method=downloadFile&amp;processId=<bean:write name="processId"/></bean:define>
								<html:link action="<%= invoiceDownloadUrl %>" paramId="fileId" paramName="invoice" paramProperty="externalId">
									<bean:write name="invoice" property="filename"/>
								</html:link><wf:isActive processName="process" activityName="RemoveRefundInvoice" scope="request">:</wf:isActive>
									
									
									<wf:activityLink id="<%= "EditRefundInvoice-" + invoiceId %>" processName="process" activityName="EditRefundInvoice" scope="request" paramName0="invoice" paramValue0="<%= invoiceId %>">
										<bean:message bundle="MYORG_RESOURCES" key="link.edit"/>
									</wf:activityLink>
									
									<wf:isActive processName="process" activityName="EditRefundInvoice" scope="request">
										|
									</wf:isActive>
										
					    			<wf:activityLink id="<%= "RemoveRefundInvoice-" + invoiceId %>" processName="process" activityName="RemoveRefundInvoice" scope="request" paramName0="refundInvoice" paramValue0="<%= invoiceId %>">
										<bean:message bundle="MYORG_RESOURCES" key="link.remove"/>
									</wf:activityLink>
									
										
								
									
									
					    	
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
</div>