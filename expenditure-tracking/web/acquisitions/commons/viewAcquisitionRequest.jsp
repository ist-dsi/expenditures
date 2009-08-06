<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<bean:define id="acquisitionProcessClass" name="processRequest" property="process.class.simpleName"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + acquisitionProcessClass %>"/>

<div class="infoop2">
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
							<html:link action="<%= actionMapping + ".do?method=downloadInvoice"%>" paramId="invoiceOid" paramName="invoice" paramProperty="externalId">
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
		
	<logic:equal name="processRequest" property="process.allowedToViewSupplierExpenditures" value="true">
		<logic:notEmpty name="processRequest" property="suppliers">
			<logic:iterate id="supplier" name="processRequest" property="suppliers">
				<bean:define id="supplierName" name="supplier" property="name"/>
				<p>
					<bean:message key="supplier.message.info.totalAllocated.withArgument" bundle="EXPENDITURE_ORGANIZATION_RESOURCES" arg0="<%= supplierName.toString() %>"/>:
					<fr:view name="supplier" property="totalAllocated"/>
				</p>
			</logic:iterate>
		</logic:notEmpty>
	</logic:equal>

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




