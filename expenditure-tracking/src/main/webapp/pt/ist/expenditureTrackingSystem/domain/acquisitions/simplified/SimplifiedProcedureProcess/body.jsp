<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoiceItem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice"%>
<%@page import="module.workflow.domain.ProcessFile"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"%>
<%@page import="pt.ist.expenditureTrackingSystem.ExpenditureExtensions"%>
<%@page import="module.workflow.domain.WorkflowProcess"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<bean:define id="acquisitionProcessOid"><bean:write name="process" property="externalId"/></bean:define>

<bean:define id="processRequest" name="process" property="request" toScope="request"/>
<bean:define id="processId" name="process" property="externalId"/>

<%--
<logic:equal name="process" property="warnRegardingProcessClassificationNeeded" value="true">
	 <div class="infobox_warning">
	 	<p class="mvert025">
	         <bean:message key="label.warning.mismatchBetweenClassificationAndUnitDefault" bundle="ACQUISITION_RESOURCES"/>
	         
	         <logic:equal name="process" property="acquisitionRequest.requestingUnit.defaultRegeimIsCCP" value="true">
	        	<p>
	        	<strong>
	        			<bean:message key="label.warning.mismatchBetweenClassificationAndUnitDefault.ccpWarn" bundle="ACQUISITION_RESOURCES"/>
	        	</strong>
	        	</p>
	         </logic:equal>
	    </p>
	</div>
</logic:equal>
 --%>
 
 
 
<logic:equal name="process" property="warnForLessSuppliersActive" value="true">
	<div class="infobox_warning">
	 	<p class="mvert025">
	 		<bean:message key="label.warning.warnForLessSuppliers" bundle="ACQUISITION_RESOURCES"/>
	 	</p>
	 </div>
</logic:equal>
<logic:equal  name="process" property="acquisitionProcessState.authorized"  value="true">
	<logic:notEmpty name="process" property="advancePaymentDocumentState">
		<div class="infobox_warning">
			<bean:define id="advancePaymentDocumentState" name="process" property="advancePaymentDocumentState"/>
			<bean:message key="<%="label.warning.advancePaymentDocument." + advancePaymentDocumentState.toString().toLowerCase()%>" bundle="ACQUISITION_RESOURCES"/>
		</div>
	</logic:notEmpty>
