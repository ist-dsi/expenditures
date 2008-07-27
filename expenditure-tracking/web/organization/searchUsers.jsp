<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.search.users" bundle="ORGANIZATION_RESOURCES"/></h2>
<br/>
<html:link action="/organization.do?method=createPerson">
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

		<fr:property name="link(edit)" value="/organization.do?method=editPerson"/>
		<fr:property name="bundle(edit)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(edit)" value="link.edit"/>
		<fr:property name="param(edit)" value="OID/personOid"/>
		<fr:property name="order(edit)" value="1"/>

		<fr:property name="link(delete)" value="/organization.do?method=deletePerson"/>
		<fr:property name="bundle(delete)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(delete)" value="link.delete"/>
		<fr:property name="param(delete)" value="OID/personOid"/>
		<fr:property name="order(delete)" value="1"/>
	</fr:layout>
</fr:view>
