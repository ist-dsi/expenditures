<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>

<bean:define id="acquisitionProcessOid"><bean:write name="process" property="externalId"/></bean:define>

<div class="wrapper"> 
<script type="text/javascript">
	$("#processControl").addClass("wrapper");
</script>


<bean:define id="processRequest" name="process" property="request" toScope="request"/>
<jsp:include page="/acquisitions/commons/viewAcquisitionRequest.jsp" flush="true"/>

<logic:equal name="process" property="acquisitionRequest.partiallyApproved" value="true">
 <div class="infobox_warning mtop15">
 	<p class="mvert025">
         <bean:message key="label.warning.multipleApprovals" bundle="ACQUISITION_RESOURCES"/>
    </p>
</div>
</logic:equal>

<logic:equal name="process" property="acquisitionRequest.partiallyAuthorized" value="true">
 <div class="infobox_warning mtop15">
 	<p class="mvert025">
         <bean:message key="label.warning.multipleAuthorizations" bundle="ACQUISITION_RESOURCES"/>
    </p>
</div>
</logic:equal>

<logic:equal name="process" property="acquisitionRequest.withInvoicesPartiallyConfirmed" value="true">
 <div class="infobox_warning mtop15">
 	<p class="mvert025">
         <bean:message key="label.warning.multipleConfirmations" bundle="ACQUISITION_RESOURCES"/>
    </p>
</div>
</logic:equal>
   
<bean:define id="payingUnits" name="process" property="acquisitionRequest.totalAmountsForEachPayingUnit"/>
<logic:notEmpty name="payingUnits">

	<table class="tstyle4 mvert1 width100pc tdmiddle thnoborder">
		<tr>	
			<th class="aleft"><bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/></th>
			<th></th>
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
		</tr>
	
	<logic:iterate id="payingUnit" name="payingUnits">
		<tr>
			<td class="aleft">
				<bean:define id="unitOID" name="payingUnit" property="payingUnit.externalId" type="java.lang.String"/>
				<html:link styleClass="secondaryLink" page="<%= "/expenditureTrackingOrganization.do?method=viewOrganization&unitOid=" + unitOID%>" target="_blank">
					<fr:view name="payingUnit" property="payingUnit.presentationName"/>
				</html:link>
				<wf:isActive processName="process" activityName="GenericRemovePayingUnit" scope="request">(</wf:isActive>
				<wf:activityLink id="<%= "remove-" + unitOID %>" processName="process" activityName="GenericRemovePayingUnit" scope="request" paramName0="payingUnit" paramValue0="<%= unitOID %>">
						<bean:message key="link.remove" bundle="MYORG_RESOURCES"/>
				</wf:activityLink>
				<wf:isActive processName="process" activityName="GenericRemovePayingUnit" scope="request">)</wf:isActive>	
			</td>
			<bean:define id="financer" name="payingUnit" property="financer"/>
			<td class="nowrap">
				<fr:view name="financer" layout="financer-status"/>
			</td>
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
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>


<bean:define id="itemSet" name="process" property="acquisitionRequest.orderedRequestItemsSet"/> 
<logic:present name="itemSet">
	
		<logic:equal  name="process" property="pastInvoiceReceived"  value="true">		
			<logic:equal name="process" property="acquisitionRequest.realValueLessThanTotalValue" value="false">
				<div class="infobox_warning">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realValueLessThanTotalValue" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
			<logic:equal name="process" property="acquisitionRequest.realUnitShareValueLessThanUnitShareValue" value="false">
				<div class="infobox_warning">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realUnitShareValueLessThanUnitShareValue" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
			<logic:equal name="process" property="acquisitionRequest.realTotalValueEqualsRealShareValue" value="false">
				<div class="infobox_warning">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realTotalValueEqualsRealShareValue" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
		</logic:equal>
	
		<logic:notEmpty name="itemSet">
			<bean:size id="size" name="itemSet"/>
			<table class="tstyle4 mvert1 width100pc tdmiddle thnoborder" id="itemResume">
				<tr>
					<th></th>
					<th class="aleft"><bean:message key="acquisitionRequestItem.label.description" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="acquisitionRequestItem.label.quantity" bundle="ACQUISITION_RESOURCES"/></th>
					<th class="aright"><bean:message key="acquisitionRequestItem.label.totalValueWithVAT" bundle="ACQUISITION_RESOURCES"/></th>
				</tr>
				<logic:iterate id="itemResume" name="itemSet" indexId="index">
					<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
					<tr>
						<td class="nowrap">
							<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
							<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= "#item" + currentIndex%>">
								Item <%= currentIndex %>
							</a>
							<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
						</td>

						<td class="aleft"><fr:view name="itemResume" property="description"/></td>
						<td class="acenter" style="width: 80px;"><fr:view name="itemResume" property="quantity"/></td>
						<td class="aright nowrap" style="width: 80px;"><fr:view name="itemResume" property="currentTotalItemValueWithAdditionalCostsAndVat"/></td>
					</tr>
				</logic:iterate>
				<logic:greaterThan value="1" name="size">
					<tr>
						<td colspan="4" class="aright"><span><b></b><fr:view name="process" property="acquisitionRequest.currentTotalRoundedValue"/></span></td>
					</tr>
				</logic:greaterThan>
			</table>
		</logic:notEmpty>
		
		<bean:size id="totalItems" name="itemSet"/>
		<logic:iterate id="acquisitionRequestItem" name="itemSet" indexId="index">
			<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
			<div class="item" id="<%= "item" + currentIndex %>">
				<strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)
				<bean:define id="itemOID" name="acquisitionRequestItem" property="externalId" type="java.lang.String"/>
				
				<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="false">
					<logic:equal name="acquisitionRequestItem" property="valueFullyAttributedToUnits" value="false">
						<div class="infobox_warning">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
						</div>
					</logic:equal>
				</logic:equal>
				
				<logic:equal  name="process" property="acquisitionProcessState.invoiceConfirmed"  value="true">		
					<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="false">
						<div class="infobox_warning">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.info.valuesNotFilled" bundle="ACQUISITION_RESOURCES"/>
						</div>
					</logic:equal>
				</logic:equal>
			
				<bean:define id="item" name="acquisitionRequestItem" toScope="request"/>
				<jsp:include page="/acquisitions/commons/viewAcquisitionRequestItem.jsp"/>
				<p class="aright">
					<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
					<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#"><bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/></a>
					<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
				</p>
			</div>
		</logic:iterate>
	</logic:present>
</div>

</div>

