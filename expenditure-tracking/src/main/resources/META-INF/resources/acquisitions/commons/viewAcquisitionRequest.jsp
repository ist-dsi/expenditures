<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="acquisitionProcessClass" name="processRequest" property="process.class.simpleName"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + acquisitionProcessClass %>"/>
<bean:define id="processId" name="processRequest" property="process.externalId"/>

<div class="infobox">

	<fr:view name="processRequest"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
			schema="viewAcquisitionRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value="width165px,,"/>
		</fr:layout>
	</fr:view>
	
	
	<div id="extraInfo" style="display: none;">
	
	<fr:view name="processRequest"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
			schema="viewAcquisitionRequest.extra">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value="width165px,,"/>
		</fr:layout>
	</fr:view>


	<logic:empty name="processRequest" property="invoices">
	<table class="tstyle1">
		<tr>
			<th style="width: 160px;"><bean:message key="label.invoices" bundle="EXPENDITURE_RESOURCES"/>:</th>
			<td> - </td>
		</tr>
	</table>
	</logic:empty>
	<logic:notEmpty name="processRequest" property="invoices">
		<table class="tstyle1">
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
		</table>
	</logic:notEmpty>
		
		<logic:notEmpty name="processRequest" property="suppliers">
			<bean:message key="supplier.title.manage" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			
			
			<logic:iterate id="supplier" name="processRequest" property="suppliers">
				<bean:define id="supplierName" name="supplier" property="name"/>
				<p>
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
				</p>
			</logic:iterate>
		</logic:notEmpty>

	</div>
	
	<p class="mvert05"><span id="show1" class="link"><bean:message key="label.moreInfo" bundle="EXPENDITURE_RESOURCES"/></span></p>
	<p class="mvert05"><span id="show2" style="display: none" class="link"><bean:message key="label.lessInfo" bundle="EXPENDITURE_RESOURCES"/></span></p>
	
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

<logic:present name="processRequest" property="acquisitionProcess.missionProcess">
	<div class="infobox mtop15">
 		<p class="mvert025">
 			<bean:message key="label.acquisition.process.consult.mission.process" bundle="ACQUISITION_RESOURCES"/>:
			<html:link target="blank" action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="processRequest" paramProperty="acquisitionProcess.missionProcess.externalId">
	 			<bean:write name="processRequest" property="acquisitionProcess.missionProcess.processIdentification"/>
			</html:link>
    	</p>
	</div>
</logic:present>
