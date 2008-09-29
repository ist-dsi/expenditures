<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="supplier.title.create" bundle="ORGANIZATION_RESOURCES"/></h2>

<bean:define id="personOID" name="USER_SESSION_ATTRIBUTE" property="person.OID"/>

<fr:edit action="/organization.do?method=createSupplier" name="bean" id="createBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreateSupplierBean"
		schema="createSupplier">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/organization.do?method=viewPerson&personOid=" + personOID %>"/>
</fr:edit>
<br/>
