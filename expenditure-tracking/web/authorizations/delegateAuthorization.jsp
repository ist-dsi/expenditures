<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2>
	<bean:message key="label.delegate.authorization" bundle="EXPENDITURE_RESOURCES"/>
</h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<logic:present name="bean">
	<fr:edit id="bean" name="bean" schema="delegateAuthorization" action="/authorizations.do?method=createDelegation">
		<fr:layout>
			<fr:property name="classes" value="tstyle3"/>
		</fr:layout>
		<fr:destination name="cancel" path="/authorizations.do?method=viewAuthorizations"/> 
	</fr:edit>
</logic:present>