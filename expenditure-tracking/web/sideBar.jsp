<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.Context"%>

<%
	final Context context = Context.getContext();
	final String module = context == null ? null : context.getModule();
	if (module == null) {
%>
	<ul>
		<logic:present role="MANAGER">
			<li>
				<html:link action="/home.do?method=showActiveRequestsForProposal">
					<bean:message key="link.sideBar.home.publicRequestsForProposal" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
		<li>
			<html:link action="/home.do?method=showAcquisitionAnnouncements">
				<bean:message key="link.sideBar.home.publicAnnouncements" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
<%
	} else {
	    final String sideBar = "/" + module + "/sideBar.jsp";
%>
		<jsp:include page="<%= sideBar %>"></jsp:include>
<%
	}
%>
