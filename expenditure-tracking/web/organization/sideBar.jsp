<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
	<li>
		<html:link action="/organization.do?method=viewOrganization">
			<span><bean:message key="link.viewOrganization" bundle="EXPENDITURE_RESOURCES"/></span>
		</html:link>
		<span class="bar">|</span>
	</li>
	<li>
		<html:link action="/organization.do?method=searchUsers">
			<span><bean:message key="search.link.users" bundle="EXPENDITURE_RESOURCES"/></span>
		</html:link>
		<span class="bar">|</span>
	</li>
	<li>
		<html:link action="/organization.do?method=manageSuppliers">
			<span><bean:message key="supplier.link.manage" bundle="ORGANIZATION_RESOURCES"/></span>
		</html:link>
	</li>	
</ul>
