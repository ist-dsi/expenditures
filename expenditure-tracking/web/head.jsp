<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<title><tiles:getAsString name="title" ignore="true" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%
	final String contextPath = request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/layout.css"  media="screen"  />
