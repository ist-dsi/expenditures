<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="supplier.title.create" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>

<fr:edit action="/expenditureTrackingOrganization.do?method=createSupplier" name="bean" id="createBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreateSupplierBean"
		schema="createSupplier">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="/expenditureTrackingOrganization.do?method=manageSuppliers"/>
</fr:edit>

