<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
	<li>
		<html:link action="/home.do?method=firstPage">
			<span>
				<bean:message key="link.topBar.home" bundle="EXPENDITURE_RESOURCES"/>
			</span>
			<div class="lic1"></div><div class="lic2"></div>
		</html:link>
	</li>
	<li>
		<html:link action="/requestForProposalProcess.do?method=showPendingRequests">
			<span>
				<bean:message key="link.topBar.requestForProposal" bundle="EXPENDITURE_RESOURCES"/>
			</span>
			<div class="lic1"></div><div class="lic2"></div>
		</html:link>
	</li>
	<li>
		<html:link action="/acquisitionProcess.do?method=showPendingProcesses">
			<span>
				<bean:message key="link.topBar.acquisitionProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</span>
			<div class="lic1"></div><div class="lic2"></div>
		</html:link>
	</li>
	<li>
		<html:link action="/organization.do?method=viewOrganization">
			<span>
				<bean:message key="link.topBar.organization" bundle="EXPENDITURE_RESOURCES"/>
			</span>
			<div class="lic1"></div><div class="lic2"></div>
		</html:link>
	</li>
	<li>
		<html:link action="/authorizations.do?method=viewAuthorizations">
			<span>
				<bean:message key="link.topBar.authorizations" bundle="EXPENDITURE_RESOURCES"/>
			</span>
			<div class="lic1"></div><div class="lic2"></div>
		</html:link>
	</li>
	<div class="c1"></div>
	<div class="c2"></div>
</ul>
