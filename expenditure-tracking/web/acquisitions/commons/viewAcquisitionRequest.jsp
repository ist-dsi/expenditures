<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<bean:define id="schemaForRequest" value="viewAcquisitionRequest.extra" toScope="request"/>

<logic:equal name="processRequest" property="invoiceReceived" value="true"> 
	<bean:define id="schemaForRequest" value="viewAcquisitionRequest.extraWithRealValues" toScope="request"/>	
</logic:equal>
	
<div class="infoop2">
	<fr:view name="processRequest"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
			schema="viewAcquisitionRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
	
	
	<div id="extraInfo" style="display: none;">
	
	<fr:view name="processRequest"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
			schema="<%= schemaForRequest %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
	
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
	
	<script type="text/javascript">
			$("#show1").click(
					function() {
						$('#extraInfo').slideToggle();
					}
				);
</script>
</div>




