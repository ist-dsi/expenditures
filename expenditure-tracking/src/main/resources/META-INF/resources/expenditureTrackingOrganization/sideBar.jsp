<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<ul>
	<li>
		<html:link action="/expenditureTrackingOrganization.do?method=viewOrganization">
			<span><bean:message key="link.viewOrganization" bundle="EXPENDITURE_RESOURCES"/></span>
		</html:link>
		<span class="bar">|</span>
	</li>
	<li>
		<html:link action="/expenditureTrackingOrganization.do?method=searchUsers">
			<span><bean:message key="search.link.users" bundle="EXPENDITURE_RESOURCES"/></span>
		</html:link>
		<span class="bar">|</span>
	</li>
	<li>
		<html:link action="/expenditureTrackingOrganization.do?method=manageSuppliers">
			<span><bean:message key="supplier.link.manage" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></span>
		</html:link>
	</li>	
</ul>