</logic:equal>
<div class="infobox3 col2-1">
	<h3>Informação</h3>
	<div>
		<table class="process-info mbottom0">
			<tr class="first">
				<td><bean:message key="label.acquisitionProcessId" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="processNumber"/></td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.processClassification" bundle="EXPENDITURE_RESOURCES"/>: <fr:view name="process" property="processClassification"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.requesterName" bundle="EXPENDITURE_RESOURCES"/>:
					 <fr:view name="process" property="requestor" layout="values">
					 	<fr:schema type="pt.ist.expenditureTrackingSystem.domain.organization.Person" bundle="EXPENDITURE_RESOURCES">
						 	<fr:slot name="user.displayName" layout="link" >
								<fr:property name="useParent" value="true" />
								<fr:property name="blankTarget" value="true" />
								<fr:property name="linkFormat"
									value="/expenditureTrackingOrganization.do?personOid=\${externalId}&method=viewPerson" />
								<fr:property name="classes" value="secondaryLink" />
							</fr:slot>
						</fr:schema>
					</fr:view>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="acquisitionProcess.label.requestingUnit" bundle="ACQUISITION_RESOURCES"/>: <fr:view name="process" property="requestingUnit.presentationName"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.supplier" bundle="EXPENDITURE_RESOURCES"/>: 
					<fr:view name="process" property="acquisitionRequest.supplier" type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier">
						<fr:layout name="null-as-label">
							<fr:property name="subLayout" value="values" />
							<fr:property name="subSchema" value="viewSupplierPresentationName.withLink" />
							<fr:property name="classes" value="nobullet" />
						</fr:layout>
					</fr:view>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.skipingSupplierFundAllocation" bundle="EXPENDITURE_RESOURCES"/>: 
					<fr:view name="process" property="skipSupplierFundAllocation"/>
					<%
						final SimplifiedProcedureProcess spp = (SimplifiedProcedureProcess) request.getAttribute("process");
						if (spp.getSkipSupplierFundAllocation() != null && spp.getSkipSupplierFundAllocation()
						        && spp.getAcquisitionRequest().getSupplier() != null
						        && MissionSystem.getInstance().getMandatorySupplierSet().contains(spp.getAcquisitionRequest().getSupplier())) {
					%>
							<bean:message key="label.acquisition.process.under.eSPAP" bundle="EXPENDITURE_RESOURCES"/>
					<%
						}
					%>
				</td>
			</tr>
		</table>



		<p class="mver1"><span id="show1" class="link"><bean:message key="label.moreInfo" bundle="EXPENDITURE_RESOURCES"/></span></p>
		<p class="mver1"><span id="show2" style="display: none" class="link"><bean:message key="label.lessInfo" bundle="EXPENDITURE_RESOURCES"/></span></p>
		

		
		<div id="extraInfo" style="display: none; margin: 0;">

			<fr:view name="processRequest"
					type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
					schema="viewAcquisitionRequest.extra">
				<fr:layout name="tabular">
					<fr:property name="classes" value="process-info lastnoborder firststrongborder mtop0 mbottom0"/>
					<fr:property name="columnClasses" value="width165px,,"/>
				</fr:layout>
			</fr:view>

			<table class="process-info firstnoborder mvert0">
				<logic:empty name="processRequest" property="invoices">
					<tr>
						<th style="width: 165px;"><bean:message key="label.invoices" bundle="EXPENDITURE_RESOURCES"/>:</th>
						<td>-</td>
					</tr>
				</logic:empty>
			
				<logic:notEmpty name="processRequest" property="invoices">
					<tr>
						<th style="width: 165px;"><bean:message key="label.invoices" bundle="EXPENDITURE_RESOURCES"/>:</th>
						<td>
							<logic:iterate id="invoice" name="processRequest" property="invoices">
								<p class="mtop0 mbottom025">
									<span><fr:view name="invoice" property="invoiceNumber"/></span> <span style="padding: 0 0.3em; color: #aaa;">|</span>
									<span><fr:view name="invoice" property="invoiceDate"/></span> <span style="padding: 0 0.3em; color: #aaa;">|</span>
									<span>
										<html:link action='<%= "/workflowProcessManagement.do?method=downloadFile&processId=" + processId%>' paramId="fileId" paramName="invoice" paramProperty="externalId">
												<fr:view name="invoice" property="filename"/>
										</html:link>
									</span>
								</p>
								<fr:view name="invoice" property="requestItems">
									<fr:layout>
										<fr:property name="eachLayout" value="values"/>
										<fr:property name="eachSchema" value="itemDescription.descriptionOnly"/>
										<fr:property name="style" value="margin: 0.25em 0 1em 0; padding-left: 1.0em;"/>
									</fr:layout>
								</fr:view>
							</logic:iterate>			
						</td>
					</tr>
				</logic:notEmpty>
				
				<logic:notEmpty name="processRequest" property="suppliers">
					<tr>
						<th>
							<bean:message key="supplier.title.manage" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
						</th>
						<td>
							<logic:notEmpty name="processRequest" property="suppliers">
							<ul>
							<logic:iterate id="supplier" name="processRequest" property="suppliers">
								<li><bean:define id="supplierName" name="supplier" property="name"/>
								<p class="mvert0">
									<logic:equal name="processRequest" property="process.allowedToViewSupplierExpenditures" value="true">
										<logic:equal name="processRequest" property="process.processClassification" value="CCP">
											<bean:message key="supplier.message.info.totalAllocated.withArgument" bundle="EXPENDITURE_ORGANIZATION_RESOURCES" arg0="<%= supplierName.toString() %>"/>:
											<fr:view name="supplier" property="totalAllocated"/>
										</logic:equal>
			
										<logic:notEqual name="processRequest" property="process.processClassification" value="CCP">
											<fr:view name="supplierName"/>
										</logic:notEqual>
									</logic:equal>
			
									<logic:equal name="processRequest" property="process.allowedToViewSupplierExpenditures" value="false">
										<fr:view name="supplierName"/>
									</logic:equal>
								</p></li>
							</logic:iterate>
							</ul>
							</logic:notEmpty>
						</td>
					</tr>
				</logic:notEmpty>
			</table>
		</div>
		<!-- #extrainfo -->

		<script type="text/javascript">
			$("#show1").click(
					function() {
						$('#extraInfo').slideToggle();
						$('#show1').hide();
						$('#show2').show();
					}
				);
	
			$("#show2").click(
					function() {
						$('#extraInfo').slideToggle();
						$('#show2').hide();
						$('#show1').show();
					}
				);
		</script>

	</div>
</div>
<!-- infobox3 -->		



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

