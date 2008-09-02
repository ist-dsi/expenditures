<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="supplier.title.view" bundle="ORGANIZATION_RESOURCES"/></h2>
<br/>
<fr:view name="supplier"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
		schema="viewSupplier">
	<fr:layout name="tabular">
	</fr:layout>
</fr:view>
<br/>
<bean:define id="supplierOID" name="supplier" property="OID"/>
<html:link action='<%= "/organization.do?method=prepareEditSupplier&supplierOid=" + supplierOID%>'>
	<bean:message key="supplier.link.edit" bundle="ORGANIZATION_RESOURCES"/>
</html:link>
<html:link action='<%= "/organization.do?method=deleteSupplier&supplierOid=" + supplierOID%>'>
	<bean:message key="supplier.link.delete" bundle="ORGANIZATION_RESOURCES"/>
</html:link>
<br/>
