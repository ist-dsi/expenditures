<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="authorizations.title.details" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infoop1">
	<ul>
		<li>
			<html:link action="/organization.do?method=viewPerson" paramId="personOid" paramName="authorization" paramProperty="person.OID">
				<bean:message key="person.label.view" bundle="ORGANIZATION_RESOURCES"/>
			</html:link>
		</li>
		<bean:define id="personFromAuthorizationOid" name="authorization" property="person.OID"/>
		<logic:equal name="USER_SESSION_ATTRIBUTE" property="person.OID" value="<%= personFromAuthorizationOid.toString() %>">
			<logic:equal name="authorization" property="canDelegate" value="true">
				<li>
					<html:link action="/organization.do?method=delegateAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="OID">
						<bean:message key="authorizations.link.delegate" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:equal>
		</logic:equal>
		<logic:equal name="authorization" property="currentUserAbleToRevoke" value="true">
			<li>
				<html:link action="/organization.do?method=revokeAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="OID">
					<bean:message key="authorizations.link.revoke" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<logic:present role="MANAGER">
			<li>
				<html:link action="/organization.do?method=editAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="OID">
					<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link action="/organization.do?method=deleteAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="OID">
					<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
	</ul>
</div>

<fr:view name="authorization" schema="viewAuthorization">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:view>
<br/>
<br/>

<strong><bean:message key="authorizations.label.delegationList" bundle="EXPENDITURE_RESOURCES"/></strong>:
<logic:notEmpty name="authorization" property="delegatedAuthorizations">
	<fr:view name="authorization" property="delegatedAuthorizations" schema="viewDelegatedAuthorizations">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>

			<fr:property name="link(view)" value="/organization.do?method=viewAuthorization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="OID/authorizationOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="authorization" property="delegatedAuthorizations">
	<p> 
		<em><bean:message key="authorizations.message.info.noDelegations" bundle="EXPENDITURE_RESOURCES"/></em>
	</p>
</logic:empty>
