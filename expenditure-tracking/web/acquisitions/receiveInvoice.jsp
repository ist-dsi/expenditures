<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.invoice.receive" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="acquisitionProcess" name="acquisitionProcess" toScope="request"/>
<bean:define id="acquisitionProcessClass" name="acquisitionProcess" property="class.simpleName" toScope="request"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + acquisitionProcessClass%>"/>

<bean:define id="processRequest" name="acquisitionProcess" property="request" toScope="request"/>
<jsp:include page="commons/viewAcquisitionRequest.jsp" flush="true"/>

<p>
	<bean:message key="acquisitionProcess.label.proposalDocument" bundle="ACQUISITION_RESOURCES"/>: 
	<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
		<html:link action="<%= actionMapping + ".do?method=downloadAcquisitionProposalDocument"%>" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
			<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
		</html:link>	
	</logic:present>
	<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
		-
	</logic:notPresent>
</p>

<bean:define id="urlView"><%= actionMapping %>.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<bean:define id="urlSave"><%= actionMapping %>.do?method=<%= request.getAttribute("invoiceActivity").toString() %>&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<fr:edit id="receiveInvoiceForm"
		name="receiveInvoiceForm"
		schema="receiveInvoiceForm"
		action="<%= urlSave %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form mtop05"/>
		<fr:property name="columnClasses" value="width215px,,tderror"/>
		<fr:property name="rowClasses" value=",,,,itemsClass"/>
	</fr:layout>
		<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
 
<script type="text/javascript">
var checkBox = $("input[id$='hasMoreInvoices']");
var itemList = $(".itemsClass");

if (checkBox.attr('checked') == false) {
	itemList.hide();
}

checkBox.click(function(){	
	itemList.toggle();
});

</script>

<div class="item">
	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet" indexId="index">
		<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
		<p><strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)</p>
		<bean:define id="item" name="acquisitionRequestItem" toScope="request"/>
		<jsp:include page="commons/viewAcquisitionRequestItem.jsp" flush="false"/>
	</logic:iterate>
</div>