<div class="clear"></div>			
<logic:notEmpty name="process" property="lastAcquisitionApprovalTerms">
	<div style="padding: 5px 10px; margin: 1em 0; border:1px solid #E0E0E0;">
		<logic:iterate id="acquisitionApprovalTerm" name="process" property="lastAcquisitionApprovalTerms">
			<div class="infobox">
				<p>
					<fr:view name="acquisitionApprovalTerm" property="approvalMessage" layout="html"/>
					<br/>
					<span class="smalltxt">
						<fr:view name="acquisitionApprovalTerm" property="date"/>
						-
						<fr:view name="acquisitionApprovalTerm" property="approver.user.displayName"/>
					</span>
				</p>
			</div>
		</logic:iterate>
	</div>
</logic:notEmpty>

<div class="clear"></div>
		
<bean:define id="itemSet" name="process" property="acquisitionRequest.orderedRequestItemsSet"/> 
<bean:size id="size" name="itemSet"/>

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
 

<div class="clear"></div>

 
<bean:define id="payingUnits" name="process" property="acquisitionRequest.totalAmountsForEachPayingUnit"/>
<logic:notEmpty name="payingUnits">


<h3>Unidades Pagadoras</h3>

<bean:define id="areFundAllocationPresent" name="process" property="fundAllocationPresent"/>
<bean:define id="areEffectiveFundAllocationPresent" name="process" property="effectiveFundAllocationPresent"/>

<table class="tview1" style="width: 100%;">
	<tr>
		<th class="aleft"><bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/></th>
		<th></th>
		<th class="acenter" style="width: 70px; white-space: nowrap;">
			<bean:message key="acquisitionProcess.label.accountingUnit" bundle="ACQUISITION_RESOURCES"/>
		</th>
		<logic:equal name="areFundAllocationPresent" value="true">
			<th>
				<bean:message key="financer.label.fundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="areEffectiveFundAllocationPresent" value="true">
			<th>
				<bean:message key="financer.label.effectiveFundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="financer.label.diaryNumber" bundle="ACQUISITION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="financer.label.transactionNumber" bundle="ACQUISITION_RESOURCES"/>
			</th>
		</logic:equal>
		<th class="aright">
			<bean:message key="acquisitionRequestItem.label.totalValueWithVAT" bundle="ACQUISITION_RESOURCES"/>
		</th>
		<th class="aright">
			<bean:message key="label.approved" bundle="EXPENDITURE_RESOURCES"/>
		</th>
		<th class="aright">
			<bean:message key="label.authorized" bundle="EXPENDITURE_RESOURCES"/>
		</th>
	</tr>
	<logic:iterate id="payingUnit" name="payingUnits">
		<tr>
			<td class="aleft">
				<bean:define id="unitOID" name="payingUnit" property="payingUnit.externalId" type="java.lang.String"/>
				<html:link styleClass="secondaryLink" page='<%= "/expenditureTrackingOrganization.do?method=viewOrganization&unitOid=" + unitOID%>' target="_blank">
					<fr:view name="payingUnit" property="payingUnit.presentationName"/>
				</html:link>
				<wf:isActive processName="process" activityName="GenericRemovePayingUnit" scope="request">(</wf:isActive>
				<wf:activityLink processName="process" activityName="GenericRemovePayingUnit" scope="request" paramName0="payingUnit" paramValue0="<%= unitOID %>">
						<bean:message key="link.remove" bundle="MYORG_RESOURCES"/>
				</wf:activityLink>
				<wf:isActive processName="process" activityName="GenericRemovePayingUnit" scope="request">)</wf:isActive>	
			</td>
			<bean:define id="financer" name="payingUnit" property="financer"/>
			<td class="nowrap tooltipWidth400px">
				<fr:view name="financer" layout="financer-status"/>
			</td>
			<td class="acenter" style="width: 80px;">
				<logic:present name="payingUnit" property="financer.accountingUnit">
					<fr:view name="payingUnit" property="financer.accountingUnit.name"/>
				</logic:present>
			</td>
			<logic:equal name="areFundAllocationPresent" value="true">
				<td>
					<logic:equal name="payingUnit" property="financer.fundAllocationPresent" value="true">
						<fr:view name="payingUnit" property="financer.fundAllocationIds"/> 
					</logic:equal>
				</td>
			</logic:equal>
			<logic:equal name="areEffectiveFundAllocationPresent" value="true">
				<td>
					<logic:equal name="payingUnit" property="financer.effectiveFundAllocationPresent" value="true">
						<fr:view name="payingUnit" property="financer.effectiveFundAllocationIds"/> 
					</logic:equal>
				</td>
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
			</logic:equal>
			<td class="aright nowrap" style="width: 80px;"><fr:view name="payingUnit" property="amount"/></td>
			<td class="aright"><fr:view name="payingUnit" property="financer.approved"/></td>
			<td class="aright"><fr:view name="payingUnit" property="financer.authorized"/></td>
		</tr>
	</logic:iterate>
