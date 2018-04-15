<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess"%>
<%@page import="pt.ist.expenditureTrackingSystem.ExpenditureExtensions"%>
<%@page import="module.workflow.domain.WorkflowProcess"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<script type="text/javascript">
	$("#processControl").addClass("wrapper");
</script>

<% final RefundProcess refundProcess = (RefundProcess) request.getAttribute("process"); %>
<% if (refundProcess.shouldAllocateFundsToSupplier()) { %>
    <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.warning.refund.process.shouldAllocateFundsToSupplier" bundle="ACQUISITION_RESOURCES"/> 
        </p>
     </div>
<% } %>

<div class="infobox3 col2-1">
	<h3>Informação</h3>
	<div>
		<table class="process-info mbottom0"> 
			<tr><td><bean:message key="label.acquisitionProcessId" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="request.process.acquisitionProcessId"/></td></tr>
			<tr><td><bean:message key="label.requesterName" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="request.requester.user.displayName"/></td></tr>
			<tr><td><bean:message key="label.underCCPRegime" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="request.process.underCCPRegime"/></td></tr>
			<tr><td><bean:message key="label.skipingSupplierFundAllocation" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="request.process.skipSupplierFundAllocation"/></td></tr>
			<tr><td><bean:message key="label.refundee" bundle="ACQUISITION_RESOURCES"/>: <fr:view name="process" property="request.refundee.refundeePresentation" layout="null-as-label"/></td></tr>
			<tr><td><bean:message key="acquisitionProcess.label.requestingUnit" bundle="ACQUISITION_RESOURCES"/>: <fr:view name="process" property="request.requestingUnit.presentationName"/></td></tr>
			<tr><td><bean:message key="label.processState" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="request.process.processState.localizedName"/></td></tr>
			<tr><td><bean:message key="label.totalValue" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="request.totalValue"/></td></tr>
			<tr><td><bean:message key="label.totalRealValue.refund" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="request.realTotalValue"/></td></tr>
			<tr><td>
				<bean:message key="acquisitionRequestItem.label.paymentReference" bundle="ACQUISITION_RESOURCES"/>: 
				<fr:view name="process" property="request.paymentReference" type="java.lang.String">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default" />
					</fr:layout>
				</fr:view>
				</td>
			</tr>
		</table>
	</div>
</div>




<div class="infobox2 col2-2">
	<h3>Estado</h3>
	<fr:view name="process" layout="process-state-view"/>
</div>


<div class="clear"></div>			

<logic:present name="process" property="missionProcess">
	<div class="infobox mtop15">
 		<p class="mvert025">
 			<bean:message key="label.acquisition.process.consult.mission.process" bundle="ACQUISITION_RESOURCES"/>:
			<html:link target="blank" action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="missionProcess.externalId">
	 			<bean:write name="process" property="missionProcess.processIdentification"/>
			</html:link>
    	</p>
	</div>
</logic:present>

