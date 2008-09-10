<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<title><tiles:getAsString name="title" ignore="true" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%
	final String contextPath = request.getContextPath();
%>
<logic:notPresent name="USER_SESSION_ATTRIBUTE">
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/layout.css" media="screen">
</logic:notPresent>
<logic:present name="USER_SESSION_ATTRIBUTE">
	<logic:present name="USER_SESSION_ATTRIBUTE" property="person.options.cascadingStyleSheet">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/user/layout.css" media="screen">
	</logic:present>
	<logic:notPresent name="USER_SESSION_ATTRIBUTE" property="person.options.cascadingStyleSheet">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/layout.css" media="screen">
	</logic:notPresent>
</logic:present>
