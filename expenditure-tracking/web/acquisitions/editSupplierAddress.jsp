<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionRequestDocument.title.editSupplierAddress" bundle="ACQUISITION_RESOURCES"/></h2>

<p class="mbottom05"><strong><bean:message key="label.supplier" bundle="EXPENDITURE_RESOURCES"/></strong></p>

<bean:define id="acquisitionProcessOID" name="acquisitionProcess" property="OID"/>
<bean:define id="url" value='<%= "/acquisitionProcess.do?method=executeCreateAcquisitionRequest&amp;acquisitionProcessOid=" + acquisitionProcessOID %>'/>

<fr:edit action="<%= url %>" name="acquisitionProcess" property="acquisitionRequest.supplier"
		schema="editSupplierAddress">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= url %>"/>
</fr:edit>

