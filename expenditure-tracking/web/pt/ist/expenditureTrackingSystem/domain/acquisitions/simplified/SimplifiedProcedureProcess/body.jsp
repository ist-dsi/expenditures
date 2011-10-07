<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>

<bean:define id="acquisitionProcessOid"><bean:write name="process" property="externalId"/></bean:define>

<bean:define id="processRequest" name="process" property="request" toScope="request"/>
<bean:define id="processId" name="process" property="externalId"/>

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

<logic:equal name="process" property="warnForLessSuppliersActive" value="true">
	<div class="infobox_warning">
	 	<p class="mvert025">
	 		<bean:message key="label.warning.warnForLessSuppliers" bundle="ACQUISITION_RESOURCES"/>
	 	</p>
	 </div>
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
						 	<fr:slot name="name" layout="link" >
								<fr:property name="useParent" value="true" />
								<fr:property name="blankTarget" value="true" />
								<fr:property name="linkFormat"
									value="/expenditureTrackingOrganization.do?personOid=${externalId}&method=viewPerson" />
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
										<html:link action="<%= "/workflowProcessManagement.do?method=downloadFile&processId=" + processId%>" paramId="fileId" paramName="invoice" paramProperty="externalId">
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
		</logic:equal>
		<th class="aright">
			<bean:message key="acquisitionRequestItem.label.totalValueWithVAT" bundle="ACQUISITION_RESOURCES"/>
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
								for (final String s : paymentDiaryNumber) {
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
		</tr>
	</logic:iterate>
</table> 
</logic:notEmpty>

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
			
			<h3>Items</h3>
	
			<bean:define id="totalItems" name="size" toScope="request"/>


			<table class="tview2" style="width: 100%;">

				
				
				
			<logic:iterate id="acquisitionRequestItem" name="itemSet" indexId="index">
				<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>" toScope="request"/>
										
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
				<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
				<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#"><bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/></a>
				<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
			</p>
			
			
		
		</logic:notEmpty>
		
		
		
	</logic:present>
	<p>
	<a href="#" id="open"><bean:message key="label.viewMoreInfoForAllItems" bundle="ACQUISITION_RESOURCES"/></a>
	| 
	<a href="#" id="close"><bean:message key="label.viewLessInfoForAllItems" bundle="ACQUISITION_RESOURCES"/></a>
	</p>


<script type="text/javascript">
                       					function close(target) {
                                               if (!$("#" + target).hasClass("close")) {
                                               var numberOfTrs = $("#" + target + " tr.extraInfo").length;
                                               $("#" + target + " td[rowspan]").each(function() {
                                                       var rowspan = $(this).attr('rowspan');
                                                       $(this).attr('rowspan',rowspan - numberOfTrs);
                                               });
                                               $("#" + target).addClass("close");
                                               $("#" + target).removeClass("open");
                                               $("#" + target + " .extraInfo").hide();
                                               $("#" + target + " .closeItem").hide();
                                               $("#" + target + " .openItem").show();
											   $("#" + target + "-less").hide();
											   $("#" + target + "-more").show();
	 
                                               }
                                       }

										function open(target) {
                                               if (!$("#" + target).hasClass("open")) {
                                                 var numberOfTrs = $("#" + target + " tr.extraInfo").length;
                                                 $("#" + target + " td[rowspan]").each(function() {
                                                       var rowspan = $(this).attr('rowspan');
                                                       $(this).attr('rowspan',rowspan + numberOfTrs);
                                                  });
 											   $("#" + target).addClass("open");
                                               $("#" + target).removeClass("close");
                                               $("#" + target + " .extraInfo").show();
                                               $("#" + target + " .closeItem").show();
                                               $("#" + target + " .openItem").hide();
											   $("#" + target + "-more").hide();
											   $("#" + target + "-less").show();
											}
										}

                                 	      function openAll() {
	                                      	var itemSize = <%= size %>;
											for (i = 1; i <= itemSize; i++) {
												open("item" + i);
											}
									  	   }
	                                       function closeAll() {
	                                        var itemSize = <%= size %>;
											for ( i = 1; i <= itemSize; i++) {
												close("item" + i);
											}
										   }
	                                       $("#open").click(function() { openAll(); });
	                                       $("#close").click(function() { closeAll(); });

	                                       closeAll();
</script>
