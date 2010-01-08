<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2>
	<bean:message key="authorizations.title.delegate" bundle="EXPENDITURE_RESOURCES"/>
</h2>

<bean:define id="urlCreate">/expenditureTrackingOrganization.do?method=createDelegation&amp;authorizationOid=<bean:write name="authorization" property="externalId"/></bean:define>
<bean:define id="urlCancel">/expenditureTrackingOrganization.do?method=viewAuthorization&amp;authorizationOid=<bean:write name="authorization" property="externalId"/></bean:define>
<logic:present name="bean">
	<fr:edit id="bean" name="bean" schema="delegateAuthorization" action="<%= urlCreate %>">
		<fr:layout>
			<fr:property name="classes" value="tstyle2"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= urlCancel %>"/> 
	</fr:edit>
</logic:present>