</table> 
</logic:notEmpty>

<h4><bean:message key="label.commitmentNumbers" bundle="EXPENDITURE_RESOURCES"/></h4>

<ul>
	<logic:iterate id="financer" name="process" property="acquisitionRequest.financersSet">
		<li>
			<bean:write name="financer" property="unit.presentationName"/>
			:
			<bean:write name="financer" property="commitmentNumber"/>

			<bean:define id="financerId" name="financer" property="externalId" type="java.lang.String"/>
			<wf:activityLink id='<%= "deleteCommitmentNumber-" + financerId %>' processName="process" activityName="DeleteCommitmentNumber" scope="request" paramName0="financer" paramValue0="<%= financerId %>">
				<bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
			</wf:activityLink>
		</li>
	</logic:iterate>
</ul>

<div class="clear"></div>	
<logic:notEmpty name="process" property="acquisitionRequest.advancePaymentRequest">
	<br>
 	<h3><bean:message key="label.advancePayment" bundle="ACQUISITION_RESOURCES"/></h3>
	<fr:view name="process" property="acquisitionRequest.advancePaymentRequest">
		<fr:schema bundle="ACQUISITION_RESOURCES" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AdvancePaymentRequest">
			<fr:slot name="advancePaymentDocumentTemplate.type" key="label.advancePayment" bundle="ACQUISITION_RESOURCES"/>
			<fr:slot name="paymentMethod" key="label.advancePayment.paymentMethod" bundle="ACQUISITION_RESOURCES"/>
			<logic:equal name="process" property="acquisitionRequest.advancePaymentRequest.advancePaymentDocumentTemplate.partialValue" value="true">
				<fr:slot name="percentage" key="label.advancePayment.percentage" bundle="ACQUISITION_RESOURCES"/>
			</logic:equal>
			<logic:equal name="process" property="acquisitionRequest.advancePaymentRequest.advancePaymentDocumentTemplate.needAcquisitionJustification" value="true">
				<fr:slot name="acquisitionJustification" key="label.advancePayment.acquisitionJustification" bundle="ACQUISITION_RESOURCES"/>
			</logic:equal>
			<logic:equal name="process" property="acquisitionRequest.advancePaymentRequest.advancePaymentDocumentTemplate.needEntityJustification" value="true">
				<fr:slot name="entityJustification" key="label.advancePayment.entityJustification" bundle="ACQUISITION_RESOURCES"/>
			</logic:equal>
		</fr:schema>
		<bean:define id="advancePaymentRequestId" name="process" property="acquisitionRequest.advancePaymentRequest.externalId" />
		<wf:activityLink id='<%= "edit-" + advancePaymentRequestId.toString() %>' processName="process" activityName="RequestAdvancePayment" scope="request" paramName0="advancePaymentRequest" paramValue0="<%= advancePaymentRequestId.toString() %>">
			<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
		</wf:activityLink>
		<wf:activityLink id='<%= "edit-" + advancePaymentRequestId.toString() %>' processName="process" activityName="DeleteAdvancePaymentRequest" scope="request" paramName0="advancePaymentRequest" paramValue0="<%= advancePaymentRequestId.toString() %>">
			| <bean:message key="link.remove" bundle="EXPENDITURE_RESOURCES"/>
		</wf:activityLink>
	</fr:view>
