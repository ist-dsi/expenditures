<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="supplier.title.edit" bundle="ORGANIZATION_RESOURCES"/></h2>

<p class="mbottom05"><strong><bean:message key="label.supplier" bundle="EXPENDITURE_RESOURCES"/></strong></p>

<bean:define id="acquisitionProcessOID" name="acquisitionProcess" property="OID"/>
<bean:define id="acquisitionProcessClass" name="acquisitionProcess" property="class.simpleName"/>

<bean:define id="url" value='<%= "/acquisition" + acquisitionProcessClass + ".do?method=executeCreateAcquisitionPurchaseOrderDocument&amp;acquisitionProcessOid=" + acquisitionProcessOID %>'/>

<fr:edit action="<%= url %>" name="acquisitionProcess" property="acquisitionRequest.supplier"
		schema="editSupplier">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= url %>"/>
</fr:edit>

