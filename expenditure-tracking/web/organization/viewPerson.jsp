<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="person.label.view" bundle="ORGANIZATION_RESOURCES"/></h2>
<br />
<fr:view name="person"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
		schema="viewPerson">
	<fr:layout name="tabular">
	</fr:layout>
</fr:view>
<br/>

<logic:iterate id="role" name="availableRoles"> 
	<p>
		<span>
			<fr:view name="role"/>: 
			<html:link action="<%= "/organization.do?method=addRole&role=" + role %>" paramId="personOid" paramName="person" paramProperty="OID">
				<bean:message key="role.label.add" bundle="ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="<%= "/organization.do?method=removeRole&role=" + role %>" paramId="personOid" paramName="person" paramProperty="OID">
				<bean:message key="role.label.remove" bundle="ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
</logic:iterate>



<html:link action="/organization.do?method=editPerson" paramId="personOid" paramName="person" paramProperty="OID">
	<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
</html:link>
<html:link action="/organization.do?method=deletePerson" paramId="personOid" paramName="person" paramProperty="OID">
	<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
</html:link>
<br/>
<br/>
<html:link action="/organization.do?method=attributeAuthorization" paramId="personOid" paramName="person" paramProperty="OID">
	<bean:message key="authorization.link.grant" bundle="ORGANIZATION_RESOURCES"/>
</html:link>
<br/>
<br/>
<bean:define id="authorizations" name="person" property="authorizations"/>
<fr:view name="authorizations"
		schema="viewAuthorizations">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>

		<fr:property name="link(delete)" value="/organization.do?method=deleteAuthorization"/>
		<fr:property name="bundle(delete)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(delete)" value="link.delete"/>
		<fr:property name="param(delete)" value="OID/authorizationOid"/>
		<fr:property name="order(delete)" value="1"/>
	</fr:layout>
</fr:view>