</logic:notEmpty>
<logic:notEmpty name="process" property="signedAdvancePaymentDocument">
	<logic:empty name="process" property="acquisitionRequest.advancePaymentRequest">
		<br>
	 	<h3><bean:message key="label.advancePayment" bundle="ACQUISITION_RESOURCES"/></h3>
 	</logic:empty>
	<fr:view name="process" >
		<fr:schema bundle="ACQUISITION_RESOURCES" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess">
			<fr:slot name="financialProcessId" layout="null-as-label" key="label.financialProcessIdentification"></fr:slot>
		</fr:schema>
	</fr:view>
	<logic:notEmpty name="process" property="request.paymentSet">
		<table class="table">
			<tr><th><bean:message key="label.advancePayment.paymentReference" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="label.advancePayment.value" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="label.advancePayment.additionalValue" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="label.advancePayment.date" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="label.advancePayment.description" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="label.advancePayment.compensationNumber" bundle="ACQUISITION_RESOURCES"/></th>
			<th /></tr>
		<logic:iterate id="payment" name="process" property="request.paymentSet" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.Payment">
			<tr>
				<td><fr:view name="payment" property="reference" layout="null-as-label" /></td>
				<td><fr:view name="payment" property="value" layout="null-as-label" /></td>
				<td><fr:view name="payment" property="additionalValue" layout="null-as-label" type="module.finance.util.Money"/></td>
				<td><fr:view name="payment" property="date" layout="null-as-label" /></td>
				<td><fr:view name="payment" property="description" layout="null-as-label" /></td>
				<td><fr:view name="payment" property="compensationNumber" layout="null-as-label" type="java.lang.String"/></td>
				<td>
					<logic:empty name="payment" property="compensationNumber">
					 	<wf:activityLink id='<%= "edit-" + payment.getExternalId() %>' processName="process" activityName="EditAdvancePayment" scope="request" paramName0="payment" paramValue0="<%= payment.getExternalId() %>">
							<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
							| 
						</wf:activityLink>
					</logic:empty>
					<wf:activityLink id='<%= "setCompensation-" + payment.getExternalId() %>' processName="process" activityName="SetAdvancePaymentCompensationNumber" scope="request" paramName0="payment" paramValue0="<%= payment.getExternalId() %>">
						<bean:message key="label.advancePayment.compensationNumber" bundle="ACQUISITION_RESOURCES"/>
					</wf:activityLink>
					
				</td>
			</tr>
		</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:notEmpty>

<%
    for (final String jspFile : WorkflowProcess.getHooksFor(ExpenditureExtensions.VIEW_ACQUISITION_PROCESS_HOOK)) {
%>
        <jsp:include page="<%= jspFile %>"/>
<%
    }
%>

