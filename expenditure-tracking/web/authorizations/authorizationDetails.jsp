<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>


<logic:equal name="authorization" property="class.simpleName" value="DelegatedAuthorization">
	<bean:define id="oid" name="authorization" property="authorization.OID" toScope="request"/>
	<bean:define id="backUrl" value="<%= "/authorizations.do?method=viewAuthorizationDetails&authorizationOID=" + oid %>" toScope="request"/>
</logic:equal>

<logic:equal name="authorization" property="class.simpleName" value="Authorization">
	<bean:define id="backUrl" value="/authorizations.do?method=viewAuthorizations" toScope="request"/>
</logic:equal>

<bean:define id="url" name="backUrl"/>

<h2>
	<bean:message key="label.authorizationDetails" bundle="EXPENDITURE_RESOURCES"/>
</h2>

	<ul>
		<li>
			<html:link page="<%= url.toString() %>">
				<bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
	</ul>


<fr:view name="authorization" schema="viewAuthorization">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:view>	

<logic:notEmpty name="authorization" property="delegatedAuthorizations">
	<strong><bean:message key="label.delegationList" bundle="EXPENDITURE_RESOURCES"/></strong>:
	
	<fr:view name="authorization" property="delegatedAuthorizations" schema="viewAuthorization">
			<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2"/>
						<fr:property name="linkFormat(revoke)" value="/authorizations.do?method=revokeAuthorization&revokeAuthorizationOID=${OID}&authorizationOID=${authorization.OID}" />
						<fr:property name="visibleIf(revoke)" value="currentUserAbleToRevoke"/>
						<fr:property name="bundle(revoke)" value="EXPENDITURE_RESOURCES"/>
						<fr:property name="key(revoke)" value="label.revoke.authorization"/>
						<fr:property name="linkFormat(details)" value="/authorizations.do?method=viewAuthorizationDetails&authorizationOID=${OID}"/>
						<fr:property name="bundle(details)" value="EXPENDITURE_RESOURCES"/>
						<fr:property name="key(details)" value="label.details.authorization"/>
			</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="authorization" property="delegatedAuthorizations"> 
	<em><bean:message key="label.noDelegations" bundle="EXPENDITURE_RESOURCES"/></em>
</logic:empty>