<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="user.label.view" bundle="ORGANIZATION_RESOURCES"/> <bean:write name="person" property="username"/></h2>

<logic:present role="MANAGER,ACQUISITION_CENTRAL_MANAGER">
	<div class="infoop1">
		<ul>
			<li>
				<html:link action="/organization.do?method=attributeAuthorization" paramId="personOid" paramName="person" paramProperty="OID">
					<bean:message key="authorizations.link.grant" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
			<logic:present role="MANAGER">
				<li>
					<html:link action="/organization.do?method=editPerson" paramId="personOid" paramName="person" paramProperty="OID">
						<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
				<li>
					<html:link action="/organization.do?method=deletePerson" paramId="personOid" paramName="person" paramProperty="OID">
						<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
		</ul>
	</div>
</logic:present>

<table class="mvert1 tdtop">
	<tbody>
		<tr>
			<td>
				<fr:view name="person"
						type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
						schema="viewPerson">
					<fr:layout name="tabular">
						<fr:property name="classes" value="form"/>
						<fr:property name="columnClasses" value=",,tderror"/>
					</fr:layout>
				</fr:view>
			</td>
			<td>
				<html:img src="https://fenix.ist.utl.pt/publico/viewHomepage.do?method=retrieveByUUID&amp;contentContextPath_PATH=/"
					paramId="uuid" paramName="person" paramProperty="username"
					align="middle" styleClass="float: right; border: 1px solid #aaa; padding: 3px;" />
			</td>
		</tr>
	</tbody>
</table>

<logic:present role="MANAGER">
	<logic:iterate id="role" name="availableRoles">
		<p>
			<span>
				<fr:view name="role"/>: 
				<html:link action='<%= "/organization.do?method=addRole&role=" + role %>' paramId="personOid" paramName="person" paramProperty="OID">
					<bean:message key="role.label.add" bundle="ORGANIZATION_RESOURCES"/>
				</html:link>
				|
				<html:link action='<%= "/organization.do?method=removeRole&role=" + role %>' paramId="personOid" paramName="person" paramProperty="OID">
					<bean:message key="role.label.remove" bundle="ORGANIZATION_RESOURCES"/>
				</html:link>
			</span>
		</p>
	</logic:iterate>
</logic:present>
<br/>
<h3>Unidades do qual é responsável</h3>
<bean:define id="authorizations" name="person" property="authorizations"/>
<logic:notEmpty name="authorizations">
	<fr:view name="authorizations"
			schema="viewAuthorizations">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>

			<fr:property name="link(view)" value="/organization.do?method=viewAuthorization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="OID/authorizationOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="authorizations">
	<em>Não tem autorizações atribuidas</em>
	<br/>
	<br/>
</logic:empty>

