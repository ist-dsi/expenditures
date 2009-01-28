<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.Context"%>
<logic:present name="USER_SESSION_ATTRIBUTE" property="person">

	<%
		final String homeClass = Context.isPresent(null) ? "selected" : "";
		final String proposalsClass = Context.isPresent("requests") ? "selected" : "";
		final String announcementsClass = Context.isPresent("announcements") ? "selected" : "";
		final String processesClass = Context.isPresent("acquisitions") ? "selected" : "";
		final String organizationClass = Context.isPresent("organization") ? "selected" : "";
		final String statisticsClass = Context.isPresent("statisticsClass") ? "selected" : "";
	%>

	<ul>
		<li class="<%= homeClass %>">
			<html:link action="/home.do?method=firstPage">
				<span>
					<bean:message key="link.topBar.home" bundle="EXPENDITURE_RESOURCES"/>
				</span>
				<div class="lic1"></div>
			</html:link>
		</li>
		<logic:present role="MANAGER">
			<logic:present user="ist151339">
			<li class="<%= proposalsClass %>">
				<html:link action="/requestForProposalProcess.do?method=showPendingRequests">
					<span>
						<bean:message key="link.topBar.requestForProposal" bundle="EXPENDITURE_RESOURCES"/>
					</span>
					<div class="lic1"></div>
				</html:link>
			</li>
		<li class="<%= announcementsClass %>">
			<html:link action="/announcementProcess.do?method=showPendingProcesses">
				<span>
					<bean:message key="link.topBar.announcements" bundle="EXPENDITURE_RESOURCES"/>
				</span>
				<div class="lic1"></div>
			</html:link>
		</li>
			</logic:present>
		</logic:present>
		<li class="<%= processesClass %>">
			<html:link action="/search.do?method=search">
				<span>
					<bean:message key="link.topBar.acquisitionProcesses" bundle="EXPENDITURE_RESOURCES"/>
				</span>
				<div class="lic1"></div>
			</html:link>
		</li>
		<li class="<%= organizationClass %>">
			<html:link action="/organization.do?method=viewPerson" paramId="personOid" paramName="USER_SESSION_ATTRIBUTE" paramProperty="person.OID">
				<span>
					<bean:message key="link.topBar.organization" bundle="EXPENDITURE_RESOURCES"/>
				</span>
				<div class="lic1"></div>
			</html:link>
		</li>
		<logic:present role="STATISTICS_VIEWER">
			<li class="<%= statisticsClass %>">
				<html:link action="/statistics.do?method=showSimplifiedProcessStatistics">
					<span>
						<bean:message key="link.topBar.statistics" bundle="EXPENDITURE_RESOURCES"/>
					</span>
					<div class="lic1"></div>
				</html:link>
			</li>
		</logic:present>
	</ul>
</logic:present>

