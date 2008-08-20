<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>


<fr:view name="authorization" property="delegatedAuthorizations" schema="viewAuthorization">
		<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2"/>
					<fr:property name="linkFormat(revoke)" value="/authorizations.do?method=revokeAuthorization&revokeAuthorizationOID=${OID}&authorizationOID=${authorization.OID}" />
					<fr:property name="visibleIf(revoke)" value="currentUserAbleToRevoke"/>
					<fr:property name="bundle(revoke)" value="EXPENDITURE_RESOURCES"/>
					<fr:property name="key(revoke)" value="label.revoke.authorization"/>
		</fr:layout>
</fr:view>

<fr:view name="authorization" schema="viewAuthorization">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:view>	
