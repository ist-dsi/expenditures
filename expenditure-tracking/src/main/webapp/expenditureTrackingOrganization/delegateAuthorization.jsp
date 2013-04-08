<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

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
