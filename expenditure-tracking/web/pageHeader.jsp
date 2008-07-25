<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="pt.ist.fenixWebFramework.security.UserView"%>
<%@page import="pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User"%>
<%
	final String contextPath = request.getContextPath();
	final User user = UserView.getUser();
%>
<% if (user == null) { %>
	<form action="<%= contextPath %>/authenticationAction.do" class="login">
		<input type="hidden" name="method" value="login"/>
		<bean:message key="label.username" bundle="EXPENDITURE_RESOURCES"/> <input type="text" name="username" size="10"/>
		<bean:message key="label.password" bundle="EXPENDITURE_RESOURCES"/> <input type="password" name="password" size="10"/>
		<bean:define id="loginLabel"><bean:message key="label.login" bundle="EXPENDITURE_RESOURCES"/></bean:define>
		<input class=" button" type="submit" name="Submit" value="<%= loginLabel %>"/>
	</form>
<% } else { %>
	<form class="login">
		<bean:message key="label.logged.is.as" bundle="EXPENDITURE_RESOURCES"/> <%= user.getUsername() %>
		<html:link action="/authenticationAction.do?method=logout"><bean:message key="label.logout" bundle="EXPENDITURE_RESOURCES"/></html:link>
	</form>
<% } %>
<h1><bean:message key="label.application.name" bundle="EXPENDITURE_RESOURCES"/></h1>
<p><bean:message key="label.application.description" bundle="EXPENDITURE_RESOURCES"/></p>
