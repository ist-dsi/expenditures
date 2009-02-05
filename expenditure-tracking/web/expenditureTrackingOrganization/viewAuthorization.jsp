<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="authorizations.title.details" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infoop1">
	<ul>
		<li>
			<html:link action="/expenditureTrackingOrganization.do?method=viewPerson" paramId="personOid" paramName="authorization" paramProperty="person.OID">
				<bean:message key="person.label.view" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</li>
		<bean:define id="personFromAuthorizationOid" name="authorization" property="person.OID"/>
		<logic:equal name="USER_SESSION_ATTRIBUTE" property="person.OID" value="<%= personFromAuthorizationOid.toString() %>">
			<logic:equal name="authorization" property="canDelegate" value="true">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=chooseDelegationUnit" paramId="authorizationOid" paramName="authorization" paramProperty="OID">
						<bean:message key="authorizations.link.delegate" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:equal>
		</logic:equal>
		<logic:equal name="authorization" property="currentUserAbleToRevoke" value="true">
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=revokeAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="OID">
					<bean:message key="authorizations.link.revoke" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=editAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="OID">
					<bean:message key="authorizations.link.edit" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=deleteAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="OID">
					<bean:message key="authorizations.link.remove" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
	</ul>
</div>

<div class="infoop2">
	<fr:view name="authorization" schema="viewAuthorization">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:view>
</div>

<p class="mtop15 mbottom05"><strong><bean:message key="authorizations.label.delegationList" bundle="EXPENDITURE_RESOURCES"/></strong></p>
<logic:notEmpty name="authorization" property="delegatedAuthorizations">
	<fr:view name="authorization" property="delegatedAuthorizations" schema="viewDelegatedAuthorizations">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAuthorization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="OID/authorizationOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="authorization" property="delegatedAuthorizations">
	<p class="mvert05"> 
		<em><bean:message key="authorizations.message.info.noDelegations" bundle="EXPENDITURE_RESOURCES"/>.</em>
	</p>
</logic:empty>
