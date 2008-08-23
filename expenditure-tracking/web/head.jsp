<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<title><tiles:getAsString name="title" ignore="true" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%
	final String contextPath = request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/layout.css" media="screen">
