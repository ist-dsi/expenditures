<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.manage.suppliers" bundle="ORGANIZATION_RESOURCES"/></h2>
<br/>
<html:link action="/organization.do?method=prepareCreateSupplier">
	<bean:message key="link.create.supplier" bundle="ORGANIZATION_RESOURCES"/>
</html:link>
<br/>
<br/>
<fr:edit id="searchSuppliers" action="/organization.do?method=manageSuppliers"
		name="searchSuppliers"
		type="pt.ist.expenditureTrackingSystem.domain.organization.SearchSuppliers"
		schema="searchSuppliers">
	<fr:layout name="tabular">
	</fr:layout>
</fr:edit>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<fr:view name="searchSuppliers" property="result"
		schema="viewSuppliersInList">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>

		<fr:property name="link(view)" value="/organization.do?method=viewSupplier"/>
		<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="param(view)" value="OID/supplierOid"/>
		<fr:property name="order(view)" value="1"/>
	</fr:layout>
</fr:view>
