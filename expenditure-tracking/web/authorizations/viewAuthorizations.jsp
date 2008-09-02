<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2>
	<bean:message key="authorizations.title" bundle="EXPENDITURE_RESOURCES"/>
</h2>

<logic:present name="person">
				<fr:view name="person" property="authorizations" schema="viewAuthorization">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2"/>
						<fr:property name="linkFormat(delegate)" value="/authorizations.do?method=delegateAuthorization&authorizationOID=${OID}"/>
						<fr:property name="visibleIf(delegate)" value="canDelegate"/>
						<fr:property name="bundle(delegate)" value="EXPENDITURE_RESOURCES"/>
						<fr:property name="key(delegate)" value="authorizations.link.delegate"/>

						<fr:property name="linkFormat(details)" value="/authorizations.do?method=viewAuthorizationDetails&authorizationOID=${OID}"/>
						<fr:property name="bundle(details)" value="EXPENDITURE_RESOURCES"/>
						<fr:property name="key(details)" value="authorizations.link.details"/>
					</fr:layout>
				</fr:view>	
</logic:present>