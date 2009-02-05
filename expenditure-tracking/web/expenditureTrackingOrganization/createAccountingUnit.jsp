<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="unit.link.create.accounting.unit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>

<fr:edit id="accountingUnitBean"
		name="accountingUnitBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.AccountingUnitBean"
		schema="createAccountingUnit"
		action="/expenditureTrackingOrganization.do?method=createNewAccountingUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="/expenditureTrackingOrganization.do?method=viewOrganization" />
</fr:edit>
