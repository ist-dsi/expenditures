<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="process" name="item" property="request.process"/>
<bean:define id="itemOid" name="item" property="OID"/>
<bean:define id="processOid" name="process" property="OID"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<bean:define id="actionMapping" value='<%= "/acquisition" + processClass%>'/>

<h2>
	<bean:message key="title.editInvoices" bundle="ACQUISITION_RESOURCES"/>
</h2>

<div class="mbottom15">
<jsp:include page="../commons/viewRefundItem.jsp"/>
</div>

<jsp:include page="../../commons/defaultErrorDisplay.jsp"/>

<div class="dinline forminline">
<fr:form action='<%= actionMapping + ".do?method=editRefundInvoice&refundProcessOid=" + processOid + "&itemOid=" + itemOid%>'>
	<fr:edit id="invoiceBeans" name="invoices" visible="false"/>
	
	<table class="tstyle5 mbottom1">
		<tr>
			<th><bean:message key="acquisitionProcess.label.invoice.number" bundle="ACQUISITION_RESOURCES"/></th>		
			<th><bean:message key="acquisitionProcess.label.invoice.date" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="acquisitionProcess.label.invoice.file" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="label.value" bundle="EXPENDITURE_RESOURCES"/></th>
			<th><bean:message key="label.vatValue" bundle="EXPENDITURE_RESOURCES"/></th>
			<th><bean:message key="label.refundValue" bundle="EXPENDITURE_RESOURCES"/></th>
		</tr>	
	<logic:iterate id="invoice" name="invoices" indexId="index">
		<tr>
		<td>
			<fr:view name="invoice" property="invoiceNumber"/>
		</td>
		<td>
			<fr:view name="invoice" property="invoiceDate" type="org.joda.time.LocalDate"/>
		</td>
		<td>
			<html:link action='<%= actionMapping + ".do?method=downloadInvoice" %>' paramId="invoiceOID" paramName="invoice" paramProperty="invoice.OID">
				<bean:write name="invoice" property="file.filename"/>
			</html:link>
		</td>
		<td>
			<fr:edit id="<%= "value-" + index %>" name="invoice" slot="value" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:layout>
						<fr:property name="size" value="10"/>
					</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="<%= "vatValue-" + index %>" name="invoice" slot="vatValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:layout>
						<fr:property name="size" value="10"/>
					</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="<%= "refundableValue-" + index %>" name="invoice" slot="refundableValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:layout>
						<fr:property name="size" value="10"/>
					</fr:layout>
			</fr:edit>
		</td>
		</tr>
	</logic:iterate>
	</table> 
	
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	
	</fr:form> 
<fr:form action='<%= actionMapping + ".do?method=viewProcess&processOid=" + processOid %>'>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:submit>
</fr:form>
</div>