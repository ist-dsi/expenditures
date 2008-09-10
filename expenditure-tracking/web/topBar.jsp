<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>

<logic:present name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person">
	<ul>
		<li>
			<html:link action="/home.do?method=firstPage">
				<span>
					<bean:message key="link.topBar.home" bundle="EXPENDITURE_RESOURCES"/>
				</span>
				<div class="lic1"></div>
			</html:link>
		</li>
		<logic:present role="MANAGER">
			<li>
				<html:link action="/requestForProposalProcess.do?method=showPendingRequests">
					<span>
						<bean:message key="link.topBar.requestForProposal" bundle="EXPENDITURE_RESOURCES"/>
					</span>
					<div class="lic1"></div>
				</html:link>
			</li>
		</logic:present>
		<li>
			<html:link action="/acquisitionProcess.do?method=showPendingProcesses">
				<span>
					<bean:message key="link.topBar.acquisitionProcesses" bundle="EXPENDITURE_RESOURCES"/>
				</span>
				<div class="lic1"></div>
			</html:link>
		</li>
		<li>
			<html:link action="/organization.do?method=viewOrganization">
				<span>
					<bean:message key="link.topBar.organization" bundle="EXPENDITURE_RESOURCES"/>
				</span>
				<div class="lic1"></div>
			</html:link>
		</li>
		<li>
			<html:link action="/authorizations.do?method=viewAuthorizations">
				<span>
					<bean:message key="link.topBar.authorizations" bundle="EXPENDITURE_RESOURCES"/>
				</span>
				<div class="lic1"></div>
			</html:link>
		</li>
	</ul>
</logic:present>

<div class="c1"></div>
<div class="c2"></div>
