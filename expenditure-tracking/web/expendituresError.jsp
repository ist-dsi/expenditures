<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="myorg.presentationTier.LayoutContext"%>
<%@page import="myorg.presentationTier.actions.ContextBaseAction"%>

<html:html xhtml="true">
<head>
	<% final LayoutContext layoutContext = (LayoutContext) ContextBaseAction.getContext(request); %>
	<logic:iterate id="head" collection="<%= layoutContext.getHead() %>" type="java.lang.String">
		<jsp:include page="<%= head %>"/>
	</logic:iterate>
</head>

<body>


<div id="errorcontainer">

	<div id="container2">

		<div id="container3">
		
			<div id="content">

				<h2 class="mbottom0"><bean:message key="messages.exception.errorOccured" bundle="EXPENDITURE_RESOURCES"/></h2>
				
				<hr/>
				
				<h4 class="mtop5"><bean:message key="messages.exception.contacts" bundle="EXPENDITURE_RESOURCES"/></h4>
				
				<ul>
<!-- 
					<li><bean:message key="messages.exception.processDoubt" bundle="EXPENDITURE_RESOURCES"/></li>
					<li><bean:message key="messages.exception.technicalContact" bundle="EXPENDITURE_RESOURCES"/></li>
 -->
 					<li><bean:message key="messages.exception.technicalContact" bundle="EXPENDITURE_RESOURCES"/></li>
				</ul>
				
				<p class="mtop15">
					<bean:message key="messages.exception.backToHomepage" bundle="EXPENDITURE_RESOURCES"/>
					<a href="<%= request.getContextPath() %>"><bean:message key="messages.exception.backToHomepageLink" bundle="EXPENDITURE_RESOURCES"/></a>
				</p>

				<logic:present name="exceptionInfo">
					<bean:define id="exceptionInfo" name="exceptionInfo" type="java.lang.String"/>
					<html:hidden property="exceptionInfo" value="<%= exceptionInfo %>"/>
				</logic:present>
		
			</div>
		</div>
		
		<%--
		<div id="footer">
			<p>&copy;2010 Instituto Superior Técnico </p>
		</div>
		--%>

	</div>
	<!-- #container2 -->
</div>
<!-- #errorcontainer -->




</body>
</html:html>
