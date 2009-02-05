<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="authorizations.title.grant" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>

<div class="infoop2">
	<fr:view name="person"
			type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
			schema="viewPerson">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<fr:edit id="bean" name="authorizationBean" schema="create.authorization.unit" action="/expenditureTrackingOrganization.do?method=createAuthorizationUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>