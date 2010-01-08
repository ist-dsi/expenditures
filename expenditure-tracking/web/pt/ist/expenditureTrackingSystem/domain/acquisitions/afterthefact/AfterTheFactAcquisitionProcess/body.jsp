<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<div class="infobox">
	<fr:view name="process" property="acquisitionAfterTheFact.supplier"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact"
			schema="viewSupplierShort">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<div class="infobox">
	<fr:view name="process" property="acquisitionAfterTheFact"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact"
			schema="viewAcquisitionAfterTheFact">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
	<logic:present name="process" property="invoice">
		<p><bean:message key="acquisitionProcess.label.invoice.date" bundle="ACQUISITION_RESOURCES"/>: <fr:view  name="process" property="invoice.invoiceDate"/></p>
		<p><bean:message key="acquisitionProcess.label.invoice.number" bundle="ACQUISITION_RESOURCES"/>: <fr:view  name="process" property="invoice.invoiceNumber"/></p>
		
	</logic:present>	
</div>

<div class="documents">
	<p>
		<bean:message key="acquisitionProcess.label.invoice" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="process" property="invoice">
			<logic:present name="process" property="invoice.content">
				<bean:define id="processId" name="process" property="externalId"/>
				<html:link action="<%= "/workflowProcessManagement.do?method=downloadFile&processId=" + processId %>" paramId="fileId" paramName="process" paramProperty="invoice.externalId">
					<bean:write name="process" property="invoice.filename"/>
				</html:link>
			</logic:present>	
			<logic:notPresent name="process" property="invoice">
				<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="process" property="invoice">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
</div>
