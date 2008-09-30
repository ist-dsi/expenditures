<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="pt.ist.fenixWebFramework.FenixWebFramework"%>

<%
	final String contextPath = request.getContextPath();
	final boolean isCasEnabled = FenixWebFramework.getConfig().isCasEnabled();
%>
<logic:notPresent name="USER_SESSION_ATTRIBUTE">
	<% if (isCasEnabled) {%>
		<div class="login">
			<% final String portString = request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort(); %>
			<bean:define id="loginUrl"><%= FenixWebFramework.getConfig().getCasLoginUrl() + request.getScheme() + "://" + request.getServerName() + portString + contextPath %>/</bean:define>
			<html:link href="<%= loginUrl %>"><bean:message key="login.label.login" bundle="EXPENDITURE_RESOURCES"/></html:link>
		</div>
	<% } else { %>
		<form action="<%= contextPath %>/authenticationAction.do" class="login" method="post">
			<input type="hidden" name="method" value="login"/>
			<bean:message key="login.label.username" bundle="EXPENDITURE_RESOURCES"/>: <input type="text" name="username" size="10"/>
			<bean:message key="login.label.password" bundle="EXPENDITURE_RESOURCES"/>: <input type="password" name="password" size="10"/>
			<bean:define id="loginLabel"><bean:message key="login.label.performLogin" bundle="EXPENDITURE_RESOURCES"/></bean:define>
			<input class="inputbuttonlogin" type="submit" name="Submit" value="<%= loginLabel %>"/>
		</form>
	<% } %>
</logic:notPresent>
<logic:present name="USER_SESSION_ATTRIBUTE">
	<div class="login">
		<html:link action="/customize.do?method=showNotificationOptions"><bean:message key="link.preferences" bundle="EXPENDITURE_RESOURCES"/></html:link> |
		<a href="https://193.136.132.88/Qualidade/Aquisicoes" target="_blank"><bean:message key="link.help" bundle="EXPENDITURE_RESOURCES"/></a> |  
		<bean:message key="login.label.loggedInAs" bundle="EXPENDITURE_RESOURCES"/>: <bean:write name="USER_SESSION_ATTRIBUTE" property="username"/> |
		<% if (isCasEnabled) {%>
			<html:link href="<%= FenixWebFramework.getConfig().getCasLogoutUrl() %>"><bean:message key="login.label.logout" bundle="EXPENDITURE_RESOURCES"/></html:link>
		<% } else { %>
			<html:link action="/authenticationAction.do?method=logout"><bean:message key="login.label.logout" bundle="EXPENDITURE_RESOURCES"/></html:link>
		<% } %>
	</div>
</logic:present>

<img src='<%= contextPath + "/images/logo03.gif" %>'/>


