<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.invoice.removeInvoices" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="acquisitionProcessClass" name="process" property="class.simpleName" toScope="request"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + acquisitionProcessClass%>"/>
<bean:define id="processOid" name="process" property="externalId" type="java.lang.String"/>

<bean:define id="processRequest" name="process" property="request" toScope="request"/>
<jsp:include page="commons/viewAcquisitionRequest.jsp" flush="true"/>

<fr:view name="process" property="request.invoices" schema="listInvoices">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5"/>
		<fr:property name="link(delete)" value="<%= actionMapping + ".do?method=removeInvoice&acquisitionProcessOid=" + processOid %>"/>
		<fr:property name="bundle(delete)" value="ACQUISITION_RESOURCES"/>
		<fr:property name="key(delete)" value="link.deleteInvoice"/>
		<fr:property name="param(delete)" value="externalId/invoiceOid"/>
		<fr:property name="order(delete)" value="1"/>
		<fr:property name="confirmationBundle(delete)" value="ACQUISITION_RESOURCES"/> 
        <fr:property name="confirmationKey(delete)" value="label.removeInvoice"/> 	
        <fr:property name="confirmationTitleKey(delete)" value="title.removeInvoice"/> 	
		
	</fr:layout>
</fr:view>