<logic:present name="itemSet">
	
		<logic:equal  name="process" property="pastInvoiceReceived"  value="true">
			<logic:present name="process" property="acquisitionRequest.realTotalValue">
				<logic:equal name="process" property="acquisitionRequest.realValueLessThanTotalValue" value="false">
					<div class="infobox_warning">
						<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realValueLessThanTotalValue" bundle="ACQUISITION_RESOURCES"/>
					</div>
				</logic:equal>
			</logic:present>
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
			
			<h3>
				<bean:message key="label.items" bundle="EXPENDITURE_RESOURCES"/>
			</h3>
	
			<bean:define id="totalItems" name="size" toScope="request"/>


			<table class="tview2" style="width: 100%;">

				
			<logic:equal name="process" property="acquisitionRequest.hasDifferentPayingUnitTypology" value="true">
				<tr>
					<td colspan="6">
						<div class="infobox_warning">
							<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> 
							<bean:message key="acquisitionRequestItem.message.info.differentUnitTypology" bundle="ACQUISITION_RESOURCES"/>				
						</div>
					</td>
				</tr>
			</logic:equal>	
				
			<logic:iterate id="acquisitionRequestItem" name="itemSet" indexId="index">
				<bean:define id="currentIndex" value='<%= String.valueOf(index + 1) %>' toScope="request"/>
										
				<bean:define id="itemOID" name="acquisitionRequestItem" property="externalId" type="java.lang.String"/>
				 
				<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="false">
					<logic:equal name="acquisitionRequestItem" property="valueFullyAttributedToUnits" value="false">
					<tr>
						<td colspan="6">
							<div class="infobox_warning">
								<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
							</div>
						</td>
					</tr>
					</logic:equal>
				</logic:equal>
				
				<logic:equal  name="process" property="acquisitionProcessState.invoiceConfirmed"  value="true">		
					<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="false">
					<tr>
						<td colspan="6">
							<div class="infobox_warning">
								<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.info.valuesNotFilled" bundle="ACQUISITION_RESOURCES"/>
							</div>
						</td>
					</tr>
					</logic:equal>
				</logic:equal>
				
				
				<bean:define id="item" name="acquisitionRequestItem" toScope="request"/>
			 	<jsp:include page="/acquisitions/commons/viewAcquisitionRequestItem.jsp" />
							
			</logic:iterate>

			</table>
					
			
			<p class="aright">
				<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#"><bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/></a>
			</p>
			
			
		
		</logic:notEmpty>
		
        <h3>
            <bean:message key="label.invoices" bundle="EXPENDITURE_RESOURCES"/>
        </h3>

        <table class="table">
            <tr>
                <th rowspan="2">
                    <bean:message key="label.invoice" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th rowspan="2">
                    <bean:message key="label.invoice.date" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <logic:notEmpty name="process" property="advancePaymentDocument">
	                <th rowspan="2">
	                    <bean:message key="label.advancePayment" bundle="ACQUISITION_RESOURCES"/>
	                </th>
                </logic:notEmpty>
                <th colspan="5">
                    <bean:message key="label.items" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th rowspan="2">
                    <bean:message key="label.invoice.state" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th rowspan="2">
                </th>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.item" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th>
                    <bean:message key="label.quantity" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th>
                    <bean:message key="label.unitValue" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th>
                    <bean:message key="label.vatValue" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th>
                    <bean:message key="label.additionalCostValue" bundle="EXPENDITURE_RESOURCES"/>
                </th>
            </tr>
        <% final SimplifiedProcedureProcess process = (SimplifiedProcedureProcess) request.getAttribute("process");
           for (final ProcessFile processFile : process.getFilesSet()) {
               if (processFile instanceof AcquisitionInvoice) {
                   final AcquisitionInvoice acquisitionInvoice = (AcquisitionInvoice) processFile;
                   final int rowCount = acquisitionInvoice.getItemSet().size();
                   final int span = rowCount == 0 ? 1 : rowCount;
                   int i = 0;
                   %>
                    <tr>
                        <td rowspan="<%= span %>">
                            <html:link action='<%= "/workflowProcessManagement.do?method=downloadFile&processId=" + processId + "&fileId=" + acquisitionInvoice.getExternalId() %>'>
                                <%= acquisitionInvoice.getInvoiceNumber() %>
                            </html:link>
                        </td>
                        <td rowspan="<%= span %>">
                            <%= acquisitionInvoice.getInvoiceDate().toString("yyyy-MM-dd") %>
                        </td>
                        <logic:notEmpty name="process" property="signedAdvancePaymentDocument">
			                <td rowspan="<%= span %>">
			                	  	<bean:message key="<%=acquisitionInvoice.getAdvancePaymentInvoice().toString()%>" bundle="ENUMERATION_RESOURCES"/>
			                </td>
		                </logic:notEmpty>
                        <% for (final AcquisitionInvoiceItem acquisitionInvoiceItem : acquisitionInvoice.getItemSet()) {
                              if (i++ == 0) {
                        %>
                                        <td>
                                            <%= acquisitionInvoiceItem.getItem().getDescription() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getQuantity() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getUnitValue().toFormatString() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getVatValue() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getAdditionalCostValue() == null ? "" : acquisitionInvoiceItem.getAdditionalCostValue().toFormatString() %>
                                        </td>
                        <%     }
                           } %>
                        <td rowspan="<%= span %>">
                            <%= acquisitionInvoice.getState() == null ? "" : acquisitionInvoice.getState().getLocalizedName() %>
                        </td>
						<td rowspan="<%= span %>">
				<% if(acquisitionInvoice.getAdvancePaymentInvoice() && acquisitionInvoice.getState() == pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoiceState.PAYED){ %>
	                           <wf:activityLink id='<%= "edit-" + acquisitionInvoice.getExternalId() %>' processName="process" activityName="CancelAdvancePaymentInvoice" scope="request" paramName0="invoice" paramValue0="<%= acquisitionInvoice.getExternalId() %>">
									<bean:message key="label.revert" bundle="EXPENDITURE_RESOURCES"/>
								</wf:activityLink>
	                	  	<% }%>
           				</td>

                    </tr>
                    <%  i = 0;
                        for (final AcquisitionInvoiceItem acquisitionInvoiceItem : acquisitionInvoice.getItemSet()) {
                          if (i++ > 0) {
                    %>
                            <tr>
                                        <td>
                                            <%= acquisitionInvoiceItem.getItem().getDescription() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getQuantity() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getUnitValue().toFormatString() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getVatValue() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getAdditionalCostValue() == null ? "" : acquisitionInvoiceItem.getAdditionalCostValue().toFormatString() %>
                                        </td>
                              </tr>
                    <%     }
                       } %>

                   <%
//                    acquisitionInvoice.getCreationDate();
//                    acquisitionInvoice.getFinancersSet();
//                    acquisitionInvoice.getProjectFinancersSet();
//                    acquisitionInvoice.getRequestItemsSet();
//                    acquisitionInvoice.getUnitItemsSet();
               }
           }
        %>
        </table>
</logic:present>
