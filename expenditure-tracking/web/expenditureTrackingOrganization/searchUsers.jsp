<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="search.label.users" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="mbottom15">
	<fr:edit id="searchUsers"
			name="searchUsers"
			type="pt.ist.expenditureTrackingSystem.domain.organization.SearchUsers"
			schema="searchUsers">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
</div>

<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
	<p class="mtop15">
		<html:link action="/expenditureTrackingOrganization.do?method=prepareCreatePerson">
			<bean:message key="person.link.create" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</html:link>
	</p>
</logic:present>

<bean:define id="people" name="searchUsers" property="result"/>
<fr:view name="people"
		schema="viewPeopleInList">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value="aleft,,,,aright,"/>
		<fr:property name="sortBy" value="name=asc"/>
		<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
		<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="param(view)" value="OID/personOid"/>
		<fr:property name="order(view)" value="1"/>
	</fr:layout>
</fr:view>