<bean:define id="payingUnits" name="process" property="request.totalAmountsForEachPayingUnit"/>
<logic:notEmpty name="payingUnits">

	<h3>Unidades Pagadoras</h3>

	<table class="tview1" style="width: 100%;">
		<tr>	
			<th class="aleft"><bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/></th>
			<th class="acenter" style="width: 70px; white-space: nowrap;">
					<bean:message key="acquisitionProcess.label.accountingUnit" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th id="fundAllocationHeader">
					<bean:message key="financer.label.fundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th id="effectiveFundAllocationHeader"> 
					<bean:message key="financer.label.effectiveFundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<% if (ExpenditureTrackingSystem.getInstance().getRegisterDiaryNumbersAndTransactionNumbers() != null
			        && ExpenditureTrackingSystem.getInstance().getRegisterDiaryNumbersAndTransactionNumbers().booleanValue()) { %>
            <th>
                <bean:message key="financer.label.diaryNumber" bundle="ACQUISITION_RESOURCES"/>
            </th>
            <th>
                <bean:message key="financer.label.transactionNumber" bundle="ACQUISITION_RESOURCES"/>
            </th>
            <% } %>
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
				<html:link styleClass="secondaryLink" page='<%= "/expenditureTrackingOrganization.do?method=viewOrganization&unitOid=" + unitOID%>' target="_blank">
					<fr:view name="payingUnit" property="payingUnit.presentationName"/>
				</html:link>
			</td>
			<bean:define id="financer" name="payingUnit" property="financer"/>
			<td class="acenter" style="width: 80px;">
				<logic:present name="payingUnit" property="financer.accountingUnit">
					<fr:view name="payingUnit" property="financer.accountingUnit.name"/>
				</logic:present>
			</td>
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
            <% if (ExpenditureTrackingSystem.getInstance().getRegisterDiaryNumbersAndTransactionNumbers() != null
                    && ExpenditureTrackingSystem.getInstance().getRegisterDiaryNumbersAndTransactionNumbers().booleanValue()) { %>
            <td>
                    <logic:equal name="payingUnit" property="financer.effectiveFundAllocationPresent" value="true">
                        <logic:present name="payingUnit" property="financer.paymentDiaryNumber">
                            <bean:define id="paymentDiaryNumber" name="payingUnit" property="financer.paymentDiaryNumber" type="pt.utl.ist.fenix.tools.util.Strings"/>
                            <%
                                for (final String s : paymentDiaryNumber.getUnmodifiableList()) {
                            %>
                                    <%= s %>
                            <%
                                }
                            %>
                        </logic:present> 
                    </logic:equal>
            </td>
            <td>
                    <logic:equal name="payingUnit" property="financer.effectiveFundAllocationPresent" value="true">
                        <logic:present name="payingUnit" property="financer.transactionNumber">
                            <bean:define id="transactionNumber" name="payingUnit" property="financer.transactionNumber" type="pt.utl.ist.fenix.tools.util.Strings"/>
                            <%
                                for (final String s : transactionNumber.getUnmodifiableList()) {
                            %>
                                    <%= s %>
                            <%
                                }
                            %>
                        </logic:present> 
                    </logic:equal>
            </td>
		      <% } %>
			<td class="aright nowrap" style="width: 80px;"><fr:view name="payingUnit" property="amount"/></td>
			<td>
				<wf:activityLink id='<%= "removePayingUnit-" + unitOID %>' processName="process" activityName="GenericRemovePayingUnit" scope="request" paramName0="payingUnit" paramValue0="<%= unitOID %>">
					<bean:message bundle="MYORG_RESOURCES" key="link.remove"/>
				</wf:activityLink>
			</td>
		</tr>
	</logic:iterate>
	</table>

    <logic:iterate id="payingUnit" name="payingUnits">
    </logic:iterate>
</logic:notEmpty>



<%
    for (final String jspFile : WorkflowProcess.getHooksFor(ExpenditureExtensions.VIEW_REFUND_PROCESS_HOOK)) {
%>
        <jsp:include page="<%= jspFile %>"/>
<%
    }
%>





<bean:size id="totalItems" name="process" property="request.requestItems"/>
<bean:define id="allItems" name="totalItems"  toScope="request"/>

<logic:notEmpty name="process" property="request.requestItems">

<h3>Itens</h3>


<table class="tview2" style="width: 100%;">

