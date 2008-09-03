<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>

<ul>

	<li class="header">
		<strong><bean:message key="link.sideBar.requestForProposal" bundle="EXPENDITURE_RESOURCES"/></strong>
		<div class="lic1"></div><div class="lic2"></div>
	</li>
	
	<logic:present name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person">
		<li>
			<html:link action="/requestForProposalProcess.do?method=prepareCreateRequestForProposalProcess">
				<bean:message key="link.sideBar.requestForProposal.create" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		 
		<li>
			<html:link action="/requestForProposalProcess.do?method=searchRequestProposalProcess">
				<bean:message key="link.sideBar.requestForProposal.search" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		
		<li>
			<html:link action="/requestForProposalProcess.do?method=showPendingRequests">
				<bean:message key="link.sideBar.requestForProposal.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
	</logic:present>
</ul>
