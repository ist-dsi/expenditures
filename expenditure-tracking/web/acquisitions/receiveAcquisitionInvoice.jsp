<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.invoice.receive" bundle="ACQUISITION_RESOURCES"/></h2>

<div class="infoop2">
	<fr:view name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact"
			schema="viewAcquisitionAfterTheFact">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<div class="documents">
	<p>
		<bean:message key="acquisitionProcess.label.invoice" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice">
			<logic:present name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice.content">
				<html:link action="/acquisitionSimplifiedProcedureProcess.do?method=downloadInvoice" paramId="invoiceOid" paramName="afterTheFactAcquisitionProcess" paramProperty="acquisitionAfterTheFact.invoice.OID">
					<bean:write name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice.filename"/>
				</html:link>
			</logic:present>	
			<logic:notPresent name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice">
				<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
</div>


<bean:define id="urlView">/acquisitionAfterTheFactAcquisitionProcess.do?method=viewAfterTheFactAcquisitionProcess&amp;acquisitionAfterTheFactOid=<bean:write name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.OID"/></bean:define>
<bean:define id="urlSave">/acquisitionAfterTheFactAcquisitionProcess.do?method=receiveAcquisitionInvoice&amp;afterTheFactAcquisitionProcessOid=<bean:write name="afterTheFactAcquisitionProcess" property="OID"/></bean:define>


<fr:form action="<%= urlSave %>" encoding="multipart/form-data">
	<fr:edit id="receiveInvoiceForm"
			name="receiveInvoiceForm"
			schema="receiveInvoiceForm.afterTheFact">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form mtop05"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>
