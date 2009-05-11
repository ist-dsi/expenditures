<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.createAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<div class="infoop2">
	<bean:message key="acquisitionProcess.message.note" bundle="ACQUISITION_RESOURCES"/>
</div>

<p class="mtop15 mbottom05"><strong><bean:message key="link.create.simplifiedAcquisitionProcedure" bundle="EXPENDITURE_RESOURCES"/></strong></p>

<fr:form action="/acquisitionSimplifiedProcedureProcess.do?method=createNewAcquisitionProcess">
	<fr:edit id="acquisitionProcessBean"
			name="acquisitionProcessBean"
			type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean"
			schema="createAcquisitionRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/></html:submit>
</fr:form>

<script src="<%= request.getContextPath() %>/javaScript/jquery.alerts.js" type="text/javascript"></script>
<script type="text/javascript">
	var inputs = $("input:hidden");
	for (i = 0; i < inputs.length ; i++) {
		if(inputs[i].id.indexOf("supplier_AutoComplete") > 0) {
			$("#" + escapeId(inputs[i].id)).change(function() {
				<%= "$.get(\"" + request.getContextPath() + "/acquisitionSimplifiedProcedureProcess.do?method=checkSupplierLimit&supplierOid=\" + $(this).attr('value'),function(data, textStatus) {dealWith(data)})" %>
			}); 
		}
	}

	function dealWith(data) {
		if(data != 'SOK') {
			<bean:define id="message">
				<bean:message key="label.supplier.aboveSoftLimit" bundle="EXPENDITURE_RESOURCES"/>
			</bean:define>

			<bean:define id="title">
				<bean:message key="title.supplier.aboveSoftLimit" bundle="EXPENDITURE_RESOURCES"/>
			</bean:define>
		
			<%= "jAlert('" + message.toString() + "','" + title.toString() + "')" %>			
		} 
	}
</script>

	