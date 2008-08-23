<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.search.users" bundle="ORGANIZATION_RESOURCES"/></h2>
<br/>
<html:link action="/organization.do?method=prepareCreatePerson">
	<bean:message key="link.create.person" bundle="ORGANIZATION_RESOURCES"/>
</html:link>
<br/>
<br/>
<fr:edit id="searchUsers"
		name="searchUsers"
		type="pt.ist.expenditureTrackingSystem.domain.organization.SearchUsers"
		schema="searchUsers">
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
<bean:define id="people" name="searchUsers" property="result"/>
<fr:view name="people"
		schema="viewPeopleInList">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>

		<fr:property name="link(view)" value="/organization.do?method=viewPerson"/>
		<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="param(view)" value="OID/personOid"/>
		<fr:property name="order(view)" value="1"/>
	</fr:layout>
</fr:view>