<logic:iterate id="refundItem" name="process" property="request.requestItems" indexId="index" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem">
<bean:define id="currentIndex" value='<%= String.valueOf(index + 1) %>' toScope="request"/>
	
	<tbody class='<%=  "item" + index %>'>

        <% if (refundItem.shouldAllocateFundsToSupplier()) { %>
            <tr>
                <td colspan="6">
                    <div class="infobox_warning">
                        <p class="mvert025">
                            <bean:message key="label.warning.refund.process.shouldAllocateFundsToSupplier.item" bundle="ACQUISITION_RESOURCES"/> 
                        </p>
                    </div>
                </td>
            </tr>
        <% } %>

		<logic:equal name="process" property="inGenesis" value="true">
			<logic:equal name="refundItem" property="valueFullyAttributedToUnits" value="false">
				<tr>
					<td colspan="6">
						<div class="infobox_warning">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
						</div>
					</td>
				</tr>
			</logic:equal>
		</logic:equal>		
		
		<logic:equal name="process" property="inAuthorizedState" value="true">
			<logic:equal name="refundItem" property="anyRefundInvoiceAvailable" value="true">
				<logic:equal name="refundItem" property="realValueFullyAttributedToUnits" value="false">
				<tr>
					<td colspan="6">
						<div class="infobox_warning">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
						</div>
					</td>
				</tr>
				</logic:equal>
			</logic:equal>
			<logic:equal name="process" property="inSubmittedForInvoiceConfirmationState" value="true">
				<logic:equal name="refundItem" property="realValueFullyAttributedToUnits" value="false">
					<tr>
						<td colspan="6">
							<div class="infobox_warning">
								<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
							</div>
						</td>
					</tr>
				</logic:equal>
			</logic:equal>
		</logic:equal>
	
		<bean:define id="item" name="refundItem" toScope="request"/>
		<logic:equal name="item" property="refundValueBiggerThanEstimateValue" value="true">
			<tr>
				<td colspan="6">
					<div class="infobox_warning">
				 		<p class="mvert025">
				 			<bean:message key="label.warning.refundValueBiggerThanEstimateValue" bundle="ACQUISITION_RESOURCES"/>
				 		</p>
					</div>
				</td>
			</tr>
		</logic:equal>
			
	

		<jsp:include page="/acquisitions/commons/viewRefundItem.jsp"/>
		
		
		
		
		<logic:notEmpty name="refundItem" property="refundableInvoices">
		
		<tr class="refund-invoices">
		
		<td colspan="6">
		
			<table>
	
			<logic:iterate id="invoice" name="refundItem" property="refundableInvoices">
				<tr>
					<td class="nowrap" rowspan="2">
						<bean:message key="acquisitionProcess.label.invoice.number" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="invoice" property="invoiceNumber"/>
					</td>
					<td class="nowrap">
						<bean:message key="acquisitionProcess.label.invoice.date" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="invoice" property="invoiceDate" type="org.joda.time.LocalDate"/>
					</td>
					<td class="nowrap">
						<bean:message key="label.invoice.value" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="invoice" property="value"/>
					</td>
					<td class="nowrap">
						<bean:message key="label.invoice.vatValue" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="invoice" property="vatValue"/>
					</td>
					<td class="nowrap">
						<bean:message key="label.invoice.totalValue" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="invoice" property="valueWithVat"/>
					</td>
					<td class="nowrap">
						<bean:message key="label.invoice.refundableValue" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="invoice" property="refundableValue"/>
					</td>								
				</tr>
				<tr>
					<td colspan="6" class="aleft">
						<bean:define id="invoiceId" name="invoice" property="externalId" type="java.lang.String"/>
						<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
	
						<bean:define id="invoiceDownloadUrl" type="java.lang.String">/workflowProcessManagement.do?method=downloadFile&amp;processId=<bean:write name="processId"/></bean:define>
						<html:link action="<%= invoiceDownloadUrl %>" paramId="fileId" paramName="invoice" paramProperty="externalId">
							<bean:write name="invoice" property="filename"/>
						</html:link><wf:isActive processName="process" activityName="RemoveRefundInvoice" scope="request">:</wf:isActive>
							
							<wf:activityLink id='<%= "EditRefundInvoice-" + invoiceId %>' processName="process" activityName="EditRefundInvoice" scope="request" paramName0="invoice" paramValue0="<%= invoiceId %>">
								<bean:message bundle="MYORG_RESOURCES" key="link.edit"/>
							</wf:activityLink>
							
							<wf:isActive processName="process" activityName="EditRefundInvoice" scope="request">
								|
							</wf:isActive>
								
			    			<wf:activityLink id='<%= "RemoveRefundInvoice-" + invoiceId %>' processName="process" activityName="RemoveRefundInvoice" scope="request" paramName0="refundInvoice" paramValue0="<%= invoiceId %>">
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
			
			</td>
			
			</tr>
			
		</logic:notEmpty>









		
	</tbody>
	
</logic:iterate>
</table>


<p class="aright" style="margin-bottom: 0;">
	<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#"><bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/></a>
</p>


</logic:notEmpty>
