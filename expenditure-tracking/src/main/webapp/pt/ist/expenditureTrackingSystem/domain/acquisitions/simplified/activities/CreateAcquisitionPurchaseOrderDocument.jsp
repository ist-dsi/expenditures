<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>
<bean:define id="supplierContactSet" name="process" property="request.supplier.supplierContactSet"/>

<%java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.PurchaseOrderAdditionalInformationTemplate> templates = pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem.getInstance().getPurchaseOrderAdditionalInformationTemplateSet();
request.setAttribute("additionalInformationTemplates", templates);
%>

<logic:notEmpty name="additionalInformationTemplates">
	<div class="form-horizontal">
		<label class="control-label col-sm-2">
		Informações adicionais pré-definidas</label>
		<div class="col-sm-10">
			<table class="tview1">
				<logic:iterate id="template" name="additionalInformationTemplates" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.PurchaseOrderAdditionalInformationTemplate">
					<tr>
					<td><input type="radio" name="radio" value="<%=template.getExternalId()%>"></td>
					<td id="<%=template.getExternalId()%>" class="aleft"><bean:write name="template" property="additionalInformation.content"/></td>
					</tr>
				</logic:iterate>
			</table>
		</div>
	</div>
</logic:notEmpty>

<div class="forminline mbottom2">
	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
			<fr:edit id="activityBean" name="information">
				<fr:schema type="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionPurchaseOrderDocumentInformation" bundle="ACQUISITION_RESOURCES">
			    	<fr:slot name="purchaseOrderAdditionalInformation" layout="longText" key="label.purchaseOrder.additionalInformation">
			    		<fr:property name="rows" value="5"></fr:property>
			    	</fr:slot>
			    	<fr:slot name="supplierContact" layout="radio-select" key="label.select.address" required="true">
			    		 <fr:property name="from" value="process.request.supplier.supplierContactSet"/>
			    		 <fr:property name="classes" value="valigntop tview1"/>
			    		 <fr:property name="eachClasses" value="mbottom05 mtop05"/>
			    		 <fr:property name="format" value="\${address.line1} <br/> \${address.line2} <br/> \${address.postalCode} -  \${address.location} <br/>\${address.country}"/>
			    	</fr:slot>
				</fr:schema>
			</fr:edit>
		<html:submit styleClass="btn btn-primary"><bean:message key="button.create" bundle="EXPENDITURE_RESOURCES"/></html:submit>
	</fr:form>
	<fr:form action='<%="/workflowProcessManagement.do?method=viewProcess&processId=" + processId%>'>
		<html:submit styleClass="btn btn-default">
			<bean:message key="renderers.form.cancel.name"
				bundle="RENDERER_RESOURCES" />
		</html:submit>
	</fr:form>
</div>

<script type="text/javascript">

	$("input[type=radio]").click(function() {
			$("textarea").val($("td#"+$(this).filter(":checked").val()).html());
	});
	$("textarea").click(function() {
			$("input[name=radio]").prop('checked', false);
	});
</script>

